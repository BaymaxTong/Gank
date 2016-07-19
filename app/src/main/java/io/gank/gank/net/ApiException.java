package io.gank.gank.net;

/**
 * 统一异常处理的类，用于显示异常信息。
 * Created baymax on 16/7/5.
 */
public class ApiException extends RuntimeException {

    public static final int REQUEST_ERROR = 100;
    public static final int WRONG_USER = 101;
    public static final int WRONG_PASSWORD = 102;
    public static final int NO_DATA = 103;

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 返回具体异常信息
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code){
        String message = "";
        switch (code) {
            case REQUEST_ERROR:
                message = "请求返回错误！";
                break;
            case WRONG_USER:
                message = "用户名错误";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            case NO_DATA:
                message = "没有更多的数据啦";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}

