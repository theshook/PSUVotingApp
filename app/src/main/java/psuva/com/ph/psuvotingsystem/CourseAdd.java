package psuva.com.ph.psuvotingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class CourseAdd extends AppCompatActivity {

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private EditText edtCourse;
  private Button btnSaveCourse;
  private Voter voterDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_add);

    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");

    edtCourse = findViewById(R.id.edtCourse);
    btnSaveCourse = findViewById(R.id.btnSaveCourse);

    btnSaveCourseClick();
  }

  private void btnSaveCourseClick() {
    btnSaveCourse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String course;
        course = edtCourse.getText().toString().trim();

        if (TextUtils.isEmpty(course)) {
          Toast.makeText(CourseAdd.this, "Please input value for Course.", Toast.LENGTH_SHORT).show();
          return;
        }

        final ProgressDialog pd = new ProgressDialog(CourseAdd.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Fetching data. . . ");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        CollectionReference dbCourse = db.collection("course");
        Course partyList = new Course(course);
        dbCourse.add(partyList)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                      Toast.makeText(CourseAdd.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(CourseAdd.this, MainActivity.class);

                      Map<String, Boolean> isVoted = (Map<String, Boolean>) voterDetails.getIsVoted();
                      Voter v = new Voter(
                              voterDetails.getVote_FirstName(),
                              voterDetails.getVote_LastName(),
                              voterDetails.getVote_Course(),
                              voterDetails.getVote_IdNumber(),
                              voterDetails.getVote_email(),
                              isVoted);
                      v.setId(voterDetails.getId());
                      i.putExtra("voterDetails", v);

                      i.putExtra("frgToLoad", "nav_course");
                      pd.dismiss();
                      finish();
                      startActivity(i);
                    } else {
                      pd.dismiss();
                      Toast.makeText(CourseAdd.this, "Please check your inputs.", Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });
  }
}
