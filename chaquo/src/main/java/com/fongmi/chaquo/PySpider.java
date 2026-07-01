package com.fongmi.chaquo;

import android.util.Base64;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.github.catvod.crawler.Spider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PySpider extends Spider {

    private final Python python;
    private final PyObject app;
    private final String api;

    public PySpider(Python python, PyObject app) {
        this.python = python;
        this.app = app;
        this.api = "";
    }

    public PySpider(Python python, PyObject app, String api) {
        this.python = python;
        this.app = app;
        this.api = api != null ? api : "";
    }

    private Object fetch(String method, Object... args) throws Exception {
        try {
            PyObject result = app.callAttr(method, args);
            return result.toJava(Object.class);
        } catch (Throwable e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Object[] proxyLocal(Map<String, String> params) throws Exception {
        Object result = fetch("proxyLocal", params);
        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            return list.toArray();
        }
        if (result instanceof String) {
            String str = (String) result;
            return new Object[]{"text/plain", new ByteArrayInputStream(str.getBytes())};
        }
        return null;
    }

    @Override
    public void destroy() {
        try {
            app.callAttr("destroy");
        } catch (Exception ignored) {
        }
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        Object result = fetch("homeContent", filter);
        return result instanceof String ? (String) result : "";
    }

    @Override
    public String homeVideoContent() throws Exception {
        Object result = fetch("homeVideoContent");
        return result instanceof String ? (String) result : "";
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        Object result = fetch("categoryContent", tid, pg, filter, extend);
        return result instanceof String ? (String) result : "";
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        JSONArray array = new JSONArray();
        for (String id : ids) {
            array.put(id);
        }
        Object result = fetch("detailContent", ids);
        return result instanceof String ? (String) result : "";
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        Object result = fetch("searchContent", key, quick);
        return result instanceof String ? (String) result : "";
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        Object result = fetch("playerContent", flag, id, vipFlags);
        return result instanceof String ? (String) result : "";
    }

    @Override
    public boolean isVideoFormat(String url) throws Exception {
        Object result = fetch("isVideoFormat", url);
        return result instanceof Boolean ? (Boolean) result : false;
    }

    @Override
    public boolean manualVideoCheck() throws Exception {
        Object result = fetch("manualVideoCheck");
        return result instanceof Boolean ? (Boolean) result : false;
    }

    private InputStream toStream(String content, boolean base64) {
        if (content == null) content = "";
        if (base64) {
            byte[] decoded = Base64.decode(content, Base64.DEFAULT);
            return new ByteArrayInputStream(decoded);
        }
        return new ByteArrayInputStream(content.getBytes());
    }
}
