package webroot.enums;

/**
 * 默认文件路径枚举
 * @author Zhanghao
 * @since 2021.11.11
 * @version 1.0
 */
public enum DefaultFilePathEnums {

    /**
     * 本地环境下的根路径
     */
    LOCAL_FILE_PATH("local path",""),
    /**
     * 文件名前缀
     */
    FILE_PATH_PREFIX("prefix of the file name","D:\\project\\stefan-web-server\\src\\main\\resources\\webroot\\"),

    /**
     * 文件名后缀
     */
    FILE_PATH_SUFFIX("suffix of the file name",".html"),

    /**
     * 如果在get中没有解析到文件名的默认页面
     */
    DEFAULT_FILE("default file to display",FILE_PATH_PREFIX.getFilePath()+"index.html"),

    /**
     * 未找到相应文件时展示的页面
     */
    FILE_NOT_FOUND_PAGE("404 Page",FILE_PATH_PREFIX.getFilePath()+"404.html"),

    /**
     * 400错误请求页面
     */
    BAD_REQUEST_PAGE("400 Page",FILE_PATH_PREFIX.getFilePath()+ "400.html"),

    /**
     * 403禁止执行页面
     */
    FORBIDDEN_PAGE("403 Page",FILE_PATH_PREFIX.getFilePath()+"403.html"),

    ;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件路径
     */
    private String filePath;

    DefaultFilePathEnums(String fileType, String filePath) {
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
