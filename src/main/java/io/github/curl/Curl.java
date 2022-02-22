package io.github.curl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 03/02/22
 * Time: 4:32 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Curl {
    /**
     * <b>
     * This is the primary annotation for assigning complete curl command
     * </b>
     *
     * <pre>{@code
     *
     *  {@code @Curl(cmd = "curl -X GET http://domain/api/test --connect-timeout 2")}
     *  CurlResponse remoteCall();
     *
     *  {@code @Curl(cmd = "curl -X GET http://domain/api/test --connect-timeout %d")}
     *  CurlResponse remoteCall(Integer timeout); //need to maintain sequence as per curl command format specifier's
     *
     *  {@code @Curl(cmd = "curl -X GET http://domain/api/test --connect-timeout 2")}
     *  void remoteCall(CurlCallBack callback);
     *
     *  {@code @Curl(cmd = "curl -X GET http://domain/api/test --connect-timeout %d")}
     *  void remoteCall(CurlCallBack callback,Integer timeout);//Callback should be first argument in case of multiple specifier's
     *
     *  {@code @Curl(cmd = "curl -X GET http://domain/api/test  -H \"Authorization: Bearer %s\" -H \"Accept: application/json\"")}
     *  void remoteCall(CurlCallBack callBack, String token);
     *
     *  {@code @Curl(cmd = "curl http://domain/download/%s")}
     *  CurlStreamResponse download(String pathVariable);
     *
     *  {@code @Curl(cmd = "curl http://domain/download/%s")}
     *  void download(CurlCallBackStream callback,String pathVariable);//In case of file operation's stream call or stream response can be used
     * }
     * </pre>
     * <b>Complete example with synchronous call</b>
     * <pre>{@code
     *
     *  public RemoteCalls{
     *          {@code @Curl(cmd = "curl -X GET http://domain/api/test --connect-timeout %d")}
     *            CurlResponse remoteCall(Integer timeout); //For streaming/file upload please use CurlStreamResponse
     *        }
     *
     *        class Demo{
     *            private static final RemoteHttpGetCalls remoteCallObject = CurlBuilder.build(RemoteCalls.class);
     *
     *            public static final void main(String args[]){
     *                CurlResponse result = remoteCallObject.remoteCall(2);
     *            }
     *        }
     *        }</pre>
     * <b>Complete example with Asynchronous call</b>
     * <pre>{@code
     *
     *  public RemoteCalls{
     *               {@code @Curl(cmd = "curl -X GET http://domain/api/test --connect-timeout %d")}
     *               void remoteCall(CurlCallBack callback,Integer timeout);
     *           }
     *
     *           class Demo{
     *               private static final RemoteHttpGetCalls remoteCallObject = CurlBuilder.build(RemoteCalls.class);
     *               public static final void main(String args[]){
     *                   remoteCallObject.remoteCall(new CurlCallBack(){ // For streaming/file please use CurlCallBackStream
     *                      {@code @Override }
     *                      void onResult(CurlResponse result){
     *                            // process response here
     *                      }
     *                   },2);
     *               }
     *           }
     * }
     * </pre>
     * cmd : as is terminal curl command goes here e.g. "curl -X GET http://domain/api/test"
     *
     * @return : curl command as string
     */
    String cmd();
}
