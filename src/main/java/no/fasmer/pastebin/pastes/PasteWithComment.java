package no.fasmer.pastebin.pastes;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fasmer.pastebin.comments.Comment;

@Data
@NoArgsConstructor
public class PasteWithComment {
    
    private String id;
    private String name;
    private String expiration;
    private String message;
    private List<Comment> comments;
    
}
