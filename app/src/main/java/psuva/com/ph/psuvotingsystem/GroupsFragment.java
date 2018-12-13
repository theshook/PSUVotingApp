package psuva.com.ph.psuvotingsystem;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
public class GroupsFragment extends Fragment {

  private FirebaseFirestore db;
  private RecyclerView recyclerGroups;
  private RecyclerView.Adapter recyclerAdapter;
  private List<Groups> groupsList;
  private Voter voterDetails;
  private Button btnGroups;

  public GroupsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_groups, container, false);

    db = FirebaseFirestore.getInstance();

    voterDetails = (Voter) getActivity().getIntent().getSerializableExtra("voterDetails");

    recyclerGroups = view.findViewById(R.id.recyclerGroups);
    recyclerGroups.setHasFixedSize(true);
    recyclerGroups.setLayoutManager(new LinearLayoutManager(getContext()));

    groupsList = new ArrayList<>();
    recyclerAdapter = new GroupsAdapter(groupsList, getContext());

    recyclerGroups.setAdapter(recyclerAdapter);

    btnGroups = view.findViewById(R.id.btnGroups);
    btnGroupsClick();

    return view;
  }

  @Override
  public void onStart() {
    final ProgressDialog pd = new ProgressDialog(getContext());
    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pd.setMessage("Fetching data. . . ");
    pd.setIndeterminate(true);
    pd.setCancelable(false);
    pd.show();
    db.collection("groups")
            .orderBy("groups", Query.Direction.ASCENDING).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                  List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                  groupsList.clear();
                  for (DocumentSnapshot d : list) {
                    Groups p = d.toObject(Groups.class);
                    p.setId(d.getId());
                    groupsList.add(p);
                  }
                  recyclerAdapter.notifyDataSetChanged();
                  pd.dismiss();
                } else {
                  pd.dismiss();
                  Toast.makeText(getContext(), "No Daa available", Toast.LENGTH_SHORT).show();
                }
              }
            });
    super.onStart();
  }

  private void btnGroupsClick() {
    btnGroups.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getContext(), GroupsAdd.class);
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
