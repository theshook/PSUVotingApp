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

import java.util.Map;

public class PartyListAdd extends AppCompatActivity {

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private EditText edtFname, edtLname;
  private Spinner spinParty, spinPosition;
  private Button btnSave;
  private Voter voterDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_party_list_add);

    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");

    edtFname = findViewById(R.id.txt_p_firstName2);
    edtLname = findViewById(R.id.txt_v_lastName);
    spinParty = findViewById(R.id.spinPartyList2);
    spinPosition = findViewById(R.id.spinPosition2);
    btnSave = findViewById(R.id.btn_p_save2);

    ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Positions, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinPosition.setAdapter(adapter);

    ArrayAdapter<CharSequence> adapter1  = ArrayAdapter.createFromResource(this, R.array.PartyList, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinParty.setAdapter(adapter1);

    btnSaveOnClick();
  }

  private void btnSaveOnClick() {
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String fname, lname, spinP, spinPartyList;
        fname = edtFname.getText().toString().trim();
        lname = edtLname.getText().toString().trim();
        spinP = spinPosition.getSelectedItem().toString();
        spinPartyList = spinParty.getSelectedItem().toString();

        if (TextUtils.isEmpty(fname)) {
          Toast.makeText(PartyListAdd.this, "Please input value for First Name.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(lname)) {
          Toast.makeText(PartyListAdd.this, "Please input value for Last Name.", Toast.LENGTH_SHORT).show();
          return;
        }

        CollectionReference dbPartyList = db.collection("partylist");
        PartyList partyList = new PartyList(fname, lname, spinP, spinPartyList, 0);
        dbPartyList.add(partyList)
          .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
              if (task.isSuccessful()) {
                Toast.makeText(PartyListAdd.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PartyListAdd.this, MainActivity.class);

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

                i.putExtra("frgToLoad", "nav_gallery");
                startActivity(i);
                finish();
              } else {
                Toast.makeText(PartyListAdd.this, "Please check your inputs.", Toast.LENGTH_SHORT).show();
              }
            }
          });
      }
    });
  }

}
