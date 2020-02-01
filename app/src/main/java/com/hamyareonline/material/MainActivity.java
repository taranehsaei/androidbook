
package com.hamyareonline.material;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.Slider;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    public static SharedPreferences shp;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBAdapter(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(ChkDb())
        new FirstLoad().execute();
        else init();
        shp = getPreferences(MODE_PRIVATE);
    }
    @Override
    public void onBackPressed() {
       if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawers();
            return;
        }else {
            new MaterialDialog.Builder(MainActivity.this)
                    .title("خروج از برنامه")
                    .content("آیا میخواهید از برنامه خارج شوید؟")
                    .positiveText("بله").negativeText("خیر")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);

                        }

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            System.exit(0);
                        }
                    })
                    .show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ListFragment a;
        Bundle bundle;
        bundle = new Bundle();
        bundle.putString("FRG","TITLES");
        a = new ListFragment();
        a.setArguments(bundle);

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(a, "");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.dr_setting:
                                Setting();
                                break;
                            case R.id.dr_apps:
                                String url = "bazaar://collection?slug=by_author&aid=479914287247";
                                Intent next = new Intent(Intent.ACTION_VIEW);
                                try {
                                    next.setData(Uri.parse(url));
                                    startActivity(next);
                                } catch (Exception e) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/developer/479914287247/?l=fa"));
                                    startActivity(browserIntent);
                                }
                                break;
                            case R.id.dr_srch:
                                Search();
                                break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        public Adapter(FragmentManager fm) {
            super(fm);
        }
        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public void Search(){
        mDrawerLayout.closeDrawers();
         final MaterialDialog md =new MaterialDialog.Builder(this)
                .title("جستجو").titleGravity(GravityEnum.END)
                .customView(R.layout.alert_search, false)
                .show();
        final Spinner sp_subjs = (Spinner) md.findViewById(R.id.sp_subjs);
        final EditText et_srch = (EditText) md.findViewById(R.id.et_srch);
        rfrshSrchList(md, sp_subjs, "ALL");
        sp_subjs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rfrshSrchList(md, sp_subjs, et_srch.getText().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        et_srch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rfrshSrchList(md,sp_subjs,charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void rfrshSrchList(MaterialDialog md,Spinner spn, String SrchTxt){

        List<sho_items> subjects = new ArrayList<>();
        db.open();
        if (spn.getSelectedItem().toString().equals(""))
        subjects = db.findSh_Flower(SrchTxt);

        db.close();

        RecyclerView recyclerView = (RecyclerView) md.findViewById(R.id.recyclerview);
        ListFragment.SimpleStringRecyclerViewAdapter listAdapter ;
        listAdapter = new ListFragment.SimpleStringRecyclerViewAdapter(MainActivity.this, subjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(listAdapter);
    }
    private boolean ChkDb(){
        DBAdapter db = new DBAdapter(getBaseContext());
        db.open();
        List<sho_items> hkmats = db.getAllItms(DBAdapter.SUB_ALL);
        db.close();
        Log.i(DBAdapter.TAG, "result: f.size():" + hkmats.size());
        return hkmats.size() <= 0;
    }
    class FirstLoad extends AsyncTask<String, String, String> {

        MaterialDialog P_dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            P_dialog = new MaterialDialog.Builder(MainActivity.this)
                    .title("loading db")
                    .content("wait").cancelable(false)
                    .progress(true, 0)
                    .show();
            Log.i(DBAdapter.TAG, "shorue copy");
        }

        protected String doInBackground(String... args) {
            Log.i(DBAdapter.TAG, "dar hal copy");
            try {
                String destPath = "/data/data/" + MainActivity.this.getPackageName()+ "/databases";
                InputStream inputStream = MainActivity.this.getAssets().open("book_DB");
                OutputStream outputStream = new FileOutputStream(destPath + "/book_DB");
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            P_dialog.dismiss();
            init();
            Log.i(DBAdapter.TAG, "finish");
        }
    }
    private void init(){

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ImageView appbar_menu = (ImageView) findViewById(R.id.appbar_menu);
        appbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });

    }
    private void Setting(){

        MaterialDialog md =new MaterialDialog.Builder(this)
                .title("تنظیمات")
                .customView(R.layout.alert_setting, true)
                .positiveText("ذخیره")
                .cancelable(true)
                .show();

        final CheckBox chk_onScreen = (CheckBox) md.findViewById(R.id.chk_onScreen);
        final TextView txt_size = (TextView) md.findViewById(R.id.txt_size);
        final Slider slider_size = (Slider) md.findViewById(R.id.slider_size);

        chk_onScreen.setChecked(shp.getBoolean("SCREEN_ON", true));
        txt_size.setTextSize(shp.getInt("SLIDER_SIZE", 19));
        slider_size.setValue(shp.getInt("SLIDER_SIZE", 19), true);
        
        slider_size.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider slider, boolean b, float v, float v1, int i, int i1) {
                txt_size.setTextSize(i1);
            }
        });

        md.getBuilder().callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                shp.edit().putBoolean("SCREEN_ON", chk_onScreen.isChecked())
                        .putInt("SLIDER_SIZE", slider_size.getValue())
                        .commit();

            }
        });
    }
}