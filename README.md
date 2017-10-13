
GDC ET Pipeline is a spring-batch based tool that transforms cancer genomic data, available from NCI's GDC repository, into appropriate file formats that can be loaded into cBioPortal tool. 

This project is developed as a part of Google Summer of Code program.<br> 
Mentors for the project : Zachary Heins, Benjamin Gross, Angelica Ochoa

A primer for the entire pipeline architecture can be found in the slides [here](https://drive.google.com/open?id=0BxMXiAE6vrzjQ0tzQk93Nk5MSDA). All discussions related to design and development of the pipeline can be found in the meeting minutes [here](https://docs.google.com/document/d/1HfelJh3HJ7AEx4f0Nm1RcIb5ddH5HyKHDIcoK4vIrFM/edit?usp=sharing).

<h3>GDC ET Pipeline</h3>
The pipeline expects a manifest file in order to know the data files it must process. More details on manifest file can be found on [GDC Data portal](https://docs.gdc.cancer.gov/Data_Transfer_Tool/Users_Guide/Preparing_for_Data_Download_and_Upload/). The batch expects the actual data files to be downloaded by the user from the GDC repository. 

There are several datatypes that are hosted at GDC and the scope for this project was to support processing of **Clinical** and **Mutation datatype files**. 

<h4>Brief overview of Steps : </h4>
The batch runs in several steps each accomplishing a different task. Current implementation stage of each step is mentioned which can be further extended upon.

1. Setup Pipeline Step : <br> Validates source and output directories.
2. Process Manifest File Step : <br>This step first parses the manifest file for file names and makes a REST call to GDC API in order to retrieve metadata such as file_id, data_format, etc of these files. This metadata is required in further steps while processing those data files.
3. GDC datatype Step : <br> From this step onwards, the data files will processed accordingly, which can be a single datatype or all currently supported datatypes depending on user input. 

<h4>Running the batch</h4>
The batch has some required options as well as optional parameters that user can provide. 

    $JAVA_HOME org.cbio.gdcpipeline.GDCPipelineApplication -<option>
    List of options : <br>
    -c,--cancer_study_id <arg>             Cancer Study Id 
    -d,--datatypes <arg>                   Datatypes to run. Default is All
    -f,--filter_normal_sample <arg>        True or False. Flag to filter
                                           normal samples. Default is True
    -h,--help                              shows this help document and
                                           quits.
    -i,--isoformOverrideSource <arg>       Isoform Override Source. Default
                                           is 'uniprot'
    -m,--manifest_file <arg>               Manifest file path
    -o,--output <arg>                      output directory for files
    -s,--source <arg>                      source directory for files
    -separate_mafs,--separate_mafs <arg>   True or False. Process MAF files
                                           individually or merge together.
                                           Default is False 









