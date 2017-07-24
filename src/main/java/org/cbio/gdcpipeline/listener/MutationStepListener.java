package org.cbio.gdcpipeline.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 24/07/17.
 */
public class MutationStepListener implements StepExecutionListener {
    private static Log LOG = LogFactory.getLog(MutationStepListener.class);
    private Map<String, List<String>> uuidToFilesMap;

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        boolean isStarted = false;
        if (stepExecution.getJobExecution().getExecutionContext().containsKey("isStartedMaf")){
            isStarted = true;
        }
        if (!isStarted) {
            List<String> mafList = new ArrayList<>();
            File source = new File(sourceDir);
            for (String f : source.list()) {
                if (f.endsWith(".maf") || f.endsWith(".maf.gz")) {
                    //TODO Skip protected ?
                    if (f.contains("protected")) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info(" Skipping processing for Protected MAF Files ");
                        }
                    } else {
                        mafList.add(f);
                    }
                }
            }
            stepExecution.getJobExecution().getExecutionContext().put("maf_files", mafList);
            if(LOG.isInfoEnabled()){
                LOG.info("Processing MAF File : " + mafList.get(0));
            }
            stepExecution.getExecutionContext().put("mafToProcess",mafList.remove(0));
            stepExecution.getJobExecution().getExecutionContext().put("isStartedMaf", true);
        }
        else{
            List<String> mafList =(List<String>)stepExecution.getJobExecution().getExecutionContext().get("maf_files");
            if(LOG.isInfoEnabled()){
                LOG.info("Processing MAF File : " + mafList.get(0));
            }
            stepExecution.getExecutionContext().put("mafToProcess",mafList.remove(0));
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        List<String> mafList = (List<String>) stepExecution.getJobExecution().getExecutionContext().get("maf_files");

        if (mafList.isEmpty()) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" Finished Processing MAF Files");
            }
            return ExitStatus.COMPLETED;
        }
        else {

        }
        return new ExitStatus("CONTINUE");
    }
}
