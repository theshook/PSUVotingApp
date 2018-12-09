package psuva.com.ph.psuvotingsystem;

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
public class ResultFragment extends Fragment {

  private FirebaseFirestore db;
  private RecyclerView recyclerView, recyclerEVP, recyclerVCPA,
          recyclerVCC, recyclerVCSRW, recyclerAuditor, recyclerBM,
          recyclerPRO;
  private RecyclerView.Adapter recyclerAdapter, recyclerAdapterEVP, recyclerAdapterVCPA,
          recyclerAdapterVCC, recyclerAdapterVCSRW, recyclerAdapterAuditor,
          recyclerAdapterBM, recyclerAdapterPRO;

  private List<PartyList> partyLists, partyListsEVP, partyListsVCPA,
          partyListsVCC, partyListsVCSRW, partyListsAuditor,
          partyListsBM, partyListsPRO;

  public ResultFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_result, container, false);

    db = FirebaseFirestore.getInstance();

    partyLists = new ArrayList<>();
    partyListsEVP = new ArrayList<>();
    partyListsVCPA = new ArrayList<>();
    partyListsVCC = new ArrayList<>();
    partyListsVCSRW = new ArrayList<>();
    partyListsAuditor = new ArrayList<>();
    partyListsBM = new ArrayList<>();
    partyListsPRO = new ArrayList<>();

    recyclerView = view.findViewById(R.id.recyclerView);
    recyclerEVP = view.findViewById(R.id.recyclerEVP);
    recyclerVCPA = view.findViewById(R.id.recyclerVCPA);
    recyclerVCC = view.findViewById(R.id.recyclerVCC);
    recyclerVCSRW = view.findViewById(R.id.recyclerVCSRW);
    recyclerAuditor = view.findViewById(R.id.recyclerAuditor);
    recyclerBM = view.findViewById(R.id.recyclerBM);
    recyclerPRO = view.findViewById(R.id.recyclerPRO);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerEVP.setHasFixedSize(true);
    recyclerEVP.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerVCPA.setHasFixedSize(true);
    recyclerVCPA.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerVCC.setHasFixedSize(true);
    recyclerVCC.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerVCSRW.setHasFixedSize(true);
    recyclerVCSRW.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerAuditor.setHasFixedSize(true);
    recyclerAuditor.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerBM.setHasFixedSize(true);
    recyclerBM.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerPRO.setHasFixedSize(true);
    recyclerPRO.setLayoutManager(new LinearLayoutManager(getContext()));

    recyclerAdapter = new ResultAdapter(partyLists, getContext());
    recyclerAdapterEVP = new ResultAdapter(partyListsEVP, getContext());
    recyclerAdapterVCPA = new ResultAdapter(partyListsVCPA, getContext());
    recyclerAdapterVCC = new ResultAdapter(partyListsVCC, getContext());
    recyclerAdapterVCSRW = new ResultAdapter(partyListsVCSRW, getContext());
    recyclerAdapterAuditor = new ResultAdapter(partyListsAuditor, getContext());
    recyclerAdapterBM = new ResultAdapter(partyListsBM, getContext());
    recyclerAdapterPRO = new ResultAdapter(partyListsPRO, getContext());

    recyclerView.setAdapter(recyclerAdapter);
    recyclerEVP.setAdapter(recyclerAdapterEVP);
    recyclerVCPA.setAdapter(recyclerAdapterVCPA);
    recyclerVCC.setAdapter(recyclerAdapterVCC);
    recyclerVCSRW.setAdapter(recyclerAdapterVCSRW);
    recyclerAuditor.setAdapter(recyclerAdapterAuditor);
    recyclerBM.setAdapter(recyclerAdapterBM);
    recyclerPRO.setAdapter(recyclerAdapterPRO);

    getQuery("President", partyLists, recyclerAdapter);
    getQuery("Executive Vice President", partyListsEVP, recyclerAdapterEVP);
    getQuery("VC for Project and Activities", partyListsVCPA, recyclerAdapterVCPA);
    getQuery("VC for Communication", partyListsVCC, recyclerAdapterVCC);
    getQuery("VC for Student Rights and Welfare", partyListsVCSRW, recyclerAdapterVCSRW);
    getQuery("Auditor", partyListsAuditor, recyclerAdapterAuditor);
    getQuery("Business Manager", partyListsBM, recyclerAdapterBM);
    getQuery("Public Relations Office", partyListsPRO, recyclerAdapterPRO);

    return view;
  }

  private void getQuery(String position, final List<PartyList> partyLists, final RecyclerView.Adapter recyclerAdapter) {
    db.collection("partylist")
      .whereEqualTo("position", position)
      .orderBy("lastName")
      .orderBy("votes", Query.Direction.DESCENDING)
      .get()
      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        if (!queryDocumentSnapshots.isEmpty()) {
          List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
          for (DocumentSnapshot d : list) {
            PartyList p = d.toObject(PartyList.class);
            p.setId(d.getId());
            partyLists.add(p);
          }
          recyclerAdapter.notifyDataSetChanged();
        }
        }
      });
  }

}
