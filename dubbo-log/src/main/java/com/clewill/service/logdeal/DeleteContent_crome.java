package com.clewill.service.logdeal;

import java.io.*;
/**
 * Created with IntelliJ IDEA
 * Created by ClearWang on 2017/7/13.
 * 背景：阿里云日志的收集  需要统计次数并删除重复信息  日志的格式具有规律性
 * 功能：统计相同的日志出现次数，并删除重复
 */
public class DeleteContent_crome {
    /*
          统计关键词出现次数,该关键可以唯一标识这条日志
     */
    static int number;
    /*
          可以唯一标识日志的关键词
     */
    static String key = "StatisticsServiceImpl.java:2508) -- org";
    public static void main( String[] args) {
        //获取log4j日志对象
        InputStreamReader inreader = null;
        BufferedReader bread = null;
        FileWriter fwriter = null;
        BufferedWriter bwriter = null;
        try {
            //创建字符包裹输出流对象，并设定要读取的文档位置和文档编码格式
            inreader = new InputStreamReader(new FileInputStream( new File("D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log.txt") ),"utf-8");
            bread = new BufferedReader(inreader);
            //创建字符包裹输入流，并设定读入的字符存放的位置（会在该位置下自动覆盖创建文件）
            fwriter = new FileWriter(new File("D:\\wangkai\\桌面\\工作相关\\plan\\aliyun_logscollection\\temp\\20170725_log_deal.txt"));
            bwriter = new BufferedWriter(fwriter);
            String str = null;

            try {
                //设置流标记点，不超过10000000个字符时设置标记   the number of characters
                bread.mark(10000000);
                str = bread.readLine();
                if ( null !=  str ) {
                    System.out.println(str);
                    bwriter.write(str);
                    bwriter.newLine();
                    bwriter.flush();
                }
                //根据日志的特点，7行一个日志，所以刚开始先写入8行数据
                for ( int i = 0; i < 6; ++i) {
                    if (null != str) {
                        str = bread.readLine();
                        System.out.println(str);
                        bwriter.write(str);
                        bwriter.newLine();
                        bwriter.flush();
                    } else {
                        break;
                    }
                }
                //从第二个日志开始判断是否和第一个日志重复
                while (  null != str ){
                    //在每个日志开始之前的位置创建流标记
                    bread.mark(10000000);
                    for ( int i = 0; i < 7; ++i) {
                        if (null != str) {
                            str = bread.readLine();
                        }
                    }
                    //判断读取的第7行是否包涵关键字 包含即和第一条日志重复  重复的日志不写入文件（相当于删除操作）
                    if ( null != str && str.contains(key) ) {
                        number++;
//                        System.out.println("key:\""+key+"\"的次数："+number);
                        continue;
                    } else {
                        //不包含关键字,说明不重复，应回到流标记点，写入这7行数据
                        bread.reset();
                        for (int i = 0; i  < 7; ++i){
                            if( null != str ) {
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
        }finally {
            try {
                bwriter.close();
                fwriter.close();
                bread.close();
                inreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //加上第一次出现次数  number+1  记录关键字重复的总次数即日志重复的总次数
        System.out.println("\n关键词重复的次数为：" + (++number));
    }

}