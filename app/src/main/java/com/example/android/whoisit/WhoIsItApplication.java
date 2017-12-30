package com.example.android.whoisit;

import android.app.Application;

import com.example.android.whoisit.models.MyObjectBox;
import com.example.android.whoisit.models.Student;

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

        boxStore = MyObjectBox.builder().androidContext(WhoIsItApplication.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
