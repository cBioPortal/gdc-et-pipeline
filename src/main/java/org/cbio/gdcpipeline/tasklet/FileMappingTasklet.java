package org.cbio.gdcpipeline.tasklet;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.Sample;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2.TcgaBcr;
import org.cbio.gdcpipeline.model.rest.response.Case;
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

/**
 * @author Dixit Patel
 */

public class FileMappingTasklet implements Tasklet {

    private static Log LOG = LogFactory.getLog(FileMappingTasklet.class);

    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;

    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    @Value("${gdc.api.endpoint}")
    private String GDC_API_ENDPOINT;

    @Value("${gdc.max.response.size}")
    private int MAX_RESPONSE_SIZE;

    private HashMap<String, List<String>> barcodeToSamplesMap = new HashMap<>();
    private HashMap<String, List<String>> uuidToFilesMap = new HashMap<>();
    private RestTemplate restTemplate = new RestTemplate();


    protected void xmlUnmarshall(File xmlFile) throws JAXBException {


        JAXBContext jaxbContext = JAXBContext.newInstance(TcgaBcr.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TcgaBcr tcgaBcr = (TcgaBcr) unmarshaller.unmarshal(xmlFile);

        String case_uuid = tcgaBcr.getPatient().getBcrPatientUuid().getValue();
        case_uuid = case_uuid.toLowerCase();
        List<String> emptyList = new ArrayList<>();
        uuidToFilesMap.put(case_uuid, emptyList);

        String barcode = tcgaBcr.getPatient().getBcrPatientBarcode().getValue();

        List<Sample> samples = tcgaBcr.getPatient().getSamples().getSample();
        List<String> samplesList = new ArrayList<>();
        for (Sample sample : samples) {
            samplesList.add(barcode + "-" + sample.getSampleTypeId().getValue());
        }

        barcodeToSamplesMap.put(barcode, samplesList);

    }


    /*  Payload format
     * {
     * "filters":{
     *          "op":"in",
     *          "content":{
     *                  "field":"cases.case_id",
     *                  "value":["ABC123"]
     *                  }
     *          },
     * "format":"JSON",
     * "fields":"file_name,cases.case_id",
     *
     * }
     */

    //TODO add to props
    protected String buildJsonRequest() {

        JsonObject node = new JsonObject();
        JsonObject filters = new JsonObject();
        JsonObject content = new JsonObject();
        JsonArray values = new JsonArray();

        for (String id : uuidToFilesMap.keySet()) {
            values.add(id);
        }

        content.addProperty("field", "cases.case_id");
        content.add("value", values);

        filters.addProperty("op", "in");
        filters.add("content", content);

        node.add("filters", filters);
        node.addProperty("format", "JSON");
        node.addProperty("fields", "file_name,cases.case_id");

        return node.toString();

    }

    protected ResponseEntity<String> callGdcApi(String url, String payload) {
        if (uuidToFilesMap.isEmpty()) {
            return null;
        }
        LOG.info(" PAYLOAD = " + payload);
        LOG.info("Calling GDC API ");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload.toString(), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response;

    }

    /* Response Format
    {
        "data": {
            "hits": [
               { "file_name": "XYZ.vcf.gz"
                 "cases": [{"case_id": "ABC123"}]
               },
               { .... }
            ]
            "pagination":{...}
        }
    }


     */

    protected GdcApiResponse parseJsonResponse(ResponseEntity<String> response) {
        LOG.info("Parsing API Response ");
        Gson g = new Gson();
        GdcApiResponse r = g.fromJson(response.getBody().toString(), GdcApiResponse.class);
        return r;
    }

    protected String getCaseIdFromResponse(Hits hit) {
        List<Case> caseList = hit.getCases();
        if (caseList.size() == 1) {
            // The only case_id
            return caseList.get(0).getCase_id();
        } else {
            for (Case c : caseList) {
                if (uuidToFilesMap.containsKey(c.getCase_id())) {
                    return c.getCase_id();
                }
            }

        }
        return null;
    }

    protected void addDataToMap(GdcApiResponse resp) {
        //Map : case_id --> file_name

        for (Hits hit : resp.getData().getHits()) {
            //Response is jumbled and can have a file name associated with many unrelated case_id's.
            // E.g  MAF files. Identify the id sent in request
            String case_id = getCaseIdFromResponse(hit);
            uuidToFilesMap.get(case_id).add(hit.getFile_name());

        }
    }

    protected RepeatStatus gdcApiRequest(String payload) {

        int callCount = 0;
        int totalCallCount = 1;
        int from = 1;

        while (callCount != totalCallCount) {
            callCount += 1;
            String url = GDC_API_ENDPOINT + "?from=" + from + "&size=" + MAX_RESPONSE_SIZE;
            LOG.info("API Request = " + url);
            ResponseEntity<String> response = callGdcApi(url, payload);
            if (response == null) {
                LOG.error(" No Case Id's to add to API call");
                return RepeatStatus.FINISHED;
            }
            if (response.getStatusCode() != HttpStatus.OK) {
                LOG.error("Error calling GDC API. Error code is :" + response.getStatusCode().toString());
                //TODO is this correct ? Handle error here
                return RepeatStatus.CONTINUABLE;

            }
            GdcApiResponse res = parseJsonResponse(response);
            addDataToMap(res);

            totalCallCount = res.getData().getPagination().getPages();
            from = callCount * MAX_RESPONSE_SIZE + 1;
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContext, ChunkContext chunkContext) throws Exception {

        long startTime = System.currentTimeMillis();

        File projectDir = new File(sourceDir + File.separator + cancer_study_id);

        List<File> xmlFiles = new ArrayList<>();
        for (File file : projectDir.listFiles()) {
            if (!file.isDirectory()) {
                if (file.getName().endsWith(".xml") && file.getName().contains("biospecimen")) {
                    xmlFiles.add(file);
                }
            }
        }

        LOG.info("Number of Biospecimen Files found = " + xmlFiles.size());

        if (xmlFiles.isEmpty()) {
            throw new Exception("No Biospecimen files found. Job terminated");
        }

        for (File biospecimenXML : xmlFiles) {
            LOG.info("Processing file : " + biospecimenXML.getName());
            //JAXB
            try {
                xmlUnmarshall(biospecimenXML);
            } catch (JAXBException e) {
                LOG.error("Unmarshall error");
                LOG.error(" Skipping File :" + biospecimenXML.getName());

            }
        }

        String payload = buildJsonRequest();

        RepeatStatus status = gdcApiRequest(payload);

        chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext()
                .put("barcodeToSamplesMap", barcodeToSamplesMap);

        chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext()
                .put("uuidToFilesMap", uuidToFilesMap);
        long endTime = System.currentTimeMillis();

        LOG.info("Finished File Mapping Step in " + ((endTime - startTime) / 1000) + " secs");

        return status;
    }

}

