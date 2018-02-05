package com.clewill.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 提供者
 *
 * @author wangkai
 * @create 2018:02:05 11:29
 **/
public class Provider {

  public static void main(String[] args) throws Exception{
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext
        ("classpath:dubbo/dubbo-provider.xml");

    synchronized (Provider.class) {
      while (true) {
        try {
          Provider.class.wait();
        } catch (InterruptedException e) {
        }
      }
    }

/*  通过下面也可以模拟阻塞过程：
    FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath:dubbo/dubbo-provider.xml");
    context.start();
    // 阻塞
    System.in.read();*/
  }
}
