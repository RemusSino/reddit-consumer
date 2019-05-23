/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer.repo;

import eu.rsino.redditconsumer.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
