package catc.tiandao.com.match.widgets.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import catc.tiandao.com.match.R;


/**
 * popwindow帮助类，避免重复写代码
 * Created by yangtufa on 2017/10/10.
 */

public class PopWindowHelper implements PopupWindow.OnDismissListener {

    private PopupWindow popupWindow;
    private View contentView;
    private Context context;

    private int xOffset;
    private int yOffset;
    private View showAtView;//在哪个view下弹出

    private int animStyle = R.style.bottomShowAnimStyle;

    private int layoutResId = -1;//布局id，优先使用此id，如果未设置，则使用contentView
    private PopupWindow.OnDismissListener onDismissListener;

    public static PopWindowHelper newInstance() {
        return new PopWindowHelper();
    }

    /***显示view*/
    public PopWindowHelper show() {
        if (layoutResId != -1) {
            contentView = LayoutInflater.from(context).inflate(layoutResId, null);
        }
        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(contentView);
        popupWindow.setAnimationStyle(animStyle);
        popupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        popupWindow.setOnDismissListener(this);
        popupWindow.showAsDropDown(showAtView, xOffset, yOffset);
        return this;
    }

    /**
     * 关闭popupWindow
     */
    public void dimiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    //必须调用的方法 start
    public PopWindowHelper with(Context context) {
        this.context = context;
        return this;
    }

    public PopWindowHelper setContentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    public PopWindowHelper setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
        return this;
    }

    public PopWindowHelper setShowAtView(View showAtView) {
        this.showAtView = showAtView;
        return this;
    }
    //必须调用的方法  end

    public PopWindowHelper setXYOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    public PopWindowHelper setAnimStyle(int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public PopWindowHelper setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    /**
     * 向外提供获取view的方法
     *
     * @param viewId viewId
     * @return view
     */
    public  <T extends View> T getView(int viewId) {
        return (T) contentView.findViewById(viewId);
    }

    /***向外提供设置组件监听的方法
     * @param elementId 控件id
     * @param onClickListener 监听器
     */
    public PopWindowHelper setOnClickListener(int elementId, View.OnClickListener onClickListener) {
        getView(elementId).setOnClickListener(onClickListener);
        return this;
    }

    /***设置提示图片*/
    public PopWindowHelper setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /****
     * 设置按钮的backgroundResource
     * @param btnId 按钮id
     * @param backgroundResId backgroundResId
     */
    public PopWindowHelper setBackgroundResource(int btnId, int backgroundResId) {
        Button button = getView(btnId);
        button.setBackgroundResource(backgroundResId);
        return this;
    }

    /***
     * 设置按钮的backgroundResource
     * @param btnId 按钮id
     * @param backgroundResId backgroundResId
     * @param textColor 按钮文本颜色
     */
    public PopWindowHelper setBackgroundResource(int btnId, int backgroundResId, int textColor) {
        Button button = getView(btnId);
        button.setBackgroundResource(backgroundResId);
        button.setTextColor(context.getResources().getColor(textColor));
        return this;
    }

    /***
     * 设置文本
     * @param viewId 控件id
     * @param msg 文本
     */
    public PopWindowHelper setText(int viewId, String msg) {
        TextView tvText = getView(viewId);
        tvText.setText(msg);
        return this;
    }

    /***
     * 设置文本颜色
     * @param viewId 控件id
     * @param textColor 文本颜色id
     */
    public PopWindowHelper setTextColor(int viewId, int textColor) {
        View view = getView(viewId);
        if (view instanceof Button) {
            ((Button) view).setTextColor(context.getResources().getColor(textColor));
        }
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(context.getResources().getColor(textColor));
        }
        return this;
    }

    /***设置背景透明度*/
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f);
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }
}
