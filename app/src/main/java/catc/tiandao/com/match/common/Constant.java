package catc.tiandao.com.match.common;

import java.util.ArrayList;
import java.util.List;
import catc.tiandao.com.matchlibrary.ben.FootballEvent;

public class Constant {


    //登录发生改变
    public static final String LOGIN_SUCCESS = "app.login.success";
    //受权返回
    public static final String APP_NET_SUCCESS = "app.net.success";
    public static final String UP_SCORE = "com.match.common_up_score";
    public static final String UP_MAIN = "com.match.common_up_main";
    public static final String UP_BALL = "com.match.common_submit_select";
    public static final String UP_BALL_1 = "com.match.common_submit_select_1";
    public static final String SELECT_UP = "com.match.common_select_UP";
    public static final String SELECT_ALL0 = "com.match.common_select_all0";
    public static final String SELECT_ALL1 = "com.match.common_select_all1";
    public static final String SELECT_INVERSE0 = "com.match.common_select_Inverse0";
    public static final String SELECT_INVERSE1 = "com.match.common_select_Inverse1";
    public static final String SUBMIT_SELECT = "com.match.common_up_ball";
    public static final String UP_BASEKET_BALL = "com.match.common_up_basket_ball";
    public static final String UP_BASEKET_BALL_1 = "com.match.common_up_basket_ball_1";

    public static final String UMENG_APP_KEY = "5dbe260d0cafb2c06100016e";
    public static final String UMENG_MESSAGE_SECRET = "db583178c20574471e7cbbe92f684910";

    public static int SenType = 0;


    public static boolean isData(String str) {

        if(str == null || str.equals( "" ) || str.equals( "null" ) || str.equals( "暂无数据" )){
            return false;
        }
        return true;
    }

    public static  List<FootballEvent> mList = new ArrayList();
    public static boolean isSelect = false;
}
