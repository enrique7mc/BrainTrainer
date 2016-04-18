package com.enrique7mc.braintrainer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout layout;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.container);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");
        overrideFonts(layout);
    }

    private void overrideFonts(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(typeface);
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }
}
