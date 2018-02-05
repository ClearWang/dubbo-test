package com.clewill.service.logdeal.improve;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA Created by ClearWang on 2017/7/13. 背景：阿里云日志的收集  需要统计次数并删除重复信息
 * 日志的格式具有规律性 功能：统计相同的日志出现次数，并删除重复
 */
public class DeleteContent_crome_mutiCondition {

  /*
        统计关键词出现次数,该关键可以唯一标识这条日志
   */
//    static int number;
    /*
          可以唯一标识日志的关键词
               moduleKey:识别模块
               contentKey：识别内容关键词
     */
//    static String moduleKey = "__tag__:__path__:/appLog/StatisticsDAO/error/error.log";
//    static String contentKey = "(ExceptionFilter.java:87) --  [DUBBO]";
  public static void main(String[] args) {

    BufferedReader bread2 = null;
    BufferedWriter bwriter2 = null;
    //循环处理的次数
    outer:for (int j = 0; j < 2000; ++j) {

      int number = 0;
      String moduleKey = null;// = "__tag__:__path__:/appLog/StatisticsDAO/error/error.log";
      String contentKey = null;// = "(ExceptionFilter.java:87) --  [DUBBO]";

      InputStreamReader inreader = null;
      BufferedReader bread = null;
      FileWriter fwriter = null;
      BufferedWriter bwriter = null;

      try {
        //创建字符包裹输出流对象，并设定要读取的文档位置和文档编码格式
        inreader = new InputStreamReader(new FileInputStream(new File(
            "D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log.txt")),
            "utf-8");
        bread = new BufferedReader(inreader);
        //创建字符包裹输入流，并设定读入的字符存放的位置（会在该位置下自动覆盖创建文件）
        fwriter = new FileWriter(new File(
            "D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log_deal.txt"));
        bwriter = new BufferedWriter(fwriter);

        String str = null;

        try {

          //设置流标记点，不超过10000000个字符时设置标记   the number of characters
          bread.mark(100000000);
//                str = bread.readLine();
//                if ( null !=  str ) {
//                    System.out.println(str);
//                    bwriter.write(str);
//                    bwriter.newLine();
//                    bwriter.flush();
//                }
          for(int i = 0; i < 7; ++i){
            str = bread.readLine();
          }
          if(null != str && str.contains("-- ERROR --")){
            bread.reset();
          }
          //根据日志的特点，7行一个日志，所以刚开始先写入7行数据
          for (int i = 0; i < 7; ++i) {
            if (i == 5) {
              moduleKey = str;
            }
            str = bread.readLine();
            if (null != str) {
              if(str.contains("iZm5e743ldq37aquhb3s8jZ")){
                str = str.replace("iZm5e743ldq37aquhb3s8jZ","118.190.137.182");
              }
              System.out.println(str);
              bwriter.write(str);
              bwriter.newLine();
              bwriter.flush();
            } else {
              break outer;
            }
          }

          if (null != str && str.contains("-- ERROR --")) {
            try {
              contentKey = str.substring(str.indexOf("("), str.indexOf(")") + 7);
            }catch (Exception e){
              //content:2018-01-05 02:05:51,715 -- ERROR -- com.clewill.weloop.statistics.service.impl.StatisticsServiceImpl.motionStatistics4Week(StatisticsServiceImpl.java:754) --
              //类似以上这种只有--没有详细的错误说明的  会出现数组越界异常，这时候需要使用下面
              contentKey = str.substring(str.indexOf("("), str.indexOf(")") + 3);
            }
          }
//          }else{
//            //不统计的行
//            contentKey = "0000";
//
//          }

          bwriter.write("重数复次：");
          bwriter.newLine();
          bwriter.write("责任人：");
          bwriter.newLine();
          bwriter.write("问题原因：");
          bwriter.newLine();
          bwriter.write("解决方案：");
          bwriter.newLine();
          bwriter.flush();

          //从第二个日志开始判断是否和第一个日志重复
          while (null != str) {
            //判断第五行是否包含moduleKey
            boolean flag = false;
            boolean b_content = false;
            //在每个日志开始之前的位置创建流标记
            bread.mark(10000000);
            //读取第五行的时候判断是否包含在对应的模块中
            for (int i = 0; i < 7; ++i) {
              if (null != str) {
                str = bread.readLine();
                if (i == 4) {
                  if (str.contains(moduleKey)) {
                    flag = true;
                  }
                }
              }
            }
            if(null != str && !str.contains("-- ERROR --")){
              b_content = true;
            }
            //判断读取的第7行是否包涵关键字 包含即和第一条日志重复  重复的日志不写入文件（相当于删除操作）
            if ( (null != str && str.contains(contentKey) && flag) || b_content ) {
              if(!b_content){
                number++;
              }
//              number++;
              //因为在循环的结尾的末尾，所以这一行continue可以不加
//                        continue;
//                        System.out.println("key:\""+key+"\"的次数："+number);
            } else {
              //不包含关键字,说明不重复，应回到流标记点，写入这7行数据
              bread.reset();
              for (int i = 0; i < 7; ++i) {
                if (null != str) {
                  if(str.contains("iZm5e743ldq37aquhb3s8jZ")){
                    str = str.replace("iZm5e743ldq37aquhb3s8jZ","118.190.137.182");
                  }
                  System.out.println(str);
                  str = bread.readLine();
                  bwriter.write(str);
                  bwriter.newLine();
                  bwriter.flush();
                }
              }
            }
          }

        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          bwriter.close();
          fwriter.close();
          bread.close();
          inreader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      //记录重复次数到另外一个文件
      try {
        bread2 = new BufferedReader(new InputStreamReader(
            new FileInputStream(new File(
                "D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log_deal.txt")),
            "utf-8"));
        if (j == 0) {
          bwriter2 = new BufferedWriter(new FileWriter(new File(
              "D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\news.txt")));
        }
        String s = bread2.readLine();
//                String s = null;
//                for (int i = 0; i < 10; ++i) {
//                    s = bread2.readLine();
//                    bwriter2.write(s);
//                    bwriter2.flush();
//                    bwriter2.newLine();
//                }
//                s = bread2.readLine();
//        while (null != s) {
        for (int i = 0; i < 11; ++i) {
          if ("重数复次：".equals(s)) {
            bwriter2.write("重数复次：" + (number + 1));
            bwriter2.flush();
            bwriter2.newLine();
          } else {
            if (null != s) {
              bwriter2.write(s);
              bwriter2.flush();
              bwriter2.newLine();
            }
          }
          s = bread2.readLine();
        }
        bwriter2.newLine();
      } catch (Exception e) {
        e.printStackTrace();
      }

      //将文件重新拷贝，从新操作
      try {
        BufferedReader bread3 = new BufferedReader(new InputStreamReader(
            new FileInputStream(new File(
                "D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log_deal.txt")),
            "utf-8"));
        BufferedWriter bwriter3 = new BufferedWriter(new FileWriter(new File(
            "D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log.txt")));
        bread3.mark(100000000);
        String s = bread3.readLine();
        for (int i = 0; i < 10; ++i) {
          s = bread3.readLine();
        }
        while (null != s) {
          s = bread3.readLine();
          if (null != s) {
            bwriter3.write(s);
            bwriter3.flush();
            bwriter3.newLine();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      //加上第一次出现次数  number+1  记录关键字重复的总次数即日志重复的总次数
      System.out.println("\n关键词重复的次数为：" + (++number));

    }
  }

}