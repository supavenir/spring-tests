package fr.caensup.td4.controllers;

import javax.annotation.security.RolesAllowed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.caensup.td4.services.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping({"/users/", "/users"})
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  @RolesAllowed("ADMIN")
  public String indexAction(ModelMap model) {
    model.put("users", userService.findAll());
    return "/users/index";
  }
}
