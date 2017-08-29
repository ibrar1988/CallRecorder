package ramt57.infotrench.com.callrecorder;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.BroadcastReciver.ExtendedReciver;
import ramt57.infotrench.com.callrecorder.DeviceAdmin.DeviceAdmin;
import ramt57.infotrench.com.callrecorder.Transformer.ZoomOutPageTransformer;
import ramt57.infotrench.com.callrecorder.adapter.ScreenSlidePagerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.fragments.AllFragment;
import ramt57.infotrench.com.callrecorder.fragments.Incomming;
import ramt57.infotrench.com.callrecorder.fragments.Outgoing;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private  ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ScreenSlidePagerAdapter adapter;
    private static final int REQUEST_CODE = 0;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        initAdmin();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager=findViewById(R.id.viewpager);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        adapter=new ScreenSlidePagerAdapter(getSupportFragmentManager());
        showlistfile();
        viewPager.setAdapter(adapter);
        tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_record_voice_over_black_24dp));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_002_incoming_phone_call_symbol));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_001_outgoing_call));
        toolbar.setTitle("Call Recorder");
        //navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    changeColorOfStatusAndActionBar();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showlistfile() {
        Bundle bundles=new Bundle();
        ArrayList<String> recordinglist=new ArrayList<>();
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/CallRecorder";
        File file=new File(path);
        if(!file.exists()){
            //no folder empty data
            file.mkdirs();
        }
        File listfiles[]=file.listFiles();
        for(File list:listfiles){
            recordinglist.add(list.getName());
        }
        bundles.putStringArrayList("RECORDING",recordinglist);
        AllFragment allFragment=new AllFragment();
        allFragment.setArguments(bundles);
        Incomming fr=new Incomming();
        fr.setArguments(bundles);
        Outgoing outgoing=new Outgoing();
        outgoing.setArguments(bundles);
        adapter.addFrag(allFragment,"All");
        adapter.addFrag(fr,"Recieved");
        adapter.addFrag(outgoing,"Outgoing");
        adapter.notifyDataSetChanged();
    }

    private void initAdmin() {
        try {
            // Initiate DevicePolicyManager.
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, DeviceAdmin.class);

            if (!mDPM.isAdminActive(mAdminName)) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click Activate to activate device admin");
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                // mDPM.lockNow();
                // Intent intent = new Intent(MainActivity.this,
                // TrackDeviceService.class);
                // startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExtendedReciver.setListener(new ExtendedReciver.refreshlist() {
            @Override
            public void notify(boolean vaar) {
                if(vaar){
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
        ContactProvider.deletelistener(new ContactProvider.deleterefresh() {
            @Override
            public void deleterefreshList(boolean var) {
                if(var){
                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
    private void changeColorOfStatusAndActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
             switch (viewPager.getCurrentItem()) {
                case 0:
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case 1:
                    toolbar.setBackgroundColor(getResources().getColor(R.color.light_blue));
                    window.setStatusBarColor(getResources().getColor(R.color.light_blue_dark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.light_blue));
                    break;
                case 2:
                    toolbar.setBackgroundColor(getResources().getColor(R.color.smooth_red));
                    window.setStatusBarColor(getResources().getColor(R.color.smooth_red_dark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.smooth_red));
                    break;
                default:
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
       inflater.inflate(R.menu.menu_resourse_file,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.setting){
            Intent intent= new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()==0){
            super.onBackPressed();
        }else{
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.setting) {
            // Handle the setting action
            Intent intent= new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.fav) {
            //open favourite activity
            Intent intent= new Intent(MainActivity.this,Favourite.class);
            startActivity(intent);
        }else if (id == R.id.nav_share) {

        } else if (id == R.id.rate_us) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
