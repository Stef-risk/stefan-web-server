package webroot;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This is a Http Server which implements Http 1.0
 * Supports GET / POST /HEAD
 * Also Multi-threaded
 *
 * @author Zhanghao
 * @version 1.0
 * @since 2021.11.12
 */
public class StefanHttpServer {

    /**
     * 主机地址
     */
    private String hostname;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 最大线程数
     */
    private Integer maxConnections;

    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 服务端socket
     */
    private ServerSocket serverSocket;

    /**
     * 存储文件的路径
     */
    private String filePath;

    /**
     * 构造方法
     */
    public StefanHttpServer() {
        //初始化线程池
        threadPoolExecutor = new ThreadPoolExecutor(10, 20, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(128));
        initServerSocket();
    }

    /**
     * 服务器端开始多线程工作
     */
    public void serverStart() {
        //主线程负责处理请求，创建新线程去执行
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                HttpWorker worker=new HttpWorker(clientSocket);
                //工作线程开始执行
                Thread thread=new Thread(worker);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化serverSocket
     */
    private void initServerSocket() {
        port = 8888;
        hostname = "127.0.0.1";
        maxConnections = 30;
        filePath = "C:\\Users\\Stefrisk\\Desktop\\Git Repositories\\stefan-web-server\\src\\main\\resources\\webroot\\";
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
