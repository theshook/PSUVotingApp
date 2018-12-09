package psuva.com.ph.psuvotingsystem;


import android.content.Intent;
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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ElectionFragment extends Fragment {

  private Button btnNext, btnNext2, btnNext3, btnNext4, btnNext5, btnNext6, btnNext7, btnNext8;
  private Voter voterDetails;

  public ElectionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_election, container, false);

    btnNext = view.findViewById(R.id.btnNext);
    btnNext2 = view.findViewById(R.id.btnNext2);
    btnNext3 = view.findViewById(R.id.btnNext3);
    btnNext4 = view.findViewById(R.id.btnNext4);
    btnNext5 = view.findViewById(R.id.btnNext5);
    btnNext6 = view.findViewById(R.id.btnNext6);
    btnNext7 = view.findViewById(R.id.btnNext7);
    btnNext8 = view.findViewById(R.id.btnNext8);

    voterDetails = (Voter) getActivity().getIntent().getSerializableExtra("voterDetails");

    String president = String.valueOf(voterDetails.getIsVoted().get("president"));
    String evp = String.valueOf(voterDetails.getIsVoted().get("evp"));

    Log.d("TAG THIS PARTY LIST GET", "onClick: AUDITOR " + voterDetails.getIsVoted().get("auditor"));
    checkIfVoted(president, btnNext);
    checkIfVoted(evp, btnNext2);

    btnNextOnclick();
    btnNext2Onclick();
    btnNext3Onclick();
    btnNext4Onclick();
    btnNext5Onclick();
    btnNext6Onclick();
    btnNext7Onclick();
    btnNext8Onclick();

    return view;
  }
  private void btnNextOnclick() {
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), PresidentActivity.class);
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
        startActivity(i);
      }
    });
  }
  private void btnNext2Onclick() {
  }
  private void btnNext3Onclick() {
  }
  private void btnNext4Onclick() {
  }
  private void btnNext5Onclick() {
  }
  private void btnNext6Onclick() {
  }
  private void btnNext7Onclick() {
    btnNext7.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), BusinessManagerActivity.class);
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
        startActivity(i);
      }
    });
  }
  private void btnNext8Onclick() {
  }



  private boolean checkIfVoted(String b, Button btn) {
    if (b.equals("true")) {
      btn.setVisibility(View.INVISIBLE);
      return true;
    }

    return false;
  }

}
