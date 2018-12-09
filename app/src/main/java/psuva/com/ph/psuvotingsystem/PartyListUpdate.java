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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class PartyListUpdate extends AppCompatActivity {
  private FirebaseFirestore db;
  private PartyList partyList;

  private EditText edtFname, edtLname;
  private Spinner spinParty, spinPosition;
  private Button btnSave, btnDelete;
  private Voter voterDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_party_list_update);

    db = FirebaseFirestore.getInstance();
    partyList = (PartyList) getIntent().getSerializableExtra("partyList");
    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");

    edtFname = findViewById(R.id.txt_p_firstName2);
    edtLname = findViewById(R.id.txt_v_lastName);
    spinParty = findViewById(R.id.spinPartyList2);
    spinPosition = findViewById(R.id.spinPosition2);
    btnSave = findViewById(R.id.btn_p_save2);
    btnDelete = findViewById(R.id.btn_p_delete);

    ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Positions, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinPosition.setAdapter(adapter);

    ArrayAdapter<CharSequence> adapter1  = ArrayAdapter.createFromResource(this, R.array.PartyList, android.R.layout.simple_spinner_item);
    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinParty.setAdapter(adapter1);

    // Set the Value
    edtFname.setText(partyList.getFirstName());
    edtLname.setText(partyList.getLastName());

    // Set the Spinner
    int spinnerPosition = adapter.getPosition(partyList.getPosition());
    spinPosition.setSelection(spinnerPosition);

    int spinnerParty = adapter1.getPosition(partyList.getPartyList());
    spinParty.setSelection(spinnerParty);

    // Button Click Listener
    btnSaveOnClick();
    btnDeleteOnClick();
  }

  private void btnDeleteOnClick() {
    btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartyListUpdate.this);
        AlertDialog ad;
        builder.setTitle("Are you sure about this?");
        builder.setMessage("Deletion is permanent...");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

            final ProgressDialog pd = new ProgressDialog(PartyListUpdate.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Fetching data. . . ");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            db.collection("partylist")
              .document(partyList.getId()).delete()
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()) {
                    Toast.makeText(PartyListUpdate.this, "Candidate Information Deleted.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PartyListUpdate.this, MainActivity.class);

                    Voter v = new Voter(
                            voterDetails.getVote_FirstName(),
                            voterDetails.getVote_LastName(),
                            voterDetails.getVote_Course(),
                            voterDetails.getVote_IdNumber(),
                            voterDetails.getVote_email(),
                            voterDetails.getIsVoted());
                    v.setId(voterDetails.getId());
                    i.putExtra("voterDetails", v);


                    i.putExtra("frgToLoad", "nav_gallery");
                    finish();
                    pd.dismiss();
                    startActivity(i);
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
        String fname, lname, spinP, spinPartyList;
        fname = edtFname.getText().toString().trim();
        lname = edtLname.getText().toString().trim();
        spinP = spinPosition.getSelectedItem().toString();
        spinPartyList = spinParty.getSelectedItem().toString();

        if (TextUtils.isEmpty(fname)) {
          Toast.makeText(PartyListUpdate.this, "Please input value for First Name.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(lname)) {
          Toast.makeText(PartyListUpdate.this, "Please input value for Last Name.", Toast.LENGTH_SHORT).show();
          return;
        }

        final ProgressDialog pd = new ProgressDialog(PartyListUpdate.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Fetching data. . . ");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        db.collection("partylist").document(partyList.getId())
                .update("firstName",fname,
                        "lastName", lname,
                        "position", spinP,
                        "partyList", spinPartyList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Toast.makeText(PartyListUpdate.this, "Succesfully saved.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PartyListUpdate.this, MainActivity.class);

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
                    finish();
                    pd.dismiss();
                    startActivity(i);
                  }
                });

      }
    });
  }
}
