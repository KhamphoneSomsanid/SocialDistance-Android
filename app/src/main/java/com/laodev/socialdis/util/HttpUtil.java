package com.laodev.socialdis.util;

import android.content.Context;

import com.zhy.http.okhttp.BuildConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static final String SERVER_URL = "http://socialdis.daxiao-itdev.com/";

    public static final String BACKEND_URL = SERVER_URL + "Backend/";
    public static final String AVATAR_URL = SERVER_URL + "uploads/";

    public static final String url_check_app = BACKEND_URL + "dis_check_app";
    public static final String url_login = BACKEND_URL + "dis_login";
    public static final String url_register = BACKEND_URL + "dis_register";
    public static final String url_payment = BACKEND_URL + "dis_payment";
    public static final String url_upgrade_membership = BACKEND_URL + "dis_upgrade_membership";
    public static final String url_change_device = BACKEND_URL + "dis_change_device";
    public static final String url_update_profile = BACKEND_URL + "dis_update_profile";
    public static final String url_add_history = BACKEND_URL + "dis_add_history";
    public static final String url_all_history = BACKEND_URL + "dis_all_histories";
    public static final String url_user_device = BACKEND_URL + "dis_user_deviceid";
    public static final String url_user_mac = BACKEND_URL + "dis_user_macaddress";
    public static final String url_team_users = BACKEND_URL + "dis_team_users";
    public static final String url_add_team = BACKEND_URL + "dis_add_team";
    public static final String url_remove_team = BACKEND_URL + "dis_remove_team";
    public static final String url_all_users = BACKEND_URL + "dis_all_users";

    public static void isCheckAppVersion(Context context, HttpUtilCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("package", context.getPackageName());
        params.put("version", BuildConfig.VERSION_NAME);
        params.put("build", String.valueOf(BuildConfig.VERSION_CODE));
        onHttpNoImageResponse(url_check_app, params, callback);
    }

    public static void onHttpNoImageResponse (String url
            , Map<String, String> params
            , HttpUtilCallback apiResponse) {
        OkHttpUtils.post().url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        apiResponse.onEventInternetError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int ret = obj.getInt("ret");
                            apiResponse.onEventCallBack(obj, ret);
                        } catch (JSONException e) {
                            apiResponse.onEventServerError(e);
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface HttpUtilCallback {
        void onEventCallBack(JSONObject obj, int ret);
        void onEventInternetError(Exception e);
        void onEventServerError(Exception e);
    }

}
