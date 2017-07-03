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

    private String METADATA_SAMPLE_FILE_NAME = "meta_clinical_sample.txt";

    private String METADATA_PATIENT_FILE_NAME = "meta_clinical_patient.txt";

    //TODO
    private String DATA_PATIENT_FILE_NAME = "data_clinical_patient.txt";

    //TODO
    private String DATA_SAMPLE_FILE_NAME = "data_clinical_sample.txt";

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;


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

        String sampleFile = outputDir + File.separator + METADATA_SAMPLE_FILE_NAME;
        String patientFile = outputDir + File.separator + METADATA_PATIENT_FILE_NAME;

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


        writeData(patientString.toString(), patientFile);
        writeData(sampleString.toString(), sampleFile);

        return RepeatStatus.FINISHED;

    }

    private void writeData(String input, String file) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"))) {
            writer.write(input.toString());
        }
    }


}
