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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartyListFragment extends Fragment {

  private FirebaseFirestore db;
  private TextView txt_p_add;
  private RecyclerView recyclerView;
  private RecyclerView.Adapter recyclerAdapter;
  private Voter voterDetails;
  private List<PartyList> partyLists;
  private Spinner spinner2;

  public PartyListFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view =  inflater.inflate(R.layout.fragment_party_list, container, false);

    db = FirebaseFirestore.getInstance();

    voterDetails = (Voter) getActivity().getIntent().getSerializableExtra("voterDetails");

    recyclerView = view.findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    partyLists = new ArrayList<>();
    recyclerAdapter = new PartyListAdapter(partyLists, getContext());

    recyclerView.setAdapter(recyclerAdapter);

    spinner2 = view.findViewById(R.id.spinner2);

    txt_p_add = view.findViewById(R.id.txt_v_add);
    txt_p_addClick();
    spinnerOnchange();
    return view;
  }

  private void spinnerOnchange() {
    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String groups = spinner2.getSelectedItem().toString();
        filterPartyList(groups);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }

  @Override
  public void onStart() {
    db.collection("groups")
      .orderBy("groups", Query.Direction.ASCENDING)
      .get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
          if (!queryDocumentSnapshots.isEmpty()) {
            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
            final List<String> courses = new ArrayList<String>();
            for (DocumentSnapshot d : list) {
              Groups p = d.toObject(Groups.class);
              p.setId(d.getId());
              courses.add(p.getGroups());
            }
            ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courses);
            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(courseAdapter);
          }
        }
      });
    super.onStart();
  }

  private void filterPartyList(String groups) {
    final ProgressDialog pd = new ProgressDialog(getContext());
    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pd.setMessage("Fetching data. . . ");
    pd.setIndeterminate(true);
    pd.setCancelable(false);
    pd.show();
    db.collection("partylist")
            .whereEqualTo("partyList", groups)
            .orderBy("lastName", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                  List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                  partyLists.clear();
                  for (DocumentSnapshot d : list) {
                    PartyList p = d.toObject(PartyList.class);
                    p.setId(d.getId());
                    partyLists.add(p);
                  }
                  recyclerAdapter.notifyDataSetChanged();
                  pd.dismiss();
                } else {
                  Toast.makeText(getContext(), "No Available Data.", Toast.LENGTH_SHORT).show();
                  pd.dismiss();
                }
              }
            });
  }

  private void txt_p_addClick() {
    txt_p_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getContext(), PartyListAdd.class);
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
