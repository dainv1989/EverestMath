package com.dainv.everestmath;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class GameActivity extends Activity {
    private ImageView ivEverestTimer;
    private ImageView ivSound;
    private TextView tvScore;
    private TextView tvQuestion;
    private TextView tvAnswer1;
    private TextView tvAnswer2;

    private static Everest everest;
    private CountDownTimer timer = null;

    private static float padding = 0;

    private static final int TIME_OUT = 2000;
    private static final int TIME_STICK = 200;
    private static boolean isGameStarted = false;
    private static boolean isPlaySound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        padding = res.getDimension(R.dimen.activity_horizontal_margin);
        padding = padding * 2;

        hideBars();
        setContentView(R.layout.activity_game);
        setRandomBackground();

        ivSound = (ImageView)findViewById(R.id.game_speaker);
        ivEverestTimer = (ImageView)findViewById(R.id.game_timer_image);
        tvScore = (TextView)findViewById(R.id.game_score);
        tvQuestion = (TextView)findViewById(R.id.game_question);
        tvAnswer1 = (TextView)findViewById(R.id.game_answer1);
        tvAnswer2 = (TextView)findViewById(R.id.game_answer2);

        // set custom font for text view items
        Typeface customFontBold = Typeface.createFromAsset(getAssets(), Everest.CUSTOM_FONT_BOLD);
        tvQuestion.setTypeface(customFontBold);
        tvScore.setTypeface(customFontBold);
        tvAnswer1.setTypeface(customFontBold);
        tvAnswer2.setTypeface(customFontBold);

        if (everest == null)
            everest = new Everest();
        everest.reset();
        showNextQuestion();

        /* set appropriate icon for sound setting */
        isPlaySound = getPlaySoundSetting();
        if (isPlaySound)
            ivSound.setImageResource(R.drawable.ic_speaker);
        else
            ivSound.setImageResource(R.drawable.ic_speaker_disable);

        /* handle event when user click to game sound icon */
        ivSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaySound) {
                    isPlaySound = false;
                    ivSound.setImageResource(R.drawable.ic_speaker_disable);
                    setPlaySoundSetting(false);
                } else {
                    isPlaySound = true;
                    ivSound.setImageResource(R.drawable.ic_speaker);
                    setPlaySoundSetting(true);
                }
            }
        });

        timer = new CountDownTimer(TIME_OUT, TIME_STICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                // do nothing here
            }

            @Override
            public void onFinish() {
                playSound(R.raw.wrong);
                shakeScreen();
                showScoreDialog(Everest.FLAG_TIMEOUT);
                //everest.reset();
            }
        };

        // user select answer 1
        tvAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGameStarted = true;

                timer.cancel();
                if ((everest.ans1 + everest.curHeight) == everest.nextHeight) {
                    everest.curHeight += everest.ans1;
                    everest.score++;
                    playSound(R.raw.correct);
                    showNextQuestion();
                } else {
                    playSound(R.raw.wrong);
                    shakeScreen();
                    showScoreDialog(Everest.FLAG_GAMEOVER);
                    //everest.reset();
                }
            }
        });

        // user select answer 2
        tvAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGameStarted = true;

                timer.cancel();
                if ((everest.ans2 + everest.curHeight) == everest.nextHeight) {
                    everest.curHeight += everest.ans2;
                    everest.score++;
                    playSound(R.raw.correct);
                    showNextQuestion();
                } else {
                    playSound(R.raw.wrong);
                    shakeScreen();
                    showScoreDialog(Everest.FLAG_GAMEOVER);
                   //everest.reset();
                }
            }
        });
    }

    private void showNextQuestion() {
        Random random = new Random();

        if (!everest.atSummit()) {
            everest.generate();
            tvAnswer1.setText(everest.ans1 + "");
            tvAnswer2.setText(everest.ans2 + "");
            tvScore.setText(everest.score + "");

            String question = "";
            if (random.nextBoolean())
                question = everest.curHeight + "+?";
            else
                question = "?+" + everest.curHeight;

            // get display screen size
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - (int)padding;

            // measure text width
            Rect bound = new Rect();
            int txtWid = 0;
            int txtSize = 110;
            tvQuestion.setLines(1);
            tvQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);

            // recalculate text size to fit with screen size
            do {
                Log.v(getClass().getSimpleName(), "....." + txtWid + " - " + width);
                Paint paint = tvQuestion.getPaint();
                paint.getTextBounds(question, 0, question.length(), bound);
                txtWid = bound.width();
                // decrease text size by 10 unit
                txtSize -= 10;
                tvQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
            } while (width < txtWid);
            Log.v(getClass().getSimpleName(), "....." + txtWid + " - " + width);
            tvQuestion.setLines(2);
            tvQuestion.setText(question + "\n=" + everest.nextHeight);

            /* start count down in two seconds */
            if (isGameStarted) {
                ((AnimationDrawable)ivEverestTimer.getBackground()).stop();
                ((AnimationDrawable)ivEverestTimer.getBackground()).start();
                timer.start();
            }
        } else {
            //everest.reset();
            isGameStarted = false;
            Intent intent = new Intent(getApplicationContext(), SummitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }

    private void showScoreDialog(int flag) {
        if (!isGameStarted)
            return;

        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_NO_HISTORY);

        int param[] = {flag, everest.score};
        intent.putExtra("params", param);
        startActivity(intent);

        /* reset game data */
        isGameStarted = false;
        return;
    }

    private void shakeScreen() {
        View view = findViewById(R.id.game_layout);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
    }

    private void setRandomBackground() {
        Resources res = this.getResources();
        /* set a random background from given color list to game screen */
        if (!isGameStarted) {
            TypedArray ta = res.obtainTypedArray(R.array.game_bkg_colors);
            int length = ta.length();
            int[] colors = new int[length];
            for (int i = 0; i < length; i++) {
                colors[i] = ta.getColor(i, 0);
            }
            ta.recycle();

            /* get random background */
            Random random = new Random();
            View view = findViewById(R.id.game_layout);
            int randomIndex = random.nextInt(length - 1);
            view.setBackgroundColor(colors[randomIndex]);
        }
    }

    private void hideBars() {
        /* hide notification bar */
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

    private boolean getPlaySoundSetting() {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean("game_sound", true);
    }

    private void setPlaySoundSetting(boolean newSetting) {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("game_sound", newSetting);
        editor.commit();
    }

    private void playSound(final int soundId) {
        if (!isPlaySound)
            return;

        Thread thread = new Thread() {
            @Override
            public void run() {
                MediaPlayer player = MediaPlayer.create(getApplicationContext(), soundId);
                player.start();

                /**
                 * release system resource for MediaPlay object
                 * after playing completed
                 */
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        mp = null;
                    }
                });
            }
        };

        thread.start();
    }

    @Override
    public void onBackPressed() {
        // disable back button pressed event
        return;
        //timer.cancel();
        //everest.reset();
        //isGameStarted = false;
        //super.onBackPressed();
    }
}
