package no.fasmer.pastebin;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class PasteTest {

    @Test
    public void test() {
        // arrange
        final Paste paste = new Paste("asdf", "theName", "1h", "a message");
        
        // act and assert
        assertThat(paste.getId()).isEqualTo("asdf");
        assertThat(paste.getName()).isEqualTo("theName");
        assertThat(paste.getExpiration()).isEqualTo("1h");
        assertThat(paste.getMessage()).isEqualTo("a message");
    }

}
