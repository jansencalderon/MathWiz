package project.mathwizv2;

import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Button start, setting;
    private Animation bounce;
    private ImageView imageView;
    private Button close, sounds, music, credits;
    private Typeface face;
    private SoundPoolHelper mSoundPoolHelper;
    private int mSoundLessId;
    private Intent svc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();

        mSoundPoolHelper = new SoundPoolHelper(1, this);
        mSoundLessId = mSoundPoolHelper.load(this, R.raw.pop, 1);
        face = Typeface.createFromAsset(getAssets(),
                "KGMissKindergarten.ttf");

        svc = new Intent(MainActivity.this, BackgroundSoundService.class);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        start = (Button) findViewById(R.id.start);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.startAnimation(bounce);
        start.setTypeface(face);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        if (prefs.getBoolean("music", true)) {
            startService(svc);
        }

        //Toast.makeText(MainActivity.this, Question.count(Question.class)+"", Toast.LENGTH_SHORT).show();
        setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(1);
                final AppCompatDialog setting_view = new AppCompatDialog(MainActivity.this);
                setting_view.setContentView(R.layout.dialog_settings);
                setting_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                setting_view.setCanceledOnTouchOutside(false);
                close = (Button) setting_view.findViewById(R.id.dialog_close);
                sounds = (Button) setting_view.findViewById(R.id.sounds);
                music = (Button) setting_view.findViewById(R.id.music);
                credits = (Button) setting_view.findViewById(R.id.credits);
                credits.setTypeface(face);
                credits.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(1);
                        setting_view.dismiss();
                        final AppCompatDialog credit_view = new AppCompatDialog(MainActivity.this);
                        credit_view.setContentView(R.layout.dialog_credits);
                        credit_view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        credit_view.setCanceledOnTouchOutside(true);
                        credit_view.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                credit_view.dismiss();
                                setting_view.show();
                            }
                        });
                        credit_view.show();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(1);
                        setting_view.dismiss();
                    }
                });

                if (prefs.getBoolean("sound", true)) {
                    sounds.setBackgroundResource(R.drawable.sounds_on);
                } else {
                    sounds.setBackgroundResource(R.drawable.sounds_off);
                }
                if (prefs.getBoolean("music", true)) {
                    music.setBackgroundResource(R.drawable.music_on);
                } else {
                    music.setBackgroundResource(R.drawable.music_off);
                }

                music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(1);
                        if (prefs.getBoolean("music", true)) {
                            editor.putBoolean("music", false);
                            music.setBackgroundResource(R.drawable.music_off);
                            Intent svc = new Intent(MainActivity.this, BackgroundSoundService.class);
                            stopService(svc);
                            editor.apply();
                        } else {
                            editor.putBoolean("music", true);
                            music.setBackgroundResource(R.drawable.music_on);
                            Intent svc = new Intent(MainActivity.this, BackgroundSoundService.class);
                            startService(svc);
                            editor.apply();
                        }
                    }
                });

                sounds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(1);
                        if (prefs.getBoolean("sound", true)) {
                            editor.putBoolean("sound", false);
                            sounds.setBackgroundResource(R.drawable.sounds_off);
                            editor.apply();
                        } else {
                            editor.putBoolean("sound", true);
                            sounds.setBackgroundResource(R.drawable.sounds_on);
                            editor.apply();
                        }
                    }
                });
                setting_view.show();
            }
        });
        if (prefs.getBoolean("firstInstall", true)) {
            new InsertAsync().execute();
        } else {
            start.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            start.startAnimation(bounce);
            imageView.startAnimation(bounce);
        }


    }


    private void playSound(int soundId) {
        if (prefs.getBoolean("sound", true)) {
            mSoundPoolHelper.play(soundId);
        }
    }

    private class InsertAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            start.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }


        @Override
        protected String doInBackground(String... params) {
            initializeData();
            return "loading";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            start.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            start.startAnimation(bounce);
            imageView.startAnimation(bounce);
            editor.putBoolean("firstInstall", false);
            editor.apply();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (prefs.getBoolean("music", true)) {
            stopService(svc);
        }
        finish();
    }

    /*
        @Override
        public void onPause(){
            super.onPause();
            stopService(svc);
            mSoundPoolHelper.release();

        }
    */
    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getBoolean("music", true)) {
            //Toast.makeText(MainActivity.this, prefs.getBoolean("music", true)+"", Toast.LENGTH_SHORT).show();
            startService(svc);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            stopService(svc);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initializeData() {
        Question.deleteAll(Question.class);

        //MAP 1 STAGE 1
        new Question("Find the sum of the first 20 terms of the sequence 4, 6, 8, 10, …", "460", " 560", "660", "760", 1, 1, 1).save();
        new Question("Find the sum of the sequence -8, -5, -2, …, 7", "-2", " -3", "-4", "-5", 2, 1, 1).save();
        new Question("How many terms of the arithmetic sequence -3, 2, 7, … must be added together for the sum of the series to be 116?", "2", " 4", "8", "16", 3, 1, 1).save();
        new Question("Find 1 + 2 + 3 + . . . + 100", "2020", " 3030", "4040", "5050", 4, 1, 1).save();
        new Question("The first term of an arithmetic sequence is 4 and the 10th term is 67. What is the common difference?", "7", " 8", "9", "10", 1, 1, 1).save();
        new Question("What is the 32th term of the arithmetic sequence -12, -7, -2, 3, …?", "153", " 143", "148", "167", 2, 1, 1).save();
        new Question("What is the 55th term of the arithmetic sequence 3, 7, 11, 15, …?", "53", " 151", "199", "203", 3, 1, 1).save();
        new Question("What is the 20th term of the arithmetic sequence 21, 18, 15, 12, …?", "-39", " 1", "20", "-36", 4, 1, 1).save();
        new Question("What is the sum of the 16th terms of the arithmetic sequence 1, 5, 9, 13, …?", "496", " 497", "498", "500", 1, 1, 1).save();
        new Question("What is the sum of the 30th terms of  the arithmetic sequence 50, 45, 40, 35, …?", "675", " -675", "676", "-676", 4, 1, 1).save();

        //MAP 1 STAGE 2
        new Question("Find the missing terms in arithmetic sequence: 3, 12, 21, ___, ___, ___", "30, 39, 48", "31, 40, 49", "22, 31, 40", "23, 32, 41", 1, 1, 2).save();
        new Question("Find the missing terms in arithmetic sequence: 8, 3, -2, ___, ___", "-3, -8", "-7, -12", "-3, -4", "0, 1", 2, 1, 2).save();
        new Question("Find the missing terms in arithmetic sequence: 5, 12, ___, 26, ___", "19, 32", "18, 33", "19, 33", "18, 32", 3, 1, 2).save();
        new Question("Find the missing terms in arithmetic sequence: 2, ___, 20, 29, ___", "10, 30", "11, 37", "10, 38", "11, 38", 4, 1, 2).save();
        new Question("Find the missing terms in arithmetic sequence: ___, 4, 10, 16, ___", "-2, 22", "0, 22", "-1, 22", "-3, 22", 1, 1, 2).save();
        new Question("What is the next number?\n" +
                "What is the 8th number?\n" +
                "9, 4, -1, -6, -11, __?", "-16 and 31", "16 and 31", "15 and 13", ".-15 and -13", 1, 1, 2).save();
        new Question("If t<sub>3</sub> = 10 and t<sub>5</sub> = 16 in an arithmetic sequence, what is t<sub>2</sub> ?", "t<sub>2</sub> = 14", "t<sub>2</sub> = 15", "t<sub>2</sub> = 11", "t<sub>2</sub> = 13", 4, 1, 2).save();
        new Question("What is the general formula of the arithmetic sequence? ", "A<sub>n</sub> = d (n-1) + a", "A<sub>n</sub> = (n+1)/d", "A<sub>n</sub> = a 1 + d (n-1)", "A<sub>n</sub> = a.r<sup>n-1<sup>", 1, 1, 2).save();
        new Question("What is the next number? \n -16,-21,-26,-31,__?", "-36", "36", "-41", "-46 ", 1, 1, 2).save();
        new Question("What is the 8th term?\n" +
                "2,5,8,11. . . .", "26", "23", "29", "35", 2, 1, 2).save();

        //MAP 1 STAGE 3
        new Question("What is the 20th number? 5,0,-5,-10,-15,-20,-25,-30. . . .", "105", "100", "120", "115", 2, 1, 3).save();
        new Question("What is the missing number? 25,__,525,775,1025. . . .", "275", "300", "225", "280", 1, 1, 3).save();
        new Question("What is the 25th number?\n" +
                "What is the next number?\n" +
                "56, 261, 466, 671, ___ . . . .", "4,920 and 876", "4,776 and 876", "4,976 and 876", "5,976 and 896", 3, 1, 3).save();
        new Question("Find the 5th term? An = (-25)", "-9,765,525", "-9,765,625", "-9,775,526", "-9,865,625", 2, 1, 3).save();
        new Question("Find the 8th term? An = 25 (n-1) + 56", "231", "258", "621", "249", 1, 1, 3).save();
        new Question("Find the missing terms in arithmetic sequence: 17, 14, ___, ___, 5", "12, 9", "11, 8", "13, 10", "10, 7", 2, 1, 3).save();
        new Question("Find the missing terms in arithmetic sequence: 4, ___, ___, 19, 24", "5, 15", "6, 15", "5, 14", "6, 14", 3, 1, 3).save();
        new Question("Find the missing terms in arithmetic sequence: ___, ___, ___, 8, 12, 16", "-1, 0, 1", "-2, 0 ,2", "-3, 0, 3", "-4, 0, 4", 4, 1, 3).save();
        new Question("Find the missing terms in arithmetic sequence: -1, ___, ___, ___, 31, 39", "7, 15, 23", "8, 16, 24", "9, 17, 25", "10, 18, 26", 1, 1, 3).save();
        new Question("Find the missing terms in arithmetic sequence:\t13, ___, ___, ___, -11, -17", "6, 0, -4", "7, 1, -5", "8, 2, -6", "9, 3, -7", 2, 1, 3).save();

        //MAP 2 STAGE 1
        new Question("There lived a King who had 7 Queens. Each Queen had 7 Princess, each Princess had 7 dolls, each dolls had 7 dresses and each dresses had 7 diamond buttons. If the cost of each button is $4900, then the total cost of all the diamond buttons.", "$82354200", "$82354500", "$82354400", ".$82354300", 4, 2, 1).save();
        new Question("Once upon a time there lived a king who had 7 Queens. Each Queen had 7 princess, each Princess had 7 rooms, each room had 7 wall paintings. If the cost of each paintings is $400, then find the total amount of all the paintings.", "$960400", "$960500", "$960300", "$960600", 1, 2, 1).save();
        new Question("What would be the next 3 terms in order of the geometric series? 2,8,32,128,…", "512, 2048 and 6144 ", "8192,2048, and 512", "512, 2048, and 8192", "512, 8192 and 2048", 3, 2, 1).save();
        new Question("What would be the next seventh term of the geometric sequence? 3,9,27,81,….", "6561", "243", "2187", "729", 3, 2, 1).save();
        new Question("Find the next term in the sequence: 6, 48, 384, 3072, ….", "24574", "24576", "24572", "24578", 2, 2, 1).save();
        new Question("Find the smallest of three geometric means between 7 and 4375.", "875", "175", "105", "35", 4, 2, 1).save();
        new Question("Find the geometric mean of the numbers 2 and 10.", "2 √5", "4 √5", "5 √5", "4", 1, 2, 1).save();
        new Question("The geometric mean of numbers a and 14 is 7 √2. find a.", "98 √2", "7∕ √2", "7", "9", 3, 2, 1).save();
        new Question("The sequence 3,6,12, 24…. Falls under which of the following categories?", "Arithmetic", "Geometric", "both", "neither", 2, 2, 1).save();
        new Question("Evaluate the expression 5x4<sup>n</sup>  for n=1,2,3 and 4 and identify the sequence formed.", "geometric", "arithmetic", "both", "neither", 1, 2, 1).save();

        //MAP 2 STAGE 2
        new Question("Find the next three terms in the sequence. 5,2.5,1.25,0.625….", "0.3125, 0.15625, and 0.078125", "0.3125, 0.15625, and 2.078125", "0.8125, 0.65625, and 0.578125", "1.3125, 0.15625, and 0.078125", 1, 2, 2).save();
        new Question("Find the 30th term of the geometric sequence: 4a, 36a<sup>2</sup> , 324a<sup>3</sup> , 2916a<sup>4</sup> ,….", "(36a)<sup>29</sup> ", "(36a)<sup>30</sup>", "(4a)(9a)<sup>29</sup>", "(4a)(9a)<sup>30</sup> ", 3, 2, 2).save();
        new Question("Find the three geometric means between 9 and 729.", "-27, -81, -243", "25, 87, 249", "29, 89, 249", "27, 81, 243", 4, 2, 2).save();
        new Question("Which term is a geometric sequence 2, 16, 128, … is 8192?", "5th term", "7th term", "6th term", "8th term", 1, 2, 2).save();
        new Question("Find the next 2 terms of the geometric sequence. 2/30<sup>2</sup> ,1/30 ,1/2", "1/15, 1/225", "15/2, 225/2", "2/15, 2/255", "15, 225", 2, 2, 2).save();
        new Question("Find the next 2 terms of the geometric sequence.", "-60, -120", "-38, -136", "-64, -128", "-34, -36", 3, 2, 2).save();
        new Question("What is the mean proportional between 4 and 64?", "16", "256", "34", "60", 1, 2, 2).save();
        new Question("What is the next number? 2,10,50,250…", "360", "1,250", "500", "2000", 2, 2, 2).save();
        new Question("What is the 9th number? 2,8,32,128…", "140, 572", "131,572", "131,072", "148, 627", 3, 2, 2).save();
        new Question("What is the next number? \nWhat is the 11th number? \n40,120,360,1080,3240,__?", "9,720 and 2,600,040", "8,440 and 5,400,200", "9,720 and 2,361,960", "9,700 and 5,400,200", 3, 2, 2).save();

        new Question("30,60,120, __. What is the next number?", "480", "240", "840", "250", 2, 2, 3).save();
        new Question("If t<sub>1</sub> = 9, t<sub>2</sub> = 81 and t<sub>4</sub> = 6561. What is t<sub>3</sub> ", "630", "540", "840", "729", 4, 2, 3).save();
        new Question("Identify the missing number in the pattern. 9, 27, 81, ___, 729, . . .", "102", "243", "124", "201", 2, 2, 3).save();
        new Question("Identify the missing term in the pattern. 1,5,25, ___, 625, . . .", "150", "125", "75", "525", 2, 2, 3).save();
        new Question("Identify the rules to describe the sequence 81, 9, 1, 1/9, . . .", "multiply by 1/9", "multiply by 11", "multiply by 10", "multiply by 9", 1, 2, 3).save();
        new Question("Identify the common ratio in the geometric sequence. 81, 27, 9, 3, 1…", "3", "2", "1/2", "1/3", 4, 2, 3).save();
        new Question("Write the rules to describe the sequence 5, 15, 45, 135, . . .", "start with 5 and add 4 repeatedly", "start with 5 and multiply by 3 repeatedly", "start with 5 and subtract 3 repeatedly", "start with 5 and multiply by 2 repeatedly", 1, 2, 3).save();
        new Question("Identify the common ratio in the geometric sequence. 2, 4, 8, 16, . . .", "1/2", "2", "1/8", "1", 2, 2, 3).save();
        new Question("Identify the common ratio in the geometric sequence. 420, 42, 4.2, 0.42, . . .", "1/20", "10", "1/10", "1/11", 3, 2, 3).save();
        new Question("Write a rule to describe the sequence ½,  ¼, 1/8, 1/16, …", "start with ½ and multiply by ½ repeatedly", "start with ½ and multiply by 2 repeatedly", "start with ½ and add by ½ repeatedly", "start with ½ and subtract by ½ repeatedly", 1, 2, 3).save();


        new Question("Divide: (6s<sup>4</sup>-8s<sup>3</sup> -6s<sup>2</sup>)÷(2s<sup>2</sup>)", "3s<sup>2</sup>+s-3", "3s<sup>2</sup>-4s<sup>2</sup>+3", "3s<sup>2</sup>-s-3", "3s<sup>2</sup>-4s-3", 4, 3, 1).save();
        new Question("Divide: (12z<sup>5</sup>-16z<sup>4</sup>-12z<sup>3</sup>)÷(4z<sup>2</sup>)", "3z<sup>3</sup>+4z<sup>2</sup>-3z", "3z<sup>3</sup>-z<sup>2</sup>-3z", "3z<sup>3</sup>+4z<sup>2</sup>+3z", "3z<sup>3</sup>-4z<sup>2</sup>-3z", 4, 3, 1).save();
        new Question("Divide: (12c<sup>5</sup>d<sup>4</sup>-9c<sup>4</sup>d<sup>6</sup>+24c<sup>3</sup>d<sup>12</sup>)÷( 3c<sup>3</sup>d<sup>4</sup>)", "4c<sup>2</sup>-3cd<sup>2</sup>-8d<sup>8</sup>", "4c<sup>2</sup>+3cd<sup>2</sup>+8d<sup>8</sup>", "4c<sup>2</sup>-3cd<sup>2</sup>+8d<sup>8</sup>", "4c<sup>2</sup>+3cd<sup>2</sup>-8d<sup>8</sup>", 4, 3, 1).save();
        new Question("Divide : (5a<sup>5</sup>-5a<sup>4</sup>+4a<sup>3</sup>-36)/a", "5a<sup>4<sup> +5a<sup>3</sup>+4a<sup>2</sup>-36/a", "5a<sup>4<sup> -5a<sup>3</sup>+4a<sup>2</sup>-36/a", "5a<sup>4<sup> -5a<sup>3</sup>-4a<sup>2</sup>-36/a", "5a<sup>4<sup> -5a<sup>3</sup>+4a<sup>2</sup>+36/a", 2, 3, 1).save();
        new Question("Divide : (-42x<sup>8</sup>+36x<sup>6</sup>+9x<sup>3</sup> -27)/-3x<sup>3</sup>", "14x<sup>5</sup>+12x<sup>3</sup>-3+9/x<sup>3</sup>", "14x<sup>5</sup>-12x<sup>3</sup>-3+9/x<sup>3</sup>", "14x<sup>5</sup>-12x<sup>3</sup>-3-9/x<sup>3</sup>", "14x<sup>5</sup>-12x<sup>3</sup>-3-9/x<sup>3</sup>", 2, 3, 1).save();
        new Question("Divide : (10x<sup>5</sup>y<sup>4</sup>z+16x<sup>4</sup>y<sup>6</sup>_2wx<sup>3</sup>y<sup>12</sup>)/-2x<sup>3</sup>y<sup>4</sup>", "5x<sup>3</sup>z -8xy<sup>2</sup>+wy<sup>8</sup>", "-8x<sup>2</sup>z +5xy<sup>2</sup>_wy<sup>8</sup>", "-5x<sup>2</sup>z -8xy<sup>2</sup>+wy<sup>8</sup>", "5x<sup>2</sup>z +8xy<sup>2</sup>+wy<sup>8</sup>", 3, 3, 1).save();
        new Question("Divide : (4n<sup>3</sup>+2n<sup>2</sup>+6)/2n+4", "2n<sup>2</sup>_ 3n+6-9/n+2", "2n<sup>2</sup>_ 3n+6+9/n+2", "2n<sup>2</sup>_ 3n+6-15/n+2", "2n<sup>2</sup>_ 3n+6-12/n+2", 1, 3, 1).save();
        new Question("Divide : (r<sup>2a</sup>+2r<sup>a</sup>+9r<sup>a</sup> -7r<sup>a -1</sup>)/r<sup>a</sup>", "r<sup>2a</sup>+2r<sup>a</sup>+9-7r<sup>-1</sup>", "r<sup>2a</sup>+2r<sup>a</sup>+9", "r<sup>2a</sup>-2r<sup>a</sup>+9-7r", "r<sup>2a</sup>-2r<sup>a</sup>+9-7r<sup>-1</sup>", 4, 3, 1).save();
        new Question("Divide : (7a<sup>5</sup>-5a<sup>4</sup>+4a<sup>3</sup>-38)/a", "7a<sup>4</sup>-5a<sup>3</sup>-4a<sup>2</sup>-38/a", "7a<sup>4</sup>-5a<sup>3</sup>+4a<sup>2</sup>-38/a", "7a<sup>4</sup>+5a<sup>3</sup>+4a<sup>2</sup>-38/a", "7a<sup>4</sup>-5a<sup>3</sup>+4a<sup>2</sup>+38/a", 2, 3, 1).save();
        new Question("Divide : (8z<sup>4</sup>-10z<sup>3</sup> -2s<sup>2</sup>)÷(2z<sup>2</sup>)", "4z<sup>2</sup>+z-1", "4z<sup>2</sup>-z-1", "4z<sup>2</sup>+5z-1", "4z<sup>2</sup>-5z-1", 4, 3, 1).save();


        new Question("Divide using synthetic division. Express your answer as: ( dividend/divisor)=quotient+(remainder/divisor). \n(x<sup>4</sup>-8x<sup>3</sup>+16x<sup>2</sup>  - 4x + 15) ÷ (x-4)", "(x<sup>3</sup> -12x<sup>2</sup>+16x-4) + 31/x-4", "(x<sup>3</sup> +4x<sup>2</sup>+4) - 1/x-4", "(x<sup>3</sup> -4x<sup>2</sup>+4) + 1/x-4", "(x<sup>3</sup> -4x<sup>2</sup>-4) - 1/x-4", 4, 3, 2).save();
        new Question("Divide using synthetic division. Express your answer as: ( dividend/divisor)=quotient+(remainder/divisor). \n(x<sup>4</sup>-6x<sup>3</sup>+8x<sup>2</sup>  - 3x + 9) ÷ (x-4)", "(x<sup>3</sup> -3x<sup>2</sup>-3)+1/x-4", "(x<sup>3</sup> -2x<sup>2</sup>+8x-3)+3/x-4", "(x<sup>3</sup> -2x<sup>2</sup>-3)-3/x-4", "(x<sup>3</sup> +2x<sup>2</sup>+3)-1/x-4", 3, 3, 2).save();
        new Question("Divide using synthetic division. Express your answer as: ( dividend/divisor)=quotient+(remainder/divisor). \n(x<sup>4</sup>-8x<sup>3</sup>+15x<sup>2</sup>  - 2x + 15) ÷ (x-5)", "(x<sup>3</sup> -3x<sup>2</sup>-2)+1/x-5", "(x<sup>3</sup> +3x<sup>2</sup>+2)-1/x-5", "(x<sup>3</sup> -3x<sup>2</sup>-2)-1/x-5", "(x<sup>3</sup> -13x<sup>2</sup> + 15x-2)+19/x-5", 3, 3, 2).save();
        new Question("Divide using synthetic division. Express your answer as: ( dividend/divisor)=quotient+(remainder/divisor). \n(2x<sup>3</sup>  - 7x + 11) ÷ (x+4)", "(2x<sup>2</sup> +8x + 25)+111/x+4", "(2x<sup>2</sup> -8x + 25)-89/x+4", "(2x<sup>2</sup> -8x + 25)+89/x+4", " (2x<sup>2</sup> -8x - 25)-89/x+4", 2, 3, 2).save();
        new Question("Divide using synthetic division. Express your answer as: ( dividend/divisor)=quotient+(remainder/divisor). \n(x<sup>3</sup>-13x<sup>2</sup>-4x  + 2) ÷(x-4)", "(x<sup>2</sup> -9x - 40)-158/x-4", "(x<sup>2</sup> -17x +40)-162/x-4", "(x<sup>2</sup> -9x - 40)+158/x-4", "(x<sup>2</sup> -9x - 40)-162/x-4", 1, 3, 2).save();
        new Question("Simplify : 8x<sup>5</sup>-4x<sup>4</sup>+3x<sup>3</sup>  - 42 ÷ x", "8x<sup>4</sup>+4x<sup>3</sup>+3x<sup>2</sup>  - 42 / x", "8x<sup>4</sup>-4x<sup>3</sup>+3x<sup>2</sup> - 42 / x", "c. 8x<sup>4</sup>-4x<sup>3</sup>+3x<sup>2</sup>  + 42 / x", "d. 8x<sup>4</sup>-4x<sup>3</sup> - 3x<sup>2</sup>  - 42 / x", 2, 3, 2).save();
        new Question("Divide :   (12r<sup>5</sup>y<sup>4</sup>-8r<sup>4</sup>y<sup>6</sup> +8r<sup>3</sup>y<sup>12</sup>)/4x<sup>3</sup>y<sup>4</sup>", "3r<sup>2</sup> -2ry<sup>2</sup>-2y<sup>8</sup>", "3r<sup>2</sup> +2ry<sup>2</sup>+2y<sup>8</sup>", "3r<sup>2</sup> -2ry<sup>2</sup>+2y<sup>8</sup>", "3r<sup>2</sup> -2ry<sup>2</sup>+2y<sup>8</sup>", 4, 3, 2).save();
        new Question("Divide :   (6y<sup>5</sup>-8y<sup>4</sup>-6y<sup>3</sup>)/2y<sup>2</sup>", "3y<sup>3</sup>+4y<sup>2</sup>+3y", "3y<sup>3</sup>-y<sup>2</sup>-3y", "3y<sup>3</sup>+4y<sup>2</sup>-3y ", "3y<sup>3</sup>-4y<sup>2</sup>-3y", 4, 3, 2).save();
        new Question("Divide :   (x<sup>2</sup>  - 9x - 18) ÷ (x-1)", "x-10-8/x+1", "-x-10-8/x+1", "x +10-8/x+1", "x-10+8/x+1", 1, 3, 2).save();
        new Question("Divide :   (x<sup>2</sup>  - 6x - 16) ÷ (x+2)", "-8x", "x-8", "x+8", "x-8-32/x+2", 2, 3, 2).save();


        new Question("What is the remainder when 3x<sup>2</sup>-5x+6 is divided by x-2?", "8", "28", "-4", "32", 2, 4, 1).save();
        new Question("What is the remainder when 5x<sup>2</sup>+3x-7 is divided by x+9?", "432", "425", "385", "371", 4, 4, 1).save();
        new Question("What is the remainder when 2x<sup>2</sup>-3x+5 is divided by 2x-1?", "2", "3", "4", "5", 3, 4, 3).save();
        new Question("What is the remainder when 3x<sup>2</sup>+6x-4 is divided by 5x+2?", "-5.92", "-1.12", "1.12", "5.92", 1, 4, 1).save();
        new Question("What is the remainder when f(x)=3x<sup>2</sup>+5x-8 divided by x-2?", "13", "14", "15", "16", 2, 4, 1).save();
        new Question("What is the remainder when f(x)=3x<sup>2</sup>+5x-8 divided by x-2?", "13", "14", "15", "16", 2, 4, 1).save();


        new Question("What is the remainder when x<sup>2</sup>+6x-17 is divided by x-1?", "-10", "10", "-11", "11", 1, 4, 2).save();
        new Question("What is the remainder when 4x<sup>2</sup>-x+3 is divided by x+2?", "21", "22", "23", "24", 2, 4, 2).save();
        new Question("What is the remainder when 2x<sup>3</sup>-x<sup>2</sup>-x-2 is divided by x-1?", "1", "0", "-1", "-2", 3, 4, 2).save();
        new Question("What is the remainder when 2x<sup>3</sup>-2x<sup>2</sup>+4x-4 is divided by x-4?", "118", "98", "128", "108", 4, 4, 2).save();
        new Question("What is the remainder when x<sup>4</sup>-5x<sup>3</sup>+x<sup>2</sup>-10x-5 is divided by x+3?", "250", "300", "350", "400", 1, 4, 2).save();
        new Question("What is one of the easiest way to find the remainder?", "Multiplication", "Addition", "Fibonacci", "Synthetic Division", 4, 4, 2).save();
        new Question("What is the remainder when  P(x)=5x<sup>2</sup>-2x+1 , r=-2?", "20", "25", "30", "35", 2, 4, 2).save();


        new Question("What is the remainder when x<sup>4</sup>-x<sup>3</sup>-x<sup>2</sup>-x-1 is divided by x-3?", "41", "42", "43", "44", 1, 4, 3).save();
        new Question("What is the remainder when x<sup>4</sup>+x<sup>3</sup>-3x<sup>2</sup>-3x-3 is divided by x-1?", "-6", "-7", "-8", "-9", 2, 4, 3).save();
        new Question("If f(x)=x<sup>3</sup>-12x<sup>2</sup>-42 divided by x-3 gives the quotient x<sup>2</sup>-9x-27 and the remainder is -123. Therefore, what is f(3)?", "f(x)=x<sup>3</sup>-12x<sup>2</sup>-42", "3", "-123", "x<sup>2</sup>-9x-27", 3, 4, 3).save();
        new Question("What is a long way method in finding the remainder?", "Polynomial Long Addition", "Polynomial Long Subtraction", "Polynomial Long Multiplication", "Polynomial Long Division", 4, 4, 3).save();
        new Question("If 425 ÷25=17, what is the dividend?", "425", "25", "17", "÷", 1, 4, 3).save();
        new Question("If 425 ÷25=17, what is the divisor?", "425", "25", "17", "÷", 2, 4, 3).save();

        new Question("Which of the following is a factor of 2x<sup>3</sup>-x<sup>2</sup>-21x+18?", "x-0", "x-1", "x-2", "x-3", 4, 5, 1).save();
        new Question("Which of the following is a root of 3x<sup>4</sup>+6x<sup>3</sup>-4x<sup>2</sup>-6x+4?", "-2", "-1", "0", "1", 1, 5, 1).save();
        new Question("Which of the following is a root of x<sup>5</sup>-2x<sup>4</sup>-9x<sup>3</sup>+17x<sup>2</sup>-x+6?", "-4", "-3", "3", "4", 2, 5, 1).save();
        new Question("Which of the following is a factor of 6x<sup>3</sup>+5x<sup>2</sup>-2x-1?", "x-1", "2x-1", "3x-1", "4x-1", 2, 5, 1).save();
        new Question("-4 is a root of x<sup>4</sup>+ax<sup>3</sup>-19x<sup>2</sup>-46x+120. What is the value of a?", "a = 1", "a = 2", "a = 3", "a = 4", 4, 5, 1).save();
        new Question("Which of the following is a factor of 2x<sup>3</sup>-x<sup>2</sup>-21x+18?", "x-0", "x-1", "x-2", "x-3", 4, 5, 1).save();


        new Question("2 is a root of kx<sup>4</sup>-11x<sup>3</sup>+kx<sup>2</sup>+13x+2. What is the value of k?", "k = 1", "k = 2", "k = 3", "k = 4", 3, 5, 2).save();
        new Question("Is (x-1) a factor of f(x)=x<sup>3</sup>+2x<sup>2</sup>-5x-6?", "Yes", "No", "Undetermined", "Maybe", 1, 5, 2).save();
        new Question("Which of the following is a factor of x<sup>2</sup>-3x-4?", "x-3", "x-4", "x-5", "x-6", 2, 5, 2).save();
        new Question("Is y-1 a factor of f(y)=2y4+3y2-5y+7?", "Yes", "No", "Undetermined", "Maybe", 2, 5, 2).save();
        new Question("Which of the following is a factor of g(y)=5y<sup>4</sup>+16y<sup>3</sup>-15y<sup>2</sup>+8y+16?", "g+4", "x+4", "y+4", "a+4", 3, 5, 2).save();
        new Question("2 is a root of kx<sup>4</sup>-11x<sup>3</sup>+kx<sup>2</sup>+13x+2. What is the value of k?", "k = 1", "k = 2", "k = 3", "k = 4", 3, 5, 2).save();


        new Question("In how many different ways can the letters of the word “CORPORATION” be arranged so that the vowels always come together?", "47200", "48000", "42000", "50400", 4, 6, 1).save();
        new Question("In how many different ways can the letters of the word “MATHEMATICS” be arranged such that the vowels must always come together?", "9800", "100020", "120960", "140020", 3, 6, 1).save();
        new Question("Evaluate 5! + 10!", "3628920", "4536290", "3628800", "3638200", 1, 6, 1).save();
        new Question("Evaluate 4! + 6!", "654", "640", "280", "744", 4, 6, 1).save();
        new Question("Evaluate 10! – 4!", "3628800", "4629800 ", "2562500", "3628776", 4, 6, 1).save();
        new Question("How many 3 letter words with or without meaning, can be formed out of the letters of the word, “LOGARITHMS”, if repetition of letters is not allowed?", "720", "420", "789", "5040", 1, 6, 1).save();
        new Question("In how many different ways can the letters of the word “LEADING” be arranged such that the vowels should always come together?", "565", "720", "420", "122", 2, 6, 1).save();
        new Question("A coin is tossed 3 times. Find out of the number of possible outcomes.", "9", "8", "2", "1", 2, 6, 1).save();
        new Question("In how many different ways can the letters of the word “DETAIL”, be arranged such that the vowels must occupy only the odd positions?", "80", "64", "120", "36", 4, 6, 1).save();
        new Question("In how many different ways can the letters of the word “JUDGE” be arranged such that the vowels always come together?", "50", "48", "32", "64", 2, 6, 1).save();


        new Question("In how many ways can the letters of the word “LEADER” be arranged?", "123", "120", "360", "720", 3, 6, 2).save();
        new Question("How many words can be formed by using all letters of the word “BIHAR”?", "720", "120", "24", "60", 2, 6, 2).save();
        new Question("How many words can be made out of the letters of the word “ENGINEERING”?", "924000", "123560", "277200", "18200", 3, 6, 2).save();
        new Question("What is the value of <sup>100</sup>P<sub>2</sub> ?", "9801", "5600", "12000", "9900", 4, 6, 2).save();
        new Question("In how many different ways can the letters of the word “RUMOUR” be arranged?", "986", "128", "360", "180", 4, 6, 2).save();
        new Question("Evaluate 2! – (-5!)", "122", "-122", "-132", "480", 1, 6, 2).save();
        new Question("Evaluate 10! – 2(5)!", "7257600", "0", "1", "3628800", 2, 6, 2).save();
        new Question("Evaluate 9! – 5(-2)!", "3991680", "3960800", "2960420", "2960410", 1, 6, 2).save();
        new Question("Evaluate 4! * 3!", "16", "144", "-144", "12", 2, 6, 2).save();
        new Question("Evaluate 7! x 3", "15120", "5040", "30240", "16280\n", 1, 6, 2).save();


        new Question("An event manager has ten patterns of chairs and eight patterns of tables. In how many ways can he make a pair of table and chair?", "100", "80", "110", "64", 2, 6, 3).save();
        new Question("25 buses are running between two places P and Q. In how many ways can a person go from P to Q and return by a different bus?", "400", "600", "576", "625", 2, 6, 3).save();
        new Question("In how many different ways can 5 girls and 5 boys form a circle such that the boys and the girls alternate?", "2880", "1400", "1200", "3212", 1, 6, 3).save();
        new Question("Find out the number of ways in which 6 rings of different types can be worn in 3 fingers?", "120", "720", "125", "729", 4, 6, 3).save();
        new Question("In how many ways can 5 man draw from 5 taps if no tap can be used for more than once?", "30", "720", "60", "120", 4, 6, 3).save();
        new Question("Evaluate 420 – 5!", "320", "415", "400", "300", 4, 6, 3).save();
        new Question("Evaluate 10! – 610", "3628800", "3628190", "3648190", "3598190", 1, 6, 3).save();
        new Question("Evaluate <sub>4</sub>P<sub>3</sub>", "24", "20", "50", "30", 2, 6, 3).save();
        new Question("Evaluate P(6,4)", "360", "180", "90", "70", 1, 6, 3).save();
        new Question("Evaluate <sub>5</sub>P<sub>2</sub>", "240", "504", "390", "480", 2, 6, 3).save();


        new Question("In how many ways can a coach choose three swimmers from among five swimmers?", "10", "12", "13", "15", 1, 7, 1).save();
        new Question("Six friends want to play enough games of chess to be sure every one plays everyone else. How many games will they have to play?", "10", "12", "13", "15", 4, 7, 1).save();
        new Question("How many combinations can the seven colors of the rainbow be arranged into groups of three colors each?", "30", "35", "40", "45", 2, 7, 1).save();
        new Question("10 people exchange greetings at a business meeting. How many greetings are exchanged if everyone greets each other once?", "30", "35", "40", "45", 4, 7, 1).save();
        new Question("How many lottery tickets must be purchased to complete all possible combinations of six numbers, each with a possibility of being from 1 to 49?", "13983814", "139838145", "13983816", "13983817", 3, 7, 1).save();
        new Question("In how many ways can you select a committee of 3 students out of 10 students?", "110", "120", "130", "140", 2, 7, 1).save();


        new Question("How many triangles can you make using 6 non collinear points on a plane?", "20", "21", "22", "23", 1, 7, 2).save();
        new Question("A committee including 3 boys and 4 girls is to be formed from a group of 10 boys and 12 girls. How many different committee can be formed from the group?", "57400", "58400", "59400", "60400", 3, 7, 2).save();
        new Question("How many distinct 5 card hands can be made from a deck of 52 cards with 3 hearts and 2 spades?", "22008", "22108", "22208", "22308", 4, 7, 2).save();
        new Question("How many 5 card hands can be made from a deck of 52 cards?", "2598960", "3598960", "4598960", "5598960", 1, 7, 2).save();
        new Question("A cookie jar contains 5 cookies. How many ways can a child choose all 5 cookies if order doesn’t matter?", "0", "1", "3", "5", 2, 7, 2).save();
        new Question("How many different 3-member committees can be selected from a class of 20 students?", "1110", "1120", "1140", "1160", 3, 7, 2).save();
        new Question("Out of 7 consonants and 4 vowels, how many words of 3 constants and 2 vowels can be formed?", "24400", "21300", "210", "25200", 3, 7, 2).save();


        new Question("What is the probability that three standard dice rolled simultaneously will all land with the same number facing up?", "1/6", "1/36", "1/216", "1/3", 2, 8, 1).save();
        new Question("Suppose you simultaneously roll a standard die and spin a spinner that is divided into 10 equal sectors, numbered 1 to 10. What is the probability of getting a 4 on both the die and the spinner?", "1/4", "1/16", "1/15", "1/60", 4, 8, 1).save();
        new Question("Suppose you simultaneously roll a standard die and spin a spinner with eight equal sectors, numbered 1 to 8. What is the probability of both rolling an even number and spinning an odd number?", "1/12", "1/48", "1/4", "1/6", 3, 8, 1).save();
        new Question("A coin is flipped and a card is drawn from a standard deck of cards. What is the probability of getting both heads and a red face card?", "3/26", "1/112", "3/52", "3/13", 3, 8, 1).save();
        new Question("If Mike does his mathematics homework today, the probability that he will do it tomorrow is 0.8. The probability that he will do his mathematics homework today is 0.7. What are the odds that he will do it both today and tomorrow?", "4:1", "8:7", "3:2", "14:11", 4, 8, 1).save();
        new Question("A bag contains three green marbles and four black marbles. If you randomly pick two marbles from the bag at the same time, what is the probability that both marbles will be black?", "4/7", "2/7", "3/7", "5/7", 2, 8, 1).save();
        new Question("What is the probability of rolling a total of 7 in two rolls of a standard die if you get an even number on the first roll?", "1/12", "1/6", "1/9", "5/18", 2, 8, 1).save();
        new Question("If a satellite launch has a 97% chance of success, what is the probability of three consecutive successful launches?", "91%", "92%", "93%", "94%", 1, 8, 1).save();
        //new Question("Carrie is a kicker on her rugby team. She estimates that her chances of scoring on a penalty kick during a game are 75% when there is no wind, but only 60% on a windy day. If the weather forecast gives a 55% probability of windy weather today, what is the probability of Carrie scoring on a penalty kick in a match this afternoon?", "65%", "66%", "67%", "68%", 3, 8, 1).save();
        new Question("A bag contains three white marbles, five green marbles, and two red marbles. What are the odds in favour of randomly picking both red marbles in the first two tries? Assume that the first marble picked is not put back into the bag.", "1:44", "1:45", "1:46", "1:47", 1, 8, 1).save();


        new Question("You flip two coins. What is the probability that you flip two heads?", "1/2", "1/3", "1/4", "1/5", 3, 8, 2).save();
        new Question("How many different meals can be served with 3 appetizers, 5 entries, and 4 deserts?", "50", "60", "70", "80", 2, 8, 2).save();
        new Question("David and Adrian have a coupon for a pizza with one topping. The choices of toppings are pepperoni, hamburger, sausage, onions, bell peppers, olives, and anchovies. If they choose at random, what is the probability that they both choose hamburger as a topping?", "1/7", "1/49", "2/7", "1/42", 2, 8, 2).save();
        new Question("What is the probability of tossing two number cubes and getting a 3 on each one?", "1/33", "1/34", "1/35", "1/36", 4, 8, 2).save();
        new Question("A box contains a nickel, a penny, and a dime. What is the probability of choosing first a dime and then, without replacing the dime, choosing a penny?", "1/4", "1/5", "1/6", "1/7", 3, 8, 2).save();
        new Question("A fair die is tossed twice. What is the probability of getting a 4 or 5 on the first toss and a 1, 2, or 3 in the second toss?", "1/4", "1/5", "1/6", "1/7", 3, 8, 2).save();
        new Question("A bag contains 5 white marbles, 3 black marbles and 2 green marbles. In each draw, a marble is drawn from the bag and not replaced. In three draws, what is the probability of obtaining white, black and green in that order?", "1/24", "1/25", "1/26", "1/27", 1, 8, 2).save();
        new Question("From a deck of 52 cards, Jerald draws two cards one after the other at random. What is the probability that both the cards are face cards?", "132/2000", "132/2652", "11/2652", "12/2652", 2, 8, 2).save();
        new Question("Two cards are drawn from a set of 10 cards numbered from 1 to 10, without replacing the first card. What is the probability that both the cards have prime numbers on them?", "2/15", "1/5", "4/15", "1/15", 1, 8, 2).save();
        new Question("Two class monitors are to be chosen, one from the boys and one from the girls in a class. What is the probability if there are 30 boys and 45 girls in the class?", "1/1350", "1/45", "30/31", "1/31", 1, 8, 2).save();


        new Question("A number cube is rolled twice. What is the probability of getting a number that is not divisible by 3 on each of the two rolls?", "4/9", "2/3", "3/4", "2/9", 1, 8, 3).save();
        new Question("When two dice are thrown, what is the probability that the numbers shown up are equal?", "2/36", "1/36", "1/6", "5/36", 2, 8, 3).save();
        new Question("A box contains all the letters of the word U N D E R S T A N D. What is the probability of selecting an ‘N’ first and a ‘D’ next, if the first letter is replaced?", "3/50", "1/25", "1/10", "2/5", 2, 8, 3).save();
        new Question("Two cards are drawn one after the other from a standard deck of  cards. What is the probability of drawing a black card and then a red card?", "13/51", "1/169", "1/13", "26/51", 1, 8, 3).save();
        new Question("If a number is selected at random from a set of 2-digit numbers, then what is the probability that the number is divisible by 11?", "2/11", "1/9", "9/10", "1/10", 4, 8, 3).save();
        new Question("A football team coach needs two more players to form a team. There are 15 boys ready to play but of them only 5 are good and the remaining are average players. What is the probability that two players selected are good players?", "3/22", "2/11", "1/11", "1/7", 2, 8, 3).save();
        new Question("There are 10 pens and 15 pencils in a box. If a student selects two of them at random, then what is the probability of selecting a pen and then a pencil?", "7/12", "1/2", "1/4", "11/12", 3, 8, 3).save();
        new Question("Tim draws a card from a pack of cards and also throws a die. What is the probability of Tim drawing a king and getting a number less than 4?", "1/26", "25/26", "5/26", "3/26", 1, 8, 3).save();
        new Question("A box contains named with the letters of the word B U S I N E S S. If two cards are drawn one after the other,  then what is the probability that both are S’s?", "3/5", "1/28", "3/28", "2/5", 3, 8, 3).save();
        new Question("In a group of 10 men, 4 speak English, 3 speaks Spanish and 3 speaks French. If 2 are selected at random, then what is the probability that the first man chosen speaks English and the second one chosen speaks French?", "2/15", "19/30", "23/60", "7/15", 3, 8, 3).save();

/*

        //uncomment this if you want to see all the stage and questions
        for (int a = 1; a < 9; a++) {
            for (int x = 1; x < 4; x++) {

                editor.putInt("map" + a + "score" + x, 5);
                editor.apply();

              */
/*  for (int y = 0; y < 10; y++) {
                    new Question("Map " + a + "- Stage" + x + " - Question: " + y, "wrong", "right", "wrong", "wrong", 2, a, x).save();
                }*//*

            }
        }
*/


    }
}
