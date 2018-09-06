package com.baozun.unex.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Component
public class ProducerFallback implements FallbackProvider {
    private final Logger logger = LoggerFactory.getLogger(FallbackProvider.class);

    //指定要处理的 service。
    @Override
    public String getRoute() {
        return "service-feign";
    }

    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
            	return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }
        
            @Override
			public InputStream getBody() throws IOException {
				JSONObject r = new JSONObject();
				r.put("state", "9999");
				r.put("msg", "The service is unavailable");
				return new ByteArrayInputStream(r.toJSONString().getBytes("UTF-8"));
			}

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
	@Override
	public ClientHttpResponse fallbackResponse(String paramString, Throwable paramThrowable) {
		if (paramThrowable != null && paramThrowable.getCause() != null) {
            String reason = paramThrowable.getCause().getMessage();
            logger.info("Excption {}",reason);
        }		
        return fallbackResponse();
	}
}