package app;

import android.app.Application;

import helper.TypefaceUtil;

/**
 * Created by marmagno on 6/30/2016.
 */
public class AppFont extends Application{

    @Override
    public void onCreate() {

        // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Bebas Neue.ttf");
    }
}
