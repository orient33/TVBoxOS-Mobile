package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.bean.CastVideo;
import com.lxj.xpopup.core.CenterPopupView;

import org.jetbrains.annotations.NotNull;

/**
 * DLNA Cast dialog - temporarily disabled due to library dependency
 */
public class CastListDialog extends CenterPopupView {

    private final CastVideo castVideo;

    public CastListDialog(@NonNull @NotNull Context context, CastVideo castVideo) {
        super(context);
        this.castVideo = castVideo;
    }

    @Override
    protected int getImplLayoutId() {
        return 0; // No layout needed
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Toast.makeText(getContext(), "DLNA casting is temporarily unavailable", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}