package com.sojay.demo;

import android.webkit.JavascriptInterface;

public class ActionSelectInterface {

    ActionSelectListener listener;

    @JavascriptInterface
    public void onInputChange(final String html) {
        if (listener != null)
            listener.onInputChange(html);
    }

    @JavascriptInterface
    public void onStatesChange(final String states) {
        if (listener != null)
            listener.onStatesChange(states);
    }

    public void setListener(ActionSelectListener l) {
        this.listener = l;
    }

    public interface ActionSelectListener {
        void onInputChange(final String html);
        void onStatesChange(final String states);
    }
}
