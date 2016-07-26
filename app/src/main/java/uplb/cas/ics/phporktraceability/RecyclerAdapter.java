package uplb.cas.ics.phporktraceability;

import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by marmagno on 6/23/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements
        View.OnTouchListener, View.OnDragListener {

    String[] arr1 = {};
    String[] arr2 = {};
    String[] arr3 = {};
    String[] ids = {};

    public RecyclerAdapter(String[] display1, String[] display2,
                              String[] display3, String[] values) {
        arr1 = display1;
        arr2 = display2;
        arr3 = display3;
        ids = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pig_list_model, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_pig.setText(ids[position]);
        holder.tv_label.setText(arr1[position]);
        holder.tv_breed.setText(arr2[position]);
        holder.tv_gender.setText(arr3[position]);
    }

    @Override
    public int getItemCount() { return ids.length; }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_pig;
        private TextView tv_label;
        private TextView tv_breed;
        private TextView tv_gender;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_pig = (TextView) itemView.findViewById(R.id.tv_pig);
            tv_label = (TextView) itemView.findViewById(R.id.tv_pig_label);
            tv_breed = (TextView) itemView.findViewById(R.id.tv_breed);
            tv_gender = (TextView) itemView.findViewById(R.id.tv_gender);

        }
    }

}
