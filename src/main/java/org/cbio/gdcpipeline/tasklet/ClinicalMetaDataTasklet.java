package org.cbio.gdcpipeline.tasklet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

/**
 * Created by Dixit on 27/06/17.
 */
public class ClinicalMetaDataTasklet implements Tasklet {

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("${clinical.metadata.sample.file.name}")
    private String METADATA_SAMPLE_FILE_NAME;

    @Value("${clinical.metadata.patient.file.name}")
    private String METADATA_PATIENT_FILE_NAME;

    @Value("${clinical.data.patient.file.name}")
    private String DATA_PATIENT_FILE_NAME;

    @Value("${clinical.data.sample.file.name}")
    private String DATA_SAMPLE_FILE_NAME;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;
    private final String PATIENT_DATATYPE = "PATIENT_ATTRIBUTES";
    private final String SAMPLE_DATATYPE = "SAMPLE_ATTRIBUTES";
    private final String GENETIC_ALTERATION_TYPE = "CLINICAL";

    private final String row1 = "cancer_study_identifier:";
    private final String row2 = "genetic_alteration_type:";
    private final String row3 = "datatype:";
    private final String row4 = "data_filename:";

    private static Log LOG = LogFactory.getLog(ClinicalMetaDataTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String sampleFile = null;
        String patientFile = null;
        if (outputDir == null || outputDir.isEmpty()) {
            outputDir = sourceDir + File.separator + cancer_study_id + File.separator + "output";
            File dir = new File(outputDir);
            dir.mkdir();
            LOG.info(" No Metadata Output directory specified. Output will be at " + outputDir);
            sampleFile = outputDir + File.separator + METADATA_SAMPLE_FILE_NAME;
            patientFile = outputDir + File.separator + METADATA_PATIENT_FILE_NAME;
        } else {
            File dir = new File(outputDir);
            dir.mkdir();
            LOG.info(" Metadata Output will be at " + outputDir);
            sampleFile = outputDir + File.separator + METADATA_SAMPLE_FILE_NAME;
            patientFile = outputDir + File.separator + METADATA_PATIENT_FILE_NAME;

        }

        StringBuilder patientString = new StringBuilder();
        patientString
                .append(row1 + cancer_study_id)
                .append("\n")
                .append(row2 + GENETIC_ALTERATION_TYPE)
                .append("\n")
                .append(row3 + PATIENT_DATATYPE)
                .append("\n")
                .append(row4 + DATA_PATIENT_FILE_NAME);


        StringBuilder sampleString = new StringBuilder();
        sampleString
                .append(row1 + cancer_study_id)
                .append("\n")
                .append(row2 + GENETIC_ALTERATION_TYPE)
                .append("\n")
                .append(row3 + SAMPLE_DATATYPE)
                .append("\n")
                .append(row4 + DATA_SAMPLE_FILE_NAME);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(sampleFile), "utf-8"))) {
            writer.write(sampleString.toString());
        }


        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(patientFile), "utf-8"))) {
            writer.write(patientString.toString());
        }
        return RepeatStatus.FINISHED;

    }


}
