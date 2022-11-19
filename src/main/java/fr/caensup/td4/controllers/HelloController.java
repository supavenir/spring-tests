package fr.caensup.td4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import fr.caensup.td4.services.HelloService;

@Controller
public class HelloController {
  @Autowired
  private HelloService helloService;

  @GetMapping("/hello")
  public @ResponseBody String helloAction() {
    return helloService.getMessage();
  }

  @ModelAttribute("message")
  public String getMessage() {
    return helloService.getMessage();
  }

  @GetMapping("/hello/view")
  public String helloViewAction() {
    return "hello";
  }
}
