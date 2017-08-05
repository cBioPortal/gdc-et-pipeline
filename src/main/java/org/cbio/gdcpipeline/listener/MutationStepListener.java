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

/**
 * @author Dixit Patel
 **/
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
            if (!separate_maf.isEmpty()) {
                if (separate_maf.equalsIgnoreCase("true")) {
                    //read individually
                    stepExecution.getJobExecution().getExecutionContext().put("maf_files", maf_files);
                    stepExecution.getExecutionContext().put("mafToProcess", maf_files.remove(0));
                    stepExecution.getJobExecution().getExecutionContext().put("isStartedMaf", true);
                } else {
                    stepExecution.getExecutionContext().put("maf_files", maf_files);
                }
            }
        } else {
            if (!separate_maf.isEmpty()) {
                if (separate_maf.equalsIgnoreCase("true")) {
                    List<File> maf_files = (List<File>) stepExecution.getJobExecution().getExecutionContext().get("maf_files");
                    stepExecution.getExecutionContext().put("mafToProcess", maf_files.remove(0));
                }
            }
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!separate_maf.isEmpty()) {
            if (separate_maf.equalsIgnoreCase("true")) {
                List<String> mafList = (List<String>) stepExecution.getJobExecution().getExecutionContext().get("maf_files");
                if (!mafList.isEmpty()) {
                    return new ExitStatus("CONTINUE");
                }
            }
        }
        return ExitStatus.COMPLETED;
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
