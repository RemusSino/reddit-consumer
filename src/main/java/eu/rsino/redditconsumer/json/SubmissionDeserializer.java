/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eu.rsino.redditconsumer.model.Submission;
import eu.rsino.redditconsumer.model.SubmissionList;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SubmissionDeserializer extends StdDeserializer<SubmissionList> {

    public SubmissionDeserializer() {
        this(null);
    }

    public SubmissionDeserializer(Class<Submission> vc) {
        super(vc);
    }

    @Override
    public SubmissionList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<Submission> result = new LinkedList<>();
        JsonNode children = node.get("data").get("children");
        Iterator<JsonNode> childrenIterator = children.elements();
        while (childrenIterator.hasNext()) {
            Submission.SubmissionBuilder submissionBuilder = Submission.builder();
            JsonNode submissionJsonNode = childrenIterator.next();
            submissionBuilder.title(submissionJsonNode.get("data").get("title").asText())
                    .subreddit(submissionJsonNode.get("data").get("subreddit").asText())
                    .redditId(submissionJsonNode.get("data").get("id").asText());
            result.add(submissionBuilder.build());
        }

        return new SubmissionList(result);
    }
}
