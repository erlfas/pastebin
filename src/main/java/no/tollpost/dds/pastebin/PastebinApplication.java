package no.tollpost.dds.pastebin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class PastebinApplication {

    public static void main(String[] args) {
        SpringApplication.run(PastebinApplication.class, args);
    }

    

}
