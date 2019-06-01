/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.rsino.redditconsumer.json.CommentDeserializer;
import eu.rsino.redditconsumer.json.SubmissionDeserializer;
import eu.rsino.redditconsumer.model.CommentListWrapper;
import eu.rsino.redditconsumer.model.SubmissionListWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        CommentDeserializer commentDeserializer = new CommentDeserializer();
        SubmissionDeserializer submissionDeserializer = new SubmissionDeserializer();
        module.addDeserializer(CommentListWrapper.class, commentDeserializer);
        module.addDeserializer(SubmissionListWrapper.class, submissionDeserializer);
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
