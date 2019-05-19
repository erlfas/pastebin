package no.fasmer.pastebin.comments;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {
    
    private final RabbitTemplate rabbitTemplate;

    public CommentController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @PostMapping("/comments")
    public Mono<String> addComment(Comment newComment) {
        return Mono.just(newComment).flatMap(comment -> Mono.fromRunnable(() -> {
            rabbitTemplate.convertAndSend("pastebin", "comments.new", comment);
        })).log("commentService-Publish").then(Mono.just("redirect:/pastes/" + newComment.getPasteId()));
    }
    
}
