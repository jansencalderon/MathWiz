package project.mathwizv2;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Jansen Calderon on 8/10/2016.
 */
public class AppController extends SugarApp implements Application.ActivityLifecycleCallbacks {

    private MainActivity mainActivity;
    private MainQuestion mainQuestion;
    private MapActivity mapActivity;
    private Tip tip;
    private Stage stage;
    private Stage4 stage4;

    //public class AppController extends SugarApp  {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        //this.startService(new Intent(this, BackgroundSoundService.class));
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof MainActivity) {
            mainActivity =(MainActivity) activity;
        }

        if (activity instanceof Stage) {
            stage = (Stage) activity;
        }
        if (activity instanceof MainQuestion) {
            mainQuestion =(MainQuestion) activity;
        }
        if (activity instanceof MapActivity) {
            mapActivity = (MapActivity)activity;
        }

        if (activity instanceof Tip) {
            tip = (Tip) activity;
        }
        
        if (activity instanceof Stage4) {
            stage4 = (Stage4) activity;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        /*if (activity instanceof MainActivity) {
            mainActivity =(MainActivity) activity;
        }
        if (activity instanceof Stage) {
            stage = (Stage) activity;
        }
        if (activity instanceof MainQuestion) {
            mainQuestion =(MainQuestion) activity;
        }
        if (activity instanceof MapActivity) {
            mapActivity = (MapActivity)activity;
        }

        if (activity instanceof Tip) {
            tip = (Tip) activity;
        }

        if (activity instanceof Stage4) {
            stage4 = (Stage4) activity;
        }*/
    }

    @Override
    public void onActivityPaused(Activity activity) {
        /*if (activity instanceof MainActivity) {
            mainActivity = null;
        }
        if (activity instanceof Stage) {
            stage = null;
        }

        if (activity instanceof MainQuestion) {
            mainQuestion = null;
        }
        if (activity instanceof MapActivity) {
            mapActivity = null;
        }

        if (activity instanceof Tip) {
            tip = null;
        }

        if (activity instanceof Stage4) {
            stage4 = null;
        }
        ///
        if (mainActivity == null
                && stage == null
                && mainQuestion == null
                && mapActivity == null
                && tip == null
                && stage4 == null
                ) {
            this.stopService(new Intent(this, BackgroundSoundService.class));
        }*/
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof MainActivity) {
            mainActivity = null;
        }
        if (activity instanceof Stage) {
            stage = null;
        }

        if (activity instanceof MainQuestion) {
            mainQuestion = null;
        }
        if (activity instanceof MapActivity) {
            mapActivity = null;
        }

        if (activity instanceof Tip) {
            tip = null;
        }

        if (activity instanceof Stage4) {
            stage4 = null;
        }
        ///
        if (mainActivity == null
                && stage == null
                && mainQuestion == null
                && mapActivity == null
                && tip == null
                && stage4 == null
                ) {
                this.stopService(new Intent(this, BackgroundSoundService.class));
        }
    }

}
