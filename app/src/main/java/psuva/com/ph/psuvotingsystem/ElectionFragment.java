package psuva.com.ph.psuvotingsystem;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ElectionFragment extends Fragment {

  private FirebaseFirestore db;
  private RecyclerView recyclerView;
  private RecyclerView.Adapter recyclerAdapter;
  private Button btnNext;
  private List<PartyList> partyLists;

  public ElectionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_election, container, false);

    db = FirebaseFirestore.getInstance();

    recyclerView = view.findViewById(R.id.recyclerView);
    btnNext = view.findViewById(R.id.btnNext);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    partyLists = new ArrayList<>();
    recyclerAdapter = new ElectionAdapter(partyLists, getContext());

    recyclerView.setAdapter(recyclerAdapter);

    CollectionReference president = db.collection("partylist");
    Query presidentQuery = president.whereEqualTo("position", "President");

    queryPositions(presidentQuery);

    btnNextOnClick();

    return view;
  }

  private void btnNextOnClick() {
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        for(int i = 0, nsize = BusinessManagerAdapter.ids.size(); i < nsize; i++) {
//          Object obj = BusinessManagerAdapter.ids.valueAt(i);
//          Log.d("TAG THIS PARTY LIST GET", "onClick: " + obj.toString());
//        }

        CollectionReference dbVotes = db.collection("votes");
        Log.d("TAG THIS PARTY LIST GET", "onClick: " + ElectionAdapter._id);

      }
    });
  }

  private void queryPositions(Query query) {
    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        if (!queryDocumentSnapshots.isEmpty()) {
          List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

          for (DocumentSnapshot d : list) {
            PartyList e = d.toObject(PartyList.class);
            e.setId(d.getId());
            partyLists.add(e);
          }
          recyclerAdapter.notifyDataSetChanged();
        }
      }
    });
  }

}
