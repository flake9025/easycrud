package fr.vvlabs.easycrud.core.security;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class BaseSecurityConfig {

  public static <USER extends BaseUser> Optional<USER> findConnectedUser() {
    if (SecurityContextHolder.getContext().getAuthentication() == null
        || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
      return Optional.empty();
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof LocalUserAuthenticationToken)) {
      return Optional.empty();
    }
    return Optional.ofNullable(((LocalUserAuthenticationToken<USER>) authentication).getUser());
  }
}
