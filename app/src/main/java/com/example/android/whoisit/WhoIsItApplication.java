package com.example.android.whoisit;

import android.app.Application;

import com.example.android.whoisit.models.MyObjectBox;
import com.example.android.whoisit.models.Student;
import com.squareup.leakcanary.LeakCanary;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by lottejespers on 30/12/17.
 */

public class WhoIsItApplication extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        boxStore = MyObjectBox.builder().androidContext(WhoIsItApplication.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
