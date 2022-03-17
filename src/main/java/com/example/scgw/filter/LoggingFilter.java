package com.example.scgw.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

	private final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
	
	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			
			log.info("Logging Filter baseMessage: {} ", config.getBaseMessage());
			
			if(config.isPreLogger()) {
				log.info("Logging PreFilter Start: request id -> {}", request.getId());
			}
			
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if(config.isPostLogger()) {
					log.info("Logging PostFilter End: response code -> {}", response.getStatusCode());
				}
			}));
		}, Ordered.LOWEST_PRECEDENCE); // 우선순위 지정
		
		return filter;
	}
	
	public static class Config {
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
		
		public String getBaseMessage() {
			return baseMessage;
		}
		public void setBaseMessage(String baseMessage) {
			this.baseMessage = baseMessage;
		}
		public boolean isPreLogger() {
			return preLogger;
		}
		public void setPreLogger(boolean preLogger) {
			this.preLogger = preLogger;
		}
		public boolean isPostLogger() {
			return postLogger;
		}
		public void setPostLogger(boolean postLogger) {
			this.postLogger = postLogger;
		}

	}

}
