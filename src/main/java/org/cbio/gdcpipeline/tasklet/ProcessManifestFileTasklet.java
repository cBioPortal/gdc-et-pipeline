package org.cbio.gdcpipeline.tasklet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.ManifestFileData;
import org.cbio.gdcpipeline.model.rest.response.GdcApiResponse;
import org.cbio.gdcpipeline.model.rest.response.Hits;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dixit Patel
 */
public class ProcessManifestFileTasklet implements Tasklet {
    @Value("#{jobParameters[manifest_file]}")
    private String manifest_file;

    @Value("${gdc.api.files.endpoint}")
    private String GDC_API_FILES_ENDPOINT;

    @Value("${gdc.max.response.size}")
    private int MAX_RESPONSE_SIZE;

    private static Log LOG = LogFactory.getLog(ProcessManifestFileTasklet.class);
    private List<ManifestFileData> manifestFileList = new ArrayList<>();
    private RestTemplate restTemplate = new RestTemplate();
    private List<Hits> gdcFileMetadatas = new ArrayList<>();

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        FlatFileItemReader<ManifestFileData> reader = new FlatFileItemReader<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
        try (BufferedReader buff = new BufferedReader(new FileReader(manifest_file))) {
            // header line
            String header = buff.readLine();
            tokenizer.setNames(header.split(DelimitedLineTokenizer.DELIMITER_TAB));
        }
        DefaultLineMapper<ManifestFileData> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(manifestFieldSetMapper());
        reader.setResource(new FileSystemResource(manifest_file));
        reader.setLineMapper(lineMapper);
        reader.setLinesToSkip(1);
        reader.open(new ExecutionContext());
        ManifestFileData record = null;
        try {
            while ((record = reader.read()) != null) {
                manifestFileList.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ItemStreamException("Error reading manifest record : "+record.toString());
        }
        reader.close();
        String payload = buildJsonRequest();
        gdcApiRequest(payload);

        chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext()
                .put("gdcFileMetadatas", gdcFileMetadatas);

        return RepeatStatus.FINISHED;
    }

    private FieldSetMapper<ManifestFileData> manifestFieldSetMapper() {
        return (FieldSetMapper<ManifestFileData>) (FieldSet fs) -> {
            ManifestFileData record = new ManifestFileData();
            for (String header : record.getHeader()) {
                try {
                    record.getClass().getMethod("set" + header, String.class).invoke(record, fs.readString(header.toLowerCase()));
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.error(" Error in setting record for :" + header);
                    }
                    e.printStackTrace();
                }
            }
            return record;
        };
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
     * "fields":"file_name,cases.case_id,data_format",
     *
     * }
     */

    protected String buildJsonRequest() {
        JsonObject node = new JsonObject();
        JsonObject filters = new JsonObject();
        JsonObject content = new JsonObject();
        JsonArray values = new JsonArray();
        for (ManifestFileData record : manifestFileList) {
            values.add(record.getId());
        }
        content.addProperty("field", "file_id");
        content.add("value", values);
        filters.addProperty("op", "in");
        filters.add("content", content);

        node.add("filters", filters);
        node.addProperty("format", "JSON");
        node.addProperty("fields", "file_id,file_name,cases.case_id,type,data_format");
        return node.toString();
    }

    protected void gdcApiRequest(String payload) throws Exception {
        int callCount = 0;
        int totalCallCount = 1;
        int from = 0;

        while (callCount < totalCallCount) {
            callCount += 1;
            String url = GDC_API_FILES_ENDPOINT + "?from=" + from + "&size=" + MAX_RESPONSE_SIZE;
            if (LOG.isInfoEnabled()) {
                LOG.info(" Calling GDC API : " + url);
                LOG.info(" Payload : " + payload);
            }
            GdcApiResponse res = callGdcApi(url, payload);
            totalCallCount = res.getData().getPagination().getPages();
            if (totalCallCount == 0) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Last API Request returned 0 results");
                }
                throw new Exception(" Last API Request returned 0 results ");
            }
            gdcFileMetadatas.addAll(res.getData().getHits().stream().collect(Collectors.toList()));
            from = callCount * MAX_RESPONSE_SIZE;
        }
    }

    protected GdcApiResponse callGdcApi(String url, String payload) throws Exception {
        if (manifestFileList.isEmpty()) {
            if (LOG.isErrorEnabled()) {
                LOG.error(" No Case Id's to add to API call");
            }
            throw new Exception();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload.toString(), httpHeaders);

        ResponseEntity<GdcApiResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, GdcApiResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error calling GDC API. Response code is :" + response.getStatusCodeValue()
                        + " Response Message is : " + response.getStatusCode().getReasonPhrase());
            }
            throw new Exception();

        }
        return response.getBody();
    }
}

