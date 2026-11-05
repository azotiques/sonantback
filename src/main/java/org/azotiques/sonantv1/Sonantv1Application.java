package org.azotiques.sonantv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.modulith.Modulithic;

@Modulithic(
        systemName = "Sonant",
        sharedModules = "utils"
)
@SpringBootApplication
@EnableJpaAuditing
public class Sonantv1Application {

    public static void main(String[] args) {
        SpringApplication.run(Sonantv1Application.class, args);
    }

}
