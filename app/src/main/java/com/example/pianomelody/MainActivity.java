package com.example.pianomelody;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    SoundPool pool;
    int[] ids = new int[6];

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        pool = new SoundPool.Builder().setMaxStreams(5)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                .build();
        for (int i = 1; i <= 5; i++) {
            int res = getResources().getIdentifier("note" + i, "raw", getPackageName());
            if (res != 0) ids[i] = pool.load(this, res, 1);
        }

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF222222);

        LinearLayout white = new LinearLayout(this);
        white.setPadding(16, 16, 16, 16);
        for (int n = 1; n <= 5; n++) {
            white.addView(key(n, Color.WHITE, 0xFFBBDEFB),
                    new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        }
        root.addView(white, new FrameLayout.LayoutParams(-1, -1));

        setContentView(root);
    }

    View key(final int note, final int color, final int pressed) {
        final View v = new View(this);
        final GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setStroke(3, Color.BLACK);
        d.setCornerRadius(12);
        v.setBackground(d);
        v.setOnTouchListener((view, e) -> {
            int a = e.getActionMasked();
            if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
                d.setColor(pressed);
                if (ids[note] != 0) pool.play(ids[note], 1f, 1f, 1, 0, 1f);
            } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {
                d.setColor(color);
            }
            return true;
        });
        return v;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pool.release();
    }
}
