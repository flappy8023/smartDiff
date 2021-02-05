package com.flappy.smartdiff.util.tcp.listener;

public abstract class OnTransListener {
    private OnDataTransforListener onDataTransforListener;

    public OnDataTransforListener getOnDataTransforListener() {
        return onDataTransforListener;
    }

    public void setOnDataTransforListener(OnDataTransforListener onDataTransforListener) {
        this.onDataTransforListener = onDataTransforListener;
    }

    public abstract void onSucessfull();

    public abstract void onFailed(Object error);
}
