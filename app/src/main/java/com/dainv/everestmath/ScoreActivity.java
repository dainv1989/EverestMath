package com.dainv.everestmath;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreActivity extends Activity {
    private TextView tvScoreTitle;
    private TextView tvNewScore;
    private TextView tvBestScore;
    private ImageView btReplay;
    private ImageView btHome;

    private int newScore = 0;
    private int bestScore = 0;
    private static String title = "";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // prevent close dialog activity when touch outside window
        setFinishOnTouchOutside(false);

        hideBars();
        setContentView(R.layout.dialog_score);
        final Context context = getApplicationContext();

        int param[] = (int[])getIntent().getIntArrayExtra("params");
        newScore = param[1];
        if (param[0] == Everest.FLAG_GAMEOVER) {
            title = "Game Over";
        } else if (param[0] == Everest.FLAG_TIMEOUT) {
            title = "Time Out";
        }

        tvScoreTitle = (TextView)findViewById(R.id.score_title);
        tvNewScore = (TextView)findViewById(R.id.score_new);
        tvBestScore = (TextView)findViewById(R.id.score_best);

        TextView tvNew = (TextView)findViewById(R.id.new_title);
        TextView tvBest = (TextView)findViewById(R.id.best_title);

        // set custom font for text view elements
        Typeface customFontBold = Typeface.createFromAsset(getAssets(), Everest.CUSTOM_FONT_BOLD);
        tvScoreTitle.setTypeface(customFontBold);
        tvNewScore.setTypeface(customFontBold);
        tvBestScore.setTypeface(customFontBold);
        tvNew.setTypeface(customFontBold);
        tvBest.setTypeface(customFontBold);

        btReplay = (ImageView)findViewById(R.id.score_replay);
        btHome = (ImageView)findViewById(R.id.score_home);

        /* get best score stored in preference */
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        String strBestScore = pref.getString("best_score", "0");
        bestScore = Integer.parseInt(strBestScore);

        /* update new best score value */
        if (newScore > bestScore) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("best_score", newScore + "");
            editor.commit();
        }

        tvNewScore.setText("" + newScore);
        tvBestScore.setText(strBestScore);
        tvScoreTitle.setText(title);

        btReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * Hide status bar and action bar
     * to display application at full screen
     */
    private void hideBars() {
        /* if Android version is lower than Jellybean (SDK VER = 16) */
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* if Android version is 17 up */
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }
    }
}
