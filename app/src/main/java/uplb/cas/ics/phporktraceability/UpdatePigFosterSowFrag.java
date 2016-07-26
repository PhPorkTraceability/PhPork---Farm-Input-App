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

/**
 * Created by marmagno on 12/11/2015.
 */
public class UpdatePigFosterSowFrag extends Fragment {

    public final static String KEY_PIGID = "pig_id";
    public final static String KEY_FOSTER = "foster_sow";
    public final static String KEY_PARENTID = "parent_id";
    public final static String KEY_LABELID = "label_id";
    private static final String LOGCAT = UpdatePigFosterSowFrag.class.getSimpleName();
    SQLiteHandler db;
    String[] lists = {};
    String[] lists2 = {};
    String[] lists3 = {};
    String[] ids = {};
    String pig_id = "";
    String pen = "";
    String house_id = "";
    String foster_sow = "";
    String cur_foster = "";
    String title = "Update Foster Sow Parent";
    private ViewPager frag_viewPager;
    private PagerAdapter frag_adapter;
    private LinearLayout ll;
    private LinearLayout bl;
    private TextView tv_item;
    private TextView tv_title;
    private ImageView iv_left, iv_right;

    public UpdatePigFosterSowFrag() {
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

        HashMap<String, String> b = db.getThePig(pig_id);
        String foster_disp = "Current Foster Parent: ";
        cur_foster = checkIfNull(b.get(KEY_FOSTER));
        HashMap<String, String> c = db.getParentLabel(cur_foster, "sow");
        foster_disp += checkIfNull(c.get(KEY_LABELID));

        tv_title.setText(title);
        tv_item.setText(foster_disp);

        loadFosters();

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
                        foster_sow = v.findViewById(id).getTag().toString();

                        int vid = to.getId();
                        if (view.findViewById(vid) == view.findViewById(R.id.bottom_container)) {
                            Toast.makeText(getActivity(), "Chosen " + foster_sow,
                                    Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Updating Foster Sow Parent...")
                                    .setMessage("Confirm update?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            db.updateFosterSowParent(pig_id, foster_sow);
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

                        Log.d(LOGCAT, "Dropped " + foster_sow);
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

    private String getLabel(String _id){
        String result = "";

        int size = _id.length();
        if(size != 0) {
            String s = "0";
            size = 6 - size;
            for (int i = 0; i < size; i++) {
                s = s + "0";
            }
            s = s + _id;
            String temp1 = s.substring(0, 2);
            String temp2 = s.substring(3, 7);
            result = temp1 + "-" + temp2;
        }
        return result;
    }

    private void loadFosters(){

        ArrayList<HashMap<String, String>> fosters = db.getSowNot(cur_foster);

        lists = new String[fosters.size()];
        lists2 = new String[fosters.size()];
        lists3 = new String[fosters.size()];
        ids = new String[fosters.size()];
        for(int i = 0;i < fosters.size();i++)
        {
            HashMap<String, String> c = fosters.get(i);
            String _id = c.get(KEY_PARENTID);
            lists[i] = "Label ID: " + c.get(KEY_LABELID);
            lists2[i] = "Sow: " + getLabel(_id);
            lists3[i] = "";
            ids[i] = c.get(KEY_PARENTID);
        }

        frag_adapter = new FragCustomPagerAdapter(getActivity(),
                lists, lists2, lists3, ids);
        frag_viewPager.setAdapter(frag_adapter);

    }

}
