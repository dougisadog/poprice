package poprice.wechat.web.rest;


public class RestResult {
    public static final int CODE_SUCCESS = 0;
    public static final String MSG_SUCCESS = "";

    /**
     * 没有这个用户账户
     */
    public static final int CODE_ACCOUNT_MISSING = 1;

    /**
     * 用户账户不可用（可能是登录受限）
     */
    public static final int CODE_ACCOUNT_INVALID = 2;


    /**
     * 用户账户无权限（可能是没有api访问权限）
     */
    public static final int CODE_ACCOUNT_IP_NOT_ALLOWED = 3;

    /**
     * 用户账户余额不足
     */
    public static final int CODE_ACCOUNT_INSUFFICIENT_BALANCE = 4;

    /**
     * 请求数据校验失败
     */
    public static final int CODE_DATA_VALIDATION_ERROR = 21;

    /**
     * 提交的数据格式错误
     */
    public static final int CODE_DATA_FORMAT_ERROR = 22;


    /**
     * 没有可用产品
     */
    public static final int CODE_DATA_NO_PRODUCT_AVAILABLE = 23;

    /**
     * 提交的数据时间戳不正确（和服务器当前时间误差过大）
     */
    public static final int CODE_DATA_TIMESTAMP_ERROR = 23;

    /**
     * 没有这个订单号
     */
    public static final int CODE_ORDER_MISSING = 41;

    /**
     * 没有这个手机号
     */
    public static final int CODE_ORDER_MOBILE_MISSING = 42;


    /**
     * 未知错误
     */
    public static final int CODE_UNKNOWN = 999;



    private Boolean ok = false;

    private String message = MSG_SUCCESS;

    private Object object;

    private Integer code = null;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setToSuccess() {
        this.setOk(true);
        this.setCode(CODE_SUCCESS);
    }
}
