package webroot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webroot.enums.ContentTypeEnum;
import webroot.enums.DefaultFilePathEnums;
import webroot.enums.HttpMethodEnum;
import webroot.enums.StatusCodeEnum;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * 用于处理请求的工作线程
 *
 * @author Zhanghao
 * @version 1.0
 * @since 2021.11.12
 */
public class HttpWorker implements Runnable {

    /**
     * 记录日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 客户端socket
     */
    private Socket socket;

    /**
     * 输出流a
     */
    private PrintWriter out;

    /**
     * 输入流
     */
    private BufferedReader in;

    /**
     * 文件输出流
     */
    private OutputStream outputStream;

    public HttpWorker(Socket clientSocket) {
        this.socket = clientSocket;
    }

    /**
     * 处理GET ， POST  ， HEAD请求
     */
    @Override
    public void run() {
        if (socket == null) {
            return;
        }

        try {
            //获取socket的输入和输出流
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();

            //读取请求url
            String requests = in.readLine();
            String[] parsedUrl = parseUrl(requests);
            //根据解析的url来获取方法
            String method = parsedUrl[0];
            //分别对解析到的GET，HEAD，POST请求进行处理
            if (method.toUpperCase(Locale.ROOT).equals(HttpMethodEnum.GET.getMethodName())) {
                processGetMethod(parsedUrl);
            } else if (method.toUpperCase(Locale.ROOT).equals(HttpMethodEnum.HEAD.getMethodName())) {
                processHeadMethod(parsedUrl);
            } else if (method.toUpperCase(Locale.ROOT).equals(HttpMethodEnum.POST.getMethodName())) {
                processPostMethod(parsedUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对请求URL进行解析，获取请求类型以及请求的资源
     *
     * @param request
     * @return
     */
    private String[] parseUrl(String request) {
        return request.split(" ");
    }

    /**
     * 处理GET请求
     *
     * @param parsedUrl
     */
    private void processGetMethod(String[] parsedUrl) {
        //先获取文件名称
        File requestedFile = null;
        try {
            String fileName = parsedUrl[1].trim().substring(1);
            //文件名称为空则直接返回默认页面
            if (StringUtils.isEmpty(fileName)) {
                displayDefaultPage(false);
            }
            //显示特定的页面
            //处理文件名后缀
            if (fileName.contains(".")) {
                requestedFile = new File(DefaultFilePathEnums.FILE_PATH_PREFIX.getFilePath() + fileName);
            } else {
                requestedFile = new File(DefaultFilePathEnums.FILE_PATH_PREFIX.getFilePath() + fileName + DefaultFilePathEnums.FILE_PATH_SUFFIX.getFilePath());
            }
            //如果文件存在则显示，否则显示默认页面
            if (requestedFile.exists()) {
                displaySpecificPage(requestedFile, false);
            } else {
                display404Page(false, requestedFile);
            }
        } catch (Exception e) {
            //请求错误
            e.printStackTrace();
            handleBadRequest(requestedFile);
        }
    }

    /**
     * 处理HEAD请求
     *
     * @param parsedUrl
     */
    private void processHeadMethod(String[] parsedUrl) {
        //获取文件名称
        File requestedFile = null;
        try {
            String fileName = parsedUrl[1].trim().substring(1);
            if (StringUtils.isBlank(fileName)) {
                displayDefaultPage(true);
            }

            if (fileName.endsWith(".*")) {
                requestedFile = new File(DefaultFilePathEnums.FILE_PATH_PREFIX.getFilePath() + fileName);
            } else {
                requestedFile = new File(DefaultFilePathEnums.FILE_PATH_PREFIX.getFilePath() + fileName + DefaultFilePathEnums.FILE_PATH_SUFFIX.getFilePath());
            }
            //如果文件存在则显示，否则显示默认页面
            if (requestedFile.exists()) {
                displaySpecificPage(requestedFile, true);
            } else {
                display404Page(true, requestedFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            handleBadRequest(requestedFile);
        }
    }

    /**
     * 处理POST请求, 当前只支持 对cgi的单独请求
     *
     * @param parsedUrl
     */
    private void processPostMethod(String[] parsedUrl) throws IOException {
        try {
            String fileName = parsedUrl[1].trim().substring(1);
            if (StringUtils.isBlank(fileName)) {
                displayDefaultPage(true);
            }

            if (fileName.contains("cgi-bin")) {
                if (fileName.contains("calculator")) {

                } else if (fileName.contains("query")) {

                }
            }
            //如果文件存在则显示，否则显示默认页面
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCalculator() throws IOException {
        Runtime.getRuntime().exec("perl D:\\project\\stefan-web-server\\src\\main\\resources\\webroot\\cgi-bin\\calculator.pl");
    }

    /**
     * 返回默认的页面
     *
     * @param forHead 表明是否为HEAD请求
     */
    private void displayDefaultPage(boolean forHead) throws IOException {
        File file = new File(DefaultFilePathEnums.DEFAULT_FILE.getFilePath());
        // 先设置HTTP头部
        out.println("HTTP/1.0 " + StatusCodeEnum.OK);
        out.println("Server: Stefan HTTP Server : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType(file).getMediaType());
        out.println("Content-length: " + file.length());
        //在头部与主体内容之间流出空行
        out.println();
        out.flush();

        //如果为get请求将主体内容写入
        if (!forHead) {
            outputStream.write(readBytesFromFile(file));
            outputStream.flush();
        }

        doLogging(HttpMethodEnum.GET, file, StatusCodeEnum.OK);
    }

    /**
     * 返回404页面
     *
     * @param forHead
     * @param requestedFile
     */
    private void display404Page(boolean forHead, File requestedFile) throws IOException {
        File file = new File(DefaultFilePathEnums.FILE_NOT_FOUND_PAGE.getFilePath());
        // 先设置HTTP头部
        out.println("HTTP/1.0 " + StatusCodeEnum.NOT_FOUND_404);
        out.println("Server: Stefan HTTP Server : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType(file).getMediaType());
        out.println("Content-length: " + file.length());
        //在头部与主体内容之间流出空行
        out.println();
        out.flush();

        //将主体内容写入
        if (!forHead) {
            outputStream.write(readBytesFromFile(file));
            outputStream.flush();
        }

    }

    /**
     * 对特定页面进行显示
     *
     * @param file
     * @param forHead
     */
    private void displaySpecificPage(File file, boolean forHead) throws IOException {
        // 先设置HTTP头部
        out.println("HTTP/1.0 " + StatusCodeEnum.OK);
        out.println("Server: Stefan HTTP Server : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType(file).getMediaType());
        out.println("Content-length: " + file.length());
        //在头部与主体内容之间流出空行
        out.println();
        out.flush();

        //将主体内容写入
        if (!forHead) {
            outputStream.write(readBytesFromFile(file));
            outputStream.flush();
        }

        doLogging(HttpMethodEnum.GET, file, StatusCodeEnum.OK);
    }

    /**
     * 处理错误请求
     *
     * @param requestedFile
     */
    private void handleBadRequest(File requestedFile) {
        File file = new File(DefaultFilePathEnums.BAD_REQUEST_PAGE.getFilePath());
        // 先设置HTTP头部
        out.println("HTTP/1.0" + StatusCodeEnum.BAD_REQUEST);
        out.println("Server: Stefan HTTP Server : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType(file).getMediaType());
        out.println("Content-length: " + file.length());
        //在头部与主体内容之间流出空行
        out.println();
        out.flush();

        doLogging(HttpMethodEnum.GET, requestedFile, StatusCodeEnum.BAD_REQUEST);
    }

    /**
     * 处理禁止执行页面
     */
    private void handleForbidden() {
        File file = new File(DefaultFilePathEnums.FORBIDDEN_PAGE.getFilePath());
        // 先设置HTTP头部
        out.println("HTTP/1.0" + StatusCodeEnum.FORBIDDEN);
        out.println("Server: Stefan HTTP Server : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + getContentType(file).getMediaType());
        out.println("Content-length: " + file.length());
        //在头部与主体内容之间流出空行
        out.println();
        out.flush();

    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    private ContentTypeEnum getContentType(File file) {
        //通过后缀来区分html文本和一般文本
        if (file.getName().endsWith(".html")) {
            return ContentTypeEnum.TEXT_HTML;
        } else {
            return ContentTypeEnum.TEXT_PLAIN;
        }
    }

    /**
     * 从文件中读取字节流存入字节数组
     *
     * @param file
     * @return
     */
    private byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];

        //将文件读入字节数组
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(bFile);
        fileInputStream.close();
        for (int i = 0; i < bFile.length; i++) {
            System.out.print((char) bFile[i]);
        }

        return bFile;
    }

    /**
     * 记录操作日志
     * 分别对访问者的ip地址，请求方法，文件名称，文件长度，录状态码进行记录
     */
    private void doLogging(HttpMethodEnum methodEnum, File file, StatusCodeEnum statusCodeEnum) {
        InetAddress localIp = socket.getLocalAddress();
        try {
            LOGGER.info("localIp: {} , method: {},fileName: {},fileSize: {},statusCode: {}",
                    Inet4Address.getLocalHost().getHostAddress(), methodEnum.getMethodName(), file.getName(), file.length(), statusCodeEnum.getCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
