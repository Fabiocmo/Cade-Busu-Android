package com.bks.meubusu.cadebusu.activity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

/**
 * Created by raullermen on 10/12/15.
 */


import com.bks.meubusu.cadebusu.R;
import com.bks.meubusu.cadebusu.util.GlobalClass;

import java.util.Locale;

public class Splash extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.splash_screen);

        //AJUSTA TEXTO
        TextView tx = (TextView)findViewById(R.id.TextoCentro);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Proxima-Nova-Alt-Bold.ttf");
        tx.setTypeface(custom_font);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent HomeIntent = new Intent(Splash.this, HomeActivity.class);
                Splash.this.startActivity(HomeIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}