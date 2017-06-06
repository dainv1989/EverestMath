package com.dainv.everestmath;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by dainv on 1/12/2016.
 */
public class SummitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBars();
        setContentView(R.layout.activity_summit);

        TextView tvTitle = (TextView)findViewById(R.id.summit_title);
        TextView tvDesc = (TextView)findViewById(R.id.summit_desc);

        Typeface customFontBold = Typeface.createFromAsset(getAssets(), Everest.CUSTOM_FONT_BOLD);
        Typeface customFont = Typeface.createFromAsset(getAssets(), Everest.CUSTOM_FONT_NORMAL);
        tvTitle.setTypeface(customFontBold);
        tvDesc.setTypeface(customFont);
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
