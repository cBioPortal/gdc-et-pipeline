package org.cbio.gdcpipeline.tasklet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.Sample;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.TcgaBcr;
import org.cbio.gdcpipeline.model.rest.response.Hits;
import org.cbio.gdcpipeline.util.CommonDataUtil;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dixit Patel
 */
public class BiospecimenXmlDataTasklet implements Tasklet {
    private static Log LOG = LogFactory.getLog(BiospecimenXmlDataTasklet.class);

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobExecutionContext[gdcFileMetadatas]}")
    private List<Hits> gdcFileMetadatas;

    private Map<String, List<String>> barcodeToSamplesMap = new HashMap<>();

    protected TcgaBcr unmarshall(File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (TcgaBcr) unmarshaller.unmarshal(xmlFile);
    }

    protected void addData(TcgaBcr tcgaBcr) {
        String barcode = tcgaBcr.getPatient().getBcrPatientBarcode().getValue();
        barcode = barcode.replaceAll("\n", "");
        barcode = barcode.trim();
        List<Sample> samples = tcgaBcr.getPatient().getSamples().getSample();
        List<String> samplesList = new ArrayList<>();
        if (!samples.isEmpty()) {
            for (Sample sample : samples) {
                samplesList.add(barcode + "-" + sample.getSampleTypeId().getValue());
            }
            barcodeToSamplesMap.put(barcode, samplesList);
        }
    }

    @Override
    public RepeatStatus execute(StepContribution stepContext, ChunkContext chunkContext) throws Exception {
        List<File> biospecimenFiles = getBiospecimenFileList();
        for (File file : biospecimenFiles) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Processing Biospecimen file : " + file.getName());
            }
            try {
                TcgaBcr tcgaBcr = unmarshall(file);
                addData(tcgaBcr);
            } catch (JAXBException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Unmarshall error. Skipping File :" + file.getName());
                }
            }
        }
        chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext()
                .put("barcodeToSamplesMap", barcodeToSamplesMap);

        return RepeatStatus.FINISHED;
    }

    private List<File> getBiospecimenFileList() {
        List<File> biospecimenFiles = new ArrayList<>();
        if (!gdcFileMetadatas.isEmpty()) {
            for (Hits data : gdcFileMetadatas) {
                if (data.getType().equals(CommonDataUtil.GDC_TYPE.BIOSPECIMEN.toString())) {
                    File file = new File(sourceDir, data.getFile_name());
                    if (file.exists()) {
                        biospecimenFiles.add(file);
                    } else {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("Biospecimen File : " + file.getAbsolutePath() + " not found.\nSkipping File");
                        }
                    }
                }
            }
        }
        return biospecimenFiles;
    }
}

