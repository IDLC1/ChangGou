package com.changgou.gateway.filter;

import com.changgou.gateway.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @File: AuthorizeFilter
 * @Author: tom
 * @Create: 2020-06-11 13:57
 * @Description: 全局过滤器，实现用户权限鉴别（校验）
 **/
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";

    /**
     * 全局拦截
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取用户令牌信息
        // 1. 从 Header 中获取
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        log.info("从头部中获取");

        // 2. 从 参数 中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            log.info("从参数中获取");
        }
        // 3. 从 Cookie 中获取
        if (StringUtils.isEmpty(token)) {
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(httpCookie != null) {
                token = httpCookie.getValue();
                log.info("从Cookie中获取");
            }
        }

        // 若没有令牌，则拦截
        if (StringUtils.isEmpty(token)) {
            // 传入空数据
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 若有令牌，则校验令牌是否有效
        try {
            // 解析成功，则放行
            JwtUtil.parseJWT(token);
            return chain.filter(exchange);
        } catch (Exception e) {
            // 无效则拦截
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
