package uk.co.hermes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {

    @JsonProperty(value = "id", required = true)
    private int id;

    @JsonProperty(value = "team_name", required = true)
    private String teamName;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(required = true)
    private String status;

    @JsonProperty(value = "job_name", required = true)
    private String jobName;

    @JsonProperty(value = "pipeline_name", required = true)
    private String pipelineName;

    @JsonProperty(value = "start_time", required = true)
    private Long startTime;

    @JsonProperty(value = "end_time")
    private Long endTime;

    public int getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getJobName() {
        return jobName;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}
