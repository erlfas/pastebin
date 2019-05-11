package no.fasmer.pastebin;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public HomeController(PasteService pasteService) {
        this.pasteService = pasteService;
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
        return pasteService.findOnePaste(id)
                .flatMap(paste -> {
                    model.addAttribute("aPaste", paste);
                    System.out.println("PASTE: " + paste);
                    return Mono.just("singlepaste");
                });
    }
    
    @PostMapping(value = BASE_PATH)
    public Mono<String> createPaste(Paste paste, Model model) { 
        model.addAttribute("paste", new Paste());
        return pasteService.createPaste(paste)
                .map(x -> "redirect:/pastes/" + x.getId());
    }

    @DeleteMapping(value = BASE_PATH + "/" + ID)
    public Mono<String> deleteFile(@PathVariable String id) {
        return pasteService.deletePaste(id).then(Mono.just("redirect:/overview"));
    }
    
}
