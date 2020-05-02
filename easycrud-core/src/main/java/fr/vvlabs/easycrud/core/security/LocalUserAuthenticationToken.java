package fr.vvlabs.easycrud.core.security;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class LocalUserAuthenticationToken<USER extends BaseUser> extends UsernamePasswordAuthenticationToken {

  public LocalUserAuthenticationToken(USER user) {
    this(user, user.getUsername(), user.getAuthorities());
  }

  public LocalUserAuthenticationToken(USER user, String username, Set<String> authorities) {
    super(username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    setDetails(user);
  }

  @SuppressWarnings("unchecked")
  public USER getUser() {
    return (USER) getDetails();
  }
}
