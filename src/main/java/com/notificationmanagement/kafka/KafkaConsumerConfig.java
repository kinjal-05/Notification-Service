package com.notificationmanagement.kafka;

import java.util.function.Consumer;

import com.notificationmanagement.kafkadtos.CategoryEvent;
import jdk.jfr.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notificationmanagement.kafkadtos.ProductEvent;
import com.notificationmanagement.kafkadtos.UserEmailEvent;
import com.notificationmanagement.services.NotificationService;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public Consumer<ProductEvent> productConsumer(NotificationService service) {
//		System.out.println("My Name is Kinjal Mistry");
		return event -> {
			System.out.println("My Name is Kinjal Mistry");
			service.handleProductEvent(event);
		};
	}

	@Bean
	public Consumer<UserEmailEvent> userEmailsConsumer(NotificationService service) {
		return event -> {
			service.handleUserEmails(event);
		};
	}

	@Bean
	public Consumer<CategoryEvent> categoryConsumer(NotificationService service)
	{
		return event -> {

			service.handleCategoryEvent(event);
		};
	}
}