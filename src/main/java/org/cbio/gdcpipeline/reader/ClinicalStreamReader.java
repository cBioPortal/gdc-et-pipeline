package org.cbio.gdcpipeline.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.GDCPipelineApplication;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dixit on 16/06/17.
 */
public class ClinicalStreamReader implements ResourceAwareItemReaderItemStream<CBioClinicalDataModel>{

    private Resource resource;
    private static Log LOG = LogFactory.getLog(ClinicalStreamReader.class);
    private List<CBioClinicalDataModel> cBioClinicalDataModelList = new ArrayList<>();


    @Override
    public CBioClinicalDataModel read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!this.cBioClinicalDataModelList.isEmpty()){
            return cBioClinicalDataModelList.remove(0);
        }
        return null;
    }

    private CBioClinicalDataModel unmarshall(File xmlFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TcgaBcr tcgaBcr = (TcgaBcr)unmarshaller.unmarshal(xmlFile);
        CBioClinicalDataModel cBioClinicalDataModel = new CBioClinicalDataModel();

        cBioClinicalDataModel.setPatient_id(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        //TODO this is not found in Clinical but Biospecimen xml
        //TODO get from execution context
        cBioClinicalDataModel.setSample_id(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        cBioClinicalDataModel.setSex(tcgaBcr.getPatient().getGender().getValue());
        cBioClinicalDataModel.setAge(Integer.parseInt(tcgaBcr.getPatient().getAgeAtInitialPathologicDiagnosis().getValue()));
        cBioClinicalDataModel.setOs_status(tcgaBcr.getPatient().getVitalStatus().getValue());

        return cBioClinicalDataModel;

    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //Open Resource and Unmarshall

        File xmlFile = null;
        try {
             xmlFile = resource.getFile();
        } catch (IOException e) {
            LOG.error("Resource Error. Cannot read file");
            e.printStackTrace();
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
