package ramt57.infotrench.com.callrecorder;

import android.app.SearchManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.BroadcastReciver.ExtendedReciver;
import ramt57.infotrench.com.callrecorder.DeviceAdmin.DeviceAdmin;
import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
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
    static querySearch queylistener;
    static querySearch2 queylistener2;
    static querySearch3 queylistener3;
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
        //setting
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean b=SP.getBoolean("LOCK",false);
        if(b){
            SharedPreferences sharedPreferences=getSharedPreferences("LOCK",MODE_PRIVATE);
            String pin=sharedPreferences.getString("PIN","");
            if(pin.isEmpty()){
                Intent intent=new Intent(this,PinLock.class);
                intent.putExtra("SET",true);
                finish();
                startActivity(intent);
            }
        }
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

        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ContactProvider.getFolderPath(this);
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
                    toolbar.setBackgroundColor(getResources().getColor(R.color.cyan));
                    window.setStatusBarColor(getResources().getColor(R.color.cyan_dark));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.cyan));
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
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
                searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    queylistener.Search_name(newText+"");
                    queylistener2.Search_name2(newText+"");
                try {
                    queylistener3.Search_name3(newText+"");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                if(!newText.isEmpty()){
                    tabLayout.setVisibility(View.GONE);
                }else{
                    tabLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
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

        }else if(id==R.id.pin_lock){
            Intent intent= new Intent(MainActivity.this,PinLock.class);
            intent.putExtra("SET",true);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static  void setQueylistener(querySearch quey){
        queylistener=quey;
    }
    public interface querySearch{
        public void Search_name(String name1);
    }
    public static  void setQueylistener2(querySearch2 quey1){
        queylistener2=quey1;
    }
    public interface querySearch2{
        public void Search_name2(String name1);
    }
    public static  void setQueylistener3(querySearch3 quey3){
        queylistener3=quey3;
    }
    public interface querySearch3{
        public void Search_name3(String name1);
    }
}
