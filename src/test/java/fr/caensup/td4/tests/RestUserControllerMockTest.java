package fr.caensup.td4.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import fr.caensup.td4.SpringTestsApplication;
import fr.caensup.td4.config.WebSecurityConfig;
import fr.caensup.td4.controllers.RestUserController;
import fr.caensup.td4.models.User;
import fr.caensup.td4.repositories.UserRepository;


@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(RestUserController.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, SpringTestsApplication.class})
class RestUserControllerMockTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private JacksonTester<User> jsonUser;

  @Autowired
  private JacksonTester<List<User>> jsonUsers;

  private User initOne(int id, String firstname, String lastname) {
    User testUser = new User(firstname, lastname);
    testUser.setId(id);
    when(userRepository.findById(id)).thenReturn(Optional.of(testUser));
    return testUser;
  }

  @Test
  void getAllShouldReturnAllUsers() throws Exception {
    // given
    List<User> users = Arrays.asList(new User("Bob", "Duke"), new User("Marcel", "Dupont"));
    when(userRepository.findAll()).thenReturn(users);
    // When
    MockHttpServletResponse response = mockMvc
        .perform(get("/rest/users").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo(jsonUsers.write(users).getJson());
  }

  @Test
  void getOneShouldReturnOne() throws Exception {
    // Given
    User testUser = initOne(1, "Bob", "Duke");
    // When
    MockHttpServletResponse response =
        mockMvc.perform(get("/rest/users/" + testUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo(jsonUser.write(testUser).getJson());
  }

  @Test
  void getOneWithNonExistingShouldReturn404() throws Exception {
    // Given
    initOne(1, "Bob", "Duke");
    // When
    MockHttpServletResponse response =
        mockMvc.perform(get("/rest/users/5000").accept(MediaType.APPLICATION_JSON)).andReturn()
            .getResponse();
    // Then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }
}
