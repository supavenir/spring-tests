package fr.caensup.td4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.caensup.td4.models.User;
import fr.caensup.td4.repositories.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User createUser(String firstname, String lastname) {
    User u = new User(firstname, lastname);
    return userRepository.save(u);
  }
}
