package fr.caensup.td4.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import fr.caensup.td4.controllers.UserController;
import fr.caensup.td4.models.User;
import fr.caensup.td4.repositories.UserRepository;
import fr.caensup.td4.services.UserService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TestUserController {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserService userService;

  @Autowired
  private WebApplicationContext context;

  protected MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();
  }

  @Test
  void shouldRejectAccessToUsersWhenUserIsAnonymous() throws Exception {
    this.mockMvc.perform(get("/users")).andExpect(status().isUnauthorized());
  }

  @Test
  void adminUserHasAccessToUsers() throws Exception {
    when(userService.findAll()).thenReturn(Arrays.asList(new User("Bob", "MockDuke")));
    this.mockMvc
        .perform(get("/users").with(
            SecurityMockMvcRequestPostProcessors.user("admin").password("admin").roles("ADMIN")))
        .andExpect(status().isOk()).andExpect(content().string(containsString("MockDuke")));
  }

}
