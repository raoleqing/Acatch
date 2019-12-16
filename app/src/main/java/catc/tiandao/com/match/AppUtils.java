package catc.tiandao.com.match;

import catc.tiandao.com.match.common.Constant;

public class AppUtils {


    private int appStatus = Constant.STATUS_FORCE_KILLED; //默认为被后台回收了

    private static AppUtils appStatusManager;

    public static AppUtils getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppUtils();
        }
        return appStatusManager;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }


}
