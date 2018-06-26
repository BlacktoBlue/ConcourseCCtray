package uk.co.hermes.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class Projects {

    @JacksonXmlElementWrapper(localName = "Card")
    @JsonProperty("Card")
    private ArrayList<Project> projects;

    public Projects(List<Job> jobs, String url, String team) {
        this.projects = new ArrayList<>();
        jobs.forEach(job -> this.projects.add(new Project(job, url, team)));
    }

    @JsonGetter
    public ArrayList<Project> getProject() {
        return projects;
    }

    @JsonSetter
    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
