package fr.caensup.td4.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
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
  public ResponseEntity<User> userByIdAction(@PathVariable int id) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isPresent()) {
      return ResponseEntity.ok(opt.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("")
  public ResponseEntity<Void> addAction(@RequestBody User us, UriComponentsBuilder uriBuilder) {
    us = userRepository.save(us);
    return ResponseEntity.created(uriBuilder.path("/rest/users/{id}").build(us.getId())).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<Void> updateAction(@PathVariable int id, @RequestBody User us,
      UriComponentsBuilder uriBuilder) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isPresent()) {
      User usloaded = opt.get();
      usloaded.setFirstname(us.getFirstname());
      usloaded.setLastname(us.getLastname());
      userRepository.save(usloaded);
      return ResponseEntity.status(HttpStatus.RESET_CONTENT)
          .location(uriBuilder.path("/rest/users/{id}").build(usloaded.getId())).build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteAction(@PathVariable int id, UriComponentsBuilder uriBuilder) {
    Optional<User> opt = userRepository.findById(id);
    if (opt.isPresent()) {
      User usloaded = opt.get();
      userRepository.delete(usloaded);
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .location(uriBuilder.path("/rest/users/").build().toUri()).build();
    }
    return ResponseEntity.notFound().build();
  }
}
