package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {

//    Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class);

    @Override
    public Object run() throws ZuulException {
        // 로그 출력: 해당 부분은 로깅을 위한 메시지를 출력하는 부분.
        //logger.info("**************** 로그 출력: ");

        log.info("**************** 로그 출력: ");

        // 현재 요청에 대한 컨텍스트 가져오기: Zuul 에서는 RequestContext 를 통해 현재 요청에 대한 정보를 얻을 수 있다.
        RequestContext ctx = RequestContext.getCurrentContext();

        // 현재 요청 객체 가져오기: 현재 요청에 대한 HttpServletRequest 객체를 가져오기.
        HttpServletRequest request = ctx.getRequest();

        // 로그에 현재 요청의 URI 출력: 현재 요청의 URI 정보를 로그에 출력.
        //logger.info("**************** " + request.getRequestURI());
        log.info("**************** " + request.getRequestURI());

        // 실행 결과가 필요 없으므로 null 반환: Zuul 필터에서는 run 메서드의 반환값이 필요하지 않습니다.
        // 필터의 목적이 주로 전처리나 후처리 작업이기 때문에 결과값은 필요 없습니다.
        return null;
    }

    @Override
    public String filterType() {
        return "pre"; // 사전 필터
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

}

/*
@Component : 일반적인 즉 역할이 정해지지 않은 객체를 Bean 에 등록할 때 사용한다.
Logger : 로거는 레벨이 존재한다. (Debug, Error 등등)
 */