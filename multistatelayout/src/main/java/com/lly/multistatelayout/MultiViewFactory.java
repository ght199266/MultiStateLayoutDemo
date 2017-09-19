package com.lly.multistatelayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


/**
 * MultiViewFactory[v 1.0.0]
 * classes:com.lly.test.multistatelayout.MultiViewFactory
 *
 * @author lileiyi
 * @date 2017/3/27
 * @time 9:59
 * @description
 */

public class MultiViewFactory {

    private Context mContext;

    static final int LOADING = 1;

    static final int EMPTY = 2;

    static final int ERROR = 3;

    static final int NETWORK = 4;

    static final int LOGIN = 5;


    public MultiViewFactory(Context mContext) {
        this.mContext = mContext;
    }

    public View createView(int type) {
        View view = null;
        switch (type) {
            case LOADING:
                view = LayoutInflater.from(mContext).inflate(R.layout.loading, null);
                break;
            case EMPTY:
                view = LayoutInflater.from(mContext).inflate(R.layout.empty, null);
                break;
            case ERROR:
                view = LayoutInflater.from(mContext).inflate(R.layout.error, null);
                break;
            case NETWORK:
                view = LayoutInflater.from(mContext).inflate(R.layout.no_network, null);
                break;
            case LOGIN:
                view = LayoutInflater.from(mContext).inflate(R.layout.un_login, null);
                break;
        }
        return view;
    }

}
