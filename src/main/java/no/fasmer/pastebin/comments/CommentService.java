package no.fasmer.pastebin.comments;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(CustomProcessor.class)
public class CommentService {

    private final CommentWriterRepository commentWriterRepository;
    private final MeterRegistry meterRegistry;

    public CommentService(CommentWriterRepository commentWriterRepository, MeterRegistry meterRegistry) {
        this.commentWriterRepository = commentWriterRepository;
        this.meterRegistry = meterRegistry;
    }
    
    @StreamListener
    @Output(CustomProcessor.OUTPUT)
    public void save(@Input(CustomProcessor.INPUT) Flux<Comment> newComments) {
        commentWriterRepository
                .saveAll(newComments)
                .flatMap(comment -> {
                    meterRegistry
                            .counter("comments.consumed", "pasteId", comment.getPasteId())
                            .increment();
                    return Mono.empty();
                });
    }
    
    @Bean
    CommandLineRunner setUp(MongoOperations mongoOperations) {
        return args -> {
            mongoOperations.dropCollection(Comment.class);
        };
    }
    
}
