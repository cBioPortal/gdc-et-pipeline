package org.cbio.gdcpipeline.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.PatientFileModel;
import org.cbio.gdcpipeline.model.cbio.SampleFileModel;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dixit on 16/06/17.
 */
public class ClinicalStreamReader implements ResourceAwareItemReaderItemStream<CBioClinicalDataModel>{

    private Resource resource;

    private static Log LOG = LogFactory.getLog(ClinicalStreamReader.class);
    private List<CBioClinicalDataModel> cBioClinicalDataModelList = new ArrayList<>();

    @Value("#{jobExecutionContext[barcodeToSamplesMap]}")
    private HashMap<String, List<String>> barcodeToSamplesMap;

    @Value("${onco.code.tcga.brca}")
    private String oncotree_code;

    @Override
    public CBioClinicalDataModel read() throws Exception {
        if (!this.cBioClinicalDataModelList.isEmpty()){
            return cBioClinicalDataModelList.remove(0);
        }
        return null;
    }

    private PatientFileModel setPatientFields(TcgaBcr tcgaBcr) {
        PatientFileModel patient = new PatientFileModel();
        patient.setPatient_id(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        patient.setSex(tcgaBcr.getPatient().getGender().getValue());
        patient.setAge(Integer.parseInt(tcgaBcr.getPatient().getAgeAtInitialPathologicDiagnosis().getValue()));
        patient.setOs_status(tcgaBcr.getPatient().getVitalStatus().getValue());

        return patient;

    }

    private SampleFileModel setSampleFields(TcgaBcr tcgaBcr) {
        SampleFileModel sample = new SampleFileModel();
        sample.setPatient_id(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        sample.setSample_id(barcodeToSamplesMap.get(sample.getPatient_id()));
        sample.setOncotree_code(oncotree_code);
        return sample;

    }

    private CBioClinicalDataModel unmarshall(File xmlFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // root
        TcgaBcr tcgaBcr = (TcgaBcr)unmarshaller.unmarshal(xmlFile);

        CBioClinicalDataModel cBioClinicalDataModel = new CBioClinicalDataModel();

        PatientFileModel patient = setPatientFields(tcgaBcr);
        SampleFileModel sample = setSampleFields(tcgaBcr);
        cBioClinicalDataModel.setPatientFileModel(patient);
        cBioClinicalDataModel.setSampleFileModel(sample);

        return cBioClinicalDataModel;

    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //Open Resource and Unmarshall

        File xmlFile = null;
        try {
             xmlFile = resource.getFile();
            if (xmlFile == null) throw new FileNotFoundException("Resource Error. Cannot read file");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            this.cBioClinicalDataModelList.add(unmarshall(xmlFile));
        } catch (JAXBException e) {
            LOG.error("Unmarshalling error for file "+xmlFile.getName());
            e.printStackTrace();
        }


    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {


    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;

    }


}
