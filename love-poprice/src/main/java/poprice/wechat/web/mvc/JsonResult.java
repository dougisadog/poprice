package poprice.wechat.web.mvc;


public class JsonResult {
    private Boolean ok = true;

    private String message;

    private Object object;

    public JsonResult() {
    }

    public JsonResult(Boolean ok, String message, Object object) {
        this.ok = ok;
        this.message = message;
        this.object = object;
    }

    public Boolean isOk() {
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
}
