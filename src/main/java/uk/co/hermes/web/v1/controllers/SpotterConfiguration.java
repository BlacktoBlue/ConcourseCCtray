package uk.co.hermes.web.v1.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpotterConfiguration {

    @Value("${concourse.username}")
    private String username;

    @Value("${concourse.password}")
    private String password;

    @Value("${concourse.url}")
    private String url;

    @Value("${concourse.team}")
    private String team;

    @Value("${showAbortedAsFailed}")
    private boolean showAbortedAsFailed;

    public String getAuthHeaderValue(String username, String password){
            String usernameAndPassword = username + ":" + password;
            return "Basic " + java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
    }

    public String getUrl() {
        return url;
    }

    public String getTeam() {
        return team;
    }

    public String getAPITeamsURL(){
        return url + "/api/v1/teams/";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean showAbortedAsFailed() {
        return showAbortedAsFailed;
    }
}
