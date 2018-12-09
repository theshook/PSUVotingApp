package psuva.com.ph.psuvotingsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VoterAdd extends AppCompatActivity {

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private EditText edtFname, edtLname, edtNumber, edtEmail;
  private Spinner spinPosition;
  private Button btnSave;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voter_add);

    edtFname = findViewById(R.id.txt_p_firstName2);
    edtLname = findViewById(R.id.txt_v_lastName);
    edtNumber = findViewById(R.id.edtId);
    edtEmail = findViewById(R.id.edtEmail);
    spinPosition = findViewById(R.id.spinPosition2);
    btnSave = findViewById(R.id.btn_p_save2);

    ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Course, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinPosition.setAdapter(adapter);

    btnSaveOnClick();
  }

  private void btnSaveOnClick() {
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String fname, lname, id, email, spinCourse;
        fname = edtFname.getText().toString().trim();
        lname = edtLname.getText().toString().trim();
        id = edtNumber.getText().toString();
        email = edtEmail.getText().toString();
        spinCourse = spinPosition.getSelectedItem().toString();

        if (TextUtils.isEmpty(fname)) {
          Toast.makeText(VoterAdd.this, "Please input value for First Name.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(lname)) {
          Toast.makeText(VoterAdd.this, "Please input value for Last Name.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(id)) {
          Toast.makeText(VoterAdd.this, "Please input value for Id Number.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(email)) {
          Toast.makeText(VoterAdd.this, "Please input value for Email.", Toast.LENGTH_SHORT).show();
          return;
        }

        CollectionReference dbVoter = db.collection("voter");
        Map<String, Boolean> isVoted = new HashMap<>();
        isVoted.put("president", false);
        isVoted.put("evp", false);
        isVoted.put("vcpa", false);
        isVoted.put("vcc", false);
        isVoted.put("vcsrw", false);
        isVoted.put("auditor", false);
        isVoted.put("bm1", false);
        isVoted.put("bm2", false);
        isVoted.put("pro1", false);
        isVoted.put("pro2", false);

        Voter voter = new Voter(fname, lname, spinCourse, id, email, isVoted);

        dbVoter.add(voter)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                      Toast.makeText(VoterAdd.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(VoterAdd.this, MainActivity.class);
                      i.putExtra("frgToLoad", "nav_slideshow");
                      startActivity(i);
                      finish();
                    } else {
                      Toast.makeText(VoterAdd.this, "Please check your inputs.", Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });
  }
}
