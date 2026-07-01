package com.github.tvbox.osc.bean;

import androidx.annotation.NonNull;

//import com.android.cast.dlna.core.ICast;

import java.util.UUID;

public class CastVideo /*implements ICast*/ {

    private final String name;
    private final String url;

    public CastVideo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @NonNull
    public String getId() {
        return UUID.randomUUID().toString();
    }

    @NonNull
    public String getUri() {
        return url;
    }

    @NonNull
    public String getName() {
        return name;
    }
}