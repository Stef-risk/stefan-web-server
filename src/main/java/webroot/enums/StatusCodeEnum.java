package webroot.enums;

/**
 * Http状态码枚举
 *
 * @author Zhanghao
 * @since 2021.11.13
 * @version 1.0
 */
public enum StatusCodeEnum {

    /**
     *404	Not Found	服务器无法根据客户端的请求找到资源（网页）。
     */
    NOT_FOUND_404("404","File Not Found"),

    /**
     *200	OK	请求成功。
     */
    OK("200","OK"),

    /**
     *400	Bad Request	客户端请求的语法错误，服务器无法理解
     */
    BAD_REQUEST("400","Bad Request"),

    /**
     *403	Forbidden	服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    FORBIDDEN("403","Forbidden")
    ;

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态码描述
     */
    private String description;

    /**
     *
     * @param code
     * @param description
     */
    StatusCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ""+getCode()+" "+getDescription();
    }
}
