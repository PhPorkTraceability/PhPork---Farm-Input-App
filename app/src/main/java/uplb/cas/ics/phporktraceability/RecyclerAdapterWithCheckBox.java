package uplb.cas.ics.phporktraceability;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by marmagno on 7/19/2016.
 */
public class RecyclerAdapterWithCheckBox extends
        RecyclerView.Adapter<RecyclerAdapterWithCheckBox.ViewHolder> {

    /**
     * Code snippets taken from http://android-pratap.blogspot.com/2015/01/recyclerview-with-checkbox-example.html
     * By Prathap Kumar on 01/09/2015
     */
    private List<CheckItemModel> chkList;

    public RecyclerAdapterWithCheckBox(List<CheckItemModel> chkList) {
        this.chkList = chkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_list_model, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_id.setText(chkList.get(position).getID());
        holder.tv_label.setText(chkList.get(position).getLabel());
        holder.tv_label2.setText(chkList.get(position).getLabel2());
        holder.cb_checkbox.setChecked(chkList.get(position).isSelected());
        holder.cb_checkbox.setTag(chkList.get(position));

        holder.cb_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;

                CheckItemModel checkItemModel = (CheckItemModel) checkBox.getTag();
                checkItemModel.setSelected(checkBox.isChecked());

                Toast.makeText(
                        v.getContext(),
                        "Chosen " + checkItemModel.getLabel(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() { return chkList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_id;
        public TextView tv_label;
        public TextView tv_label2;
        public CheckBox cb_checkbox;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_label = (TextView) itemView.findViewById(R.id.tv_label);
            tv_label2 = (TextView) itemView.findViewById(R.id.tv_label2);
            cb_checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);

        }
    }

    public List<CheckItemModel> getCheckList(){
        return chkList;
    }

}