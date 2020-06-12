
package cn.larry

import java.util.concurrent.TimeUnit

import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global
import java.io.InputStream
import java.security.{KeyStore, SecureRandom}


import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import scala.concurrent.Await
import scala.concurrent.duration._

object FutureDemo{
  def main(args: Array[String]): Unit = {
    System.out.println("main thread "+Thread.currentThread())
    //  val session = socialNetwork.createSessionFor("user", credentials)
    val f: Future[List[String]] = Future {
      //session.getFriends()
      System.out.println("future thread "+Thread.currentThread())
      List("1","2")
    }
    f.onComplete{
      case Success(value) => TimeUnit.SECONDS.sleep(10)
        System.out.println("success thread:"+Thread.currentThread()+" success")
      case Failure(exception)=>System.out.println("failed thread:"+Thread.currentThread()+" failed")
    }
   Await.result(f,10.seconds)
    System.out.println("result:"+f.value.get)
    TimeUnit.SECONDS.sleep(10)
  }





}