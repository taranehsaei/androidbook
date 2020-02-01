
package com.hamyareonline.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "item_name";
    DBAdapter db;
    sho_items thisitem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        final int cheeseID = intent.getIntExtra(EXTRA_ID, 0);
        db = new DBAdapter(DetailActivity.this);
        db.open();
        thisitem = db.getItm(cheeseID);
        db.close();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(thisitem.getTitle());
        CustomTxtView ctxt = (CustomTxtView) findViewById(R.id.txt_cnt);
        ctxt.setText(thisitem.getContent());
        ctxt.setTextSize(MainActivity.shp.getInt("SLIDER_SIZE", 19));
        ctxt.setTextSize(MainActivity.shp.getInt("SLIDER_SIZE", 19));
        if(MainActivity.shp.getBoolean("SCREEN_ON", true))
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.d(DBAdapter.TAG,"Safhe Rushan Mimanad!");
        }

        loadBackdrop();
        SetFab_fav();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBackdrop() {
        int ImageResource = getResources().getIdentifier(thisitem.getImg_adrs(),
                "drawable", getPackageName());
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(ImageResource).centerCrop().into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void SetFab_fav (){
        final FloatingActionButton fab_fav = (FloatingActionButton) findViewById(R.id.fab_fav);

        fab_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,getCopyTxt());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, thisitem.getTitle());
                startActivity(Intent.createChooser(shareIntent, "اشتراک گذاری"));
            }
        });
    }

    private String getCopyTxt(){
        return thisitem.getTitle()+ "\n********** \n"  +thisitem.getContent();
    }


}
