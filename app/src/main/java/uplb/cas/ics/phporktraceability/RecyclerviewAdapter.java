package uplb.cas.ics.phporktraceability;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by marmagno on 6/16/2016.
 */
public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;

    public RecyclerviewAdapter(Context context){
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.pig_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pig;
        TextView tv_pig_label;
        TextView tv_breed;
        TextView tv_gender;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_pig = (TextView) itemView.findViewById(R.id.tv_pig);
            tv_pig_label = (TextView) itemView.findViewById(R.id.tv_pig_label);
            tv_breed = (TextView) itemView.findViewById(R.id.tv_breed);
            tv_gender = (TextView) itemView.findViewById(R.id.tv_gender);
        }


    }
}
