package com.team41.boromi.models;

import java.io.Serializable;
import javax.inject.Singleton;

/**
 * A user class. Each user class must have a username assigned
 */
@Singleton
public class User implements Serializable {

  // SerialVersionUID generated by Android Studio
  private static final long serialVersionUID = 3665013645816941883L;

  private String username;
  private String email;
  private String uuid;

  /**
   * A no-argument constructor required by firebase in order to deserialize
   */
  public User() {
  }

  /**
   * constructor
   *
   * @param username
   * @param email
   */
  public User(String uuid, String username, String email) {
    this.uuid = uuid;
    this.username = username;
    this.email = email;
  }

  // Getters Start
  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getUUID() {
    return uuid;
  }

  // Setters Start
  public void setUsername(String username) { this.username = username; }

  public void setEmail(String email) { this.email = email; }
}