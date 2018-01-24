
package com.yc.sleepmm.base.rxnet;

/**
 * TinyHung@Outlook.com
 * 2017/3/22.
 * UI组件实现的请求回调
 */
public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();
    }

    public interface BaseView {

        void showErrorView();

        void complete();
    }
}
