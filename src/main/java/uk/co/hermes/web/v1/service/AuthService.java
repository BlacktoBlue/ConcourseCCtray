package uk.co.hermes.web.v1.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.hermes.web.v1.controllers.SpotterConfiguration;

@Component
public class AuthService {

    private String authToken;

    private SpotterConfiguration configuration;

    @Autowired
    public AuthService(final SpotterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Cacheable("AuthToken")
    public void authenticate(){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", configuration.getAuthHeaderValue());

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(configuration.getAPITeamURL() + "/auth/token",
                HttpMethod.GET, entity, String.class);

        this.authToken = new JSONObject(response.getBody()).getString("value");
    }

    String getAuthToken() {
        return authToken;
    }
}
