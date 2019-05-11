package no.fasmer.pastebin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;


@SpringBootApplication
public class PastebinApplication {

    public static void main(String[] args) {
        SpringApplication.run(PastebinApplication.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        // https://stackoverflow.com/questions/50913444/how-to-properly-register-hiddenhttpmethodfilter-in-spring-boot
        return new HiddenHttpMethodFilter();
    }

}
