package uk.co.hermes.web.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.co.hermes.model.Build;
import uk.co.hermes.model.Job;
import uk.co.hermes.web.v1.controllers.SpotterConfiguration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class XMLConverterService {
    private SpotterConfiguration configuration;

    @Autowired
    public XMLConverterService(final SpotterConfiguration configuration) {
        this.configuration = configuration;
    }

    public Document convertJobsToProjectXML(List<Job> jobs){
        try {
            List<Job> _jobs = new ArrayList<>(jobs);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Projects");
            doc.appendChild(rootElement);

            for (Job job: _jobs) {
                Build nextBuild = job.getNextBuild();
                Build lastBuild = job.getFinishedBuild();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                String name = job.getPipelineName() + " :: " + job.getName();
                String webUrl = configuration.getUrl() + "/teams/" + configuration.getTeam() + "/pipelines/" + job.getPipelineName() + "/jobs/" + job.getName();
                String activity;
                String lastBuildStatus;
                String lastBuildLabel;
                String lastBuildTime;

                if (nextBuild != null && !job.getPaused() && nextBuild.getStartTime() != null){
                    activity = "Building";
                } else {
                    activity = "Sleeping";
                }


                Element project = doc.createElement("Project");
                rootElement.appendChild(project);
                project.setAttribute("name", name);
                project.setAttribute("activity", activity);

                if(lastBuild != null){
                    if(lastBuild.getStatus().equalsIgnoreCase("succeeded")){
                        lastBuildStatus = "Success";
                    } else {
                        lastBuildStatus = "Failure";
                    }
                    project.setAttribute("lastBuildStatus", lastBuildStatus);
                    if(lastBuild.getName() != null){
                        lastBuildLabel = lastBuild.getName();
                        project.setAttribute("lastBuildLabel", lastBuildLabel);
                    }
                    if(lastBuild.getStartTime() != null){
                        Date date = new Date(lastBuild.getStartTime() * 1000);
                        lastBuildTime = sdf.format(date);
                        project.setAttribute("lastBuildTime", lastBuildTime);
                    }
                }
                project.setAttribute("webUrl", webUrl);


            }
            return doc;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        return null;
    }
}
