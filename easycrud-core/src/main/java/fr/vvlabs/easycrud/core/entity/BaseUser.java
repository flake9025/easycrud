package fr.vvlabs.easycrud.core.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Transient;
import org.springframework.util.CollectionUtils;

public interface BaseUser extends Serializable {

  String getUsername();

  String getPassword();

  boolean isEnabled();

  Set<String> getAuthorities();

  /**
   * return true if user has the authority
   */
  @Transient
  default boolean hasAuthority(String authority) {
    return this.getAuthorities() != null && this.getAuthorities().contains(authority);
  }

  /**
   * return true if user has any authority
   */
  @Transient
  default boolean hasAnyAuthority(Collection<String> authorities) {
    return getAuthorities() != null && authorities != null && CollectionUtils.containsAny(getAuthorities(), authorities);
  }

  /**
   * return true if user has any authority
   */
  @Transient
  default boolean hasAnyAuthority(String... authorities) {
    return hasAnyAuthority(Arrays.asList(authorities));
  }
}
