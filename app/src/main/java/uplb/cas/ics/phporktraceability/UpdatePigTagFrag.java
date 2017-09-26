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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import helper.SQLiteHandler;
import helper.SessionManager;
import listeners.OnSwipeTouchListener;

/**
 * Created by marmagno on 12/11/2015.
 */
public class UpdatePigTagFrag extends Fragment {

    public final static String KEY_TAGID = "tag_id";
    public final static String KEY_TAGRFID = "tag_rfid";
    public final static String KEY_LABEL = "label";
    private static final String LOGCAT = UpdatePigTagFrag.class.getSimpleName();
    SQLiteHandler db;
    SessionManager session;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    String user_id = "";
    String pig_id = "";
    String pen_id = "";
    String house_id = "";
    String tag_id = "";
    String prev_tagid = "";
    String tag_rfid = "";
    String label = "";
    String title = "Update Pig Tag";
    private ViewPager frag_viewPager;
    private PagerAdapter frag_adapter;
    private LinearLayout ll;
    private LinearLayout bl;
    private TextView tv_item;
    private TextView tv_title;
    private ImageView iv_left, iv_right;

    public UpdatePigTagFrag() {
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

        return theView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = getActivity().getIntent();
        pig_id = i.getStringExtra("pig_id");
        house_id = i.getStringExtra("house_id");
        pen_id = i.getStringExtra("pen_id");

        db = SQLiteHandler.getInstance();

        session = new SessionManager(getContext());
        HashMap<String, String > user = session.getUserLoc();
        user_id = user.get(SessionManager.KEY_USERID);

        HashMap<String, String> b = db.getThePig(pig_id);
        String tag_disp = "Current Tag: ";
        tag_rfid = checkIfNull(b.get(KEY_TAGRFID));
        label = checkIfNull(b.get(KEY_LABEL));
        tag_disp += label + " (" + tag_rfid + ")";
        prev_tagid = b.get(KEY_TAGID);

        tv_title.setText(title);
        tv_item.setText(tag_disp);

        loadTags();

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
                prevItem();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextItem();

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
                        tag_id = v.findViewById(id).getTag().toString();
                        Log.d(LOGCAT, "Dropped " + tag_id);

                        int vid = to.getId();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Updating Pig Tag...")
                                .setMessage("Confirm update?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        String pig_label = getLabel(pig_id);
                                        db.updateTag(pig_id, prev_tagid, null, user_id, "inactive");
                                        db.updateTag(null, tag_id, pig_id, user_id, "active");
                                        //db.updateTagLabel(tag_id, pig_label);
                                        Intent i = new Intent(getActivity(),
                                                UpdatePigActivity.class);
                                        i.putExtra("pig_id", pig_id);
                                        i.putExtra("pen_id", pen_id);
                                        i.putExtra("house_id", house_id);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        getActivity().finish();
                                        getActivity().startActivity(i);

                                        dialog.cancel();
                                    }

                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getActivity(),
                                                UpdatePigActivity.class);
                                        i.putExtra("pig_id", pig_id);
                                        i.putExtra("pen_id", pen_id);
                                        i.putExtra("house_id", house_id);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        getActivity().finish();
                                        getActivity().startActivity(i);

                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
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

    public void nextItem() {
        int item = frag_viewPager.getCurrentItem();
        frag_viewPager.setCurrentItem(item + 1);
    }

    public void prevItem() {
        int item = frag_viewPager.getCurrentItem();
        frag_viewPager.setCurrentItem(item - 1);
    }

    private String checkIfNull(String _value){
        String result = "";
        if(_value != null && !_value.isEmpty() && !_value.equals("null")) return _value;
        else return result;
    }

    private void loadTags(){

        ArrayList<HashMap<String, String>> theList = db.getInactiveTags();

        lists = new String[theList.size()];
        lists2 = new String[theList.size()];
        lists3 = new String[theList.size()];
        ids = new String[theList.size()];
        for(int i = 0;i < theList.size();i++)
        {
            HashMap<String, String> c = theList.get(i);
            String _id = c.get(KEY_TAGID);
            lists[i] = "Tag id: " + _id;
            lists2[i] = "RFID: " + c.get(KEY_TAGRFID);
            lists3[i] = "Tag Label: " + c.get(KEY_LABEL);
            ids[i] = c.get(KEY_TAGID);
        }

        frag_adapter = new FragCustomPagerAdapter(getActivity(),
                lists, lists2, lists3, ids);
        frag_viewPager.setAdapter(frag_adapter);
    }

    private String getLabel(String _id){
        String result = "";

        int size = _id.length();
        String s = "0";
        size = 6 - size;
        for(int i = 0; i < size;i++){
            s = s + "0";
        }
        s = s + _id;
        String temp1 = s.substring(0,2);
        String temp2 = s.substring(3,7);
        result = temp1 + "-" + temp2;
        return result;
    }
}
