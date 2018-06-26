package uk.co.hermes.web.v1.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.hermes.model.Pipeline;
import uk.co.hermes.web.v1.controllers.SpotterConfiguration;

import java.util.ArrayList;
import java.util.List;

@Component
public class PipelineService {

    private SpotterConfiguration configuration;
    private AuthService authService;

    @Autowired
    public PipelineService(final SpotterConfiguration configuration, final AuthService authService) {
        this.configuration = configuration;
        this.authService = authService;
    }

    @Cacheable("Pipelines")
    public List<Pipeline> getPipelinesFromConcourse() {
        List<Pipeline> pipelines = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Cookie", "ATC-Authorization=\"Bearer " + authService.getAuthToken() + "\"");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response;

        headers.set("Cookie", "ATC-Authorization=\"Bearer " + authService.getAuthToken() + "\"");
        response = restTemplate.exchange(configuration.getAPITeamURL() + "/pipelines",
                HttpMethod.GET, entity, String.class);

        JSONArray results = new JSONArray(response.getBody());

        for (Object _pipeline : results) {
            pipelines.add(new Pipeline((JSONObject) _pipeline));
        }
        return pipelines;
    }
}
