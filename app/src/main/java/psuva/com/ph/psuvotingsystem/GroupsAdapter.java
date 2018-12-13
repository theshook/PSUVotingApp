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

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

  private List<Groups> listItemList;
  private Context context;

  public GroupsAdapter(List<Groups> listItemList, Context context) {
    this.listItemList = listItemList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_course, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Groups listItem = listItemList.get(position);
    holder.txtCourse.setText(listItem.getGroups());
  }

  @Override
  public int getItemCount() {
    return listItemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder
          implements View.OnClickListener{

    public TextView txtCourse;

    public ViewHolder(View itemView) {
      super(itemView);

      txtCourse = itemView.findViewById(R.id.txtCourse);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      Groups course = listItemList.get(getAdapterPosition());
      Voter voterDetails = (Voter) ((Activity) context).getIntent().getSerializableExtra("voterDetails");
      Intent intent = new Intent(context, GroupsUpdate.class);
      Voter v = new Voter(
              voterDetails.getVote_FirstName(),
              voterDetails.getVote_LastName(),
              voterDetails.getVote_Course(),
              voterDetails.getVote_IdNumber(),
              voterDetails.getVote_email(),
              voterDetails.getIsVoted());
      v.setId(voterDetails.getId());
      intent.putExtra("voterDetails", v);
      intent.putExtra("course", course);
      context.startActivity(intent);
    }
  }
}