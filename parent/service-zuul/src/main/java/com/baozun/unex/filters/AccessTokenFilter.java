package com.baozun.unex.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Email miles02@163.com
 *
 * @author fangzhipeng
 * create 2018-07-09
 **/
@Component
public class AccessTokenFilter extends ZuulFilter {

	 private final Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

	    @Override
	    public String filterType() {
	        return "pre"; // 可以在请求被路由之前调用
	    }

	    @Override
	    public int filterOrder() {
	        return 0; // filter执行顺序，通过数字指定 ,优先级为0，数字越大，优先级越低
	    }

	    @Override
	    public boolean shouldFilter() {
	        return true;// 是否执行该过滤器，此处为true，说明需要过滤
	    }

	    @Override
	    public Object run() {
	        RequestContext ctx = RequestContext.getCurrentContext();
	        HttpServletRequest request = ctx.getRequest();

	        logger.info("--->>> TokenFilter {},{}", request.getMethod(), request.getRequestURL().toString());

	        String token = request.getParameter("token");// 获取请求的参数

	        if (StringUtils.isNotBlank(token)) {
	            ctx.setSendZuulResponse(true); //对请求进行路由
	            ctx.setResponseStatusCode(200);
	            ctx.set("isSuccess", true);
	            return null;
	        } else {
	            ctx.setSendZuulResponse(false); //不对其进行路由
	            ctx.setResponseStatusCode(400);
	            ctx.setResponseBody("{\"message\":\"token is empty\",\"code\":\"1111012\",\"success\":\"false\"}");
	            ctx.set("isSuccess", false);
	            return null;
	        }
	    }

}