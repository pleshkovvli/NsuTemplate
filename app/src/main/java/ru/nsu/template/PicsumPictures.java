package ru.nsu.template;

import android.app.Application;

public class PicsumPictures extends Application {
    public static PicsumPictures application;

    public static PicsumPictures getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }
}
