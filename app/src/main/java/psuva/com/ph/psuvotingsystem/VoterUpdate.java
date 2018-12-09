package psuva.com.ph.psuvotingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VoterUpdate extends AppCompatActivity {

  private FirebaseFirestore db;
  private Voter voter;

  private EditText edtFname, edtLname, edtNumber, edtEmail;
  private Spinner spinPosition;
  private Button btnSave, btnDelete;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voter_update);

    db = FirebaseFirestore.getInstance();
    voter = (Voter) getIntent().getSerializableExtra("voter");

    edtFname = findViewById(R.id.txt_v_firstName);
    edtLname = findViewById(R.id.txt_v_lastName);
    edtNumber = findViewById(R.id.edtId);
    edtEmail = findViewById(R.id.edtEmail);
    spinPosition = findViewById(R.id.spinPosition2);
    btnSave = findViewById(R.id.btn_v_update);
    btnDelete = findViewById(R.id.btn_v_delete);

    ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Course, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinPosition.setAdapter(adapter);

    // Set Value
    edtFname.setText(voter.getVote_FirstName());
    edtLname.setText(voter.getVote_LastName());
    edtNumber.setText(voter.getVote_IdNumber());
    edtEmail.setText(voter.getVote_email());

    // Set Value of Spinner
    int spinnerPosition = adapter.getPosition(voter.getVote_Course());
    spinPosition.setSelection(spinnerPosition);

    // Button Click Listener
    btnSaveOnClick();
    btnDeleteOnClick();
  }

  private void btnDeleteOnClick() {
    btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VoterUpdate.this);
        AlertDialog ad;
        builder.setTitle("Are you sure about this?");
        builder.setMessage("Deletion is permanent...");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            db.collection("voter")
                    .document(voter.getId()).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                          Toast.makeText(VoterUpdate.this, "Voter Information Deleted.", Toast.LENGTH_SHORT).show();
                          Intent i = new Intent(VoterUpdate.this, MainActivity.class);
                          i.putExtra("frgToLoad", "nav_slideshow");
                          startActivity(i);
                          finish();
                        }
                      }
                    });
          }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

          }
        });

        ad = builder.create();
        ad.show();
      }
    });
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
          Toast.makeText(VoterUpdate.this, "Please input value for First Name.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(lname)) {
          Toast.makeText(VoterUpdate.this, "Please input value for Last Name.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(id)) {
          Toast.makeText(VoterUpdate.this, "Please input value for Id Number.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(email)) {
          Toast.makeText(VoterUpdate.this, "Please input value for Email.", Toast.LENGTH_SHORT).show();
          return;
        }

        db.collection("voter").document(voter.getId())
                .update("vote_Course", spinCourse,
                        "vote_FirstName", fname,
                        "vote_IdNumber", id,
                        "vote_LastName", lname,
                        "vote_email", email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Toast.makeText(VoterUpdate.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(VoterUpdate.this, MainActivity.class);
                    i.putExtra("frgToLoad", "nav_slideshow");
                    startActivity(i);
                    finish();
                  }
                });

      }
    });
  }
}
