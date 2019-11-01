package catc.tiandao.com.match.webservice;

import android.content.Context;
import android.util.Xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    public static final String URL = "http://www.leisuvip1.com/";

    //注册获取短信
    public static final String GET_REGISTER_SMS = "LSQB/GetRegisterSms?phone=";
    public static final String GET_PASSWORD_SMS = "LSQB/GetPasswordSms?phone=";

   //注册手机
    public static final String GET_APPLOCAL_TOKEN = "LSQB/GetAppFirstKey";
    //未注册用户获取token
    public static final String APP_LOGINON = "LSQB/AppLoginOn";
    //注册
    public static final String REGISTER = "LSQB/Register";

    //登录name=用户名&psw=加密密码&ip=ip
    public static final String LOGIN_ON = "LSQB/LoginOn";
    //第三方注册
    public static final String REGISTER_BY_OTHERS = "LSQB/RegisterByOthers";
    public static final String QUICKLY_LOGINON = "LSQB/QuicklyLoginOn";

    //忘记密码
    public static final String SAVE_CHANGE_PSW = "LSQB/SaveChangePsw";

    //修改用户名
    public static final String UPDATE_NAME = "LSQB/UpdateName";
    public static final String UPDATE_SEX = "LSQB/UpdateSex";

    public static final String LOGIN_OFF = "LSQB/LoginOff";

    //获取投诉类型
    public static final String GET_SUGGEST_TYPE = "LSQB/GetSuggestType";
    //提交投诉意见
    public static final String SAVE_SUGGEST = "LSQB/SaveSuggest";


    //获取专家列表
    public static final String GET_EXPERT = "LSQB/GetExpert";
    //获取足球赛事列表
    public static final String GET_FOOTBALL_MATH = "LSQB/GetFootballMath";
    //获取篮球赛事列表
    public static final String GET_BASKETBALL_MATH = "LSQB/GetBasketballMath";

    //获取足球比分列表
    public static final String GET_FOOTBALL_SCORE = "LSQB/GetFootballScore";
    //获取篮球比分列表
    public static final String GET_BASKETBALL_SCORE = "LSQB/GetBasketballScore";

    //足球比分详情页-技术统计
    public static final String GET_FOOTBALL_SCORE_DETAIL = "LSQB/GetFootballScoreDetail_JiShuTongJi";
    public static final String GET_FOOTBALL_SHIJIAN = "LSQB/GetFootballScoreDetail_JiShiShiJian";
    //
    public static final String GET_BASKETBALL_SCORE_DETAIL = "LSQB/GetBasketballScoreDetail_JiShuTongJi";
    public static final String GET_BASKETBALL_SHIJIAN = "LSQB/GetBasketballScoreDetail_JiShiShiJian";

    //足球赛事详情页-情报分析
    public static final String GET_FOOTBALL_MATCH_DETAIL = "LSQB/GetFootballMatchDetail_qingBaoFenXi";
    //蓝球赛事详情页-情报分析
    public static final String GET_BASKETBALL_MATCHDETAIL = "LSQB/GetBasketballMatchDetail_qingBao";
    //指数
    public static final String GET_FOOTBALL_MATCHDETAIL_ZHISHU = "LSQB/GetFootballMatchDetail_zhiShu";
    public static final String GET_BASKETBALL_MATCHDETAIL_ZHISHU = "LSQB/GetBasketballMatchDetail_zhiShu";
    //阵容
    public static final String GET_FOOTBALL_MATCHDETAIL_ZHENRONG = "LSQB/GetFootballMatchDetail_zhenRong";
    public static final String GET_BASKETBALL_MATCHDETAIL_ZHENRONG = "LSQB/GetBasketballMatchDetail_zhenRong";
    //数据
    public static final String GET_FOOTBALL_MATCHDETAIL_SHUJU = "LSQB/GetFootballMatchDetail_shuJu";
    public static final String GET_BASKETBALL_MATCHDETAIL_SHUJU = "LSQB/GetBasketballMatchDetail_shuJu";
    //详情
    public static final String GET_FOOTBALL_MATCHDETAIL_HUIYUAN = "LSQB/GetFootballMatchDetail_huiYuan";
    public static final String GET_BASKETBALL_MATCHDETAIL_HUIYUAN = "LSQB/GetBasketballMatchDetail_huiYuan";
    //首页
    public static final String GET_HOME_HEADNEW = "LSQB/GetHomeHeadNew";
    //获取转会与交易新闻
    public static final String GET_CHANGED_NEW = "LSQB/GetChangedNew";

    public static final String GET_HOME_FOOTBALL_DATA = "LSQB/GetHomeFootballData";
    public static final String GET_BASKETBALL_DATA = "LSQB/GetBasketballData";


    public static final String GET_NEWBY_TYPE = "LSQB/GetNewByType";

    public static final String GET_ALL_FOOTBALL_EVENT = "LSQB/GetAllFootballEvent";
    public static final String GET_HOT_FOOTBALL_EVENT = "LSQB/GetHotFootballEvent";
    public static final String GET_HOT_BASKETBALL_EVENT = "LSQB/GetHotBasketballEvent";
    public static final String GET_ALL_BASKETBALL_EVENT = "LSQB/GetAllBasketballEvent";


    public static final String SET_USER2_EVENT = "LSQB/SetUser2Event";
    public static final String SET_USER2_EVENTB = "LSQB/SetUser2EventB";

    public static final String FOOTBALL_MATCH_COLLECT_ANDCANCEL = "LSQB/FootballMatchCollectAndCancel";
    public static final String BASKETBAL_MATCH_COLLECT_ANDCANCEL = "LSQB/BasketballMatchCollectAndCancel";
    public static final String GET_HEADS = "LSQB/GetHomeHeadNew";
    public static final String GET_MY_NEW = "LSQB/GetMyNew";
    public static final String NEW_OPERATION_RUN = "LSQB/NewOperationRun";
    public static final String GETNEW = "LSQB/GetNew";


    public static void getDatasync(final Context mContext, String method , final HttpUtilInterface mInterface){
        try {

            String url;
            if(method.contains( "http" )){
                url = method;
            }else {
                url = URL + method;
            }

            System.out.println("url: " + url);
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(url)//请求接口。如果需要传参拼接到接口后面。
                    .get()
                    .build();//创建Request 对象

            client.newCall(request).enqueue(new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    if(code == 200){
                        String value = response.body().string();
                        mInterface.onResponse(value);
                    }else {
                        String message = response.message();
                        System.out.println(message);
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


}


    public static void getDatasync(final Context mContext, String method ,FormBody.Builder formBody, final HttpUtilInterface mInterface){
        try {
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(URL + method)//请求接口。如果需要传参拼接到接口后面。
                    .get()
                    .patch( formBody.build())
                    .build();//创建Request 对象

            client.newCall(request).enqueue(new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    if(code == 200){
                        String value = response.body().string();
                        mInterface.onResponse(value);
                    }else {
                        String message = response.message();
                        System.out.println(message);
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    /**
     * post
     * **/
    public static void postDataWithParame(final Context mContext, String method,String json, final HttpUtilInterface mInterface) {

        try {

            String url = URL + method;

            System.out.println("url: "  + url);
            System.out.println("json: "  + json);

            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
            RequestBody body = RequestBody.create( JSON,json.toString() );
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            client.newCall( request ).enqueue( new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    if(code == 200){
                        mInterface.onResponse( response.body().string());
                    }else {
                        String message = response.message();
                        System.out.println(message);
                    }
                }
            } );



        }catch (Exception e){
            e.printStackTrace();
        }

    }



    // POST 方法
    public static void post(Context mContext, String methodName, HashMap<String, String> param, final HttpUtilInterface mInterface) {


        FormBody.Builder formBody = new FormBody.Builder();

        if(param != null && !param.isEmpty()) {
            for (Map.Entry<String,String> entry: param.entrySet()) {
                formBody.add(entry.getKey(),entry.getValue());
            }
        }


        RequestBody form = formBody.build();
        Request.Builder builder = new Request.Builder();

        Request request = builder.post(form)
                .url(URL + methodName)
                .addHeader("User-Agent","BiLiBiLi WP Client/4.20.0 (orz@loli.my)")
                .build();


        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if(code == 200){
                    mInterface.onResponse( response.body().string());
                }else {
                    String message = response.message();
                    System.out.println(message);
                }
            }
        });
    }




    // POST 方法
    public static void get(Context mContext, String methodName, final HttpUtilInterface mInterface) {




        Request.Builder builder = new Request.Builder();

        Request request = builder.get()
                .url(URL + methodName)
                .addHeader("User-Agent","BiLiBiLi WP Client/4.20.0 (orz@loli.my)")
                .build();


        OkHttpClient client = new OkHttpClient();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if(code == 200){
                    mInterface.onResponse( response.body().string());
                }else {
                    String message = response.message();
                    System.out.println(message);
                }
            }
        });
    }



    public interface HttpUtilInterface{
        void onResponse(String body);
    }

}
