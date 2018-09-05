package uk.co.hermes.web.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.hermes.model.Build;
import uk.co.hermes.model.Job;
import uk.co.hermes.model.Pipeline;
import uk.co.hermes.web.v1.controllers.SpotterConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JobService {
    private SpotterConfiguration configuration;
    private AuthService authService;
    private List<Job> jobsOnPipelines;
    private int buildCounter = 5;

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    public JobService(final SpotterConfiguration configuration, final AuthService authService) {
        this.configuration = configuration;
        this.authService = authService;
    }

    public void getJobsFromConcourse() {
        jobsOnPipelines = new ArrayList<>();

        authService.authenticate();

        List<Pipeline> pipelines = pipelineService.getPipelinesFromConcourse();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Cookie", "ATC-Authorization=\"Bearer " + authService.getAuthToken() + "\"");

        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        pipelines.forEach(pipeline -> {
            ResponseEntity<String> response;
            headers.set("Cookie", "ATC-Authorization=\"Bearer " + authService.getAuthToken() + "\"");
            response = restTemplate.exchange(configuration.getAPITeamURL() + "/pipelines/" + pipeline.getName() + "/jobs",
                    HttpMethod.GET, entity, String.class);

            JSONArray results = new JSONArray(response.getBody());

            for (Object job : results) {
                Job _job = null;
                try {
                    _job = objectMapper.readValue(job.toString(), Job.class);
                    this.buildCounter = 5;
                    if(_job.getFinishedBuild().getStatus().equalsIgnoreCase("aborted") && !configuration.showAbortedAsFailed()){
                        Build finishedBuild = getLastNonAbortedBuildFromConcourse(pipeline.getName(), _job.getName() , Integer.parseInt(_job.getFinishedBuild().getName()) - 1);
                        if (finishedBuild != null) {
                            _job.setFinishedBuild(finishedBuild);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jobsOnPipelines.add(_job);
            }
        });
    }

    private Build getLastNonAbortedBuildFromConcourse(String pipelineName, String jobName, int buildNo) throws IOException{
        this.buildCounter -= 1;
        authService.authenticate();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("Cookie", "ATC-Authorization=\"Bearer " + authService.getAuthToken() + "\"");

        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<String> response;
        headers.set("Cookie", "ATC-Authorization=\"Bearer " + authService.getAuthToken() + "\"");
        response = restTemplate.exchange(configuration.getAPITeamURL() + "/pipelines/" + pipelineName + "/jobs/" + jobName + "/builds/" + buildNo,
                HttpMethod.GET, entity, String.class);

        JSONObject results = new JSONObject(response.getBody());

        Build build = objectMapper.readValue(results.toString(), Build.class);
        if (build.getStatus().equalsIgnoreCase("aborted") && this.buildCounter > 0) {
            return getLastNonAbortedBuildFromConcourse(pipelineName, jobName, buildNo - 1);
        }
        return build;
    }

        public List<Job> getJobsOnPipelines() {
        return jobsOnPipelines;
    }
}
