package org.cbio.gdcpipeline.JobConfiguration;

import org.cbio.gdcpipeline.tasklet.ClinicalTransformationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.cbio.gdcpipeline.writer.ClinicalDataWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Job clinicalDataJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Step beginClinicalTransformation(){
        return stepBuilderFactory.get("beginClinicalTransformation")
                .tasklet(clinicalTransformationTasklet())
                .build();
    }

    @Bean
    public Step writeClinicalData(){
        return stepBuilderFactory.get("writeClinicalData")
                .tasklet(clinicalDataWriter())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet clinicalTransformationTasklet(){
        return new ClinicalTransformationTasklet();
    }
    @Bean
    @StepScope
    public Tasklet clinicalDataWriter(){
        return new ClinicalDataWriter();
    }

//    /*@Bean
//    public Job mainJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
//        Step clinicalJobStep = new JobStepBuilder(new StepBuilder("clinicalDataJob"))
//                .job(clinicalDataJob)
//                .launcher(jobLauncher)
//                .repository(jobRepository)
//                .transactionManager(platformTransactionManager)
//                .build();
//
//        //TODO
//        return jobBuilderFactory.get("mainJob")
//                .start(beginTransformation())
//                .next(clinicalJobStep)
//                .build();
//    }*/

    @Bean
    public Job mainJob(){
        return jobBuilderFactory.get("mainJob")
                .start(beginClinicalTransformation())
                .next(writeClinicalData())
                .build();
    }





}
