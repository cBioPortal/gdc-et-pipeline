package org.cbio.gdcpipeline;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dixit Patel
 */
@SpringBootApplication
public class GDCPipelineApplication {
	private static Log LOG = LogFactory.getLog(GDCPipelineApplication.class);
	private final static String GDC_JOB = "gdcJob";
	private final static String DEFAULT_DATATYPES = "ALL";
	private final static String DEFAULT_FILTER_NORMAL_SAMPLE = "true";

    private static Options getOptions(String []input){
		Options options = new Options();
        options.addRequiredOption("s","source",true,"source directory for files");
		options.addRequiredOption("o","output",true,"output directory for files");
		options.addRequiredOption("c", "cancer_study_id", true, "Cancer Study Id");
		options.addRequiredOption("m","manifest_file",true,"Manifest file path");
        options.addOption("f", "filter_normal_sample", true, "True or False. Flag to filter normal samples. Default is True ");
		options.addOption("d", "datatypes", true, "Datatypes to run. Default is All");
		options.addOption("h", "help", true, "shows this help document and quits.");
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

	private static void launchJob(String[] args, String sourceDirectory, String outputDirectory, String cancer_study_id, String manifest_file, String filter_normal_sample, String datatypes) throws Exception {
		SpringApplication app = new SpringApplication(GDCPipelineApplication.class);
		ConfigurableApplicationContext ctx= app.run(args);
		Job gdcJob = ctx.getBean(GDC_JOB, Job.class);
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("sourceDirectory", sourceDirectory)
				.addString("outputDirectory", outputDirectory)
                .addString("cancer_study_id", cancer_study_id)
                .addString("manifest_file",manifest_file)
				.addString("filter_normal_sample", filter_normal_sample)
				.addString("datatypes", datatypes)
				.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(gdcJob, jobParameters);
	}

	public static void main(String[] args) throws Exception {
		Options options = GDCPipelineApplication.getOptions(args);
		CommandLineParser parser = new DefaultParser();
		CommandLine cli = parser.parse(options,args);
		if(cli.hasOption("h") || cli.hasOption("help")){
			GDCPipelineApplication.help(options, 0, "");
		}
		String datatypes = DEFAULT_DATATYPES;
		if (cli.hasOption("datatypes")) {
			datatypes = cli.getOptionValue("datatypes");
		}
		String filter_normal_sample = DEFAULT_FILTER_NORMAL_SAMPLE;
		if (cli.hasOption("filter_normal_sample")) {
			if (cli.getOptionValue("filter_normal_sample").toLowerCase().equals("false")) {
				filter_normal_sample = "false";
			} else if (!cli.getOptionValue("filter_normal_sample").toLowerCase().equals("true")) {
				GDCPipelineApplication.help(options, 0, "Filter Option must either be True or False");
			}
		}
		launchJob(args, cli.getOptionValue("source"), cli.getOptionValue("output"), cli.getOptionValue("cancer_study_id"), cli.getOptionValue("manifest_file"), filter_normal_sample, datatypes);
	}
}
