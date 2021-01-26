package com.saic.demos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.saic.demos.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link User} class.
 */
public class UserTest {
  private static final String DEFAULT_LOGIN = "johndoe";
  private static final String password = RandomStringUtils.random(60);
  private static final String email = "johndoe@localhost";
  private static final String firstName = "john";
  private static final String lastName = "doe";
  private static final String imageURL = "image_url";
  private static final String directDeposit = "123456789";
  private static final String langKey = "en";

  private User user;

  @BeforeEach
  public void init() {
    user = new User();
    user.setLogin(DEFAULT_LOGIN);
    user.setPassword(password);
    user.setActivated(true);
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setImageUrl(imageURL);
    user.setDirectDeposit(directDeposit);
    user.setLangKey(langKey);
  }

  @Test
  public void testGetLogin() {
    assertThat(user.getLogin() == DEFAULT_LOGIN).isTrue();
  }

  @Test
  public void testGetPassword() {
    assertThat(user.getPassword() == password).isTrue();
  }

  @Test
  public void testGetEmail() {
    assertThat(user.getEmail() == email).isTrue();
  }

  @Test
  public void testGetFirstName() {
    assertThat(user.getFirstName() == firstName).isTrue();
  }

  @Test
  public void testGetLastName() {
    assertThat(user.getLastName() == lastName).isTrue();
  }

  @Test
  public void testGetImageUrl() {
    assertThat(user.getImageUrl() == imageURL).isTrue();
  }

  @Test
  public void testGetDirectDeposit() {
    assertThat(user.getDirectDeposit() == directDeposit).isTrue();
  }

  @Test
  public void testGetLangKey() {
    assertThat(user.getLangKey() == langKey).isTrue();
  }
}
