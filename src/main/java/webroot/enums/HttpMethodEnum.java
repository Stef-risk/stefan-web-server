package webroot.enums;

/**
 * Http请求方法枚举类
 * @author Zhanghao
 * @since 2021.11.11
 * @version 1.0
 */
public enum HttpMethodEnum {

    /**
     * GET请求
     */
    GET("GET","GET request"),

    /**
     * HEAD请求
     */
    HEAD("HEAD","HEAD request"),

    /**
     * POST请求
     */
    POST("POST","POST request");

    /**
     * 请求名称
     */
    private String methodName;

    /**
     * 请求描述
     */
    private String description;

    HttpMethodEnum(String methodName, String description) {
        this.methodName = methodName;
        this.description = description;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
