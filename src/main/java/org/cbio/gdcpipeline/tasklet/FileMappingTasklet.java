package org.cbio.gdcpipeline.tasklet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.Sample;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.Samples;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.TcgaBcr;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

   /*
     1. Get all TCGA barcodes from biospecimen files alongwith all sample-ids
        Add barcodes to a list and also to a map with sample_ids
     2. Use list to call GDC API and create a map for each barcode with filenames
     3. Add both maps to execution context
     */

public class FileMappingTasklet implements Tasklet {

    private static Log LOG = LogFactory.getLog(FileMappingTasklet.class);

    //TODO get from GDC file name
    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    List<String> barcodes = new ArrayList<>();
    HashMap<String,List<String>> barcodeToSamples = new HashMap<>();
    HashMap<String,List<String>> barcodeToFiles = new HashMap<>();



    private void xmlUnmarshall(File xmlFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TcgaBcr tcgaBcr = (TcgaBcr) unmarshaller.unmarshal(xmlFile);
        String barcode = tcgaBcr.getPatient().getBcrPatientBarcode().getValue();
        barcodes.add(barcode);
        List<Sample> samples =tcgaBcr.getPatient().getSamples().getSample();
        List<String> samplesList = null;
        for(Sample sample:samples){
            samplesList.add(barcode+"-"+sample.getSampleTypeId().getValue());
        }
        barcodeToSamples.put(barcode,samplesList);

    }


    @Override
    public RepeatStatus execute(StepContribution stepContext, ChunkContext chunkContext) throws Exception {

        File dir = new File(sourceDir);
        long startTime = System.currentTimeMillis();
        File[] xmlFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".xml")) {
                    return name.contains("biopspecimen");
                }
                return false;
            }
        });
        LOG.info("Number of Biospecimen Files found = " + xmlFiles.length);

        for (File biospecimenXML : xmlFiles) {
            LOG.info("Proccesing file : " + biospecimenXML.getName());
            //JAXB
            try {
                xmlUnmarshall(biospecimenXML);
            } catch (JAXBException e) {
                LOG.error("Unmarshall error");
            }
        }

        //Call GDC API with barcodes


        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("barcodeToSamplesMap", barcodeToSamples);

        return RepeatStatus.FINISHED;
    }
}
