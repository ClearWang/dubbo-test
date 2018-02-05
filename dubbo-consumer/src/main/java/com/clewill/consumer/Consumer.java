package com.clewill.consumer;

import com.clewill.provider.service.ServiceFacade;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 消费者
 *
 * @author wangkai
 * @create 2018:02:05 11:30
 **/
public class Consumer {
  public static void main(String[] args) throws Exception{
    FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext
        ("classpath:dubbo/dubbo-consumer.xml");
    context.start();
    //context.start();
    // 阻塞 (通过从键盘输入来阻塞线程)  这样做的好处是只要从键盘随便输入一个值就可以结束阻塞过程
    System.in.read();

    // Test dubbo protocal
    ServiceFacade serviceDemo = context.getBean(ServiceFacade.class);
    System.out.println("Dubbo result: " + serviceDemo.hello());

  }
}
