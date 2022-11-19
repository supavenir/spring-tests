package fr.caensup.td4.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import fr.caensup.td4.SpringTestsApplication;
import fr.caensup.td4.config.WebSecurityConfig;
import fr.caensup.td4.controllers.HelloController;
import fr.caensup.td4.services.HelloService;

@WebMvcTest(HelloController.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, SpringTestsApplication.class})
class HelloControllerTest {

  @MockBean
  private HelloService helloService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void helloShouldReturnBonjour() throws Exception {
    when(helloService.getMessage()).thenReturn("Bonjour");
    this.mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(containsString("Bonjour")));
  }

  @Test
  void helloViewShouldReturnBonjour() throws Exception {
    when(helloService.getMessage()).thenReturn("Bonjour");
    this.mockMvc.perform(MockMvcRequestBuilders.get("/hello/view")).andExpect(view().name("hello"))
        .andExpect(model().attribute("message", "Bonjour"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(containsString("Bonjour")));
  }
}
