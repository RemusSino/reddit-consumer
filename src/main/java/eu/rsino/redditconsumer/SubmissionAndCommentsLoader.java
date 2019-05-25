/*
 * (C) Copyright 2015-2017 Trivadis AG. All rights reserved.
 */
package eu.rsino.redditconsumer;

import eu.rsino.redditconsumer.consumer.CommentRetriever;
import eu.rsino.redditconsumer.consumer.SubmissionRetriever;
import eu.rsino.redditconsumer.model.Comment;
import eu.rsino.redditconsumer.model.Submission;
import eu.rsino.redditconsumer.repo.CommentRepository;
import eu.rsino.redditconsumer.repo.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubmissionAndCommentsLoader {

    @Value("${subreddits}")
    private List<String> subreddits;
    private CommentRetriever commentConsumer;
    private SubmissionRetriever submissionConsumer;
    private CommentRepository commentRepository;
    private SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionAndCommentsLoader(CommentRetriever commentConsumer,
                                       SubmissionRetriever submissionConsumer, CommentRepository commentRepository, SubmissionRepository submissionRepository) {
        this.commentConsumer = commentConsumer;
        this.submissionConsumer = submissionConsumer;
        this.commentRepository = commentRepository;
        this.submissionRepository = submissionRepository;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 60000)
    public void loadSubmissionAndComments() {
        List<Submission> submissions = subreddits.stream()
                .map(submissionConsumer::getSubmissions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        submissionRepository.saveAll(submissions);

        List<Comment> comments = submissions.stream()
                .map(commentConsumer::getComments)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        commentRepository.saveAll(comments);
    }
}
