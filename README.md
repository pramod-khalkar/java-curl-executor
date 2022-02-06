# java-curl-executor
curl command executor in java using annotation


How to use it? (As of now supports only basic commands)

Create interface each method indicates respective curl command

    public interface RemoteCalls {
        @Curl(cmd = "curl http://localhost:8080/test -H api-key:12345")
        String callOne(CurlCallBack callBack);

        @Curl(cmd = "curl http://localhost:8080/test/%s -H api-key:%s")
        String callTwo(CurlCallBack callBack,String pathParam,String key);
    }

And use as below in consumer class

    public class RemoteCallConsumer {
        public void remoteCallTest() {
            RemoteCalls remoteCalls = CurlBuilder.build(RemoteCalls.class);

            remoteCalls.callOne(new CurlCallBack() {
                @Override
                public void onSuccess(int responseCode, String successResponse) {
            
                }
                @Override
                public void onError(int responseCode, String errorResponse) {
                
                }
            });
            
            remoteCalls.callTwo(new CurlCallBack() {
                @Override
                public void onSuccess(int responseCode, String successResponse) {
            
                }
                @Override
                public void onError(int responseCode, String errorResponse) {
                
                }
            },"pathParam","12345");
        }
    }