package org.cbio.gdcpipeline;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.gdc.clinical.GDCClinicalDataModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

@SpringBootApplication
@EnableBatchProcessing
public class GDCPipelineApplication {


	private static Log LOG = LogFactory.getLog(GDCPipelineApplication.class);

	private static Options getOptions(String []input){
		Options options = new Options();
		options.addOption("j","job",false,"Job to run");
		options.addOption("s","source",true,"source directory for files");
		options.addOption("o","output",true,"output directory for files");
		options.addOption("h", "help", false, "shows this help document and quits.");
		return options;
	}

	private static void help(Options gnuOptions, int exitStatus) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("GDCPipelineApplication", gnuOptions);
		System.exit(exitStatus);
	}

	private static void launchJob(String[] args, String sourceDirectory, String outputDirectory,String jobName) throws Exception {
		SpringApplication app = new SpringApplication(GDCPipelineApplication.class);
		ApplicationContext ctx= app.run(args);
		Job mainJob = ctx.getBean("mainJob", Job.class);
		long t = System.currentTimeMillis();
		String time = String.valueOf(t);

		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("sourceDirectory", sourceDirectory)
				.addString("outputDirectory", outputDirectory)
				.addString("job",jobName)
				.addString("time",time)
				.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(mainJob, jobParameters);
		LOG.info("GDC Pipeline Job completed.");
	}


	public static void main(String[] args) throws Exception {

		Options options = GDCPipelineApplication.getOptions(args);
		CommandLineParser parser = new DefaultParser();

		CommandLine cli = parser.parse(options,args);

		if(cli.hasOption("h") || cli.hasOption("help")){
			GDCPipelineApplication.help(options,0);
		}

		String sourceDirectory="";String jobName="";String outputDirectory="";

		if (cli.hasOption("source")){ sourceDirectory = cli.getOptionValue("source");}
		if (cli.hasOption("output")){ outputDirectory = cli.getOptionValue("output");}
		if (cli.hasOption("job")){ jobName = cli.getOptionValue("job");}

		launchJob(args,sourceDirectory,outputDirectory,jobName);



	}
}
