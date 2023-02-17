package personal.cstettler.thymeleaf.patternlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import personal.cstettler.thymeleaf.dialect.ComponentDialect;

@SpringBootApplication
public class PocThymeleafPatternLibraryApplication {

  public static void main(String[] args) {
    SpringApplication.run(PocThymeleafPatternLibraryApplication.class, args);
  }

  @Bean
  public ComponentDialect componentDialect() {
    return new ComponentDialect();
  }
}
