package no.fasmer.pastebin.comments;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentWriterRepository commentWriterRepository;

    public CommentService(CommentWriterRepository commentWriterRepository1) {
        this.commentWriterRepository = commentWriterRepository1;
    }
    
    @RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(value = "pastebin"), key = "comments.new"))
    public void save(Comment comment) {
        commentWriterRepository
                .save(comment)
                .log("commentService-save")
                .subscribe();
        
    }
    
    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    CommandLineRunner setUp(MongoOperations mongoOperations) {
        return args -> {
            mongoOperations.dropCollection(Comment.class);
        };
    }
    
}
