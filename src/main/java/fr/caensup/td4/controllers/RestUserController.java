package fr.caensup.td4.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.caensup.td4.models.User;
import fr.caensup.td4.repositories.UserRepository;


@RestController
@RequestMapping("/rest/users")
public class RestUserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping("")
  public Iterable<User> indexAction() {
    return userRepository.findAll();
  }

  @GetMapping("{id}")
  public User userByIdAction(@PathVariable int id) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isPresent()) {
      return opt.get();
    }
    return null;
  }

  @PostMapping("")
  public User addAction(@RequestBody User us) {
    userRepository.save(us);
    return us;
  }

  @PutMapping("{id}")
  public User updateAction(@PathVariable int id, @RequestBody User us) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isPresent()) {
      User usloaded = opt.get();
      usloaded.setFirstname(us.getFirstname());
      usloaded.setLastname(us.getLastname());
      userRepository.save(usloaded);
      return usloaded;
    }
    return null;
  }
}
