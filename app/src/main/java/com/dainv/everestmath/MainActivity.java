package com.dainv.everestmath;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private ImageView btPlay;
    private ImageView btTopChart;
    private ImageView btRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* hide notification bar */
        hideBars();
        setContentView(R.layout.activity_main);

        btPlay = (ImageView)findViewById(R.id.main_play);
        btTopChart = (ImageView)findViewById(R.id.main_top_chart);
        btRate = (ImageView)findViewById(R.id.main_rate);

        // set custom font for text view item
        TextView tvCopyright = (TextView)findViewById(R.id.main_copyright);
        Typeface customFontBold = Typeface.createFromAsset(getAssets(), Everest.CUSTOM_FONT_BOLD);
        tvCopyright.setTypeface(customFontBold);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        btRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * open Google Play market to rate app
                 * or start web browser if Google Play app is not available
                 */
                Uri uri = Uri.parse("market://details?id=" +
                        getApplicationContext().getPackageName());
                Intent itGotoMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(itGotoMarket);
                }
                catch (ActivityNotFoundException e) {
                    Uri webUri = Uri.parse("http://play.google.com/store/apps/details?id=" +
                            getApplicationContext().getPackageName());
                    Intent itWebMarket = new Intent(Intent.ACTION_VIEW, webUri);
                    startActivity(itWebMarket);
                }
            }
        });

        btTopChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Under construction",
                        Toast.LENGTH_SHORT).show();
                // TODO: display game top chart using Google Game Services
            }
        });
    }

    @Override
    public void onResume() {
        /**
         * from Game screen, user presses back button
         * status (system notification) bar is displayed, so we need to hide it
         * at onResume function
         */
        //hideBars();
        super.onResume();
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
