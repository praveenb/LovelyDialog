package com.yarolegovich.lovelydialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.HtmlCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yarolegovich on 16.04.2016.
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
public abstract class AbsLovelyDialog<T extends AbsLovelyDialog> {

    private static final String KEY_SAVED_STATE_TOKEN = "key_saved_state_token";

    private Dialog dialog;
    private View dialogView;

    private ImageView iconView;
    private TextView topTitleView;
    private TextView topMessageCode;
    private ImageView topIconView;
    private TextView titleView;
    private TextView messageView;

    public AbsLovelyDialog(Context context) {
        init(new AlertDialog.Builder(context));
    }

    public AbsLovelyDialog(Context context, int theme) {
        init(new AlertDialog.Builder(context, theme));
    }

    private void init(AlertDialog.Builder dialogBuilder) {
        dialogView = LayoutInflater.from(dialogBuilder.getContext()).inflate(getLayout(), null);
        dialog = dialogBuilder.setView(dialogView).create();

        iconView = findView(R.id.ld_icon);
        titleView = findView(R.id.ld_title);
        messageView = findView(R.id.ld_message);
        topTitleView = findView(R.id.ld_top_title);
        topMessageCode = findView(R.id.ld_top_code);
        topIconView = findView(R.id.ld_top_icon);

    }

    @LayoutRes
    protected abstract int getLayout();

    public T setMessage(@StringRes int message) {
        return setMessage(string(message));
    }

    public T setMessage(CharSequence message) {
        messageView.setVisibility(View.VISIBLE);
        messageView.setText(HtmlCompat.fromHtml(message.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        return (T) this;
    }

    public T setTitle(@StringRes int title) {
        return setTitle(string(title));
    }

    public T setTopTitle(@StringRes int title) {
        return setTopTitle(string(title));
    }

    public T setTitle(CharSequence title) {
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(title);
        return (T) this;
    }

    public T setTopTitle(CharSequence title) {
        topTitleView.setVisibility(View.VISIBLE);
        topTitleView.setText(title);
        return (T) this;
    }

    public T setTopTitleColor(int color) {
        if (color != -1)
            topTitleView.setTextColor(color(color));
        return (T) this;
    }

    public T setTitleColor(int color) {
        if (color != -1)
            titleView.setTextColor(color(color));
        return (T) this;
    }

    public T setTopMessageCode(CharSequence title) {
        topMessageCode.setVisibility(View.VISIBLE);
        topMessageCode.setText(title);
        return (T) this;
    }

    public T setTopMessageCodeColor(int color) {
        if (color != -1)
            topMessageCode.setTextColor(color(color));
        return (T) this;
    }

    public T setMessageColor(int color) {
        if (color != -1)
            messageView.setTextColor(color(color));
        return (T) this;
    }

    public T setIcon(Bitmap bitmap) {
        iconView.setVisibility(View.VISIBLE);
        iconView.setImageBitmap(bitmap);
        return (T) this;
    }

    public T setIcon(Drawable drawable) {
        iconView.setVisibility(View.VISIBLE);
        iconView.setImageDrawable(drawable);
        return (T) this;
    }

    public T setIcon(@DrawableRes int iconRes) {
        iconView.setVisibility(View.VISIBLE);
        iconView.setImageResource(iconRes);
        return (T) this;
    }

    public T setTopIcon(Bitmap bitmap) {
        topIconView.setVisibility(View.VISIBLE);
        topIconView.setImageBitmap(bitmap);
        return (T) this;
    }

    public T setTopIcon(Drawable drawable) {
        topIconView.setVisibility(View.VISIBLE);
        topIconView.setImageDrawable(drawable);
        return (T) this;
    }

    public T setTopIcon(@DrawableRes int iconRes) {
        topIconView.setVisibility(View.VISIBLE);
        topIconView.setImageResource(iconRes);
        return (T) this;
    }

    public T setIconTintColor(int iconTintColor) {
        iconView.setColorFilter(iconTintColor);
        return (T) this;
    }

    public T setTitleGravity(int gravity) {
        titleView.setGravity(gravity);
        return (T) this;
    }

    public T setMessageGravity(int gravity) {
        messageView.setGravity(gravity);
        return (T) this;
    }

    public T setTopColor(@ColorInt int topColor) {
        if (topColor != -1)
            findView(R.id.ld_color_area).setBackgroundColor(topColor);
        return (T) this;
    }

    public T setTopColorRes(@ColorRes int topColoRes) {
        return setTopColor(color(topColoRes));
    }

    public T setMessageBgColor(@ColorInt int messageBgColor) {
        if (messageBgColor != -1)
            findView(R.id.ld_msg_area).setBackgroundColor(messageBgColor);
        return (T) this;
    }

    public T setMessageBgColorRes(@ColorRes int messageBgColorRes) {
        return setTopColor(color(messageBgColorRes));
    }

    /*
     * You should call method saveInstanceState on handler object and then use saved info to restore
     * your dialog in onRestoreInstanceState. Static methods wasDialogOnScreen and getDialogId will
     * help you in this.
     */
    public T setInstanceStateHandler(int id, LovelySaveStateHandler handler) {
        handler.handleDialogStateSave(id, this);
        return (T) this;
    }

    public T setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return (T) this;
    }

    public T setSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean hasSavedStateHere =
                    savedInstanceState.keySet().contains(KEY_SAVED_STATE_TOKEN) &&
                            savedInstanceState.getSerializable(KEY_SAVED_STATE_TOKEN) == getClass();
            if (hasSavedStateHere) {
                restoreState(savedInstanceState);
            }
        }
        return (T) this;
    }

    public Dialog show() {
        dialog.show();
        return dialog;
    }

    public Dialog create() {
        return dialog;
    }

    public void dismiss() {
        dialog.dismiss();
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_SAVED_STATE_TOKEN, getClass());
    }

    void restoreState(Bundle savedState) {
    }

    boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    protected String string(@StringRes int res) {
        return dialogView.getContext().getString(res);
    }

    protected int color(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    protected Context getContext() {
        return dialogView.getContext();
    }

    protected <ViewClass extends View> ViewClass findView(int id) {
        return (ViewClass) dialogView.findViewById(id);
    }

    protected class ClickListenerDecorator implements View.OnClickListener {

        private View.OnClickListener clickListener;
        private boolean closeOnClick;

        protected ClickListenerDecorator(View.OnClickListener clickListener, boolean closeOnClick) {
            this.clickListener = clickListener;
            this.closeOnClick = closeOnClick;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                if (clickListener instanceof LovelyDialogCompat.DialogOnClickListenerAdapter) {
                    LovelyDialogCompat.DialogOnClickListenerAdapter listener =
                            (LovelyDialogCompat.DialogOnClickListenerAdapter) clickListener;
                    listener.onClick(dialog, v.getId());
                } else {
                    clickListener.onClick(v);
                }
            }
            if (closeOnClick) {
                dismiss();
            }
        }
    }
}
