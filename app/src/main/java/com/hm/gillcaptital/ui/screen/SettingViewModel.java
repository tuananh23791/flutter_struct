package com.hm.gillcaptital.ui.screen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class SettingViewModel extends AndroidViewModel {
    private boolean isNOtificationChecked;
    private String notificationText;
    private boolean isCameraChecked;
    private String cameraText;

    public SettingViewModel(@NonNull Application application) {
        super(application);

        initModel();
    }

    public boolean isNOtificationChecked() {
        return isNOtificationChecked;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public boolean isCameraChecked() {
        return isCameraChecked;
    }

    public String getCameraText() {
        return cameraText;
    }

    public void setNOtificationChecked(boolean NOtificationChecked) {
        isNOtificationChecked = NOtificationChecked;
        notificationText = getTextSwitch(isNOtificationChecked);
    }

    public void setCameraChecked(boolean cameraChecked) {
        isCameraChecked = cameraChecked;
        cameraText = getTextSwitch(isCameraChecked);
    }

    private void initModel() {
        isNOtificationChecked = false;
        isCameraChecked = false;

        notificationText = getTextSwitch(isNOtificationChecked);
        cameraText = getTextSwitch(isCameraChecked);
    }

    public String getTextSwitch(boolean isChecked) {
        if (isChecked) {
            return "On";
        } else {
            return "Off";
        }
    }


}
