package psuva.com.ph.psuvotingsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class GroupsUpdate extends AppCompatActivity {

  private FirebaseFirestore db;
  private Groups groups;

  private EditText edtUpdateGroups;
  private Button btnUpdateGroups, btnDeleteGroups;
  private Voter voterDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_groups_update);

    db = FirebaseFirestore.getInstance();
    groups = (Groups) getIntent().getSerializableExtra("course");
    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");

    edtUpdateGroups = findViewById(R.id.editText);
    btnUpdateGroups = findViewById(R.id.button);
    btnDeleteGroups = findViewById(R.id.button2);

    edtUpdateGroups.setText(groups.getGroups());

    btnUpdateGroupsOnClick();
    btnDeleteGroupsOnClick();
  }

  private void btnUpdateGroupsOnClick() {
    btnUpdateGroups.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String newCourse;
        newCourse = edtUpdateGroups.getText().toString().trim();

        if (TextUtils.isEmpty(newCourse)) {
          Toast.makeText(GroupsUpdate.this, "Please input value for Groups.", Toast.LENGTH_SHORT).show();
          return;
        }

        final ProgressDialog pd = new ProgressDialog(GroupsUpdate.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Fetching data. . . ");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        db.collection("groups").document(groups.getId())
                .update("groups", newCourse)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Toast.makeText(GroupsUpdate.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(GroupsUpdate.this, MainActivity.class);

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

                    i.putExtra("frgToLoad", "nav_groups");
                    finish();
                    startActivity(i);
                    pd.dismiss();
                  }
                });
      }
    });
  }

  private void btnDeleteGroupsOnClick() {
    btnDeleteGroups.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupsUpdate.this);
        AlertDialog ad;
        builder.setTitle("Are you sure about this?");
        builder.setMessage("Deletion is permanent...");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            final ProgressDialog pd = new ProgressDialog(GroupsUpdate.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Fetching data. . . ");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            db.collection("groups").document(groups.getId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                          Toast.makeText(GroupsUpdate.this, "Course Information Deleted.", Toast.LENGTH_SHORT).show();
                          Intent i = new Intent(GroupsUpdate.this, MainActivity.class);

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

                          i.putExtra("frgToLoad", "nav_groups");

                          finish();
                          startActivity(i);
                          pd.dismiss();
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
}
