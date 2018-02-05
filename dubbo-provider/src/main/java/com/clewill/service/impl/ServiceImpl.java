package com.clewill.service.impl;

import com.clewill.provider.service.ServiceFacade;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * 测试dubbo rest
 *
 * @author wangkai
 * @create 2018:02:05 11:12
 **/
@Path("demo")
public class ServiceImpl implements ServiceFacade {


  @GET
  @Path("hello")
  public String hello() {
    return "hi clewill";
  }
}
