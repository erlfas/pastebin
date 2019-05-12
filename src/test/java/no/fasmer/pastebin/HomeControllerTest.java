package no.fasmer.pastebin;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = HomeController.class)
@Import({ThymeleafAutoConfiguration.class})
public class HomeControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    PasteService pasteService;
    
    @Test
    public void deletePasteShouldWork() {
        // given
        given(pasteService.deletePaste(anyString())).willReturn(Mono.empty());
        
        // when
        final EntityExchangeResult<String> result = webClient
                .delete().uri("/pastes/id1")
                .exchange()
                .expectStatus().isSeeOther()
                .expectBody(String.class).returnResult();
        
        // then
        verify(pasteService).deletePaste("id1");
        verifyNoMoreInteractions(pasteService);
        assertThat(result.getResponseBody()).isNull();
    }
    
    @Test
    public void createPasteShouldWork() {
        // given
        final Paste paste = new Paste(null, "name1", "1h", "message 1");
        given(pasteService.createPaste(any())).willReturn(Mono.just(new Paste("id1", "name1", "1h", "message 1")));
        
        // when
        final EntityExchangeResult<String> result = webClient
                .post().uri("/pastes").body(Mono.just(paste), Paste.class)
                .exchange()
                .expectStatus().isSeeOther()
                .expectBody(String.class).returnResult();
        
        // then
        verify(pasteService).createPaste(any());
        verifyNoMoreInteractions(pasteService);
        assertThat(result.getResponseBody()).isNull();
    }
    
    @Test
    public void onePasteRouteShouldWork() {
        // given
        final Paste paste1 = new Paste("id1", "name1", "1h", "message 1");
        given(pasteService.findOnePaste("id1")).willReturn(Mono.just(paste1));
        
        // when
        final EntityExchangeResult<String> result = webClient
                .get().uri("/pastes/id1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();
        
        // then
        verify(pasteService).findOnePaste("id1");
        verifyNoMoreInteractions(pasteService);
        assertThat(result.getResponseBody())
                .contains("<h1>Your paste</h1>")
                .contains("<p>Message:</p>");
    }
    
    @Test
    public void baseRouteShouldWork() {
        // when
        final EntityExchangeResult<String> result = webClient
                .get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        // then
        verifyNoMoreInteractions(pasteService);
        assertThat(result.getResponseBody())
                .contains("<h1>Pastebin</h1>")
                .contains("<td>Expiration</td>");
    }

    @Test
    public void overviewRouteShouldFindAllPastes() {
        // given
        final Paste paste1 = new Paste("id1", "name1", "1h", "message 1");
        final Paste paste2 = new Paste("id2", "name2", "1h", "message 2");
        final Paste paste3 = new Paste("id3", "name3", "1h", "message 3");
        given(pasteService.findAllPastes()).willReturn(Flux.just(paste1, paste2, paste3));

        // when
        final EntityExchangeResult<String> result = webClient
                .get().uri("/overview")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult();

        // then
        verify(pasteService).findAllPastes();
        verifyNoMoreInteractions(pasteService);
        assertThat(result.getResponseBody())
                .contains("<h1>Overview</h1>")
                .contains("<th>Id</th>")
                .contains("<th>Name</th>")
                .contains("<th>Expiration</th>")
                .contains("<th>Message</th>");
    }

}
