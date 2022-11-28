package fr.caensup.td4.services;

import java.util.Optional;
import org.springframework.stereotype.Service;
import fr.caensup.td4.models.User;
import fr.caensup.td4.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(String firstname, String lastname) {
    User u = new User(firstname, lastname);
    return userRepository.save(u);
  }

  public void deleteAll() {
    userRepository.deleteAll();
  }

  public Iterable<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findOne(int id) {
    return userRepository.findById(id);
  }
}
