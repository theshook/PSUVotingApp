package psuva.com.ph.psuvotingsystem;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class PartyList implements Serializable {
  @Exclude private String id;
  private String firstName, lastName, position, partyList;
  int votes;

  public PartyList() {

  }

  public PartyList(String firstName, String lastName, String position, String partyList, int votes) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.position = position;
    this.partyList = partyList;
    this.votes = votes;
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

  public String getPartyList() {
    return partyList;
  }

  public int getVotes() {
    return votes;
  }

}
