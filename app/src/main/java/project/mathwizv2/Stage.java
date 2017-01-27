package project.mathwizv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Stage extends AppCompatActivity {

    private ArrayList<LinearLayout> but = new ArrayList<LinearLayout>();

    private ArrayList<TextView> scr = new ArrayList<TextView>();

    static SharedPreferences prefs;
    static SharedPreferences.Editor editor;

    private TextView map;

    private int mapInt;

    private List<Question> q;
    private Typeface font_kindergarten;
    private LinearLayout button1, button2, button3, button4;
    private TextView stage1_tv, stage2_tv, stage3_tv;
    private int total_score;
    private LinearLayout logic_button;
    private ImageView logic_image;
    private SoundPoolHelper mSoundPoolHelper;
    private int mSoundLessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        map = (TextView) findViewById(R.id.map);

        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();

        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.pop, 1);
        font_kindergarten = Typeface.createFromAsset(getAssets(),
                "KGMissKindergarten.ttf");
        int[] myImageList = new int[]{
                R.drawable.stage1,
                R.drawable.stage2,
                R.drawable.stage3,
                R.drawable.stage4,
                R.drawable.stage5,
                R.drawable.stage6,
                R.drawable.stage7,
                R.drawable.stage8,
                R.drawable.stage9};

        ArrayList<String> title = new ArrayList<String>();

        title.add("Default Title");
        title.add("Arithmetic Sequence");
        title.add("Geometric Sequence");
        title.add("Division of Polynomials");
        title.add("Remainder Theorem");
        title.add("Factor Theorem");
        title.add("Permutation");
        title.add("Combination");
        title.add("Dependent and Independent Events");
        title.add("Logic");

        mapInt = prefs.getInt("Map", 0);
        map.setText(title.get(mapInt));


        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.stage_linear);
        linearLayout.setBackgroundResource(myImageList[mapInt - 1]);
        q = Question.find(Question.class, "map = ?", "" + mapInt);
        int stage = 0;
        for (int x = 0; x < q.size(); x++) {
            if (stage < q.get(x).getStage()) {
                stage = q.get(x).getStage();
            }

        }

        //Toast.makeText(this, ""+stage,Toast.LENGTH_SHORT).show();

        //map.setText("Map" + q.get(q.size()-1).getStage());
        Button back = (Button) findViewById(R.id.backbutton);
        back.setTypeface(font_kindergarten);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button info = (Button) findViewById(R.id.info);
        assert info != null;
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatDialog setting_view = new AppCompatDialog(Stage.this);
                setting_view.setContentView(R.layout.dialog_instructions);
                setting_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                setting_view.setCanceledOnTouchOutside(false);
                Button close = (Button) setting_view.findViewById(R.id.dialog_close);
                assert close != null;
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(1);
                        setting_view.dismiss();
                    }
                });
                setting_view.show();
            }
        });

        logic_button = (LinearLayout) findViewById(R.id.stage4);
        logic_image = (ImageView) findViewById(R.id.logic_image);

        but.add((LinearLayout) findViewById(R.id.stage1));
        but.add((LinearLayout) findViewById(R.id.stage2));
        but.add((LinearLayout) findViewById(R.id.stage3));
        scr.add((TextView) findViewById(R.id.score1));
        scr.add((TextView) findViewById(R.id.score2));
        scr.add((TextView) findViewById(R.id.score3));


        button1 = (LinearLayout) findViewById(R.id.stage1);
        button2 = (LinearLayout) findViewById(R.id.stage2);
        button3 = (LinearLayout) findViewById(R.id.stage3);
        button4 = (LinearLayout) findViewById(R.id.stage4);


        stage1_tv = (TextView) findViewById(R.id.stage1_tv);
        stage2_tv = (TextView) findViewById(R.id.stage2_tv);
        stage3_tv = (TextView) findViewById(R.id.stage3_tv);

        //setfont
        map.setTypeface(font_kindergarten);
        stage1_tv.setTypeface(font_kindergarten);
        stage2_tv.setTypeface(font_kindergarten);
        stage3_tv.setTypeface(font_kindergarten);


        for (int x = 0; x < 3; x++) {

            scr.get(x).setText(prefs.getInt("map" + mapInt + "score" + (x + 1), 0) + "/5");
            scr.get(x).setTypeface(font_kindergarten);

            final int locx = x + 1;
            but.get(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(1);
                    Intent intent = new Intent(Stage.this, MainQuestion.class);
                    intent.putExtra("stage", locx);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                    editor.putInt("stage", locx);
                    editor.apply();
                    finish();
                }
            });


        }

        for (int x = 0; x < 3; x++) {
            if (x >= stage) {
                but.get(x).setVisibility(View.GONE);
                scr.get(x).setVisibility(View.GONE);
            }
            but.get(x).setEnabled(false);
        }


        int y = 0;
        do {
            but.get(y).setBackgroundResource(R.drawable.stage_selector);
            but.get(y).setEnabled(true);
            y++;
        }
        while (prefs.getInt("map" + mapInt + "score" + (y), 0) > 2 && y < 3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        total_score = 0;
        for (int x = 0; x < 3; x++) {
            prefs.getInt("map" + mapInt + "score" + (x + 1), 0);
            //    Toast.makeText(Stage.this,"Stage "+x+" :"+        prefs.getInt("map" + mapInt + "score" + (x + 1),0), Toast.LENGTH_SHORT).show();
            total_score = total_score + prefs.getInt("map" + mapInt + "score" + (x + 1), 0);
        }
        if (total_score >= 12) {
            logic_button.setBackgroundResource(R.drawable.stage_selector);
            logic_image.setImageResource(R.drawable.star);
            logic_button.setEnabled(true);
            logic_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(1);
                    Intent intent = new Intent(Stage.this, Stage4.class);
                    startActivity(intent);
                    finish();
                }
            });

        } else {
            logic_button.setEnabled(false);
            logic_button.setBackgroundResource(R.drawable.stage_locked);
            logic_image.setImageResource(R.drawable.q_mark);
        }
        //  Toast.makeText(Stage.this,"Total Score: "+total_score, Toast.LENGTH_SHORT).show();
    }

    private void playSound(int soundId) {
        if (prefs.getBoolean("sound", true)) {
            mSoundPoolHelper.play(soundId);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playSound(1);
        finish();
        startActivity(new Intent(this, MapActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
