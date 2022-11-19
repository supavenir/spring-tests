package fr.caensup.td4.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

  @GetMapping("/index")
  public @ResponseBody String helloAction() {
    return "Index page";
  }
}
