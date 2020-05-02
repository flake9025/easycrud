package fr.vvlabs.easycrud.test.model;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import java.util.Set;

public class User implements BaseUser {

  private String username;
  private String password;
  private Set<String> authorities;
  private boolean isEnabled;

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  @Override
  public Set<String> getAuthorities() {
    return authorities;
  }
}
