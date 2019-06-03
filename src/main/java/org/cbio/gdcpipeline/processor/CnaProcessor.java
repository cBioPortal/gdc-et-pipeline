package org.cbio.gdcpipeline.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.cbio.CnaRecord;
import org.cbio.gdcpipeline.model.genomenexus.GenomeNexusEnsemblResponse;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author heinsz
 */
public class CnaProcessor implements ItemProcessor<CnaRecord,CnaRecord>{
    @Value("${cna.genomenexus.endpoint}")
    private String url;

    @Value("${cna.geneId.field}")
    private String geneId;

    private RestTemplate restTemplate = new RestTemplate();
    private static Log LOG = LogFactory.getLog(CnaProcessor.class);

    @Override
    public CnaRecord process(CnaRecord record) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String ensembleGeneId = record.getEnsemblGeneId();
        ensembleGeneId = ensembleGeneId.substring(0, ensembleGeneId.indexOf("."));
        String payload = "?" + geneId + "=" + ensembleGeneId;
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            ResponseEntity<GenomeNexusEnsemblResponse[]> response = restTemplate.exchange(url + payload, HttpMethod.GET, entity, GenomeNexusEnsemblResponse[].class);
            record.setHugoSymbol(response.getBody()[0].getHugoSymbols().get(0));
        }
        catch (Exception e){
            LOG.error("Failed to retreive gene annotation from genome nexus for gene " + ensembleGeneId);
            record.setHugoSymbol(record.getEnsemblGeneId());
        }


        return record;
    }

}
