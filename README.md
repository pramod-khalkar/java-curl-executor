# java-curl-executor
curl command executor in java using annotation


How to use it? (As of now supports only basic http/https support)

Create interface each method indicates respective curl command

    public interface RemoteCalls {
        @Curl(cmd = "curl http://localhost:8080/test -H api-key:12345")
        void callOne(CurlCallBack callBack); // Asynchronous call

        @Curl(cmd = "curl http://localhost:8080/test/%s -H api-key:%s")
        void callTwo(CurlCallBack callBack,String pathParam,String key); // Asynchronous call

        @Curl(cmd = "curl http://localhost:8080/test/%s -H api-key:%s")
        CurlResponse callThree(String pathParam,String key); //Synchronous call

        @Curl(cmd = "curl http://localhost:8080/test/%s -H api-key:%s")
        CurlStreamResponse callFour(String pathParam,String key); //Synchronous call with stream as output

        @Curl(cmd = "curl http://localhost:8080/test/%s -H api-key:%s")
        void callFive(CurlCallBackStream callback,String pathParam,String key); //Synchronous call with stream as output
    }

Using CurlBuilder create instance of above interface

    public class RemoteCallConsumer {
        public void remoteCallTest() {
            RemoteCalls remoteCalls = CurlBuilder.build(RemoteCalls.class);
            remoteCalls.remoteOne(result -> { //Async call
                // call back     
            });

            remoteCalls.remoteTwo(result -> { // Async call
                // call back
            },"path_variable_value","token_value");

            remoteCalls.remoteFive(result -> { // Async call with stream as output
                // call back
            },"path_variable_value","token_value");
            
            CurlResponse response = remoteCalls.remoteThree("path_variable_value","token_value"); // Sync call

            CurlStreamResponse response = remoteCalls.remoteFour("path_variable_value","token_value"); // Sync call
        }
    }

following are sample supported command's

````
curl -XDELETE http://localhost:8080/api/test/%s
curl http://localhost:8080/api/test --max-time %d
curl http://localhost:8080/api/test --connect-timeout %d
curl http://localhost:8080/api/test
curl -X GET http://localhost:8080/api/test  -H "Authorization: Bearer %s" -H "Accept: application/json"
curl -X GET http://localhost:8080/api/test  -H'Authorization:Basic %s'
curl -X GET http://localhost:8080/api/test  --basic -u dummy_user:1234
curl -X GET http://localhost:8080/api/test  --basic --user dummy_user:1234
curl -X GET http://localhost:8080/api/test  --user dummy_user:1234
curl -X GET http://localhost:8080/api/test  -u dummy_user:1234
curl -XGET http://localhost:8080/api/test
curl -X POST http://localhost:8080/api/create --basic --user %s:%s --data '%s'
curl -XPOST http://localhost:8080/api/create -H 'Authorization:Bearer %s' --data '%s'
curl -XPUT http://localhost:8080/api/create --basic --user %s:%s -d '%s'
curl -X PUT http://localhost:8080/api/create --basic --user %s:%s --data '%s'
curl -XPUT http://localhost:8080/api/create -H 'Authorization:Bearer %s' --data '%s'
curl -XPUT --form 'file_one=@%s' http://localhost:8080/upload --form 'file_two=@%s' -H 'Authorization:Bearer %s'
````
You can include following dependency to use this

``<dependency>
  <groupId>io.github.pramod-khalkar</groupId>
  <artifactId>java-curl-executor</artifactId>
  <version>0.0.2</version>
</dependency>

implementation 'io.github.pramod-khalkar:java-curl-executor:0.0.2'

``

Feel free to contribute and expect improvements!!
