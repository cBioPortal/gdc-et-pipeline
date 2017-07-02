package org.cbio.gdcpipeline;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
@EnableBatchProcessing
public class GDCPipelineApplication {


	private static Log LOG = LogFactory.getLog(GDCPipelineApplication.class);

	private final static String MAIN_JOB = "mainJob";

	private static Options getOptions(String []input){
		Options options = new Options();
        options.addOption("s","source",true,"source directory for files");
		options.addOption("o","output",true,"output directory for files");
		options.addOption("study", "study", true, "Cancer Study Id");
		options.addOption("filter_sample", "filter_sample", false, "True or False. Flag to filter Normal samples. Default is True ");
		options.addOption("h", "help", false, "shows this help document and quits.");
		return options;
	}

	private static void help(Options gnuOptions, int exitStatus, String opt) {
		HelpFormatter helpFormatter = new HelpFormatter();
		if (!opt.isEmpty()) {
			System.out.println("\'" + opt + "\'" + " option is required.");
		}
		helpFormatter.printHelp(" Pipeline Options ", gnuOptions);
		System.exit(exitStatus);
	}

	private static void launchJob(String[] args, String sourceDirectory, String outputDirectory, String study, String filter_sample) throws Exception {
		SpringApplication app = new SpringApplication(GDCPipelineApplication.class);
		ApplicationContext ctx= app.run(args);
		Job mainJob = ctx.getBean(MAIN_JOB, Job.class);
		long t = System.currentTimeMillis();
		//TODO remove time
		String time = String.valueOf(t);

		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("sourceDirectory", sourceDirectory)
				.addString("outputDirectory", outputDirectory)
                .addString("study", study)
				.addString("filter_sample", filter_sample)
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
			GDCPipelineApplication.help(options, 0, "");
		}

		if (!cli.hasOption("source")) {
			GDCPipelineApplication.help(options, 0, "source");
		}
		if (!cli.hasOption("output")) {
			GDCPipelineApplication.help(options, 0, "output");
		}
		if (!cli.hasOption("study")) {
			GDCPipelineApplication.help(options, 0, "study");
		}

		//TODO create a common validator
		String filter_sample = "true";
		if (cli.hasOption("filter_sample")) {
			if (cli.getOptionValue("filter_sample").toLowerCase().equals("false")) {
				filter_sample = "false";
			} else if (!cli.getOptionValue("filter_sample").toLowerCase().equals("true")) {
				GDCPipelineApplication.help(options, 0, "Filter Option must either be True or False");
			}
		}

		launchJob(args, cli.getOptionValue("source"), cli.getOptionValue("output"), cli.getOptionValue("study"), filter_sample);

	}
}
