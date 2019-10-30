package catc.tiandao.com.match.common;

public class Constant {


    //登录发生改变
    public static final String LOGIN_SUCCESS = "app.login.success";
    //受权返回
    public static final String APP_NET_SUCCESS = "app.net.success";
    public static final String UP_SCORE = "com.match.common_up_score";
    public static final String UP_MAIN = "com.match.common_up_main";
    public static final String UP_BALL = "com.match.common_up_ball";
    public static final String UP_BASEKET_BALL = "com.match.common_up_basket_ball";

    public static final String UMENG_APP_KEY = "5da86e7f0cafb2fa14000590";
    public static final String UMENG_MESSAGE_SECRET = "9pcq9egfk3eayaovnxcud5ppbr7rg3pm";


    public static boolean isData(String str) {

        if(str == null || str.equals( "" ) || str.equals( "null" ) || str.equals( "暂无数据" )){
            return false;
        }

        return true;
    }
}
