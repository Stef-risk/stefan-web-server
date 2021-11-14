package webroot.enums;

/**
 * 默认文件路径枚举
 * @author Zhanghao
 * @since 2021.11.11
 * @version 1.0
 */
public enum DefaultFilePathEnums {

    /**
     * 如果在get中没有解析到文件名的默认页面
     */
    DEFAULT_FILE("default file to display","C:\\Users\\Stefrisk\\Desktop\\Git Repositories\\stefan-web-server\\src\\main\\resources\\webroot\\index.html"),

    /**
     * 未找到相应文件时展示的页面
     */
    FILE_NOT_FOUND("404 Page","C:\\Users\\Stefrisk\\Desktop\\Git Repositories\\stefan-web-server\\src\\main\\resources\\webroot\\404.html"),

    /**
     * 文件名前缀
     */
    FILE_PATH_PREFIX("prefix of the file name","C:\\Users\\Stefrisk\\Desktop\\Git Repositories\\stefan-web-server\\src\\main\\resources\\webroot\\");

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
