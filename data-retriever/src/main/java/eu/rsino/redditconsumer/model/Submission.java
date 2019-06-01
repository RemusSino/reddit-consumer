/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@ToString
@Document("submission")
@Getter
public class Submission {
    @Indexed
    private String subreddit;
    private String redditId;
    private String title;
    @Indexed
    private long creationTimestamp;
}
