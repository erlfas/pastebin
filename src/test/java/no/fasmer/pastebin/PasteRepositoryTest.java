package no.fasmer.pastebin;

import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PasteRepositoryTest {

    @Autowired
    PasteRepository pasteRepository;
    
    @Autowired
    MongoOperations mongoOperations;
    
    @Before
    public void setUp() {
        mongoOperations.dropCollection(Paste.class);
        mongoOperations.insert(new Paste("id1", "name1", "1h", "message 1"));
        mongoOperations.insert(new Paste("id2", "name2", "1h", "message 2"));
        mongoOperations.insert(new Paste("id3", "name3", "1h", "message 3"));
        mongoOperations.findAll(Paste.class).forEach(paste -> {
            System.out.println(paste.toString());
        });
    }
    
    @Test
    public void findAllShouldWork() {
        final Flux<Paste> pastes = pasteRepository.findAll();
        StepVerifier.create(pastes)
                .recordWith(ArrayList::new)
                .expectNextCount(3)
                .consumeRecordedWith(results -> {
                    assertThat(results).hasSize(3);
                    assertThat(results)
                            .extracting(Paste::getName)
                            .contains("name1", "name2", "name3");
                })
                .expectComplete()
                .verify();
    }
    
    @Test
    public void findByNameShouldWork() {
        final Flux<Paste> pastes = pasteRepository.findByName("name3");
        StepVerifier.create(pastes)
                .recordWith(ArrayList::new)
                .expectNextCount(1)
                .expectNextMatches(paste -> {
                    assertThat(paste.getName()).isEqualTo("name3");
                    assertThat(paste.getId()).isEqualTo("id3");
                    assertThat(paste.getExpiration()).isEqualTo("1h");
                    assertThat(paste.getMessage()).isEqualTo("message 3");
                    return true;
                });
    }
    
}
