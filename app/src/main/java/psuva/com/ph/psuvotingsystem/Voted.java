package psuva.com.ph.psuvotingsystem;

public class Voted {
  private String candidateId, voterId, voterFirstName, voterLastName, voterCourse, voterEmail;

  public Voted() {

  }

  public Voted(String candidateId, String voterId, String voterFirstName, String voterLastName, String voterCourse, String voterEmail) {
    this.candidateId = candidateId;
    this.voterId = voterId;
    this.voterFirstName = voterFirstName;
    this.voterLastName = voterLastName;
    this.voterCourse = voterCourse;
    this.voterEmail = voterEmail;
  }

  public String getCandidateId() {
    return candidateId;
  }

  public String getVoterId() {
    return voterId;
  }

  public String getVoterFirstName() {
    return voterFirstName;
  }

  public String getVoterLastName() {
    return voterLastName;
  }

  public String getVoterCourse() {
    return voterCourse;
  }

  public String getVoterEmail() {
    return voterEmail;
  }
}
