/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.rsino.redditconsumer.model.Submission;
import eu.rsino.redditconsumer.model.SubmissionListWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class SubmissionRetriever {
    private static String url = "https://www.reddit.com/r/%s/new/.json?limit=100&before=%s";
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public SubmissionRetriever(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Submission> getSubmissions(String subreddit, String lastSubmissionFullName) {
        log.info("Retrieving submissions for subreddit: " + subreddit);
        //todo: do with after=
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-agent", "Chrome");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> submissionsResponse = restTemplate.exchange(String.format(url, subreddit, lastSubmissionFullName),
                HttpMethod.GET, httpEntity, String.class);
        if (!submissionsResponse.getStatusCode().is2xxSuccessful()
                && submissionsResponse.hasBody())
            return Collections.emptyList();

        try {
            return objectMapper.readValue(submissionsResponse.getBody(), SubmissionListWrapper.class).get();
        } catch (IOException e) {
            log.error("Exception when deserialize submission response " + submissionsResponse.getBody(), e);
            return Collections.emptyList();
        }
    }
}
