package no.fasmer.pastebin.pastes;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class Paste {

    @Id
    private String id;
    private String name;
    private String expiration;
    private String message;

    public Paste(String id, String name, String expiration, String message) {
        this.id = id;
        this.name = name;
        this.expiration = expiration;
        this.message = message;
    }
    
}
