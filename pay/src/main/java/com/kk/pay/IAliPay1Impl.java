package com.kk.pay;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.utils.SignUtils;
import com.kk.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhangkai on 2017/3/18.
 */

public class IAliPay1Impl extends IPayImpl {

    //默认参数配置
    private static String APPID = "2018020502145910"; //支付宝APPID
    private static String PARTNERID = "2088821411331682";//商户UID
    private static String EMAIL = "3258186647@qq.com"; //邮箱
    private static String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCFn2grl81KYOWGOO4Hj8gq6XaTLqFTNEuFa/Zak3qZzHnJXlIa01jGMzPZzKNx50ugpogHqsGaKZzJ0gOT/WtxrBbecKnlzajrKks+KlwR+cPDF+/J9j3BFohNw5SMWNRsa6jHTjhqu9q9iEQtOl/ZPG9Stjbgz8Nwkb6t8UJtL0mS91+sL+6k+7qlAsmBXfXdYuZN8n8aTMxssHg75/0/WLRhoDKq6MXGFq9ojUUlgpFZ9YWYfqvyVN/isRV7qLOiNlKq3Ul/FC5Vh7C2JXu8gkBL792Bzo6Prz4kSADknkfyt2ixfU8fRxNlKhb0CCmcE4cSKaYZdXS7YUiDm625AgMBAAECggEBAIQweNt3Z44HYclxFHnCkiFxqO5eGRa5C2VIswRUFduE346Iku0a3B3nlYoH2kHasYSSeswq7zBVrlgyPXvwHpTrzAvupQVEebF7u8gaG89kqXedfIjt45XbdqiHrbNSRYOk1emlxkZA5VrPLbMYpq3ADdljjdLFdvEsHdjybeaYfu4d2rXtNOthMqUjLABEgg+R1FR8qtqcXewr66OvjarMaKbxk4lHWe0WDTGUXue3rLDPhRyzzbwq+sykTJ3G4eDBQbK1btqeKWjMUTu0L69NPrIAzx8ZynC/Ih8LN/7a82hjXg35FNTArPzGqwv5qdCmYnBVbYq1WGzo6veUaskCgYEA9LTOyZJTlCoJZu7RNOgE6d9v6+jb/GHeANme7J2iV/N7pFqn4NNXyqPSDhAK4pLbo/UycXarXvYVbEEkLSF9kcp/gm60l8+6uTOi1b+wDx2tpJ7dVpDLf+5y6x6s6lBIE0qzqqfVH8BmNySUvhfcuVULvhGnV4a62wLkGPRkMpsCgYEAi8om8MKvYlHbiTCBKbC+smg2e5sXmmAFIUmSKHzUwF2+A28Vl6IXdK8k9jmiJzPCIZVkOBYyf5kQwqCNEYkGHrFwuZYWKU1PmhQ/thNJDY6pVweVODMtlbUbPW44n4fO516+CnXh0p60YIfdJ/Noc8xfO7ZZE6L6RS5pNOVaTDsCgYEAqwDnOQmein3N0Da30AcL1/uilPNHN9mUcHejjaqi1l96WeJYSs6mHR/aHKWtVaRXBP7GX3AUVeoWEm57P2aV/5nI/XcrkKND00R4WHL6L7YHsHCsMGBDNYWGnTP0llG/7GtZnrpmPygOxh40K5s1TfZWnF5t2MZ4MbCVgbLXil0CgYAsTeanmdyCOdVtlqzpLLZTzwmthUsmx1X45dtn76ByU4OcjQF37Iyok7YQP9XvvMzDXhjEWeypWjbgqIftTdNduPk0KKhfcLxQ5igHKfeWT2fGSIX3qOjCsQKwJpXVofJmjDWJFTF8UoZyrbzfI5swUE7CmxmCSr4tXPdFJ4fF7wKBgQCFo7yvk25iSFq+6Z5zjrYEamd5BfGDeV1jqYSrYxwUMXO+XJZNJy8hUm4hPQG3IvEfZ/07DAdcbzJqzoYo/mYkPQbtV165SvjCx+fjykmuOmFrj7OQ6AL76RrrQvsLHn3jFg+NQg/pLS2ijWgNTnWSmdt7L9IoV7NfOkjEl5iAcA==";
    private static String NOTIFY_URL = "http://u.xcjz8.com/api/index/notify_url/alipay"; //回调地址

    public IAliPay1Impl(Activity context) {
        super(context);
    }

    @Override
    public void pay(OrderInfo orderInfo, IPayCallback iPayCallback) {
        isGen = true;
        if (orderInfo.getPayInfo() != null) {
            APPID = get(orderInfo.getPayInfo().getAppid(), APPID);
            PARTNERID = get(orderInfo.getPayInfo().getPartnerid(), PARTNERID);
            EMAIL = get(orderInfo.getPayInfo().getEmail(), EMAIL);
            PRIVATE_KEY = get(orderInfo.getPayInfo().getPrivatekey(), PRIVATE_KEY);
            NOTIFY_URL = get(orderInfo.getPayInfo().getNotify_url(), NOTIFY_URL);
        }
        alipay(orderInfo, orderInfo.getMoney() + "", orderInfo.getOrder_sn(), orderInfo.getName(), orderInfo.getName(),
                iPayCallback);
    }

    /**
     * 支付宝支付
     *
     * @param money
     * @param ordeID
     * @param theOrderName
     * @param theOrderDetail
     */
    private void alipay(OrderInfo orderInfo, String money, String ordeID, String theOrderName, String theOrderDetail,
                        IPayCallback
                                iPayCallback) {
        String privatekey = GoagalInfo.get().getPublicKey(PRIVATE_KEY);
        Map<String, String> params = buildOrderParamMap(money, theOrderName, theOrderDetail, ordeID);
        String orderParam = buildOrderParam(params);//对订单地址排序
        String sign = getSign(params, privatekey);
        try {
            if (!TextUtils.isEmpty(sign)) {
                // 完整的符合支付宝参数规范的订单信息
                final String payInfo = orderParam + "&" + sign;
                //调用新线程支付
                new Thread(new AlipayRunnable(orderInfo, payInfo, iPayCallback)).start();
            } else {
                new IllegalThreadStateException("签名错误");
            }
        } catch (Exception e) {
        }
    }


    /**
     * 支付宝支付
     */

    private class AlipayRunnable implements Runnable {
        private OrderInfo orderInfo;
        private String mPayInfo;
        private IPayCallback iPayCallback;

        public AlipayRunnable(OrderInfo orderInfo, String payInfo, IPayCallback iPayCallback) {
            this.orderInfo = orderInfo;
            this.mPayInfo = payInfo;
            this.iPayCallback = iPayCallback;
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(mContext);

            // 调用支付接口，获取支付结果
            Map<String, String> result = alipay.payV2(mPayInfo, false);
            PayResult payResult = new PayResult(result);
            final String resultInfo = payResult.getResult();// 同步返回需要验证的信息

            String resultStatus = payResult.getResultStatus();
            if (TextUtils.equals(resultStatus, "9000")) {

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderInfo.setMessage("支付成功");
                        iPayCallback.onSuccess(orderInfo);
                        ToastUtil.toast(mContext, "支付成功");
//                        checkOrder(orderInfo, iPayCallback, Config.CHECK_URL);
                    }
                });

            } else if (TextUtils.equals(resultStatus, "6001")) {

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderInfo.setMessage("支付取消");
                        iPayCallback.onFailure(orderInfo);
                        ToastUtil.toast(mContext, "支付取消");
                    }
                });
            } else {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderInfo.setMessage("支付失败");
                        iPayCallback.onFailure(orderInfo);
                        ToastUtil.toast(mContext, "支付失败");
//                        checkOrder(orderInfo, iPayCallback, Config.CHECK_URL);
                    }
                });
            }
        }
    }


    /**
     * 对订单签名
     *
     * @param map
     * @param rsaKey
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey) {
        List<String> keys = new ArrayList<>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, true);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 生成订单信息
     *
     * @param money
     * @param theOrderName
     * @param theOrderDetail
     * @param ordeID
     * @return
     */

    private static Map<String, String> buildOrderParamMap(String money, String theOrderName, String theOrderDetail, String ordeID) {

        Map<String, String> keyValues = new HashMap<>();
        keyValues.put("app_id", APPID);
        keyValues.put("partner", PARTNERID);
        keyValues.put("seller_id", EMAIL);
        keyValues.put("notify_url", NOTIFY_URL);
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + money + "\",\"subject\":\"" + theOrderName + "\",\"body\":\"" + theOrderDetail + "\",\"out_trade_no\":\"" + ordeID + "\"}");
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type", "RSA2");
        keyValues.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        keyValues.put("payment_type", "1");
        keyValues.put("version", "1.0");

        return keyValues;
    }


    private static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));
        return sb.toString();
    }

    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }


    private class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }
}
