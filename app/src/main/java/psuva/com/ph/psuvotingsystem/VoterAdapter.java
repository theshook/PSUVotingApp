package psuva.com.ph.psuvotingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VoterAdapter  extends RecyclerView.Adapter<VoterAdapter.ViewHolder> {

  private List<Voter> voterList;
  private Context context;

  public VoterAdapter(List<Voter> voterList, Context context) {
    this.voterList = voterList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_voter, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Voter listItem = voterList.get(position);

    holder.txtName_txtCourse.setText(listItem.getVote_LastName() + ", " + listItem.getVote_FirstName() + " (" + listItem.getVote_Course() +")");
    holder.txtId.setText("Id: " + listItem.getVote_IdNumber());
    holder.txtEmail.setText("Email:" + listItem.getVote_email());
  }

  @Override
  public int getItemCount() {
    return voterList.size();
  }

  public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtName_txtCourse, txtId, txtEmail;
    public ViewHolder (View itemView) {
      super (itemView);
      txtName_txtCourse = itemView.findViewById(R.id.txtName_txtCourse);
      txtEmail = itemView.findViewById(R.id.txtEmail);
      txtId = itemView.findViewById(R.id.txtId);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      Voter voter = voterList.get(getAdapterPosition());
      Voter voterDetails = (Voter) ((Activity) context).getIntent().getSerializableExtra("voterDetails");
      Intent intent = new Intent(context, VoterUpdate.class);
      Voter v = new Voter(
              voterDetails.getVote_FirstName(),
              voterDetails.getVote_LastName(),
              voterDetails.getVote_Course(),
              voterDetails.getVote_IdNumber(),
              voterDetails.getVote_email(),
              voterDetails.getIsVoted());
      v.setId(voterDetails.getId());
      intent.putExtra("voterDetails", v);
      intent.putExtra("voter", voter);
      context.startActivity(intent);
    }
  }
}
