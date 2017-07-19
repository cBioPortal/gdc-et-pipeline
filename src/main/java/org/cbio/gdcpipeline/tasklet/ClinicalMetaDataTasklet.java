package org.cbio.gdcpipeline.tasklet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.util.MetaFileWriter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dixit on 27/06/17.
 */
public class ClinicalMetaDataTasklet implements Tasklet {
    private String patientFile;
    @Value("${clinical.data.sample.file}")
    private String sampleFile;

    @Value("${clinical.metadata.sample.file}")
    private String METADATA_SAMPLE_FILE_NAME;

    @Value("${clinical.metadata.patient.file}")
    private String METADATA_PATIENT_FILE_NAME;

    @Value("${clinical.data.patient.file}")
    private String DATA_PATIENT_FILE_NAME;

    @Value("${clinical.data.sample.file}")
    private String DATA_SAMPLE_FILE_NAME;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    private static Log LOG = LogFactory.getLog(ClinicalMetaDataTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String sampleFile = outputDir + File.separator + METADATA_SAMPLE_FILE_NAME;
        String patientFile = outputDir + File.separator + METADATA_PATIENT_FILE_NAME;

        Map<String, String> patient = new LinkedHashMap<>();
        Map<String, String> sample = new LinkedHashMap<>();

        patient.put("cancer_study_identifier", cancer_study_id);
        patient.put("genetic_alteration_type", "CLINICAL");
        patient.put("datatype", "PATIENT_ATTRIBUTES");
        patient.put("data_filename", DATA_PATIENT_FILE_NAME);

        sample.put("cancer_study_identifier", cancer_study_id);
        sample.put("genetic_alteration_type", "CLINICAL");
        sample.put("datatype", "SAMPLE_ATTRIBUTES");
        sample.put("data_filename", DATA_SAMPLE_FILE_NAME);

        MetaFileWriter.writeMetadata(patient, patientFile);
        MetaFileWriter.writeMetadata(sample, sampleFile);

        return RepeatStatus.FINISHED;

    }
}
