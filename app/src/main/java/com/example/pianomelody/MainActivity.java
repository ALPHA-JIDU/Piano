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
    int[] ids = new int[11];

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        pool = new SoundPool.Builder().setMaxStreams(8)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                .build();
        for (int i = 1; i <= 10; i++) {
            int res = getResources().getIdentifier("note" + i, "raw", getPackageName());
            if (res != 0) ids[i] = pool.load(this, res, 1);
        }

        int h = getResources().getDisplayMetrics().heightPixels;
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF222222);

        LinearLayout white = new LinearLayout(this);
        white.setPadding(16, 16, 16, 16);
        for (int n : new int[]{1, 3, 5, 6, 8, 10}) {
            white.addView(key(n, Color.WHITE, 0xFFBBDEFB, false),
                    new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        }
        root.addView(white, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout black = new LinearLayout(this);
        black.setPadding(16, 16, 16, 16);
        float[] sp = {1.5f, 1f, 3f, 1f, 1.5f};
        int[] bn = {2, 4, 7, 9};
        for (int i = 0; i < 4; i++) {
            black.addView(spacer(sp[i]));
            black.addView(key(bn[i], Color.BLACK, 0xFF616161, true),
                    new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        }
        black.addView(spacer(sp[4]));
        root.addView(black, new FrameLayout.LayoutParams(-1, (int) (h * 0.6)));

        setContentView(root);
    }

    View spacer(float weight) {
        return new View(this) {{
            setLayoutParams(new LinearLayout.LayoutParams(0, -1, weight));
        }};
    }

    View key(final int note, final int color, final int pressed, boolean dark) {
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
