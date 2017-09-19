package com.lly.multistatelayout;

/**
 * VOperationInterface[v 1.0.0]
 * classes:com.lly.test.multistatelayout.VOperationInterface
 *
 * @author lileiyi
 * @date 2017/3/27
 * @time 9:39
 * @description
 */

public interface VOperationInterface {

    void showLoadingLayout();

    void showErrorLayout(String errorInfo);

    void showEmptyLayout();

    void showNoNetWorkLayout();

    void showLoginLayout();

    void onComplete();




}
