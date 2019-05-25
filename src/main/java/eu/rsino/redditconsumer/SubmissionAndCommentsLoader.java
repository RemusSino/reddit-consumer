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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SubmissionAndCommentsLoader {
    private AtomicReference<String> lastSubmissionId = new AtomicReference<>("");
    private AtomicReference<String> lastCommentId = new AtomicReference<>("");
    @Value("${subreddits}")
    private List<String> subreddits;
    private CommentRetriever commentConsumer;
    private SubmissionRetriever submissionConsumer;
    private CommentRepository commentRepository;
    private SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionAndCommentsLoader(CommentRetriever commentConsumer,
                                       SubmissionRetriever submissionConsumer,
                                       CommentRepository commentRepository,
                                       SubmissionRepository submissionRepository) {
        this.commentConsumer = commentConsumer;
        this.submissionConsumer = submissionConsumer;
        this.commentRepository = commentRepository;
        this.submissionRepository = submissionRepository;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    public void loadSubmissionAndComments() {
        log.info("Starting scheduled run at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        List<Submission> submissions = getSubmissionsFromReddit();
        log.info("Found " + submissions.size() + " submissions");
        saveSubmissionsToDb(submissions);

        List<Comment> comments = getCommentsFromReddit(submissions);
        log.info("Found " + comments.size() + " comments");
        saveCommentsToDb(comments);
    }

    private void saveCommentsToDb(List<Comment> comments) {
        if (!CollectionUtils.isEmpty(comments)) {
            lastCommentId.set(comments.get(0).getRedditId());
            commentRepository.saveAll(comments);
        }
    }

    private List<Comment> getCommentsFromReddit(List<Submission> submissions) {
        return submissions.parallelStream()
                .map((Submission submission) -> commentConsumer.getComments(submission,
                        "t1_" + lastCommentId.get()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void saveSubmissionsToDb(List<Submission> submissions) {
        if (!CollectionUtils.isEmpty(submissions)) {
            lastSubmissionId.set(submissions.get(0).getRedditId());
            submissionRepository.saveAll(submissions);
        }
    }

    private List<Submission> getSubmissionsFromReddit() {
        return subreddits.parallelStream()
                .map((String subreddit) -> submissionConsumer.getSubmissions(subreddit,
                        "t3_" + lastSubmissionId.get()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
