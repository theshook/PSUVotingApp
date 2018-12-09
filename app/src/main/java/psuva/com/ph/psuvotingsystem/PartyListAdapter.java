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

public class PartyListAdapter extends RecyclerView.Adapter<PartyListAdapter.ViewHolder> {

  private List<PartyList> listItemList;
  private Context context;

  public PartyListAdapter(List<PartyList> listItemList, Context context) {
    this.listItemList = listItemList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_partylist, parent, false);


    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PartyList listItem = listItemList.get(position);

    holder.txtName.setText(listItem.getLastName() + ", " + listItem.getFirstName() + " (" + listItem.getPartyList() +")");
    holder.txtPosition.setText(listItem.getPosition());
  }

  @Override
  public int getItemCount() {
    return listItemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName, txtPosition;
    private Voter voterDetails;

    public ViewHolder(View itemView) {
      super(itemView);

      txtName = itemView.findViewById(R.id.txtName);
      txtPosition = itemView.findViewById(R.id.txtPosition);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      PartyList partyList = listItemList.get(getAdapterPosition());
      Voter voterDetails = (Voter) ((Activity) context).getIntent().getSerializableExtra("voterDetails");
      Intent intent = new Intent(context, PartyListUpdate.class);
      Voter v = new Voter(
              voterDetails.getVote_FirstName(),
              voterDetails.getVote_LastName(),
              voterDetails.getVote_Course(),
              voterDetails.getVote_IdNumber(),
              voterDetails.getVote_email(),
              voterDetails.getIsVoted());
      v.setId(voterDetails.getId());
      intent.putExtra("voterDetails", v);
      intent.putExtra("partyList", partyList);
      context.startActivity(intent);
    }
  }
}
