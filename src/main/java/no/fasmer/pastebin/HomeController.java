package no.fasmer.pastebin;

import java.util.List;
import no.fasmer.pastebin.pastes.CommentReaderRepository;
import no.fasmer.pastebin.pastes.Paste;
import no.fasmer.pastebin.pastes.PasteService;
import no.fasmer.pastebin.pastes.PasteWithComment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private static final String BASE_PATH = "/pastes";
    private static final String ID = "{id:.+}";

    private final PasteService pasteService;
    private final CommentReaderRepository repository; 

    public HomeController(PasteService pasteService, CommentReaderRepository repository) {
        this.pasteService = pasteService;
        this.repository = repository;
    }

    @GetMapping("/")
    public Mono<String> paste(Model model) {
        model.addAttribute("paste", new Paste());
        return Mono.just("index");
    }
    
    @GetMapping("/overview")
    public Mono<String> overview(Model model) {
        model.addAttribute("pastes", pasteService.findAllPastes());
        return Mono.just("overview");
    }

    @GetMapping(value = BASE_PATH + "/" + ID)
    public Mono<String> onePaste(@PathVariable String id, Model model) {
        final Mono<PasteWithComment> result = pasteService.findOnePaste(id)
                .flatMap(paste -> Mono.just(paste).zipWith(repository.findByPasteId(paste.getId()).collectList()))
                .map(x -> { 
                    final PasteWithComment pasteWithComment = new PasteWithComment();   
                    pasteWithComment.setId(x.getT1().getId());
                    pasteWithComment.setName(x.getT1().getName());
                    pasteWithComment.setExpiration(x.getT1().getExpiration());
                    pasteWithComment.setMessage(x.getT1().getMessage());
                    pasteWithComment.setComments(x.getT2());
                    
                    System.out.println(x);
                    
                    return pasteWithComment;
                });
        
        model.addAttribute("aPaste", result);
        
        return Mono.just("singlepaste");
    }
    
    @PostMapping(value = BASE_PATH)
    public Mono<String> createPaste(Paste paste, Model model) { 
        model.addAttribute("paste", new PasteWithComment());
        return pasteService.createPaste(paste)
                .map(x -> "redirect:/pastes/" + x.getId());
    }

    @DeleteMapping(value = BASE_PATH + "/" + ID)
    public Mono<String> deletePaste(@PathVariable String id) {
        return pasteService.deletePaste(id).then(Mono.just("redirect:/overview"));
    }
    
}
