package uplb.cas.ics.phporktraceability;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import helper.SessionManager;
import helper.TestSessionManager;

/**
 * Created by marmagno on 3/9/2016.
 */
public class ChooseModule extends AppCompatActivity
        implements View.OnDragListener {

    private static final String LOGCAT = ChooseModule.class.getSimpleName();

    ViewPager viewPager;
    PagerAdapter adapter;
    LinearLayout ll;
    LinearLayout bl;
    TextView tv_title;
    ImageView iv_left, iv_right;
    SessionManager session;
    String module = "";
    String function = "";
    private Toolbar toolbar;
    private int[] mResources = {
            R.drawable.ic_addpig,
            R.drawable.ic_feeds,
            R.drawable.ic_medications,
            R.drawable.ic_viewlist,
            R.drawable.ic_senddata };
    private int[] mResources2 = {
            R.drawable.ic_feeds,
            R.drawable.ic_medications,
            R.drawable.ic_viewlist,
            R.drawable.ic_senddata };
    private String[] names = {
            "Add Pig",
            "Feed Pig",
            "Medicate Pig",
            "View List of Pigs",
            "Send Data to Server" };
    private String[] names2 = {
            "Feed Pig",
            "Medicate Pig",
            "View List of Pigs",
            "Send Data to Server" };
    private int[] list;

//    TestSessionManager test;
//    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        test = new TestSessionManager(getApplicationContext());
//        HashMap<String, Integer> testuser = test.getCount();
//        count = testuser.get(TestSessionManager.KEY_COUNT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_phpork);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String > user = session.getUserLoc();
        function = user.get(SessionManager.KEY_FUNC);

        bl = (LinearLayout) findViewById(R.id.bottom_container);

        bl.setOnDragListener(this);
        //ll.setOnDragListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if(function.equals("weaning")) {
            adapter = new ImagePagerAdapter(getApplicationContext(), mResources, names);
            list = mResources;
            viewPager.setAdapter(adapter);
        } else {
            adapter = new ImagePagerAdapter(getApplicationContext(), mResources2, names2);
            list = mResources2;
            viewPager.setAdapter(adapter);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    } else if (currentPage == list.length) {

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

        iv_left = (ImageView)findViewById(R.id.iv_left);
        iv_right = (ImageView)findViewById(R.id.iv_right);

        checkList();
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = viewPager.getCurrentItem();
                viewPager.setCurrentItem(item - 1);
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = viewPager.getCurrentItem();
                viewPager.setCurrentItem(item + 1);

            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        String title = "Choose What to Do";
        tv_title.setText(title);

    }

    public void checkList(){
        int count = viewPager.getCurrentItem();
        if(count + 1 < list.length){
            iv_right.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case R.id.action_help:
            	 show_help();
            	 return true;
            case android.R.id.home:
//                count++;
//                test.updateCount(count);
                Intent i = new Intent(ChooseModule.this, LocationPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("function", function);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
        int action = e.getAction();
        switch(action){
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(LOGCAT, "Drag event started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(LOGCAT, "Drag event entered into "+ v.toString());
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(LOGCAT, "Drag event exited from " + v.toString());
                break;
            case DragEvent.ACTION_DROP:
                TextView tv_drag = (TextView) findViewById(R.id.tv_dragHere);
                View view = (View) e.getLocalState();
                ViewGroup from = (ViewGroup) view.getParent();
                from.removeView(view);
                view.invalidate();
                LinearLayout to = (LinearLayout) v;
                to.addView(view);
                to.removeView(tv_drag);
                view.setVisibility(View.VISIBLE);

                int id = view.getId();
                module = view.findViewById(id).getTag().toString();

                int vid = to.getId();
                if(findViewById(vid) == findViewById(R.id.bottom_container)){

                    //Toast.makeText(ChooseModule.this, "Chosen " + module, Toast.LENGTH_LONG).show();

                    if(module.equals(names[0])) {
                        Intent i = new Intent(ChooseModule.this, ChooseBoarPage.class);
                        startActivity(i);
                        finish();
                    } else if(module.equals(names[1])) {
                        Intent i = new Intent(ChooseModule.this, ChooseSelection.class);
                        i.putExtra("module", module);
                        startActivity(i);
                        finish();
                    } else if(module.equals(names[2])) {
                        Intent i = new Intent(ChooseModule.this, ChooseSelection.class);
                        i.putExtra("module", module);
                        startActivity(i);
                        finish();
                    } else if(module.equals(names[3])){
                        Intent i = new Intent(ChooseModule.this, ChooseViewHouse.class);
                        i.putExtra("function", function);
                        startActivity(i);
                        finish();
                    } else if(module.equals(names[4])) {
                        Intent i = new Intent(ChooseModule.this, ExportData.class);
                        startActivity(i);
                        finish();
                    }
                }

                Log.d(LOGCAT, "Dropped " + module);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(LOGCAT, "Drag ended");
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

//        count++;
//        test.updateCount(count);

        Intent i = new Intent(ChooseModule.this, LocationPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("function", function);
        startActivity(i);
        finish();

    }

    public void show_help(){
        Intent intent = new Intent(this, HelpPage.class);
        intent.putExtra("help_page", 3);
        startActivity(intent);
    }

    class ImagePagerAdapter extends PagerAdapter implements View.OnTouchListener {

        LayoutInflater mInflater;
        int[] images;
        String[] titles;
        private Context mContext;

        public ImagePagerAdapter(Context context, int[] _images, String[] _titles) {
            mContext = context;
            images = _images;
            titles = _titles;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mInflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = mInflater.inflate(R.layout.viewpager_image, container, false);

            TextView imageTitle = (TextView) itemView.findViewById(R.id.tv_subitem1);
            imageTitle.setText(titles[position]);

            Resources res = getResources();
            ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            Bitmap bm = BitmapFactory.decodeResource(res, images[position]);
            imageView.setImageBitmap(bm);
            //imageView.setImageResource(images[position]);

            LinearLayout tl = (LinearLayout) itemView.findViewById(R.id.whole_item);
            tl.setTag(titles[position]);
            tl.setOnTouchListener(this);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
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
}
