package psuva.com.ph.psuvotingsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditorActivity extends AppCompatActivity {
  private FirebaseFirestore db;
  private CollectionReference voted, president;
  private Query countVotesQuery;
  private RecyclerView recyclerView;
  private RecyclerView.Adapter recyclerAdapter;
  private Button btnNext;
  private List<PartyList> partyLists;
  private Voter voterDetails;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auditor);

    db = FirebaseFirestore.getInstance();

    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");
    Log.d("TAG THIS PARTY LIST GET", "President Activity: " + voterDetails.getId());
    recyclerView = findViewById(R.id.recyclerView);
    btnNext = findViewById(R.id.btnNext);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    partyLists = new ArrayList<>();
    recyclerAdapter = new AuditorAdapter(partyLists, this);

    recyclerView.setAdapter(recyclerAdapter);

    president = db.collection("partylist");
    voted = db.collection("voted");

    Query presidentQuery = president.whereEqualTo("position", "Auditor");


    queryPositions(presidentQuery);

    btnNextOnClick();
  }

  private void btnNextOnClick() {
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (AuditorAdapter._id.equals(null) || AuditorAdapter._id.equals("")) {
          Toast.makeText(AuditorActivity.this, "Kindly select to vote.", Toast.LENGTH_SHORT).show();
          return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AuditorActivity.this);
        AlertDialog ad;
        builder.setTitle("Are you sure about this?");
        builder.setMessage("Voting is permanent and cannot be change later");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

            final ProgressDialog pd = new ProgressDialog(AuditorActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Fetching data. . . ");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            countVotes(db.collection("partylist").document(AuditorAdapter._id));
            updateVoter();

            pd.dismiss();
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

  private void updateVoter() {
    db.collection("voter").document(voterDetails.getId())
            .update("isVoted.auditor", true)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Toast.makeText(AuditorActivity.this, "Succesfully voted.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AuditorActivity.this, MainActivity.class);

                Map<String, Boolean> isVoted = (Map<String, Boolean>) voterDetails.getIsVoted();

                if (isVoted.containsKey("auditor")) {
                  isVoted.put("auditor", true);
                }

                Voter v = new Voter(
                        voterDetails.getVote_FirstName(),
                        voterDetails.getVote_LastName(),
                        voterDetails.getVote_Course(),
                        voterDetails.getVote_IdNumber(),
                        voterDetails.getVote_email(),
                        isVoted);
                v.setId(voterDetails.getId());
                i.putExtra("voterDetails", v);
                i.putExtra("frgToLoad", "nav_camera");
                finish();
                startActivity(i);
              }
            });
  }

  private Task<Void> countVotes(final DocumentReference partylist) {
    return db.runTransaction(new Transaction.Function<Void>() {
      @Override
      public Void apply(Transaction transaction) throws FirebaseFirestoreException {
        PartyList pl = transaction.get(partylist).toObject(PartyList.class);

        int newNumRatings = pl.getVotes() + 1;
        pl.votes = newNumRatings;
        transaction.set(partylist, pl);

        return null;
      }
    });
  }

  private void queryPositions(Query query) {
    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        if (!queryDocumentSnapshots.isEmpty()) {
          List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

          for (final DocumentSnapshot d : list) {
            PartyList e = d.toObject(PartyList.class);
            e.setId(d.getId());
            partyLists.add(e);

            countVotesQuery = voted.whereEqualTo("candidateId", d.getId());
            countVotesQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                int counter = 0;
                for (DocumentSnapshot ds : list) {
                  Voted vd = ds.toObject(Voted.class);
                  if (vd.getCandidateId().equals(d.getId())) {
                    counter++;
                    Log.d("TAG ME SIZE", "Total Votes for: " + vd.getCandidateId() + " (" + counter +")");
                  }
                }
              }
            });
          }
          Log.d("TAG ME SIZE", "onSuccess: " + list.size());
          recyclerAdapter.notifyDataSetChanged();
        }
      }
    });
  }
}
