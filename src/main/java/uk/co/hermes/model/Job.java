package uk.co.hermes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    @JsonProperty(value = "id", required = true)
    private int id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "pipeline_name", required = true)
    private String pipelineName;

    @JsonProperty(value = "team_name", required = true)
    private String teamName;

    @JsonProperty(value = "paused", defaultValue = "false")
    private boolean paused;

    @JsonProperty(value = "next_build")
    private Build nextBuild;

    @JsonProperty(value = "finished_build")
    private Build finishedBuild;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public String getTeamName() {
        return teamName;
    }

    public Boolean getPaused() {
        return paused;
    }

    public Build getNextBuild() {
        return nextBuild;
    }

    public Build getFinishedBuild() {
        return finishedBuild;
    }

    public void setFinishedBuild(Build finishedBuild) {
        this.finishedBuild = finishedBuild;
    }
}
