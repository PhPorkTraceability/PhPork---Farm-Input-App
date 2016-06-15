package uplb.cas.ics.phporktraceability;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by marmagno on 11/25/2015.
 */
public class CustomPagerAdapter extends PagerAdapter
        implements  View.OnTouchListener {

    String[] arr1 = {};
    String[] arr2 = {};
    String[] arr3 = {};
    String[] ids = {};
    LayoutInflater inflater;
    private Context mContext;

    public CustomPagerAdapter(Context context, String[] display1, String[] display2,
                               String[] display3, String[] values) {
        mContext = context;
        arr1 = display1;
        arr2 = display2;
        arr3 = display3;
        ids = values;
    }

    @Override
    public int getCount() {
        return arr1.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        TextView tv_item, tv_item2, tv_item3;
        LinearLayout tl;
        ImageView iv_left, iv_right;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);

        // Locate the TextViews in viewpager_item.xml
        tv_item = (TextView) itemView.findViewById(R.id.tv_subitem1);
        // Capture position and set to the TextViews
        tv_item.setText(arr1[position]);
        // Locate the TextViews in viewpager_item.xml
        tv_item2 = (TextView) itemView.findViewById(R.id.tv_subitem2);
        // Capture position and set to the TextViews
        tv_item2.setText(arr2[position]);
        // Locate the TextViews in viewpager_item.xml
        tv_item3 = (TextView) itemView.findViewById(R.id.tv_subitem3);
        // Capture position and set to the TextViews
        tv_item3.setText(arr3[position]);

        tl = (LinearLayout) itemView.findViewById(R.id.whole_item);
        tl.setTag(ids[position]);
        tl.setOnTouchListener(this);
        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);
        //itemView.setOnTouchListener(this);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((LinearLayout) object);

    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            return true;
        }
        else { return false; }
    }


}
