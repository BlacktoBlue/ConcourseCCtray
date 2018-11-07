package uk.co.hermes.web.v1.service;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

import java.util.TimeZone;

@Component
public class AuthService {

    private String authToken;

    private DateTime expirartionDate;

    private SpotterConfiguration configuration;

    @Autowired
    public AuthService(final SpotterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Cacheable("AuthToken")
    public void authenticate(String teamName, String username, String password){

        getAuthentication(teamName, username, password);
        String[] authTokenSplit = authToken.split("\\.");

        byte[] byteArray = Base64.decodeBase64(authTokenSplit[1].getBytes());
        String decodedString = new String(byteArray);
        JSONObject obj = new JSONObject(decodedString);
        Long val = obj.getLong("exp");
        expirartionDate = new DateTime(val * 1000, DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")));

    }

    private void getAuthentication(String teamName, String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", configuration.getAuthHeaderValue(username, password));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(configuration.getAPITeamsURL() + teamName + "/auth/token",
                HttpMethod.GET, entity, String.class);

        this.authToken = new JSONObject(response.getBody()).getString("value");
    }

    String getAuthToken(String username, String password) {
        if (this.expirartionDate.minusHours(2).isBeforeNow()){
            getAuthentication(configuration.getTeam(), username, password);
        }
        return authToken;
    }
}
