package psuva.com.ph.psuvotingsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

  private FirebaseFirestore db;
  private Button btnLogin;
  private EditText edtUsername, edtPassword;
  private CollectionReference voterCredentials;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    db = FirebaseFirestore.getInstance();

    btnLogin = findViewById(R.id.btnLogin);
    edtUsername = findViewById(R.id.edtUsername);
    edtPassword = findViewById(R.id.edtPassword);

    voterCredentials = db.collection("voter");

    btnLoginOnClick();
  }

  private void btnLoginOnClick() {
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String username, password;
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
          Toast.makeText(LoginActivity.this, "Please input value for Username.", Toast.LENGTH_SHORT).show();
          return;
        }

        if (TextUtils.isEmpty(password)) {
          Toast.makeText(LoginActivity.this, "Please input value for Password.", Toast.LENGTH_SHORT).show();
          return;
        }

        Query logInfo = voterCredentials
                .whereEqualTo("vote_email", username)
                .whereEqualTo("vote_IdNumber", password);

        logInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (QueryDocumentSnapshot document : task.getResult()) {
                Object u = document.get("vote_email");
                Object p = document.get("vote_IdNumber");
                if(username.equals(u) && password.equals(p)) {
                  Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                TODO: PASS THE LOGIN DETAILS
//                  for (Map<String, Object> map : list) {
//                    for (Map.Entry<String, Object> entry : map.entrySet()) {
//                      String key = entry.getKey();
//                      Object value = entry.getValue();
//                    }
//                  }
                  Log.d("TAG THIS PARTY LIST GET", "onClick: " + document.get("isVoted").equals("president"));
                  Map<String, Boolean> isVoted = (Map<String, Boolean>) document.get("isVoted");
                  Voter voterDetails = new Voter(
                          document.get("vote_FirstName").toString(),
                          document.get("vote_LastName").toString(),
                          document.get("vote_Course").toString(),
                          document.get("vote_IdNumber").toString(),
                          document.get("vote_email").toString(),
                          isVoted);
                  voterDetails.setId(document.getId());
                  i.putExtra("voterDetails", voterDetails);
                  startActivity(i);
                  finish();
                  return;

                }
              }
              Toast.makeText(LoginActivity.this, "Username or Password is invalid.", Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    });
  }
}
