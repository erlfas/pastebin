package no.tollpost.dds.pastebin;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private static final String BASE_PATH = "/pastes";
    private static final String FILENAME = "{filename:.+}";

    private final PasteService pasteService;

    public HomeController(PasteService pasteService) {
        this.pasteService = pasteService;
    }

    @GetMapping("/")
    public Mono<String> fillModel(Model model) {
        model.addAttribute("pastes", pasteService.findAllPastes());
        model.addAttribute("paste", new Paste());
        return Mono.just("index");
    }

    @GetMapping(value = BASE_PATH + "/" + FILENAME, produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<?>> onePaste(@PathVariable String filename) {
        return pasteService.findOnePaste(filename)
                .map(paste -> ResponseEntity.ok().body(paste));
    }
    
//    @PostMapping(value = BASE_PATH)
//    public Mono<String> createFile(Flux<Paste> pastes, Model model) { 
//        model.addAttribute("paste", new Paste());
//        return pasteService.createPaste(pastes).then(Mono.just("redirect:/"));
//    }
    
    @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(Paste paste, Model model) { 
        model.addAttribute("paste", new Paste());
        return pasteService.createPaste(paste).then(Mono.just("redirect:/"));
    }

    @DeleteMapping(value = BASE_PATH + "/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String id) {
        return pasteService.deletePaste(id).then(Mono.just("redirect:/"));
    }
    
}
