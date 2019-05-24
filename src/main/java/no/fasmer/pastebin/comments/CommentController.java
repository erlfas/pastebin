package no.fasmer.pastebin.comments;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {
    
    private final RabbitTemplate rabbitTemplate;
    private final MeterRegistry meterRegistry;

    public CommentController(RabbitTemplate rabbitTemplate, MeterRegistry meterRegistry) {
        this.rabbitTemplate = rabbitTemplate;
        this.meterRegistry = meterRegistry;
    }
    
    @PostMapping("/comments")
    public Mono<String> addComment(Comment newComment) {
        return Mono.just(newComment)
                .flatMap(comment -> Mono.fromRunnable(() -> {
                    rabbitTemplate.convertAndSend("pastebin", "comments.new", comment);
                }))
                .log("commentController-AddComment")
                .then(Mono.just(newComment))
                .flatMap(comment -> Mono.fromRunnable(() -> {
                    meterRegistry.counter("comments.produced", "pasteId", comment.getPasteId()).increment();
                }))
                .then(Mono.just(newComment))
                .flatMap(comment -> Mono.just("redirect:/pastes/" + comment.getPasteId()));
    }
    
}
