package fr.caensup.td4.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.Getter;

@Service
@Getter
public class HelloService {
  private String message = "Bonjour";

  @PreAuthorize("authenticated")
  public String getAuthMessage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return message + " " + authentication;
  }
}
