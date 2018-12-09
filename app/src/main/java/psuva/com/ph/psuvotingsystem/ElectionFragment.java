package psuva.com.ph.psuvotingsystem;


import android.content.Intent;
import android.graphics.Color;
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
    String bm1 = String.valueOf(voterDetails.getIsVoted().get("bm1"));
    String pro1 = String.valueOf(voterDetails.getIsVoted().get("pro1"));
    String vcc = String.valueOf(voterDetails.getIsVoted().get("vcc"));
    String vcpa = String.valueOf(voterDetails.getIsVoted().get("vcpa"));
    String vcsrw = String.valueOf(voterDetails.getIsVoted().get("vcsrw"));
    String auditor = String.valueOf(voterDetails.getIsVoted().get("auditor"));


    checkIfVoted(president, btnNext);
    checkIfVoted(evp, btnNext2);
    checkIfVoted(vcpa, btnNext3);
    checkIfVoted(vcc, btnNext4);
    checkIfVoted(vcsrw, btnNext5);
    checkIfVoted(auditor, btnNext6);
    checkIfVoted(bm1, btnNext7);
    checkIfVoted(pro1, btnNext8);

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
        getActivity().finish();
      }
    });
  }
  private void btnNext2Onclick() {
    btnNext2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), ExecutiveVicePresidentActivity.class);
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
        getActivity().finish();
      }
    });
  }
  private void btnNext3Onclick() {
    btnNext3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), ProjectAndActivitiesActivity.class);
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
        getActivity().finish();
      }
    });
  }
  private void btnNext4Onclick() {
    btnNext4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), CommunicationAcitivity.class);
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
        getActivity().finish();
      }
    });
  }
  private void btnNext5Onclick() {
    btnNext5.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), StudentRightsActivity.class);
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
        getActivity().finish();
      }
    });
  }
  private void btnNext6Onclick() {
    btnNext6.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), AuditorActivity.class);
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
        getActivity().finish();
      }
    });
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
        getActivity().finish();
      }
    });
  }
  private void btnNext8Onclick() {
    btnNext8.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), PublicRelationsOfficeActivity.class);
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
        getActivity().finish();
      }
    });
  }

  private boolean checkIfVoted(String b, Button btn) {
    if (b.equals("true")) {
      btn.setText(btn.getText().toString() + " (VOTED)");
      btn.setBackgroundColor(Color.GRAY);
      btn.setEnabled(false);
      return true;
    }

    return false;
  }

}
