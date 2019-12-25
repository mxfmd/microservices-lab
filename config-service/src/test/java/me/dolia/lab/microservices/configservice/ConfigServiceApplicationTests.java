package me.dolia.lab.microservices.configservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
public class ConfigServiceApplicationTests {

  @LocalServerPort
  private int port;
  @Value("${spring.application.name}")
  private String name;
  @Autowired
  private TestRestTemplate rest;

  @Test
  public void retrievesConfigurationPropertiesFromGit() {
    var url = "http://localhost:" + port + "/" + name + "/test/";

    var response = rest.getForEntity(url, Environment.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var body = response.getBody();
    assertThat(body).isNotNull();
    var foo = body.getPropertySources()
        .stream()
        .map(PropertySource::getSource)
        .map(s -> (String) s.get("foo"))
        .filter(Objects::nonNull)
        .findFirst();
    assertThat(foo).hasValue("bar");
  }
}