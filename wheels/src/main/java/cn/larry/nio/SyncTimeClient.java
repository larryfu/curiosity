package cn.larry.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by larryfu on 16-2-14.
 */
public class SyncTimeClient {

    private static final ExecutorService service = Executors.newFixedThreadPool(100);
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int num = 100;

        CountDownLatch latch = new CountDownLatch(num);
        for(int i =0;i<num;i++){
            service.submit(()->{
                try{
                    for(int j=0;j<1000;j++)
                        request();
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("cost : "+(end-start));
    }

    private static void request(){
        int port = 8080;
        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("QUERY TIME ORDER");
            // System.out.println(" send order to server succeed");
            String res = in.readLine();
            System.out.println( res);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
