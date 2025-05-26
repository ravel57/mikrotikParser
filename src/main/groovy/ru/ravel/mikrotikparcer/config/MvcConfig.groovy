package ru.ravel.mikrotikparcer.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class MvcConfig implements WebMvcConfigurer {

	@Override
	void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index")
	}


	@Override
	void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/static/js/")
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/")
		registry.addResourceHandler("/favicon.ico")
				.addResourceLocations("classpath:/static/favicon.ico")
	}
}