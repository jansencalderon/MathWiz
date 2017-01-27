package project.mathwizv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Tip extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private TextView tip,tipMap,tipTitle;
    private Typeface font_kindergarten;
    private Button ok;
    private SoundPoolHelper mSoundPoolHelper;
    private int mSoundLessId;
    private Animation slide_in;
    private LinearLayout come;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();
        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.pop, 1);

        slide_in= AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        font_kindergarten = Typeface.createFromAsset(getAssets(),
                "KGMissKindergarten.ttf");
        int[] rainbow = this.getResources().getIntArray(R.array.map_colors);

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

        tip = (TextView)findViewById(R.id.tip);
        tipMap = (TextView)findViewById(R.id.tipMap);
        tipTitle = (TextView)findViewById(R.id.tipTitle);

        come = (LinearLayout) findViewById(R.id.come);
        come.startAnimation(slide_in);

        int x = prefs.getInt("Map",1);


        final ArrayList<String> tips = new ArrayList<String>();



        tips.add("You will work with patterns. Recognizing and extending patterns are important skills for learning concepts related to an arithmetic sequence.");
        tips.add("Is  a sequence of numbers where each term  after the  first  is found by  multiplying the previous one by a fixed, non-zero number  called  the common ratio.");
        tips.add("It works just like the long division you did back in elementary school, except that now you’re dividing with variables.");
        tips.add("It is based on synthetic division, which is the process of  dividing a polynomial by a polynomial and finding the remainder.\n");
        tips.add("The Factor Theorem is used  to often with the remainder theorem in that, when dividing by a number that is a potential root of the polynomial and arriving at a zero remainder in the synthetic division, the number is a root, and x minus the number is a factor.");
        tips.add("All possible arrangements of a collection of things , where the order is important.");
        tips.add("A collection of things, in which the order does not matter.");
        tips.add("Two events are said to be independent if the result of the second event is not affected by the result of the first event.\n");
        tips.add("THINK \n" +
                "and\n" +
                " UNDERSTAND\n");

        ArrayList<String> title = new ArrayList<String>();

        title.add("Arithmetic Sequence");
        title.add("Geometric Sequence");
        title.add("Division of Polynomials");
        title.add("Remainder Theorem");
        title.add("Factor Theorem");
        title.add("Permutation");
        title.add("Combination");
        title.add("Dependent and Independent Events");
        title.add("Logic");



        tip.setText(tips.get(x-1));
        tipMap.setText("Map " + x);
        tipTitle.setText(title.get(x-1));

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tip_linear);
        linearLayout.setBackgroundResource(myImageList[x-1]);

        //set font and color
        tip.setTypeface(font_kindergarten);
        tipMap.setTypeface(font_kindergarten);
        tipTitle.setTypeface(font_kindergarten);




        ok = (Button) findViewById(R.id.ok);
        ok.setTypeface(font_kindergarten);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int title =prefs.getInt("Map",1);
                /*if (title < 9) {
                    Intent intent = new Intent(Tip.this, Stage.class);
                    intent.putExtra("Map", title);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Tip.this, Stage9.class);
                    intent.putExtra("Map", title);
                    startActivity(intent);
                    finish();
                }*/
                Intent intent = new Intent(Tip.this, Stage.class);
                intent.putExtra("Map", title);
                startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public void onBackPressed(){
        playSound(1);
        startActivity(new Intent(Tip.this,MapActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void playSound(int soundId) {
        if(prefs.getBoolean("sound", true)){
            mSoundPoolHelper.play(soundId);
        }
    }
}
