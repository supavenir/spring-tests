package fr.caensup.td4.tests;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import fr.caensup.td4.models.User;
import fr.caensup.td4.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RestUserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  private static User testUser;


  @BeforeEach
  public void setup() {
    testUser = userService.createUser("Bob", "Duke");
  }

  @AfterEach
  public void tearDown() {
    userService.deleteAll();
  }

  @Test
  void getAllShouldReturnAllUsers() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/users"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname").value("Bob"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname").value("Duke"));
  }

  @Test
  void getOneShouldReturnDuke() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/" + testUser.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUser.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("Bob"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value("Duke"));
  }

  @Test
  void getOneWithNonExistingShouldReturn404() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/users/5000"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void shouldAllowCreationForUnauthenticatedUsers() throws Exception {
    this.mockMvc
        .perform(post("/rest/users").contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstname\": \"Bob\", \"lastname\":\"Duke\"}").with(csrf()))
        .andExpect(status().isCreated()).andExpect(header().exists("Location")).andExpect(
            header().string("Location", Matchers.containsString((testUser.getId() + 1) + "")));
  }

  @Test
  void deleteShouldDelete() throws Exception {
    this.mockMvc.perform(delete("/rest/users/" + testUser.getId())
        .contentType(MediaType.APPLICATION_JSON).with(csrf())).andExpect(status().isNoContent());
  }

  @Test
  void deleteWithNonExistingShouldReturn404() throws Exception {
    this.mockMvc
        .perform(delete("/rest/users/5000").contentType(MediaType.APPLICATION_JSON).with(csrf()))
        .andExpect(status().isNotFound());
  }


  @Test
  void shouldAllowUpdateForUnauthenticatedUsers() throws Exception {
    this.mockMvc
        .perform(put("/rest/users/" + testUser.getId()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstname\": \"Marcel\", \"lastname\":\"Duke\"}").with(csrf()))
        .andExpect(status().isResetContent()).andExpect(header().exists("Location"))
        .andExpect(header().string("Location", Matchers.containsString((testUser.getId()) + "")));
    this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/users"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname").value("Marcel"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname").value("Duke"));
  }

  @Test
  void updateWithNonExistingshouldReturn404() throws Exception {
    this.mockMvc
        .perform(put("/rest/users/5000").contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstname\": \"Marcel\", \"lastname\":\"Duke\"}").with(csrf()))
        .andExpect(status().isNotFound());
  }
}
