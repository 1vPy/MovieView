package com.roy.movieview.utils.common;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SmsErrorUtils {
    public static String getErrorMsg(String code) {
        String errorMsg = "";
        switch (Integer.valueOf(code)) {
            case 466:
                errorMsg = "校验的验证码为空";
                break;
            case 468:
                errorMsg = "需要校验的验证码错误";
                break;
            case 477:
                errorMsg = "当前手机号发送短信的数量超过限额";
                break;
            case 600:
                errorMsg = "API使用受限制";
                break;
            case 601:
                errorMsg = "短信发送受限";
                break;
            case 602:
                errorMsg = "无法发送此地区短信";
                break;
            case 603:
                errorMsg = "请填写正确的手机号码";
                break;
            case 604:
                errorMsg = "当前服务暂不支持此国家";
                break;
            default:
                errorMsg = "未知错误："+code+",请重新验证";
                break;
        }
        return errorMsg;

    }
}
