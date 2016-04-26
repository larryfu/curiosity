package cn.larry.demo;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试串行读文件和并行读文件何者较快，结果表明两者速度差不多，更多情况下串行较快，接近硬盘读速度上限
 * 因此制约读写时间的式硬盘读写速度而不是cpu线程
 * 并行读如不注意控制线程数量容易oom
 * Created by larryfu on 16-4-25.
 */
public class ReadFile {

    private static final ExecutorService executor = //Executors.newCachedThreadPool();
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    private static final AtomicInteger ai = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String path = "/home/larryfu/data";
        readserial(path);
        long cost = System.currentTimeMillis() - start;
        System.out.println("read serial cost time:" + cost);
        long start1 = System.currentTimeMillis();
        //String path = "/home/larryfu/data";
        readParal(path);
        long cost1 = System.currentTimeMillis() - start1;
        System.out.println("read parallel cost time:" + cost1);
    }

    public static void readserial(String dir) throws IOException {
        readserial(new File(dir));
    }

    public static void readserial(File file) throws IOException {
        if (file.isFile()) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            //  System.out.println("read file:"+file.getCanonicalPath()+",size:"+bytes.length);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                readserial(file1);
            }
        }

    }

    public static void readParal(String dir) {
        readParal(new File(dir));
        while (ai.get() != 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    public static void readParal(File file) {
        if (file.isFile()) {
            readFile(file);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                readParal(file1);
            }
        }
    }

    private static void readFile(File file) {
        executor.execute(() -> {
            try {
                ai.incrementAndGet();
                byte[] bytes = Files.readAllBytes(file.toPath());
                //    System.out.println("read file:"+file.getCanonicalPath()+",size:"+bytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ai.decrementAndGet();
            }
        });
    }
}
