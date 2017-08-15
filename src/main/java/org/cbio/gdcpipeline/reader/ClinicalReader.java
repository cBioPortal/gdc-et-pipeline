package org.cbio.gdcpipeline.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.cbio.gdcpipeline.model.cbio.Patient;
import org.cbio.gdcpipeline.model.cbio.Sample;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.cbio.gdcpipeline.model.rest.response.GdcFileMetadata;
import org.cbio.gdcpipeline.util.CommonDataUtil;
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
 * @author Dixit Patel
 */
public class ClinicalReader implements ItemStreamReader<ClinicalDataModel> {
    private static Log LOG = LogFactory.getLog(ClinicalReader.class);

    @Value("#{jobExecutionContext[barcodeToSamplesMap]}")
    private Map<String, List<String>> barcodeToSamplesMap;

    @Value("#{jobExecutionContext[gdcFileMetadatas]}")
    private List<GdcFileMetadata> gdcFileMetadatas;

    @Value("#{jobParameters[filter_sample]}")
    private String filter_sample_flag;

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[cancer_study_id]}")
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
        List<File> clinicalFileNames = getClinicalFileList();
        if (!clinicalFileNames.isEmpty()) {
            for (File clinicalFile : clinicalFileNames) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Processing Clinical file : " + clinicalFile.getName());
                }
                try {
                    TcgaBcr tcgaBcr = unmarshall(clinicalFile);
                    addData(tcgaBcr);
                } catch (JAXBException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Unmarshalling error. Skipping file " + clinicalFile.getName());
                    }
                    continue;
                }
                if (filter_sample_flag.equalsIgnoreCase("true")) {
                    for (int m = 0; m < this.clinicalDataModelList.size(); m++) {
                        ClinicalDataModel c = this.clinicalDataModelList.get(m);
                        if (c instanceof Sample) {
                            if (((Sample) c).getSample_id().endsWith(CommonDataUtil.NORMAL_SAMPLE_SUFFIX)) {
                                clinicalDataModelList.remove(m);
                            }
                        }
                    }
                }
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info(" No Clinincal Files Found");
            }
        }
    }

    private TcgaBcr unmarshall(File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        // root
        TcgaBcr tcgaBcr = (TcgaBcr) unmarshaller.unmarshal(xmlFile);
        return tcgaBcr;
    }

    private void addData(TcgaBcr tcgaBcr) {
        String barcode = tcgaBcr.getPatient().getBcrPatientBarcode().getValue();
        if (!barcodeToSamplesMap.containsKey(barcode)) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Biospecimen file for Barcode : " + barcode + " does not exist. Skipping File");
            }
        } else {
            List<String> sampleList = barcodeToSamplesMap.get(barcode);
            // patient
            ClinicalDataModel patient = new Patient(
                    barcode,
                    tcgaBcr.getPatient().getVitalStatus().getValue(),
                    tcgaBcr.getPatient().getGender().getValue(),
                    Integer.parseInt(tcgaBcr.getPatient().getAgeAtInitialPathologicDiagnosis().getValue()));
            this.clinicalDataModelList.add(patient);

            //sample
            for (String sample_id : sampleList) {
                this.clinicalDataModelList.add(new Sample(
                        tcgaBcr.getPatient().getBcrPatientBarcode().getValue(),
                        sample_id,
                        oncotree_code
                ));
            }
        }
    }

    private List<File> getClinicalFileList() throws ItemStreamException {
       return CommonDataUtil.getFileList(gdcFileMetadatas,CommonDataUtil.GDC_TYPE.CLINICAL,sourceDir);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }
}
