package org.cbio.gdcpipeline.tasklet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.brca._2.TcgaBcr;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ClinicalTransformationTasklet implements Tasklet {
    private static Log LOG = LogFactory.getLog(ClinicalTransformationTasklet.class);

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;


    private CBioClinicalDataModel xmlUnmarshall(File xmlFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TcgaBcr tcgaBcr = (TcgaBcr)unmarshaller.unmarshal(xmlFile);
        CBioClinicalDataModel cBioClinicalDataModel = new CBioClinicalDataModel();

        cBioClinicalDataModel.setPatient_id(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        //TODO this is not found in Clinical but Biospecimen xml
        cBioClinicalDataModel.setSample_id(tcgaBcr.getPatient().getBcrPatientBarcode().getValue());
        cBioClinicalDataModel.setSex(tcgaBcr.getPatient().getGender().getValue());
        cBioClinicalDataModel.setAge(Integer.parseInt(tcgaBcr.getPatient().getAgeAtInitialPathologicDiagnosis().getValue()));
        cBioClinicalDataModel.setOs_status(tcgaBcr.getPatient().getVitalStatus().getValue());

        return cBioClinicalDataModel;

    }


    @Override
    public RepeatStatus execute(StepContribution stepContext, ChunkContext chunkContext) throws Exception {

        File dir = new File(sourceDir);
        long startTime = System.currentTimeMillis();
        File [] xmlFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        LOG.info("Files found = "+xmlFiles.length);
        List<CBioClinicalDataModel> cBioWrite = new ArrayList<>();

        for(File clinicalXML:xmlFiles){
            LOG.info("Proccesing file : "+clinicalXML.getName());
            //JAXB
            try {
                cBioWrite.add(xmlUnmarshall(clinicalXML));
            }catch(JAXBException e){
                LOG.error("Unmarshall error");
            }
        }
        long stopTime = System.currentTimeMillis();

        LOG.info("Finished Unmarshalling "+cBioWrite.size()+" objects in "+((stopTime-startTime)/1000)+" seconds");

        LOG.info("Adding " + cBioWrite.size() + " cases to execution context.");
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("cBioClinicalDataModel", cBioWrite);

        return RepeatStatus.FINISHED;
    }
}
