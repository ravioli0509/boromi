package com.team41.boromi.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.inject.Singleton;

/**
 * A user class. Each user class must have a username assigned
 */
@Singleton
public class User implements OwnerInterface, BorrowerInterface, Serializable {

  // SerialVersionUID generated by Android Studio
  private static final long serialVersionUID = 3665013645816941883L;

  private String username;
  private String email;
  private String uuid;

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

  /** for serializer
   *
   */
  public User() {}


  // getter start
  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getUUID() {
    return uuid;
  }
}