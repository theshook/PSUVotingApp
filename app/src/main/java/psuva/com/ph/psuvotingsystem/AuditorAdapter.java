package psuva.com.ph.psuvotingsystem;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.RadioButton;
        import android.widget.TextView;

        import java.util.List;

public class AuditorAdapter extends RecyclerView.Adapter<AuditorAdapter.ViewHolder> {
  public static String _id = "";
  private List<PartyList> listItemList;
  private Context context;
  private int lastCheckedPosition = -1;
  PartyList election;

  public AuditorAdapter(List<PartyList> listItemList, Context context) {
    this.listItemList = listItemList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_election, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PartyList listItem = listItemList.get(position);

    holder.txtName.setText(listItem.getLastName() + ", " + listItem.getFirstName() + " (" + listItem.getPartyList() + ")");
    holder.txtPosition.setText(listItem.getPosition());
    holder.radioButton.setChecked(position == lastCheckedPosition);
  }

  @Override
  public int getItemCount() {
    return listItemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName, txtPosition;
    public RadioButton radioButton;

    public ViewHolder(View itemView) {
      super(itemView);

      txtName = itemView.findViewById(R.id.txtName);
      txtPosition = itemView.findViewById(R.id.txtPosition);
      radioButton = itemView.findViewById(R.id.radioButton);

      radioButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          lastCheckedPosition = getAdapterPosition();
          election = listItemList.get(getAdapterPosition());
          _id = election.getId();
          notifyDataSetChanged();

        }
      });

    }
  }
}


