package psuva.com.ph.psuvotingsystem;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.RadioButton;
        import android.widget.TextView;
        import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
  private List<PartyList> listItemList;
  private Context context;
  PartyList election;

  public ResultAdapter(List<PartyList> listItemList, Context context) {
    this.listItemList = listItemList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_result, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PartyList listItem = listItemList.get(position);

    holder.txtName.setText(listItem.getLastName() + ", " + listItem.getFirstName() + " (" + listItem.getPartyList() + ")");
    holder.txtVotes.setText("Total Votes: " + listItem.getVotes());
    holder.txtPosition.setText(listItem.getPosition());
  }

  @Override
  public int getItemCount() {
    return listItemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName, txtVotes, txtPosition;

    public ViewHolder(View itemView) {
      super(itemView);

      txtName = itemView.findViewById(R.id.txtName);
      txtVotes = itemView.findViewById(R.id.txtVotes);
      txtPosition = itemView.findViewById(R.id.txtPosition);
    }
  }
}
