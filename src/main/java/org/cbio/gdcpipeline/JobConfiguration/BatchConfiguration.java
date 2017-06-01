package org.cbio.gdcpipeline.JobConfiguration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Job clinicalJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Step beginTransformation(){
        return stepBuilderFactory.get("beginTransformation")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        //TODO add logger
                        System.out.println("Begin Transformation");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job mainJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        Step clinicalJobStep = new JobStepBuilder(new StepBuilder("clinicalJobStep"))
                .job(clinicalJob)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .build();

        //TODO
        return jobBuilderFactory.get("mainJob")
                .start(beginTransformation())
                .next(clinicalJobStep)
                .build();
    }




}
