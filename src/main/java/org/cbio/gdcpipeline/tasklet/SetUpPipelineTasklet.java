package org.cbio.gdcpipeline.tasklet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author Dixit Patel
 */
public class SetUpPipelineTasklet implements Tasklet {
    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[cancer_study_id]}")
    private String cancer_study_id;


    private HashSet<String> supportedCancerStudy = new HashSet<>();
    private static String SYSTEM_TMP_DIR_PROPERTY = "java.io.tmpdir";
    private static String TMP_DIR = "gdcpipeline";

    private static Log LOG = LogFactory.getLog(SetUpPipelineTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        initiateCancerStudy();
        validateCancerStudy();
        createOutputDirectory();
        validateSourceDirectory();
        extractCompressedFiles();
        if (LOG.isInfoEnabled()) {
            LOG.info(" ######### Pipeline Setup Completed #########");
        }
        return RepeatStatus.FINISHED;
    }

    private void initiateCancerStudy() {
        supportedCancerStudy.add("TCGA_BRCA");
    }

    private void validateCancerStudy() throws Exception {
        if (!supportedCancerStudy.contains(cancer_study_id)) {
            if (LOG.isErrorEnabled()) {
                LOG.error(" Invalid Cancer Study Type ");
            }
            throw new Exception();
        }
    }

    private void validateSourceDirectory() throws FileNotFoundException {
        File source = new File(sourceDir);
        if (!source.exists()) {
            if (LOG.isErrorEnabled()) {
                LOG.error(" Invalid Source directory ");
            }
            throw new FileNotFoundException();
        }
    }

    private void createOutputDirectory() throws FileNotFoundException {
        File outputDirectory = new File(outputDir);
        if (outputDirectory.mkdir()) {
            if (LOG.isInfoEnabled()) {
                LOG.info(" Output will be at " + outputDir);
            }
        } else if (!outputDirectory.exists()) {
            if (LOG.isErrorEnabled()) {
                LOG.error(" Invalid output directory ");
            }
            throw new FileNotFoundException();
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info(" Output directory is valid ");
            }
        }
    }

    private File createTempDirectory() throws Exception {
        String tmp_path = System.getProperty(SYSTEM_TMP_DIR_PROPERTY);
        File tmp_dir = new File(tmp_path, TMP_DIR);
        if (tmp_dir.exists()) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Temp directory already exists. Attempting to delete directory and its contents :" + tmp_dir.getAbsolutePath());
            }
            deleteTempDir(tmp_dir);
        }
        tmp_dir.mkdir();
        return tmp_dir;
    }

    private void deleteTempDir(File tmp_dir) throws Exception {
        File[] entries = tmp_dir.listFiles();
        if (entries != null) {
            for (File entry : entries) {
                deleteTempDir(entry);
            }
        }
        tmp_dir.delete();
    }

    private void extractCompressedFiles() throws Exception {
        if (LOG.isInfoEnabled()) {
            LOG.info("######### Extracting Compressed Files #########");
        }
        File temp_dir = createTempDirectory();


        File source = new File(sourceDir);
        for (File extractFile : source.listFiles()) {
            if (extractFile.isFile() && extractFile.getName().endsWith(".gz")) {
                File tmp_file;
                try {
                    tmp_file = File.createTempFile(extractFile.getName(), ".tmp", temp_dir);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception("Error creating temp file in : " + temp_dir.getAbsolutePath() + "\nSkipping File");
                }
                try {
                    FileInputStream fis = new FileInputStream(extractFile);
                    FileOutputStream fos = new FileOutputStream(tmp_file);
                    GZIPInputStream gzip = new GZIPInputStream(fis);
                    byte[] buffer = new byte[1024];
                    while (gzip.read(buffer) != -1) {
                        fos.write(buffer);
                    }
                    fos.close();
                    fis.close();
                    gzip.close();
                    String destination = sourceDir + File.separator + extractFile.getName().replace(".gz", "");
                    Files.move(Paths.get(tmp_file.getAbsolutePath()), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                    deleteTempDir(temp_dir);
                    throw new Exception("Error While decompressing files");
                }
            }
        }
        deleteTempDir(temp_dir);
    }

    private List<File> getFilesToExtract() {
        File source = new File(sourceDir);
        List<File> filesToExtract = new ArrayList<>();
        for (File file : source.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".gz")) {
                filesToExtract.add(file);
            }
        }
        return filesToExtract;
    }
}
