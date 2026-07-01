package com.fongmi.chaquo;

import android.app.Application;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class Loader {

    private final Python python;

    public Loader() {
        if (!Python.isStarted()) {
            try {
                Application app = Application.class.cast(
                        Class.forName("android.app.ActivityThread")
                                .getMethod("currentApplication").invoke(null));
                Python.start(new AndroidPlatform(app));
            } catch (Exception e) {
                throw new RuntimeException("Failed to start Python", e);
            }
        }
        python = Python.getInstance();
    }

    public PySpider spider(String api) {
        PyObject app = python.getModule("app");
        return new PySpider(python, app.callAttr("spider", api));
    }
}
