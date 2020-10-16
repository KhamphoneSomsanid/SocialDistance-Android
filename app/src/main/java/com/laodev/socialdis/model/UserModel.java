package com.laodev.socialdis.model;

import android.content.Context;

import com.google.gson.Gson;
import com.laodev.socialdis.util.AppUtil;
import com.laodev.socialdis.util.ContactUtils;
import com.laodev.socialdis.util.HttpUtil;
import com.laodev.socialdis.util.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserModel {

    public String id = "";
    public String phone = "";
    public String name = "";
    public String email = "";
    public String device_id = "";
    public String imgurl = "";
    public String mac_address = "";
    public String regdate = "";
    public String expiredate = "";
    public int is_active = 0;
    public int is_prime = 0;
    public String other = "";

    public String getShowName() {
        if (!name.isEmpty()) {
            return name;
        } else {
            return phone;
        }
    }

    public static void getUserByUserID(String device_id, UserModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("device_id", device_id);
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_user_device, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                try {
                    if (ret == 10000) {
                        UserModel userModel = new Gson().fromJson(obj.getString("result"), UserModel.class);
                        callback.onSuccess(userModel);
                    } else {
                        callback.onFailed("The history list is contained Unknown user.");
                    }
                } catch (JSONException e) {
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

    public static void getUserByMacAddress(String address, UserModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("mac_address", address);
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_user_mac, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                try {
                    if (ret == 10000) {
                        UserModel userModel = new Gson().fromJson(obj.getString("result"), UserModel.class);
                        callback.onSuccess(userModel);
                    } else {
                        callback.onFailed("The history list is contained Unknown user.");
                    }
                } catch (JSONException e) {
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

    public void getTeamUsers(UserModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("sender_id", id);
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_team_users, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                try {
                    List<UserModel> userModels = new ArrayList<>();
                    JSONArray obj_users = obj.getJSONArray("result");
                    for (int i = 0; i < obj_users.length(); i++) {
                        String obj_user = obj_users.getString(i);
                        UserModel userModel = new Gson().fromJson(obj_user, UserModel.class);
                        userModels.add(userModel);
                    }
                    callback.onSuccess(userModels);
                } catch (JSONException e) {
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

    public void addTeamUser(UserModel userModel, UserModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("sender_id", id);
        params.put("receiver_id", userModel.id);
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_add_team, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                callback.onSuccess();
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

    public void removeTeamUser(UserModel userModel, UserModelInterface callback) {
        Map<String, String> params = new HashMap<>();
        params.put("sender_id", id);
        params.put("receiver_id", userModel.id);
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_remove_team, params, new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                callback.onSuccess();
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

    public static void getAllUsers(Context context, UserModelInterface callback) {
        HttpUtil.onHttpNoImageResponse(HttpUtil.url_all_users, new HashMap<>(), new HttpUtil.HttpUtilCallback() {
            @Override
            public void onEventCallBack(JSONObject obj, int ret) {
                List<UserModel> users = new ArrayList<>();
                UserModel currentUser = SharedPreferenceUtil.getCurrentUser();
                try {
                    JSONArray obj_users = obj.getJSONArray("result");
                    for (int i = 0; i < obj_users.length(); i++) {
                        String obj_user = obj_users.getString(i);
                        UserModel user = new Gson().fromJson(obj_user, UserModel.class);
                        if (user.id.equals(currentUser.id)) {
                            continue;
                        }
                        boolean isContact = false;
                        for (PhoneContact contact: AppUtil.phoneContacts) {
                            String nationalPhone = ContactUtils.getNationalPhoneNumber(context, user.phone);
                            String contactPhone = contact.getPhoneNumbers().get(0).replace(" ", "");
                            contactPhone = contactPhone.replace("-", "");
                            if (contactPhone.contains(nationalPhone)) {
                                isContact = true;
                                break;
                            }
                        }
                        if (isContact) {
                            users.add(user);
                        }
                    }
                    Collections.sort(users, (obj1, obj2) -> obj2.getShowName().compareToIgnoreCase(obj1.getShowName()));
                    callback.onSuccess(users);
                } catch (JSONException e) {
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

    public interface UserModelInterface {
        default void onSuccess(UserModel userModel) {}
        default void onSuccess(List<UserModel> userModels) {}
        default void onSuccess() {}
        default void onNoFound() {}
        default void onFailed(String error) {}
    }

    public static boolean isContainTeamUserByUserID(String userid) {
        List<UserModel> teamUsers = SharedPreferenceUtil.getTeamUsers();
        if (teamUsers.size() > 0) {
            for (UserModel userModel: teamUsers) {
                if (userModel.id.equals(userid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isContainTeamUserByUUID(String uuid) {
        List<UserModel> teamUsers = SharedPreferenceUtil.getTeamUsers();
        if (teamUsers.size() > 0) {
            for (UserModel userModel: teamUsers) {
                if (userModel.mac_address.equals(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

}
