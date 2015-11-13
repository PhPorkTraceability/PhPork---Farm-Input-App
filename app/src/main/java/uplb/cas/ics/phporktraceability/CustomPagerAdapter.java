package uplb.cas.ics.phporktraceability;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by marmagno on 11/13/2015.
 */
public class CustomPagerAdapter extends PagerAdapter
        implements  View.OnTouchListener {

    private Context mContext;
    String[] arr = {};
    LayoutInflater inflater;

    public CustomPagerAdapter(Context context, String[] values) {
        mContext = context;
        arr = values;
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        TextView tv_item;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);

        // Locate the TextViews in viewpager_item.xml
        tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        // Capture position and set to the TextViews
        tv_item.setText(arr[position]);
        tv_item.setTag(arr[position]);
        tv_item.setOnTouchListener(this);
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
        //itemView.setOnTouchListener(this);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

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
