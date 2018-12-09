package psuva.com.ph.psuvotingsystem;

import android.content.Intent;
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

public class BusinessManagerActivity extends AppCompatActivity {

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
    setContentView(R.layout.activity_business_manager);

    db = FirebaseFirestore.getInstance();

    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");
    Log.d("TAG THIS PARTY LIST GET", "President Activity: " + voterDetails.getId());
    recyclerView = findViewById(R.id.recyclerView);
    btnNext = findViewById(R.id.btnNext);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    partyLists = new ArrayList<>();
    recyclerAdapter = new BusinessManagerAdapter(partyLists, this);

    recyclerView.setAdapter(recyclerAdapter);

    president = db.collection("partylist");
    voted = db.collection("voted");

    Query bmQuery = president.whereEqualTo("position", "Business Manager");


    queryPositions(bmQuery);

    btnNextOnClick();
  }

  private void btnNextOnClick() {
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (BusinessManagerAdapter.ids.size() <= 0) {
          Toast.makeText(BusinessManagerActivity.this, "Kindly select a Business Manager to vote.", Toast.LENGTH_SHORT).show();
          return;
        }
        for(int i = 0, nsize = BusinessManagerAdapter.ids.size(); i < nsize; i++) {
          Object obj = BusinessManagerAdapter.ids.valueAt(i);
          countVotes(db.collection("partylist").document(obj.toString()));
          Log.d("TAG THIS PARTY LIST GET", "onClick: " + obj.toString());
        }
        updateVoter();
      }
    });
  }

  private void updateVoter() {
    db.collection("voter").document(voterDetails.getId())
            .update("isVoted.bm1", true)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Toast.makeText(BusinessManagerActivity.this, "Succesfully voted.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(BusinessManagerActivity.this, MainActivity.class);

                Map<String, Boolean> isVoted = (Map<String, Boolean>) voterDetails.getIsVoted();

                if (isVoted.containsKey("bm1")) {
                  isVoted.put("bm1", true);
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
                startActivity(i);
                finish();
              }
            });
  }

  private Task<Void> countVotes(final DocumentReference partylist) {
    // Create reference for new rating, for use inside the transaction
//    final DocumentReference ratingRef = partylist.collection("partylist").document();

    // In a transaction, add the new rating and update the aggregate totals
    return db.runTransaction(new Transaction.Function<Void>() {
      @Override
      public Void apply(Transaction transaction) throws FirebaseFirestoreException {
        PartyList pl = transaction.get(partylist).toObject(PartyList.class);

        // Compute new number of ratings
        int newNumRatings = pl.getVotes() + 1;

        // Set new restaurant info
        pl.votes = newNumRatings;

        // Update restaurant
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
          }
          Log.d("TAG ME SIZE", "onSuccess: " + list.size());
          recyclerAdapter.notifyDataSetChanged();
        }
      }
    });
  }
}
