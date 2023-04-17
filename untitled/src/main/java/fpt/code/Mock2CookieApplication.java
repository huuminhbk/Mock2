package fpt.code;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Mock2CookieApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mock2CookieApplication.class, args);
		logger.debug("debug log");
		logger.error("error log");
		logger.info("info log");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	private static final Logger logger = Logger.getLogger(Mock2CookieApplication.class);

}
