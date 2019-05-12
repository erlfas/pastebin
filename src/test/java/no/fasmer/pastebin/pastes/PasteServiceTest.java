package no.fasmer.pastebin.pastes;

import no.fasmer.pastebin.pastes.Paste;
import no.fasmer.pastebin.pastes.PasteRepository;
import no.fasmer.pastebin.pastes.PasteService;
import static org.assertj.core.api.Assertions.tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasteServiceTest {
    
    @Autowired
    PasteService pasteService;
    
    @MockBean
    PasteRepository pasteRepository;
    
    @Test
    public void findAllPastesTest() {
        // given
        given(pasteRepository.findAll()).willReturn(
                Flux.just(
                        new Paste("id1", "name1", "1h", "message 1"),
                        new Paste("id2", "name2", "1h", "message 2"),
                        new Paste("id3", "name3", "1h", "message 3")
                )
        );
        
        // when
        final Flux<Paste> pastes = pasteService.findAllPastes();
        
        // then
        then(pastes).isNotNull();
        then(pastes.collectList().block())
                .hasSize(3)
                .extracting(Paste::getId, Paste::getName, Paste::getExpiration, Paste::getMessage)
                .contains(
                        tuple("id1", "name1", "1h", "message 1"),
                        tuple("id2", "name2", "1h", "message 2"),
                        tuple("id3", "name3", "1h", "message 3")
                );
    }
    
    @Test
    public void findOnePasteTest() {
        // given
        given(pasteRepository.findById("id4")).willReturn(Mono.just(new Paste("id4", "name4", "1h", "message 4")));
        
        // when
        final Mono<Paste> monoPaste = pasteService.findOnePaste("id4");
        
        // then
        then(monoPaste).isNotNull();
        final Paste paste = monoPaste.block();
        then(paste.getId()).isEqualTo("id4");
        then(paste.getName()).isEqualTo("name4");
        then(paste.getExpiration()).isEqualTo("1h");
        then(paste.getMessage()).isEqualTo("message 4");
    }
    
    @Test
    public void findByNameTest() {
        // given
        given(pasteRepository.findByName("name")).willReturn(
                Flux.just(
                        new Paste("id5", "name", "1h", "one message"),
                        new Paste("id6", "name", "1h", "another message")
                )
        );
        
        // when
        final Flux<Paste> pastes = pasteService.findByName("name");
        
        // then
        then(pastes).isNotNull();
        then(pastes.collectList().block())
                .hasSize(2)
                .extracting(Paste::getId, Paste::getName, Paste::getExpiration, Paste::getMessage)
                .contains(
                        tuple("id5", "name", "1h", "one message"),
                        tuple("id6", "name", "1h", "another message")
                );
    }
    
    @Test
    public void createPasteTest() {
        // given
        given(pasteRepository.save(any())).willReturn(Mono.just(new Paste("id7", "name7", "1h", "the message")));
        
        // when
        final Mono<Paste> monoPaste = pasteService.createPaste(any());
        
        // then
        then(monoPaste).isNotNull();
        final Paste paste = monoPaste.block();
        then(paste.getId()).isEqualTo("id7");
        then(paste.getName()).isEqualTo("name7");
        then(paste.getExpiration()).isEqualTo("1h");
        then(paste.getMessage()).isEqualTo("the message");
    }
    
    @Test
    public void deletePasteTest() {
        // given 
        given(pasteRepository.findById("id7")).willReturn(Mono.just(new Paste("id7", "name7", "1h", "the message")));
        given(pasteRepository.delete(any())).willReturn(Mono.empty());
        
        // when
        final Mono<Void> mono = pasteService.deletePaste("id7");
        
        // then
        then(mono).isNotNull();
        mono.block();
        verify(pasteRepository).findById("id7");
        verify(pasteRepository).delete(any());
        verifyNoMoreInteractions(pasteRepository);
    }
    
}
