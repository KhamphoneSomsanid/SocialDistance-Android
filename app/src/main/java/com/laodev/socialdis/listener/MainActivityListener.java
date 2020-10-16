package com.laodev.socialdis.listener;

import com.laodev.socialdis.model.UserModel;

public interface MainActivityListener {
    default void onAddTeamSuccess(UserModel userModel) {}
    default void onClickFabBtn() {}
    default void onBLEScanningStart() {}
    default void onBLEScanningStop() {}
}
