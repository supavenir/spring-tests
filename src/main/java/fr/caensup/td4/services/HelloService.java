package fr.caensup.td4.services;

import org.springframework.stereotype.Service;
import lombok.Getter;

@Service
@Getter
public class HelloService {
  private String message = "Bonjour";
}
