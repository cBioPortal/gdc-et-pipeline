package org.cbio.gdcpipeline.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.Patient;
import org.cbio.gdcpipeline.model.cbio.Sample;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 16/06/17.
 */
public class ClinicalReader implements ItemStreamReader<ClinicalDataModel> {

    private static Log LOG = LogFactory.getLog(ClinicalReader.class);

    @Value("#{jobExecutionContext[barcodeToSamplesMap]}")
    private Map<String, List<String>> barcodeToSamplesMap;

    @Value("#{jobExecutionContext[uuidToFilesMap]}")
    private Map<String, List<String>> uuidToFilesMap;

    @Value("#{jobParameters[filter_sample]}")
    private String filter_sample_flag;

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    @Value("${onco.code.tcga.brca}")
    private String oncotree_code;

    private List<ClinicalDataModel> clinicalDataModelList = new ArrayList<>();


    @Override
    public ClinicalDataModel read() throws Exception {
        if (!this.clinicalDataModelList.isEmpty()) {
            return clinicalDataModelList.remove(0);
        }
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {


        List<String> clinicalFileNames = getClinicalFileList();

        if (clinicalFileNames.isEmpty()) {
            throw new ItemStreamException("Empty Clinical Files list");
        }

        for (String clinicalFile : clinicalFileNames) {
            File file = new File(sourceDir + File.separator + cancer_study_id + File.separator + clinicalFile);
            if (!file.exists()) {
                throw new ItemStreamException(" Resource Error. File does not exists : " + file.getAbsolutePath());
            }

            try {
                TcgaBcr tcgaBcr = unmarshall(file);
                addPatient(tcgaBcr);
                addSamples(tcgaBcr);
            } catch (JAXBException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Unmarshalling error for file " + file.getName());
                }
                throw new ItemStreamException(" Error while Unmarshalling");
            }


            if (filter_sample_flag.equalsIgnoreCase("true")) {
                for (int m = 0; m < this.clinicalDataModelList.size(); m++) {
                    ClinicalDataModel c = this.clinicalDataModelList.get(m);
                    if (c instanceof Sample) {
                        if (((Sample) c).getSample_id().endsWith("-10")) {
                            clinicalDataModelList.remove(m);
                        }
                    }

                }

            }

        }


    }

    //TODO Non-TCGA

    private TcgaBcr unmarshall(File xmlFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // root
        TcgaBcr tcgaBcr = (TcgaBcr) unmarshaller.unmarshal(xmlFile);

        return tcgaBcr;

    }

    private void addPatient(TcgaBcr tcgaBcr) {
        ClinicalDataModel patient = new Patient(
                tcgaBcr.getPatient().getBcrPatientBarcode().getValue(),
                tcgaBcr.getPatient().getVitalStatus().getValue(),
                tcgaBcr.getPatient().getGender().getValue(),
                Integer.parseInt(tcgaBcr.getPatient().getAgeAtInitialPathologicDiagnosis().getValue()));
        this.clinicalDataModelList.add(patient);
    }

    private void addSamples(TcgaBcr tcgaBcr) {

        List<String> sampleList = barcodeToSamplesMap.get(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        for (String sample_id : sampleList) {
            this.clinicalDataModelList.add(new Sample(
                    tcgaBcr.getPatient().getBcrPatientBarcode().getValue(),
                    sample_id,
                    oncotree_code
            ));
        }
    }


    private List<String> getClinicalFileList() throws ItemStreamException {
        if (uuidToFilesMap.isEmpty()) {
            if (LOG.isErrorEnabled()) {
                LOG.error(" UUID to Files Map is Empty.");
            }
            throw new ItemStreamException("Empty File Map");
        }
        List<String> fileNames = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : uuidToFilesMap.entrySet()) {
            for (String file : entry.getValue()) {
                if (file.endsWith(".xml") && file.contains("clinical")) {
                    fileNames.add(file);
                }
            }
        }
        return fileNames;
    }


    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {


    }

}
