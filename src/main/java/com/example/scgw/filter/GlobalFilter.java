package com.example.scgw.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

	public GlobalFilter() {
		super(Config.class);
	}
	

	@Override
	public GatewayFilter apply(Config config) {
		return((exchange, chain) -> {
			log.info("GlobalFilter baseMessage >>> " + config.getBaseMessage());
			if (config.isPreLogger()) {
				log.info("GlobalFilter Start >>> " + exchange.getRequest());
			}
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("GlobalFilter End >>> " + exchange.getResponse());
				}
			}));
		});
		
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
