package webroot.enums;

/**
 * 返回内容的类型枚举类
 * @author Zhanghao
 * @since 2021.11.11
 * @version 1.0
 */
public enum ContentTypeEnum {

    /**
     * HTML文本
     */
    TEXT_HTML("text/html"),

    /**
     * 纯文本
     */
    TEXT_PLAIN("text/plain");

    /**
     * 媒体类型
     */
    private String mediaType;

    ContentTypeEnum(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
