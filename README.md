# java-curl-executor
curl command executor in java using annotation


How to use it? (As of now supports only basic commands)

Create interface each method indicates respective curl command

    public interface RemoteCalls {
        @Curl(cmd = "curl http://localhost:8080/test -H api-key:12345")
        String callOne(CurlCallBack callBack);
    }

And use as below in consumer class

    public class RemoteCallConsumer {
        public void remoteCallTest() {
            CurlBuilder.build(RemoteCalls.class).callOne(new CurlCallBack() {
                @Override
                public void onSuccess(int responseCode, String successResponse) {
            
                }
                @Override
                public void onError(int responseCode, String errorResponse) {
                
                }
            });
        }
    }