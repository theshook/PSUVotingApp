package psuva.com.ph.psuvotingsystem;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Course implements Serializable {
  @Exclude private String id;
  private String course;

  public Course() {

  }

  public Course(String course) {
    this.course = course;
  }

  public String getCourse() {
    return course;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
