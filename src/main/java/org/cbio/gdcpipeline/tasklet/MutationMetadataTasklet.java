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
 * Created by Dixit on 11/07/17.
 */
public class MutationMetadataTasklet implements Tasklet {
    @Value("${mutation.data.file}")
    private String MUTATION_DATA_FILE ;

    @Value("${mutation.metadata.file}")
    private String MUTATION_METADATA_FILE;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    private static Log LOG = LogFactory.getLog(MutationMetadataTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String metafile = outputDir + File.separator + MUTATION_METADATA_FILE;
        Map<String, String> mutationMetadata = new LinkedHashMap<>();

        mutationMetadata.put("cancer_study_identifier",cancer_study_id);
        mutationMetadata.put("genetic_alteration_type","MUTATION_EXTENDED");
        mutationMetadata.put("datatype","MAF");
        mutationMetadata.put("stable_id","mutations");
        mutationMetadata.put("show_profile_in_analysis_tab","true");
        mutationMetadata.put("profile_description","Mutation data from whole exome sequencing");
        mutationMetadata.put("profile_name","Mutations");
        mutationMetadata.put("data_filename",MUTATION_DATA_FILE);

        MetaFileWriter.writeMetadata(mutationMetadata,metafile);
        return RepeatStatus.FINISHED;
    }
}
