package psuva.com.ph.psuvotingsystem;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Groups implements Serializable {
  @Exclude String id;
  private String groups;

  public Groups() {

  }

  public Groups(String groups) {
    this.groups = groups;
  }

  public String getGroups() {
    return groups;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
