package com.laodev.socialdis.model;

import com.google.gson.Gson;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryModel {

    public String userid;
    public String address;
    public long timeStamp = 0;
    public int txPower = 0;
    public int rssi = 0;
    public int isAndroid = 1;
    public int isMember = 0;
    public String status = "Warning";
    public double latitude = 0.0;
    public double longitude = 0.0;

    public void add(HistoryModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", userid);
        params.put("address", address);
        params.put("timeStamp", String.valueOf(timeStamp));
        params.put("txPower", String.valueOf(txPower));
        params.put("rssi", String.valueOf(rssi));
        params.put("isAndroid", String.valueOf(isAndroid));
        params.put("isMember", String.valueOf(isMember));
        params.put("status", status);
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));

        HttpUtil.onHttpNoImageResponse(HttpUtil.url_add_history, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                try {
                    HistoryModel historyModel = new Gson().fromJson(obj.getString("result"), HistoryModel.class);
                    callback.onSuccess(historyModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                callback.onFailed(e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                callback.onFailed(e.getMessage());
            }
        });
    }

    public static void getAllHistories(HistoryModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", SharedPreferenceUtil.getCurrentUser().id);

        HttpUtil.onHttpNoImageResponse(HttpUtil.url_all_history, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                try {
                    List<HistoryModel> historyModels = new ArrayList<>();
                    JSONArray obj_histories = obj.getJSONArray("result");
                    for (int i = 0; i < obj_histories.length(); i++) {
                        String obj_history = obj_histories.getString(i);
                        HistoryModel historyModel = new Gson().fromJson(obj_history, HistoryModel.class);
                        historyModels.add(historyModel);
                    }
                    callback.onSuccess(historyModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                callback.onFailed(e.getMessage());
            }

            @Override
            public void onEventServerError(Exception e) {
                callback.onFailed(e.getMessage());
            }
        });
    }

    public interface HistoryModelInterface {
        default void onSuccess(HistoryModel model) {}
        default void onSuccess(List<HistoryModel> models) {}
        default void onFailed(String error) {}
    }

}
