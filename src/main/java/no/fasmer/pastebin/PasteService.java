package no.fasmer.pastebin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PasteService {

    private final PasteRepository pasteRepository;

    public PasteService(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    public Flux<Paste> findAllPastes() {
        return pasteRepository.findAll();
    }

    public Mono<Paste> findOnePaste(String id) {
        return pasteRepository.findById(id);
    }

    public Mono<Void> createPaste(Paste paste) {
        return pasteRepository.save(paste).then();
    }

    public Mono<Void> deletePaste(String id) {
        return pasteRepository
                .findById(id)
                .flatMap(pasteRepository::delete)
                .then();
    }

    @Component
    public class InitDatabase {

        @Bean
        CommandLineRunner init(MongoOperations operations) {
            return args -> {
                operations.dropCollection(Paste.class);

                operations.insert(new Paste("1", "Navn 1", "1h", "meldingsinnhold 1"));
                operations.insert(new Paste("2", "Navn 2", "1h", "meldingsinnhold 2"));
                operations.insert(new Paste("3", "Navn 3", "1h", "meldingsinnhold 3"));

                operations.findAll(Paste.class).forEach(paste -> {
                    System.out.println(paste.toString());
                });
            };
        }
    }

}
