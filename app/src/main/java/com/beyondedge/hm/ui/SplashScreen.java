package com.beyondedge.hm.ui;

import android.content.Intent;

import com.beyondedge.hm.R;
import com.beyondedge.hm.config.SingleDownloadActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class SplashScreen extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorBackground); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("");
//        configSplash.setTitleTextColor(R.color.Wheat);
//        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(500);
//        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//        configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        //transit to another activity here
        //or do whatever you want

//        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        startActivity(new Intent(SplashScreen.this, SingleDownloadActivity.class));
        finish();
    }
}
