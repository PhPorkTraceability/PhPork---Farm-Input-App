package uplb.cas.ics.phporktraceability;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import helper.SQLiteHandler;

/**
 * Created by marmagno on 1/11/2016.
 */
public class UpdatePigFeedFrag extends Fragment {

    public final static String KEY_FEEDID = "feed_id";
    public final static String KEY_FEEDNAME = "feed_name";
    public final static String KEY_FEEDTYPE = "feed_type";
    public final static String KEY_PROD = "prod_date";
    private static final String LOGCAT = UpdatePigFeedFrag.class.getSimpleName();
    SQLiteHandler db;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    String pig_id = "";
    String pen = "";
    String house_id = "";
    String feed_id = "";
    String feed_name = "";
    String feed_type = "";
    String quantity = "";
    String title = "Update Last Feed Given";
    DateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat curTime = new SimpleDateFormat("HH:mm:ss");
    Date dateObj = new Date();
    private ViewPager frag_viewPager;
    private PagerAdapter frag_adapter;
    private LinearLayout bl;
    private TextView tv_item;
    private TextView tv_title;
    private EditText et_edit;
    private ImageView iv_left, iv_right;

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
        iv_left = (ImageView)theView.findViewById(R.id.iv_left);
        iv_right = (ImageView)theView.findViewById(R.id.iv_right);
        tv_item = (TextView) theView.findViewById(R.id.tv_item);
        tv_title = (TextView) theView.findViewById(R.id.tv_title);
        frag_viewPager = (ViewPager) theView.findViewById(R.id.frag_viewpager);
        bl = (LinearLayout) theView.findViewById(R.id.bottom_container);
        et_edit = (EditText) theView.findViewById(R.id.et_editText);

        return theView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = getActivity().getIntent();
        pig_id = i.getStringExtra("pig_id");
        house_id = i.getStringExtra("house_id");
        pen = i.getStringExtra("pen");

        db = new SQLiteHandler(getContext());

        HashMap<String, String> b = db.getPigFeed(pig_id);
        String last_feed = "Last Feed Given: ";
        feed_name = b.get(KEY_FEEDNAME);
        feed_type = b.get(KEY_FEEDTYPE);

        last_feed += feed_name + " (" + feed_type + ")";

        tv_title.setText(title);
        tv_item.setText(last_feed);

        et_edit.setVisibility(View.VISIBLE);

        loadFeeds();

        frag_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    // Log.i("View Pager", "page selected " + position);

                    int currentPage = position + 1;
                    if (currentPage == 1) {
                        iv_left.setVisibility(View.INVISIBLE);
                        iv_right.setVisibility(View.VISIBLE);
                    } else if (currentPage == lists.length) {

                        iv_left.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.INVISIBLE);
                    } else {
                        iv_left.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        checkList();

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = frag_viewPager.getCurrentItem();
                frag_viewPager.setCurrentItem(item - 1);
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = frag_viewPager.getCurrentItem();
                frag_viewPager.setCurrentItem(item + 1);

            }
        });

        bl.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent e) {
                int action = e.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(LOGCAT, "Drag event started");
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(LOGCAT, "Drag event entered into " + v.toString());
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(LOGCAT, "Drag event exited from " + v.toString());
                        break;
                    case DragEvent.ACTION_DROP:
                        TextView tv_drag = (TextView) view.findViewById(R.id.tv_dragHere);
                        View view2 = (View) e.getLocalState();
                        ViewGroup from = (ViewGroup) view2.getParent();
                        from.removeView(view2);
                        view2.invalidate();
                        LinearLayout to = (LinearLayout) v;
                        to.addView(view2);
                        to.removeView(tv_drag);
                        view2.setVisibility(View.VISIBLE);

                        int id = view2.getId();
                        feed_id = v.findViewById(id).getTag().toString();

                        Log.d(LOGCAT, "Dropped " + feed_id);

                        int vid = to.getId();
                        if (view.findViewById(vid) == view.findViewById(R.id.bottom_container)) {
                            Toast.makeText(getActivity(), "Chosen " + feed_id,
                                    Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Updating Last Feed...")
                                    .setMessage("Confirm update?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {

                                            if (et_edit.getText().length() > 0) {
                                                String date = curDate.format(dateObj);
                                                String time = curTime.format(dateObj);
                                                String prod_date = "";
                                                quantity = et_edit.getText().toString().trim();
                                                String unit = "";
                                                db.feedPigRecAuto(quantity, unit, date, time, pig_id,
                                                        feed_id, prod_date, "false");
                                                Intent i = new Intent(getActivity(),
                                                        UpdatePigActivity.class);
                                                i.putExtra("pig_id", pig_id);
                                                i.putExtra("pen", pen);
                                                i.putExtra("house_id", house_id);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                getActivity().finish();
                                                getActivity().startActivity(i);

                                                dialog.dismiss();
                                            } else {
                                                AlertDialog.Builder builder =
                                                        new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Fill up data.")
                                                        .setMessage("Please Enter feed quantity before proceeding.")
                                                        .setCancelable(false)
                                                        .setNeutralButton("OK", null);
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }

                                        }

                                    })
                                    .setNegativeButton("No", null);

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(LOGCAT, "Drag ended");
                        break;
                    default:
                        break;
                }
                return true;

            }
        });

    }

    public void checkList(){
        int count = frag_viewPager.getCurrentItem();
        if(count + 1 < lists.length){
            iv_right.setVisibility(View.VISIBLE);
        }

    }

    private String checkIfNull(String _value){
        String result = "";
        if(_value != null && !_value.isEmpty() && !_value.equals("null")) return _value;
        else return result;
    }

    private void loadFeeds(){

        ArrayList<HashMap<String, String>> theList = db.getFeeds();

        lists = new String[theList.size()];
        lists2 = new String[theList.size()];
        lists3 = new String[theList.size()];
        ids = new String[theList.size()];
        for(int i = 0;i < theList.size();i++)
        {
            HashMap<String, String> c = theList.get(i);
            String _id = c.get(KEY_FEEDID);
            lists[i] = "Feed Name: " + c.get(KEY_FEEDNAME);
            lists2[i] = "Feed Type: " + c.get(KEY_FEEDTYPE);
            lists3[i] = "";
            ids[i] = _id;
        }

        frag_adapter = new FragCustomPagerAdapter(getActivity(),
                lists, lists2, lists3, ids);
        frag_viewPager.setAdapter(frag_adapter);
    }
}
