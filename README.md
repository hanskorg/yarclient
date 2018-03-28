# yarclient
## Introduction
  Yar is a RPC framework which aims to provide a simple and easy way to do communication between PHP applications
  this project is an implement for Java.
  for more infomation see: 
  https://github.com/laruence/yar
## example
```
public class SimpleClient {
    
    private interface TestA{
        public void test(String a);
    }

    public static void main(String[] args) throws IOException {

        HttpYarClient yarClient = new HttpYarClient("http://domain/rpc/test",3000,3000,"json");
        TestA object = (TestA) yarClient.call(TestA.class);
        object.test("1");
        return ;
        }
}
```
## todo list
* Optimize Http Transport & something else
* Implement tcp transport client
