package uk.co.hermes.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Project {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String activity;

    @JacksonXmlProperty(isAttribute = true)
    private String lastBuildStatus;

    @JacksonXmlProperty(isAttribute = true)
    private String lastBuildLabel;

    @JacksonXmlProperty(isAttribute = true)
    private String lastBuildTime;

    @JacksonXmlProperty(isAttribute = true)
    private String webUrl;

    public Project(Job job, String url, String team) {
        Build nextBuild = job.getNextBuild();
        Build lastBuild = job.getFinishedBuild();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        this.name = job.getPipelineName() + " :: " + job.getName();
        this.webUrl = url + "/teams/" + team + "/pipelines/" + job.getPipelineName() + "/jobs/" + job.getName();
        this.activity = (nextBuild != null && !job.getPaused() && nextBuild.getStartTime() != null)? "Building" : "Sleeping";

        if(lastBuild != null){
            if(lastBuild.getStatus().equalsIgnoreCase("succeeded")){
                this.lastBuildStatus = "Success";
            } else {
                this.lastBuildStatus = "Failure";
            }
            if(lastBuild.getName() != null){
                this.lastBuildLabel = lastBuild.getName();
            }
            if(lastBuild.getStartTime() != null){
                Date date = new Date(lastBuild.getStartTime() * 1000);
                this.lastBuildTime = sdf.format(date);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getActivity() {
        return activity;
    }

    public String getLastBuildStatus() {
        return lastBuildStatus;
    }

    public String getLastBuildLabel() {
        return lastBuildLabel;
    }

    public String getLastBuildTime() {
        return lastBuildTime;
    }
}
