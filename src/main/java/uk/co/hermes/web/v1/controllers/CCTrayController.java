package uk.co.hermes.web.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import uk.co.hermes.web.v1.service.JobService;
import uk.co.hermes.web.v1.service.XMLConverterService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@RestController
public class CCTrayController {

    @Autowired
    private JobService jobService;

    @Autowired
    private SpotterConfiguration configuration;

    @Autowired
    private XMLConverterService xmlConverterService;

    @RequestMapping(
            value = "/cctray.xml",
            produces = MediaType.APPLICATION_XML_VALUE)
    public String cctray(HttpServletRequest request,
                         @RequestParam(value = "team", required = false) String team,
                         @RequestParam(value = "url", required = false) String url)
            throws TransformerException {

        if(request.getHeader("Authorization") != null &&
                !request.getHeader("Authorization").isEmpty()) {
            configuration.setAuthHeaderValue(request.getHeader("Authorization"));
        }

        if(team != null && !team.isEmpty()){
            configuration.setTeam(team);
        }

        if(url != null && !url.isEmpty()){
            configuration.setUrl(url);
        }


        jobService.getJobsFromConcourse();
        Document document =xmlConverterService.convertJobsToProjectXML(jobService.getJobsOnPipelines());
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }


}
