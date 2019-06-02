/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@ToString
@Document("comment")
@Getter
public class Comment {
    private String redditId;
    @TextIndexed
    private String comment;
    @Indexed
    private String subreddit;
    @Indexed
    private long creationTimestamp;
}
