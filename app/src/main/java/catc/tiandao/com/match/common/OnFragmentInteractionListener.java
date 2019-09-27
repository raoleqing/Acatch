package catc.tiandao.com.match.common;

import android.net.Uri;

/**
 * Created by Administrator on 2018/1/18 0018.
 *
 * Fragement 回调接口
 */
public interface OnFragmentInteractionListener {


    public static final String PROGRESS_SHOW =  "content://com.yiqi.calendar.ui.memo/showProgress";
    public static final String PROGRESS_HIDE =  "content://com.yiqi.calendar.ui.memo/hideProgress";


    void onFragmentInteraction(Uri uri);
}