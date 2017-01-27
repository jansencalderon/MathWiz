package project.mathwizv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainQuestion extends AppCompatActivity {

    private TextView aButton, bButton, cButton, dButton;

    private TextView quest;

    private List<Question> q;

    private int correct = 0, wrong = 0;


    private ArrayList<Integer> a = new ArrayList<Integer>();

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private int num;

    private int score;

    private int map;

    private int stage;
    private SoundPoolHelper mSoundPoolHelper;
    private int mSoundLessId;
    private ImageView owl;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_question);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "KGMissKindergarten.ttf");


        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();


        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.pop, 1);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.shing, 2);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.wrong, 3);

        map = prefs.getInt("Map", 0);
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


        aButton = (TextView) findViewById(R.id.aButton);
        bButton = (TextView) findViewById(R.id.bButton);
        cButton = (TextView) findViewById(R.id.cButton);
        dButton = (TextView) findViewById(R.id.dButton);

        quest = (TextView) findViewById(R.id.questBox);

        quest.setTypeface(face);
        aButton.setTypeface(face);
        cButton.setTypeface(face);
        bButton.setTypeface(face);
        dButton.setTypeface(face);


        Bundle bundle = getIntent().getExtras();
        stage = bundle.getInt("stage");


        owl = (ImageView) findViewById(R.id.owl);
        Glide.with(this)
                .load(R.drawable.owl_gif)
                .asGif()
                .into(owl);
        ImageView linearLayout = (ImageView) findViewById(R.id.question_linear);
        Glide.with(this).load(myImageList[map - 1]).dontAnimate().into(linearLayout);

        num = 0;
        score = 0;
        wrong = 0;


        q = Question.find(Question.class, "stage = ? and map = ?", "" + stage, "" + map);


        for (int i = 0; i < q.size(); i++) {
            a.add(i);
        }
        Collections.shuffle(a);

        setQuestion(a.get(num));
        back = (Button) findViewById(R.id.backbutton);
        back.setTypeface(face);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                onBackPressed();
            }
        });

    }

    public void clickA(View v) {
        if (correct == 1) {
            score++;
            playSound(2);
        } else {
            wrong++;
            playSound(3);
            if (wrong >= 2) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Failed");

                alertDialogBuilder.setNegativeButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(MainQuestion.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();

                    }
                });

                alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainQuestion.this, MainQuestion.class);
                        intent.putExtra("stage", stage);
                        startActivity(intent);
                        editor.putInt("stage", stage);
                        editor.apply();
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);


                if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                    editor.putInt("map" + map + "score" + stage, score);
                    editor.apply();
                }
            }


        }

        num++;
        if (num < 5) {
            setQuestion(a.get(num));
        } else {

            if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                editor.putInt("map" + map + "score" + stage, score);
                editor.apply();
            }

            if (score == 5) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Congratulation. You can move on to the next level");

                alertDialogBuilder.setPositiveButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);
            }

            if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                editor.putInt("map" + map + "score" + stage, score);
                editor.apply();
            }


            startActivity(new Intent(MainQuestion.this, Stage.class));
            editor.putInt("stage", stage);
            editor.apply();
            finish();
        }

    }

    public void clickB(View v) {
        if (correct == 2) {
            score++;
            playSound(2);
        } else {
            wrong++;
            playSound(3);
            if (wrong >= 2) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Failed");

                alertDialogBuilder.setNegativeButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(MainQuestion.this,"You clicked yes button",Toast.LENGTH_LONG).show();

                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();

                    }
                });

                alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainQuestion.this, MainQuestion.class);
                        intent.putExtra("stage", stage);
                        startActivity(intent);
                        editor.putInt("stage", stage);
                        editor.apply();
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);


                if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                    editor.putInt("map" + map + "score" + stage, score);
                    editor.apply();
                }
            }


        }
        num++;
        if (num < 5) {
            setQuestion(a.get(num));
        } else {
            if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                editor.putInt("map" + map + "score" + stage, score);
                editor.apply();


            }

            if (score == 5) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Congratulation. You can move on to the next level");

                alertDialogBuilder.setPositiveButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);
            }

            startActivity(new Intent(MainQuestion.this, Stage.class));
            editor.putInt("stage", stage);
            editor.apply();
            finish();

        }
    }

    public void clickC(View v) {
        if (correct == 3) {
            score++;
            playSound(2);
        } else {
            wrong++;
            playSound(3);
            if (wrong >= 2) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Failed");

                alertDialogBuilder.setNegativeButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(MainQuestion.this,"You clicked yes button",Toast.LENGTH_LONG).show();

                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();

                    }
                });

                alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainQuestion.this, MainQuestion.class);
                        intent.putExtra("stage", stage);
                        startActivity(intent);
                        editor.putInt("stage", stage);
                        editor.apply();
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);


                if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                    editor.putInt("map" + map + "score" + stage, score);
                    editor.apply();
                }
            }


        }


        num++;
        if (num < 5) {

            setQuestion(a.get(num));
        } else {
            if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                editor.putInt("map" + map + "score" + stage, score);
                editor.apply();
            }

            if (score == 5) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Congratulation. You can move on to the next level");
                alertDialogBuilder.setPositiveButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);

            }

            if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                editor.putInt("map" + map + "score" + stage, score);
                editor.apply();
            }

            startActivity(new Intent(MainQuestion.this, Stage.class));
            editor.putInt("stage", stage);
            editor.apply();
            finish();
        }
    }


    public void clickD(View v) {
        if (correct == 4) {
            score++;
            playSound(2);
        } else {
            wrong++;
            playSound(3);
            if (wrong >= 2) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Failed");

                alertDialogBuilder.setNegativeButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(MainQuestion.this,"You clicked yes button",Toast.LENGTH_LONG).show();

                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();

                    }
                });

                alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainQuestion.this, MainQuestion.class);
                        intent.putExtra("stage", stage);
                        startActivity(intent);
                        editor.putInt("stage", stage);
                        editor.apply();
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);


                if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                    editor.putInt("map" + map + "score" + stage, score);
                    editor.apply();
                }
            }


        }

        num++;
        if (num < 5) {
            setQuestion(a.get(num));
        } else {
            if (prefs.getInt("map" + map + "score" + stage, 0) < score) {
                editor.putInt("map" + map + "score" + stage, score);
                editor.apply();
            }
            if (score == 5) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Congratulation. You can move on to the next level");

                alertDialogBuilder.setPositiveButton("Back to Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        startActivity(new Intent(MainQuestion.this, Stage.class));
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.setCancelable(false);


            }

            startActivity(new Intent(MainQuestion.this, Stage.class));
            editor.putInt("stage", stage);
            editor.apply();
            finish();

        }
    }

    public void setQuestion(int x) {


        quest.setText(Html.fromHtml(q.get(x).getQuest()));

        aButton.setText(Html.fromHtml(q.get(x).getA()));
        bButton.setText(Html.fromHtml(q.get(x).getB()));
        cButton.setText(Html.fromHtml(q.get(x).getC()));
        dButton.setText(Html.fromHtml(q.get(x).getD()));

        correct = q.get(x).getCorrect();

        //  Toast.makeText(this,""+correct, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, Stage.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void playSound(int soundId) {
        if (prefs.getBoolean("sound", true)) {
            mSoundPoolHelper.play(soundId);
        }
    }


}