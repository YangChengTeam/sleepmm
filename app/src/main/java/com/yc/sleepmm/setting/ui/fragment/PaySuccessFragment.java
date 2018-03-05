package com.yc.sleepmm.setting.ui.fragment;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.base.view.BaseDialogFragment;
import com.yc.sleepmm.setting.constants.BusAction;
import com.yc.sleepmm.setting.contract.PaySuccessContract;
import com.yc.sleepmm.setting.presenter.PaySuccessPresenter;
import com.yc.sleepmm.setting.widget.NoUnderlineSpan;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/3/5 11:02.
 */

public class PaySuccessFragment extends BaseDialogFragment<PaySuccessPresenter> implements PaySuccessContract.View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_success_confirm)
    Button btnSuccessConfirm;
    @BindView(R.id.tv_link_phone)
    TextView tvLinkPhone;

    private String orderSn;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pay_success_dialog;
    }

    @Override
    public void init() {
        if (getArguments() != null) {
            orderSn = getArguments().getString("orderSn");
        }

        mPresenter = new PaySuccessPresenter(getActivity(), this);
        deleteLinkUnderLine();
        RxView.clicks(btnSuccessConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.uploadPhone(etPhone.getText().toString().trim(), orderSn);
            }
        });

    }

    public void deleteLinkUnderLine() {
        if (tvLinkPhone.getText() instanceof Spannable) {
            URLSpan[] urlSpans = (((Spannable) tvLinkPhone.getText()).getSpans(0, tvLinkPhone.getText().length() - 1, URLSpan.class));
            for (URLSpan urlSpan : urlSpans) {
                String url = urlSpan.getURL();
                int start = ((Spannable) tvLinkPhone.getText()).getSpanStart(urlSpan);
                int end = ((Spannable) tvLinkPhone.getText()).getSpanEnd(urlSpan);
                NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan(url);
                Spannable s = (Spannable) tvLinkPhone.getText();
                s.setSpan(noUnderlineSpan, start, end, Spanned.SPAN_POINT_MARK);
            }
        }
    }

    @Override
    public void showLoadingDialog(String mess) {

    }

    @Override
    public void showLoadingProgressDialog(String mess, boolean isProgress) {
        ((BaseActivity) getActivity()).showLoadingProgressDialog(mess, isProgress);
    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void dismissProgressDialog() {
        ((BaseActivity) getActivity()).dismissProgressDialog();
    }

    @Override
    public void showUploadResult(String data) {
        ToastUtils.showCenterToast(data);
//        if (listener!=null){
//            listener.onViewFinish(this);
//        }
        dismiss();
        RxBus.get().post(BusAction.VIEW_FINISH, "finish");
    }

    public interface onViewFinishListener {
        void onViewFinish(PaySuccessFragment fragment);
    }

    public onViewFinishListener listener;

    public void setOnViewFinishListener(onViewFinishListener listener) {
        this.listener = listener;
    }
}
