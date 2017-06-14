package org.cbio.gdcpipeline.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CBioClinicalDataModel;
import org.cbio.gdcpipeline.processor.ClinicalDataProcessor;
import org.cbio.gdcpipeline.tasklet.ClinicalTransformationTasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by Dixit on 14/06/17.
 */
public class ClinicalDataWriter implements Tasklet {

    @Value("#{jobParameters[outputDirectory]}")
    private String outputDir;

    private String dataOutputFile;

    public static final String CLINICAL_DATA_FILE_NAME="data_clinical_samples.txt";
    public static final String CLINICAL_METADATA_FILE_NAME="meta_clinical_samples.txt";


    private static Log LOG = LogFactory.getLog(ClinicalTransformationTasklet.class);


    public FlatFileHeaderCallback segmentedDataHeader(){
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                //TODO
                StringBuffer header= new StringBuffer("#PATIENT IDENTIFIER\tSAMPLE_ID\tGENDER\tAGE\tOVERALL SURVIVAL STATUS");
                String row2="";
                String row3="";
                String row4="";
                String row5=System.lineSeparator()+"PATIENT_ID\tSAMPLE_ID\tSEX\tAGE\tOS_STATUS";
                header.append(row5);
                writer.write(header.toString());

            }
        };
    }

    public FieldExtractor<CBioClinicalDataModel> createFieldExtractor(){
        BeanWrapperFieldExtractor<CBioClinicalDataModel> ext = new BeanWrapperFieldExtractor<>();
        ext.setNames(new String[]{"patient_id", "sample_id", "sex", "age","os_status"});
        return  ext;
    }

    public void writeData(List<CBioClinicalDataModel> cBioDataList){

        FlatFileItemWriter<CBioClinicalDataModel> dataWriter = new FlatFileItemWriter<>();
        dataWriter.setAppendAllowed(false);
        dataWriter.setShouldDeleteIfExists(true);
        dataWriter.setHeaderCallback(segmentedDataHeader());
        dataWriter.setLineSeparator(System.lineSeparator());

        DelimitedLineAggregator<CBioClinicalDataModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        FieldExtractor<CBioClinicalDataModel> fe = createFieldExtractor();
        lineAggregator.setFieldExtractor(fe);
        dataWriter.setLineAggregator(lineAggregator);
        dataOutputFile = outputDir+File.separator+CLINICAL_DATA_FILE_NAME;
        File outputFile = new File(dataOutputFile);
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            LOG.error("ERROR in creating Clinical File at"+dataOutputFile);
            e.printStackTrace();
        }

        dataWriter.setResource(new FileSystemResource(outputFile));
        dataWriter.open(new ExecutionContext());
        try {
            long startTime = System.currentTimeMillis();
            dataWriter.write(cBioDataList);
            long stopTime = System.currentTimeMillis();
            LOG.info(" Finished Writing in "+((stopTime-startTime)/1000)+" seconds");
        } catch (Exception e) {
            LOG.error(" Failed to Write Clinical Data to File");
            e.printStackTrace();
        }

    }

    public void writeMetaData(){



    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        LOG.info("=========== EXECUTING WRITER TASKLET===============");
        List<CBioClinicalDataModel> cBioDataList =(List<CBioClinicalDataModel>) chunkContext.getStepContext().getJobExecutionContext().get("cBioClinicalDataModel");
        LOG.info("Writing "+cBioDataList.size()+" cases to clincal file");
        writeData(cBioDataList);
        writeMetaData();

        return RepeatStatus.FINISHED;
    }
}
