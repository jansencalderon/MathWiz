package project.mathwizv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Stage4 extends AppCompatActivity {

    EditText answer;
    private Typeface font_kindergarten;
    TextView question;
    Button button;
    private Animation shake;
    LinearLayout question_linear;
    RelativeLayout congrats_rl, move_rl;
    private TextView question_tv;
    private Button back;
    private ArrayList<String> arrayListquestions = new ArrayList<>();
    private ArrayList<String> arrayListanswers = new ArrayList<>();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ImageView background;
    private int mapInt;
    private SoundPoolHelper mSoundPoolHelper;
    private int mSoundLessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage4);
        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();


        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.pop, 1);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.shing, 2);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.wrong, 3);

        answer = (EditText) findViewById(R.id.answer);
        question = (TextView) findViewById(R.id.questionTV);
        button = (Button) findViewById(R.id.button);
        background = (ImageView) findViewById(R.id.background_stage);
        arrayListquestions.add("If you are in a race, what place are you in if you take over the person who is in second place?");
        arrayListquestions.add("Which word, if pronounced right, is wrong, but if pronounced wrong is right?");
        arrayListquestions.add("Which word is the odd one out: First Second Third Forth Fifth Sixth Seventh Eighth");
        arrayListquestions.add("Mike is a butcher. He is 5'10\" tall. What does he weigh?");
        arrayListquestions.add("Which is heavier: a ton of gold or a ton of feathers?");
        arrayListquestions.add("You are in a cabin and it is pitch black. You have one match on you. Which do you light first, the newspaper, the lamp, the candle or the fire?");
        arrayListquestions.add("What happens only in the middle of each month, in all of the seasons, except\n" +
                "summer and happens only in the night, never in the day?");
        arrayListquestions.add("What ends in a 'w' but has no end?");

        arrayListanswers.add("Second");
        arrayListanswers.add("Wrong");
        arrayListanswers.add("Forth");
        arrayListanswers.add("Meat");
        arrayListanswers.add("Both");
        arrayListanswers.add("Match");
        arrayListanswers.add("N");
        arrayListanswers.add("Rainbow");

        mapInt = prefs.getInt("Map", 0);
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

        Glide.with(this).load(myImageList[mapInt - 1]).dontAnimate().into(background);

        shake = AnimationUtils.loadAnimation(this, R.anim.tilt);
        font_kindergarten = Typeface.createFromAsset(getAssets(),
                "KGMissKindergarten.ttf");
        question.setTypeface(font_kindergarten);
        question.setText(arrayListquestions.get(mapInt - 1));
        button.setTypeface(font_kindergarten);
        answer.setTypeface(font_kindergarten);

        back = (Button) findViewById(R.id.backbutton);
        back.setTypeface(font_kindergarten);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                onBackPressed();
            }
        });

        Button congrats = (Button) findViewById(R.id.start);
        congrats.setTypeface(font_kindergarten);
        congrats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                startActivity(new Intent(Stage4.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        question_linear = (LinearLayout) findViewById(R.id.question_linears);
        congrats_rl = (RelativeLayout) findViewById(R.id.congrats);

        congrats_rl.setVisibility(View.GONE);

        Button move = (Button) findViewById(R.id.go_move);
        TextView move_tv = (TextView) findViewById(R.id.move_tv);
        move_tv.setTypeface(font_kindergarten);
        assert move != null;
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                startActivity(new Intent(Stage4.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                editor.putInt("Map", mapInt + 1);
                editor.apply();
                finish();
            }
        });

        move_rl = (RelativeLayout) findViewById(R.id.move);
        move_rl.setVisibility(View.GONE);

    }

    public void check(View v) {
        String ans = answer.getText().toString().toUpperCase().trim();
        playSound(1);
        if (ans.toUpperCase().equals(arrayListanswers.get(mapInt - 1).toUpperCase())) {

            //Toast.makeText(Stage4.this, arrayListanswers.get(mapInt-1).toUpperCase(), Toast.LENGTH_SHORT).show();
            // Toast.makeText(Stage4.this, "Pak Ganern", Toast.LENGTH_SHORT).show();
            if (mapInt == 8) {
                question_linear.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                congrats_rl.setVisibility(View.VISIBLE);
                playSound(2);
            } else {
                question_linear.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                move_rl.setVisibility(View.VISIBLE);
                playSound(2);
            }
            editor.putBoolean("Map" + mapInt, true);
            editor.commit();


        } else {
            answer.startAnimation(shake);
            playSound(3);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playSound(1);
        startActivity(new Intent(Stage4.this, Stage.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


    private void playSound(int soundId) {
        if (prefs.getBoolean("sound", true)) {
            mSoundPoolHelper.play(soundId);
        }
    }

}
