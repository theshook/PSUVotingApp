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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class GroupsAdd extends AppCompatActivity {

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private EditText edtGroups;
  private Button btnSaveGroups;
  private Voter voterDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_groups_add);

    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");

    edtGroups = findViewById(R.id.edtGroups);
    btnSaveGroups = findViewById(R.id.btnSaveGroups);

    btnSaveCourseClick();
  }

  private void btnSaveCourseClick() {
    btnSaveGroups.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String groups;
        groups = edtGroups.getText().toString().trim();

        if (TextUtils.isEmpty(groups)) {
          Toast.makeText(GroupsAdd.this, "Please input value for groups.", Toast.LENGTH_SHORT).show();
          return;
        }

        final ProgressDialog pd = new ProgressDialog(GroupsAdd.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Fetching data. . . ");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        CollectionReference dbGroups = db.collection("groups");
        Groups partyList = new Groups(groups);
        dbGroups.add(partyList)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                      Toast.makeText(GroupsAdd.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(GroupsAdd.this, MainActivity.class);

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
                      pd.dismiss();
                      finish();
                      startActivity(i);
                    } else {
                      pd.dismiss();
                      Toast.makeText(GroupsAdd.this, "Please check your inputs.", Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });
  }
}
