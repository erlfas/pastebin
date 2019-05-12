package no.fasmer.pastebin.pastes;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PasteRepository extends ReactiveCrudRepository<Paste, String> {
    
    Flux<Paste> findByName(String name);
    
}
