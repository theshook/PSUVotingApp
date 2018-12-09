package psuva.com.ph.psuvotingsystem;

        import android.content.Context;
        import android.support.v4.util.ArrayMap;
        import android.support.v7.widget.RecyclerView;
        import android.util.SparseArray;
        import android.util.SparseBooleanArray;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.RadioButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;

public class PublicRelationsOfficeAdapter extends RecyclerView.Adapter<PublicRelationsOfficeAdapter.ViewHolder> {
  private final SparseBooleanArray array=new SparseBooleanArray();
  public static SparseArray ids=new SparseArray();
  private List<PartyList> listItemList;
  private Context context;
  int numberOfCheckboxesChecked = 0;
  public PartyList election;

  public PublicRelationsOfficeAdapter(List<PartyList> listItemList, Context context) {
    this.listItemList = listItemList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_election2, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    PartyList listItem = listItemList.get(position);

    holder.txtName.setText(listItem.getLastName() + ", " + listItem.getFirstName() + " (" + listItem.getPartyList() + ")");
    holder.txtPosition.setText(listItem.getPosition());
    if(array.get(position)){
      holder.checkBox.setChecked(true);
    }else{
      holder.checkBox.setChecked(false);
    }
  }

  @Override
  public int getItemCount() {
    return listItemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public TextView txtName, txtPosition;
    public CheckBox checkBox;

    public ViewHolder(View itemView) {
      super(itemView);

      txtName = itemView.findViewById(R.id.txtName);
      txtPosition = itemView.findViewById(R.id.txtPosition);
      checkBox = itemView.findViewById(R.id.checkBox);

      checkBox.setOnCheckedChangeListener(this);
      checkBox.setOnClickListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
      if (b && numberOfCheckboxesChecked >= 2) {
        checkBox.setChecked(false);

      } else {
        // the checkbox either got unchecked
        // or there are less than 2 other checkboxes checked
        // change your counter accordingly
        if (b) {
          numberOfCheckboxesChecked++;
          array.put(getAdapterPosition(),true);
          election = listItemList.get(getAdapterPosition());
          //Toast.makeText(context, "ADD: " + array, Toast.LENGTH_SHORT).show();
          ids.put(getAdapterPosition(), election.getId());
        } else {
          numberOfCheckboxesChecked--;
          array.delete(getAdapterPosition());
          ids.delete(getAdapterPosition());
          //Toast.makeText(context, "MINUS: " + array, Toast.LENGTH_SHORT).show();

        }

        // now everything is fine and you can do whatever
        // checking the checkbox should do here
      }
    }

    @Override
    public void onClick(View view) {
    }
  }
}




