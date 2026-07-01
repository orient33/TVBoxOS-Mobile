package com.github.tvbox.osc.ui.adapter;

//import com.android.cast.dlna.dmc.OnDeviceRegistryListener;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.api.ApiConfig;
import com.github.tvbox.osc.bean.Movie;

//import org.fourthline.cling.model.meta.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pj567
 * @date :2020/12/23
 * @description:
 */
public class CastDevicesAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public CastDevicesAdapter() {
        super(R.layout.item_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        // Stub implementation
        helper.setText(R.id.title, "Unknown Device");
    }
}