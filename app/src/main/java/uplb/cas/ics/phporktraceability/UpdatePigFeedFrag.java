package uplb.cas.ics.phporktraceability;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 1/11/2016.
 */
public class UpdatePigFeedFrag extends Fragment {

    private static final String LOGCAT = UpdatePigFeedFrag.class.getSimpleName();
    private ViewPager frag_viewPager;
    private PagerAdapter frag_adapter;
    private LinearLayout ll;
    private LinearLayout bl;
    private TextView tv_item;
    private TextView tv_title;

    SQLiteHandler db;

    public final static String KEY_FEEDID = "feed_id";
    public final static String KEY_FEEDNAME = "feed_name";
    public final static String KEY_FEEDTYPE = "feed_type";
    public final static String KEY_PROD = "prod_date";

    String[] lists1 = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};

    String pig_id = "";
    String feed_id = "";
    String feed_name = "";

    public UpdatePigFeedFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_view, container, false);
        tv_item = (TextView) theView.findViewById(R.id.tv_item);
        tv_title = (TextView) theView.findViewById(R.id.tv_title);
        frag_viewPager = (ViewPager) theView.findViewById(R.id.frag_viewpager);
        bl = (LinearLayout) theView.findViewById(R.id.bottom_container);

        return theView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
