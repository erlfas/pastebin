package no.tollpost.dds.pastebin;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PasteRepository extends ReactiveCrudRepository<Paste, String> {
    
    Mono<Paste> findByName(String id);
    
}
