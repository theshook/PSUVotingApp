package psuva.com.ph.psuvotingsystem;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Election implements Serializable {
  @Exclude private String id;
  private String firstName, lastName, position;

  public Election() {

  }

  public Election(String firstName, String lastName, String position) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.position = position;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPosition() {
    return position;
  }
}
