package cn.larry.commons.util;

/**
 * Created by Thinkpad on 2015/10/12.
 */

/**
* @author larryfu  all rights reserved
*
*/
public class HttpRequestException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public HttpRequestException() {
       super();
   }

   public HttpRequestException(String s) {
       super(s);
   }

   public HttpRequestException(String message, Throwable cause) {
       super(message, cause);
   }

   public HttpRequestException(Throwable cause) {
       super(cause);
   }
}
