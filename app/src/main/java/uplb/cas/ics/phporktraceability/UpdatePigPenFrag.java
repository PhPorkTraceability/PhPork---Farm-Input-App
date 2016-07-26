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

/**
 * Created by marmagno on 12/11/2015.
 */
public class UpdatePigPenFrag extends Fragment {

    public final static String KEY_PENID = "pen_id";
    public final static String KEY_PENNO = "pen_no";
    public final static String KEY_FUNC = "function";
    private static final String LOGCAT = UpdatePigPenFrag.class.getSimpleName();
    SQLiteHandler db;
    SessionManager session;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    String pig_id = "";
    String pen = "";
    String house_id = "";
    String pen_id = "";
    String pen_no = "";
    String function = "";
    String title = "Update Holding Pen";
    String location = "";
    private ViewPager frag_viewPager;
    private PagerAdapter frag_adapter;
    private LinearLayout ll;
    private LinearLayout bl;
    private TextView tv_item;
    private TextView tv_title;
    private ImageView iv_left, iv_right;

    public UpdatePigPenFrag() {
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
        pen = i.getStringExtra("pen");


        db = SQLiteHandler.getInstance();

        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getUserSession();
        location = user.get(SessionManager.KEY_LOC);

        HashMap<String, String> b = db.getPigGroup(pig_id);
        String pen_disp = "Current Pen: ";
        pen_no = b.get(KEY_PENNO);
        function = b.get(KEY_FUNC);
        pen_disp += pen_no + " (" + function + ")";

        tv_title.setText(title);
        tv_item.setText(pen_disp);

        loadPens();

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
                        pen_id = v.findViewById(id).getTag().toString();
                        Log.d(LOGCAT, "Dropped " + pen_id);

                        int vid = to.getId();
                        if (view.findViewById(vid) == view.findViewById(R.id.bottom_container)) {
                            Toast.makeText(getActivity(), "Chosen " + pen_id,
                                    Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Updating Holding Pen...")
                                    .setMessage("Confirm update?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            db.updatePigPen(pig_id, pen_id);
                                            Intent i = new Intent(getActivity(),
                                                    UpdatePigActivity.class);
                                            i.putExtra("pig_id", pig_id);
                                            i.putExtra("pen", pen);
                                            i.putExtra("house_id", house_id);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            getActivity().finish();
                                            getActivity().startActivity(i);

                                            dialog.cancel();
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

    private void loadPens(){

        ArrayList<HashMap<String, String>> the_list = db.getPensByLocs(location);

        lists = new String[the_list.size()];
        lists2 = new String[the_list.size()];
        lists3 = new String[the_list.size()];
        ids = new String[the_list.size()];
        for(int i = 0;i < the_list.size();i++)
        {
            HashMap<String, String> c = the_list.get(i);

            lists[i] = "Pen: " + c.get(KEY_PENNO);
            lists2[i] = "Function: " + c.get(KEY_FUNC);
            lists3[i] = "";
            ids[i] = c.get(KEY_PENID);
        }

        frag_adapter = new FragCustomPagerAdapter(getActivity(),
                lists, lists2, lists3, ids);
        frag_viewPager.setAdapter(frag_adapter);

    }

}
