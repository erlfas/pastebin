package no.fasmer.pastebin.comments;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Comment {

    @Id
    private String id;
    private String pasteId;
    private String comment;

}
