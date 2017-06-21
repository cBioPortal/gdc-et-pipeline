package org.cbio.gdcpipeline.tasklet;


import com.google.gson.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.Sample;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.TcgaBcr;
import org.cbio.gdcpipeline.model.rest.response.GdcApiResponse;
import org.cbio.gdcpipeline.model.rest.response.Hits;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

   /*
     1. Get all TCGA barcodes from biospecimen files alongwith all sample-ids
        Add barcodes to a list and also to a map with sample_ids
     2. Use list to call GDC API and create a map for each barcode with filenames
     3. Add both maps to execution context
     */

public class FileMappingTasklet implements Tasklet {

    private static Log LOG = LogFactory.getLog(FileMappingTasklet.class);

    //TODO get from GDC file name ?
    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    private List<String> barcodes = new ArrayList<>();
    private List<String> case_uuid_list = new ArrayList<>();

    //TODO add to props
    private final static String GDC_API_ENDPOINT = "https://api.gdc.cancer.gov/files";

    private HashMap<String, List<String>> barcodeToSamples = new HashMap<>();
    private HashMap<String, List<String>> uuidToFiles = new HashMap<>();
    private RestTemplate restTemplate = new RestTemplate();


    private void xmlUnmarshall(File xmlFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TcgaBcr tcgaBcr = (TcgaBcr) unmarshaller.unmarshal(xmlFile);

        String case_uuid = tcgaBcr.getPatient().getBcrPatientUuid().getValue();
        case_uuid = case_uuid.toLowerCase();
        case_uuid_list.add(case_uuid);

        String barcode = tcgaBcr.getPatient().getBcrPatientBarcode().getValue();
        barcodes.add(barcode);

        List<Sample> samples = tcgaBcr.getPatient().getSamples().getSample();
        List<String> samplesList = new ArrayList<>();
        for (Sample sample : samples) {
            samplesList.add(barcode + "-" + sample.getSampleTypeId().getValue());
        }

        barcodeToSamples.put(barcode, samplesList);

    }


    /**
     * {
     * "filters":{
     * "op":"in",
     * "content":{
     * "field":"cases.case_id",
     * "value":[
     * "784de7ac-8424-42eb-83d4-a1bebaa42b97"
     * ]
     * }
     * },
     * "format":"JSON",
     * "fields":"file_name",
     * "size":"5"
     * }
     **/

    //TODO add to props
    private String buildJSON() {

        JsonObject node = new JsonObject();
        JsonObject filters = new JsonObject();
        JsonObject content = new JsonObject();
        JsonArray values = new JsonArray();

        for (String id : case_uuid_list) {
            values.add(id);
        }

        content.addProperty("field", "cases.case_id");
        content.add("value", values);

        filters.addProperty("op", "in");
        filters.add("content", content);

        node.add("filters", filters);
        node.addProperty("format", "JSON");
        node.addProperty("fields", "file_name,cases.case_id");
        node.addProperty("size", "100");

        return node.toString();

    }

    private ResponseEntity<String> callGDCApi() {
        if (case_uuid_list.isEmpty()) {
            return null;
        }

        String payload = buildJSON();
        LOG.info(" PAYLOAD = " + payload);
        LOG.info("Calling GDC API ");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload.toString(), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(GDC_API_ENDPOINT, HttpMethod.POST, entity, String.class);
        return response;

    }

    /*
    {
        "data": {
            "hits": [{
                "file_name": "a7b9df41-37f7-4f9b-ad86-2d60224ada26.vep.reheader.vcf.gz"
                "cases": [{
                        "case_id": "6e7d5ec6-a469-467c-b748-237353c23416"
                         }]
            }]
            "pagination":{...}
        }
    }


     */

    //TODO revise parsing

    private GdcApiResponse parseJsonResponse(ResponseEntity<String> response){
//        JsonParser jsonParser = new JsonParser();
//        JsonElement jsonTree = jsonParser.parse(response.getBody().toString());
//        if(jsonTree.isJsonObject()){
//            JsonObject node = jsonTree.getAsJsonObject();
//            LOG.info(node.toString());
//            JsonElement j1 = node.get("data");
//            JsonObject j2 = j1.getAsJsonObject();
//            JsonElement j3 = j2.get("hits");
//            JsonArray subList = j3.getAsJsonArray();
//            JsonObject j4 = subList.getAsJsonObject();
//
//            JsonObject fileName=j4.get("file_name").getAsJsonObject();
//            JsonArray caseList = j4.get("cases").getAsJsonArray();
//            JsonObject caseId = caseList.getAsJsonObject().get("case_id").getAsJsonObject();
//
//
//            System.exit(0);
//        }
//        System.exit(0);

        LOG.info("Parsing API Response ");

        Gson g = new Gson();
        GdcApiResponse r = g.fromJson(response.getBody().toString(),GdcApiResponse.class);
        return r;



    }

    @Override
    public RepeatStatus execute(StepContribution stepContext, ChunkContext chunkContext) throws Exception {

        long startTime = System.currentTimeMillis();

        //TODO Assuming all files are in a single directory
        File dir = new File(sourceDir);

        List<File> xmlFiles = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                if (file.getName().endsWith(".xml") && file.getName().contains("biospecimen")) {
                    xmlFiles.add(file);
                }
            }
        }

        LOG.info("Number of Biospecimen Files found = " + xmlFiles.size());

        for (File biospecimenXML : xmlFiles) {
            LOG.info("Proccesing file : " + biospecimenXML.getName());
            //JAXB
            try {
                xmlUnmarshall(biospecimenXML);
            } catch (JAXBException e) {
                LOG.error("Unmarshall error");
            }
        }

        //Call GDC API with barcodes

        ResponseEntity<String> response = callGDCApi();
        if (response == null) {
            LOG.error(" No Case Id's to add to API call");
            return RepeatStatus.FINISHED;
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            LOG.error("Error calling GDC API. Error code is :" + response.getStatusCode().toString());
            // System.exit(0);
            //TODO is this correct ? Handle error here
            return RepeatStatus.CONTINUABLE;

        }

        GdcApiResponse resp = parseJsonResponse(response);

        //TODO work on Pagination

        // Add to map
        for(Hits hit : resp.getData().getHits()){
            String case_id = hit.getCases().get(0).getCase_id();
            if(uuidToFiles.containsKey(case_id)){
                uuidToFiles.get(case_id).add(hit.getFile_name());
            }
            else{
                List<String> fileNames = new ArrayList<>();
                fileNames.add(hit.getFile_name());
                uuidToFiles.put(case_id,fileNames);
            }
        }


        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("barcodeToSamplesMap", barcodeToSamples);

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("uuidToFilesMap", uuidToFiles);
        long endTime = System.currentTimeMillis();

        LOG.info("Finished File Mapping Step in "+((endTime-startTime)/1000)+" secs");

        return RepeatStatus.FINISHED;
    }
}

