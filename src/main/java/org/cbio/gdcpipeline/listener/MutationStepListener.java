package org.cbio.gdcpipeline.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.rest.response.GdcFileMetadata;
import org.cbio.gdcpipeline.util.CommonDataUtil;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.cbio.gdcpipeline.reader.MutationReader.DEFAULT_MERGED_MAF_FILENAME;

/**
 * Created by Dixit on 24/07/17.
 */
public class MutationStepListener implements StepExecutionListener {
    private static Log LOG = LogFactory.getLog(MutationStepListener.class);

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("#{jobExecutionContext[gdcFileMetadatas]}")
    private List<GdcFileMetadata> gdcFileMetadatas;

    @Value("#{jobParameters[separate_maf]}")
    private String separate_maf;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        boolean isStarted = false;
        if (stepExecution.getJobExecution().getExecutionContext().containsKey("isStartedMaf")) {
            isStarted = true;
        }
        if (!isStarted) {
            List<File> maf_files = getMutationFileList();
            stepExecution.getJobExecution().getExecutionContext().put("maf_files", maf_files);
            if (LOG.isInfoEnabled()) {
                LOG.info("Processing MAF File : " + maf_files.get(0));
            }
            stepExecution.getExecutionContext().put("mafToProcess", maf_files.remove(0));
            stepExecution.getJobExecution().getExecutionContext().put("isStartedMaf", true);
        } else {
            List<File> maf_files = (List<File>) stepExecution.getJobExecution().getExecutionContext().get("maf_files");
            if (LOG.isInfoEnabled()) {
                LOG.info("Processing MAF File : " + maf_files.get(0).getName());
            }
            stepExecution.getExecutionContext().put("mafToProcess", maf_files.remove(0));
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<String> mafList = (List<String>) stepExecution.getJobExecution().getExecutionContext().get("maf_files");
        if (mafList.isEmpty()) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" Finished Processing MAF Files");
            }
            boolean isStartedFinalMaf = false;
            if(stepExecution.getJobExecution().getExecutionContext().containsKey("isStartedFinalMaf")){
                isStartedFinalMaf=(boolean)stepExecution.getJobExecution().getExecutionContext().get("isStartedFinalMaf");
            }
            if (separate_maf.equalsIgnoreCase("false") && !isStartedFinalMaf) {
                List<File> finalMaf = new ArrayList<>();
                finalMaf.add(new File(outputDir, DEFAULT_MERGED_MAF_FILENAME));
                stepExecution.getJobExecution().getExecutionContext().put("maf_files", finalMaf);
                stepExecution.getJobExecution().getExecutionContext().put("isStartedFinalMaf", true);
                return new ExitStatus("CONTINUE");
            }
            return ExitStatus.COMPLETED;
        }
        return new ExitStatus("CONTINUE");
    }

    public List<File> getMutationFileList() {
        List<File> maf_files = new ArrayList<>();
        List<String> filenames = new ArrayList<>();
        if (!gdcFileMetadatas.isEmpty()) {
            for (GdcFileMetadata data : gdcFileMetadatas) {
                if (data.getType().equals(CommonDataUtil.GDC_TYPE.MUTATION.toString())) {
                    filenames.add(data.getFile_name().replace(".gz", ""));
                }
            }
        }
        for (String f : filenames) {
            File file = new File(sourceDir, f);
            if (file.exists()) {
                maf_files.add(file);
            } else {
                if (LOG.isInfoEnabled()) {
                    LOG.info("MAF File : " + file.getAbsolutePath() + " not found.\nSkipping File");
                }
            }
        }
        return maf_files;
    }
}
