package no.fasmer.pastebin.comments;

import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends Repository<Comment, String> {

    Mono<Void> save(Comment comment);
    
    Flux<Comment> saveAll(Flux<Comment> newComment);

    Flux<Comment> findByPasteId(String id);
    
    Mono<Comment> findById(String id);

    Mono<Void> deleteAll();

}
