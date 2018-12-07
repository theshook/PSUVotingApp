package psuva.com.ph.psuvotingsystem;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Voter implements Serializable {
  @Exclude private String id;
  private String vote_FirstName, vote_LastName, vote_Course, vote_IdNumber, vote_email;

  public Voter() {

  }

  public Voter(String vote_FirstName, String vote_LastName, String vote_Course, String vote_IdNumber, String vote_email) {
    this.vote_FirstName = vote_FirstName;
    this.vote_LastName = vote_LastName;
    this.vote_Course = vote_Course;
    this.vote_IdNumber = vote_IdNumber;
    this.vote_email = vote_email;
  }

  public String getVote_FirstName() {
    return vote_FirstName;
  }

  public String getVote_LastName() {
    return vote_LastName;
  }

  public String getVote_Course() {
    return vote_Course;
  }

  public String getVote_IdNumber() {
    return vote_IdNumber;
  }

  public String getVote_email() {
    return vote_email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
