package catc.tiandao.com.match.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import catc.tiandao.com.match.R;


/**
 * 封装一个带背景蒙层效果的Dialog
 * 在构建这个实例时,传递一个LayoutResId进来即可
 * <p>
 * Created by yangtufa on 2017/3/16.
 */
public class CommonDialog extends Dialog {

    private final View dialogView;

    public CommonDialog(@NonNull Context context, int layoutResId) {
        super(context, R.style.commonDialogStyle);

        dialogView = LayoutInflater.from(context).inflate(layoutResId, null);
        setContentView(dialogView);
        setCancelable(true);
    }

    /***设置提示图片*/
    public void setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
    }

    /****
     * 设置按钮的backgroundResource
     * @param btnId 按钮id
     * @param backgroundResId backgroundResId
     */
    public void setBackgroundResource(int btnId, int backgroundResId) {
        Button button = getView(btnId);
        button.setBackgroundResource(backgroundResId);
    }

    /***
     * 设置按钮的backgroundResource
     * @param btnId 按钮id
     * @param backgroundResId backgroundResId
     * @param textColor 按钮文本颜色
     */
    public void setBackgroundResource(int btnId, int backgroundResId, int textColor) {
        Button button = getView(btnId);
        button.setBackgroundResource(backgroundResId);
        button.setTextColor(getContext().getResources().getColor(textColor));
    }

    /***
     * 设置文本
     * @param viewId 控件id
     * @param msg 文本
     */
    public void setText(int viewId, String msg) {
        TextView tvText = getView(viewId);
        tvText.setText(msg);
    }

    /***
     * 设置文本颜色
     * @param viewId 控件id
     * @param textColor 文本颜色id
     */
    public void setTextColor(int viewId, int textColor) {
        View view = getView(viewId);
        if (view instanceof Button) {
            ((Button) view).setTextColor(getContext().getResources().getColor(textColor));
        }
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(getContext().getResources().getColor(textColor));
        }
    }

    /**
     * desc 设置某个view的显示和隐藏
     *
     * @param viewId     viewId
     * @param showOrHide View.GONE  or  View.VISIBLE
     * @author liaofucheng on 2017/4/18 17:32
     */
    public void toggleView(int viewId, int showOrHide) {
        try {
            getView(viewId).setVisibility(showOrHide);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向外提供获取view的方法
     *
     * @param viewId viewId
     * @return view
     */
    public <T extends View> T getView(int viewId) {
        return (T) dialogView.findViewById(viewId);
    }

    /***向外提供设置组件监听的方法
     * @param elementId 控件id
     * @param onClickListener 监听器
     */
    public void setOnClickListener(int elementId, View.OnClickListener onClickListener) {
        getView(elementId).setOnClickListener(onClickListener);
    }
}
