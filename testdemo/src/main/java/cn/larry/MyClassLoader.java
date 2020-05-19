package cn.larry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

         private static final String MESSAGE_CLASS_PATH="D:"+ File.separator+"Message.class";
      /**
        * 进行指定类的加载
        * @param className 类的完整名称【包.类】
        * @return 返回一个指定类的class对象
        * @throws Exception 如果类文件不存在则无法加载
        */
              public Class<?> loadData(String className)throws Exception{
                byte[] data=this.loadClassData();//读取二进制数据文件
                if(data!=null){//读取到数据
                         return super.defineClass(className,data,0,data.length);
                    }
                return null;

            }
     private byte[] loadClassData()throws Exception{//通过文件进行类的加载
                 InputStream input=null;
                ByteArrayOutputStream bos=null;//将数据加载到内存之中
                 byte[] data=null;
                 try{
                         bos=new ByteArrayOutputStream();//实例化内存流
                         input=new FileInputStream(new File(MESSAGE_CLASS_PATH));//文件流加载
             //            byte[] data=new byte[1024];//进行读取方式①
                       //  input.transferTo(bos);//读取数据方式②
                         data= bos.toByteArray();//将所有读取到的字节数组取出
                    }catch (Exception e){

                     }finally {
                        if(input!=null){
                                input.close();
                             }
                        if(bos!=null){
                                 bos.close();
                             }
                     }
                return data;
             }

    protected Class<?> findClass(String name) {
                 try {
                         String path = "D:\\BigData\\JavaSE\\yinzhengjieData\\" + name + ".class" ;
                        FileInputStream in = new FileInputStream(path) ;
                         ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
                         byte[] buf = new byte[1024] ;
                         int len = -1 ;
                        while((len = in.read(buf)) != -1){
                                baos.write(buf , 0 , len);
                             }
                        in.close();
                         byte[] classBytes = baos.toByteArray();
                         return defineClass(classBytes , 0 , classBytes.length) ;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                return null ;
            }
}
