package webroot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
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
     * 线程池
     */
    private final ThreadPoolExecutor threadPoolExecutor;

    /**
     * 服务端socket
     */
    private ServerSocket serverSocket;


    /**
     * 构造方法
     */
    public StefanHttpServer() {
        //初始化线程池,设置淘汰策略为弃老策略
        threadPoolExecutor = new ThreadPoolExecutor(10, 20, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(128));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        initServerSocket();
    }

    /**
     * 服务器端开始多线程工作
     */
    public void serverStart() {
        //主线程负责处理请求，创建新线程去执行
        printBanner();
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                HttpWorker worker = new HttpWorker(clientSocket);
                //工作线程开始执行
                threadPoolExecutor.execute(worker);
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
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器开始工作时打印banner
     */
    private void printBanner() {
        System.out.println("\n" +
                "  _________ __          _____                 __      __      ___.       _________                                \n" +
                " /   _____//  |_  _____/ ____\\____    ____   /  \\    /  \\ ____\\_ |__    /   _____/ ______________  __ ___________ \n" +
                " \\_____  \\\\   __\\/ __ \\   __\\\\__  \\  /    \\  \\   \\/\\/   // __ \\| __ \\   \\_____  \\_/ __ \\_  __ \\  \\/ // __ \\_  __ \\\n" +
                " /        \\|  | \\  ___/|  |   / __ \\|   |  \\  \\        /\\  ___/| \\_\\ \\  /        \\  ___/|  | \\/\\   /\\  ___/|  | \\/\n" +
                "/_______  /|__|  \\___  >__|  (____  /___|  /   \\__/\\  /  \\___  >___  / /_______  /\\___  >__|    \\_/  \\___  >__|   \n" +
                "        \\/           \\/           \\/     \\/         \\/       \\/    \\/          \\/     \\/                 \\/       \n");
    }

}
