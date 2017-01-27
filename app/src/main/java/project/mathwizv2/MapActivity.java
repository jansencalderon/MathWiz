package project.mathwizv2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qozix.tileview.TileView;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.qozix.tileview.widgets.ZoomPanLayout;

import java.util.ArrayList;

public class MapActivity extends TileViewActivity {

    private ArrayList<String> titleArrayList;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Typeface face;
    private FrameLayout frameLayout;
    private Button full_map;
    private Button back;
    private SoundPoolHelper mSoundPoolHelper;
    private int mSoundLessId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();
        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.pop, 1);
        face = Typeface.createFromAsset(getAssets(),
                "KGMissKindergarten.ttf");
        final TileView tileView = getTileView();
        tileView.setSize(1920, 1080);  // the original size of the untiled image
        tileView.addDetailLevel(1.000f, "tiles/island/1000/%d_%d.jpg",256,256);
        tileView.addDetailLevel(0.500f, "tiles/island/500/%d_%d.jpg",256,256);
        tileView.addDetailLevel(0.250f, "tiles/island/250/%d_%d.jpg",256,256);
        tileView.addDetailLevel(0.125f, "tiles/island/125/%d_%d.jpg",128,128);

        tileView.setScaleLimits(0, 4);

        tileView.setMarkerAnchorPoints(-0.5f,-0.5f);

        tileView.setScale(3);
        setContentView( R.layout.activity_tileview );
        frameLayout =  (FrameLayout) findViewById(R.id.frame_container);
        frameLayout.addView(tileView);


       /* insertStage("Arithmetic\nSequence", 648, 369);
        insertStage("Geometric\nSequence", 646, 632);
        insertStage("Division of\nPolynomials", 860, 803);
        insertStage("Remainder\nTheorem", 1052, 759);
        insertStage("Factor\nTheorem", 1259, 790);
        insertStage("Permutation", 1362, 593);
        insertStage("Combination", 1355, 188);
        insertStage("Dependent and\nIndependent Events", 947, 316);*/



        titleArrayList = new ArrayList<String>();

        titleArrayList.add("Arithmetic\nSequence");
        titleArrayList.add("Geometric\nSequence");
        titleArrayList.add("Division of\nPolynomials");
        titleArrayList.add("Remainder\nTheorem");
        titleArrayList.add("Factor\nTheorem");
        titleArrayList.add("Permutation");
        titleArrayList.add("Combination");
        titleArrayList.add("Dependent &\nIndependent Events");
        insertStage("1", 648, 369);
        insertStage("2", 646, 632);
        insertStage("3", 860, 803);
        insertStage("4", 1052, 759);
        insertStage("5", 1259, 790);
        insertStage("6", 1362, 593);
        insertStage("7", 1355, 188);
        insertStage("8", 947, 316);
/*
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(50,50));
        linearLayout.setBackgroundResource(R.drawable.stone);
        getTileView().addMarker(linearLayout, 1,4,null,null);*/

        back = (Button) findViewById(R.id.backbutton);
        back.setTypeface(face);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        full_map = (Button) findViewById(R.id.viewfullmap);
        full_map.setTypeface(face);
        full_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                tileView.setScale(1);
            }
        });

    }
    @Override
    public void onBackPressed() {
        playSound(1);
        startActivity(new Intent(MapActivity.this,MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void insertStage(final String title, double x, double y) {
        Button button = new Button(this);
        button.setTypeface(face);
        int args = Integer.parseInt(title);

        int stage = 0;

        if((args-1) == 3 || (args-1) == 5 || (args-1) == 7){
            stage = 2;
        }else{
            stage = 3;
        }

        if(prefs.getBoolean("Map"+(args-1),false)|| args == 1){// (prefs.getInt("map" + (args-1) + "score" + stage, 0) > 4
               // || args == 1) {
            button.setBackgroundResource(R.drawable.stone);
            //button.setText("Map "+title);
            button.setText(titleArrayList.get(args-1));
            button.setTag(title);
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            if(prefs.getInt("Map", 1) == Integer.parseInt(title)){
                button.setBackgroundResource(R.drawable.stone_active);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(1);
                    goToMap(Integer.parseInt(v.getTag().toString()));
                }
            });
        } else {
            button.setBackgroundResource(R.drawable.stone_locked);
            button.setText("LOCKED");
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            button.setTag("LOCKED");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(1);
                    shakeAnim(v.getTag().toString());
                }
            });
        }
        /*if (prefs.getInt("map" + (args-1) + "score" + stage, 0) > 4
                || args == 1) {
            button.setBackgroundResource(R.drawable.stone);
            //button.setText("Map "+title);
            button.setText(titleArrayList.get(args-1));
            button.setTag(title);
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            if(prefs.getInt("Map", 1) == Integer.parseInt(title)){
                button.setBackgroundResource(R.drawable.stone_active);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(1);
                    goToMap(Integer.parseInt(v.getTag().toString()));
                }
            });
        } else {
            button.setBackgroundResource(R.drawable.stone_locked);
            button.setText("LOCKED");
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            button.setTag("LOCKED");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(1);
                    shakeAnim(v.getTag().toString());
                }
            });
        }*/
        getTileView().addMarker(button, x, y, null, null);

        if(prefs.getInt("Map", 1) == Integer.parseInt(title)){
            frameTo(x,y);
        }

    }

    private void deleteButton(String tag) {
        Button button = new Button(this);
        button.findViewWithTag(tag);
        button.setVisibility(View.GONE);
    }

    public void shakeAnim(String tag) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.tilt);
        // View element to be shaken
        // Perform animation
        Button button = new Button(this);
        button.findViewWithTag(tag);
        button.startAnimation(shake);
    }
    private void playSound(int soundId) {
        if(prefs.getBoolean("sound", true)){
            mSoundPoolHelper.play(soundId);
        }
    }
    public void goToMap(final int title) {
        editor.putInt("Map", title);
        editor.apply();
        startActivity(new Intent(this, Tip.class));
        finish();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (title < 9) {
                    Intent intent = new Intent(MapActivity.this, Stage.class);
                    intent.putExtra("Map", title);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MapActivity.this, Stage9.class);
                    intent.putExtra("Map", title);
                    startActivity(intent);
                }
                finish();


            }
        }, 5000);*/
    }

}
