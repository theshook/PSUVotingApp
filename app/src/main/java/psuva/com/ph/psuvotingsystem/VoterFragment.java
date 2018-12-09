package psuva.com.ph.psuvotingsystem;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VoterFragment extends Fragment {

  private FirebaseFirestore db;
  TextView txt_v_add;
  private RecyclerView recyclerView;
  private RecyclerView.Adapter recyclerAdapter;
  private List<Voter> voters;
  private Voter voterDetails;

  public VoterFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_voter, container, false);

    db = FirebaseFirestore.getInstance();

    voterDetails = (Voter) getActivity().getIntent().getSerializableExtra("voterDetails");

    recyclerView = view.findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    voters = new ArrayList<>();
    recyclerAdapter = new VoterAdapter(voters, getContext());

    recyclerView.setAdapter(recyclerAdapter);

    db.collection("voter").orderBy("vote_LastName", Query.Direction.ASCENDING).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                  List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                  for (DocumentSnapshot d : list) {
                    Voter p = d.toObject(Voter.class);
                    p.setId(d.getId());
                    voters.add(p);
                  }
                  recyclerAdapter.notifyDataSetChanged();
                }
              }
            });

    txt_v_add = view.findViewById(R.id.txt_v_add);
    txt_p_addClick();

    return view;
  }

  private void txt_p_addClick() {
    txt_v_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getContext(), VoterAdd.class);
        Voter v = new Voter(
                voterDetails.getVote_FirstName(),
                voterDetails.getVote_LastName(),
                voterDetails.getVote_Course(),
                voterDetails.getVote_IdNumber(),
                voterDetails.getVote_email(),
                voterDetails.getIsVoted());
        v.setId(voterDetails.getId());
        intent.putExtra("voterDetails", v);
        startActivity(intent);
      }
    });
  }

}
