/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eu.rsino.redditconsumer.model.Comment;
import eu.rsino.redditconsumer.model.CommentList;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CommentDeserializer extends StdDeserializer<CommentList> {
    public CommentDeserializer() {
        this(null);
    }

    public CommentDeserializer(Class<Comment> vc) {
        super(vc);
    }

    @Override
    public CommentList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        List<Comment> result = new LinkedList<>();
        if (node.isNull())
            return new CommentList(result);

        Iterator<JsonNode> elementsIterator = node.elements();
        while (node.isArray() && elementsIterator.hasNext()) {
            JsonNode listingJsonNode = elementsIterator.next();
            JsonNode commentListJsonNode = listingJsonNode.get("data").get("children");
            if (!commentListJsonNode.isNull() && commentListJsonNode.isArray()) {
                Iterator<JsonNode> commentListIterator = commentListJsonNode.elements();
                while (commentListIterator.hasNext()) {
                    JsonNode commentJsonNode = commentListIterator.next();
                    if (!"t1".equalsIgnoreCase(commentJsonNode.get("kind").asText())) {
                        continue;
                    }

                    Queue<JsonNode> queue = new LinkedList<>();
                    queue.offer(commentJsonNode);

                    while (!queue.isEmpty()) {
                        commentJsonNode = queue.poll();

                        Comment.CommentBuilder commentBuilder = Comment.builder();
                        commentBuilder.comment(commentJsonNode.get("data").get("body").asText())
                                .redditId(commentJsonNode.get("data").get("id").asText());
                        result.add(commentBuilder.build());
                        JsonNode repliesJsonNode = commentJsonNode.get("data").get("replies");
                        if (!repliesJsonNode.isNull()
                                && repliesJsonNode.get("data") != null
                                && repliesJsonNode.get("data").get("children") != null) {
                            Iterator<JsonNode> repliesIterator = repliesJsonNode.get("data").get("children").elements();
                            while (repliesIterator.hasNext()) {
                                JsonNode replyCommentJsonNode = repliesIterator.next();
                                if (!"t1".equalsIgnoreCase(replyCommentJsonNode.get("kind").asText())) {
                                    continue;
                                }

                                queue.offer(replyCommentJsonNode);
                            }
                        }
                    }
                }
            }
        }

        return new CommentList(result);
    }
}