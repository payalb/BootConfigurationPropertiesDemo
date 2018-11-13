package com.java.util;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@PropertySource("classpath:myapp.properties")
@ConfigurationProperties(prefix="app", ignoreUnknownFields=false)
public class ConfigProperties {
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NestedConfigurationProperty
	@NotNull
	private Log log= new Log();
	
	static public class Log{
		@NestedConfigurationProperty
		private Debug debug= new Debug();
		
		
		static public class Debug{
			@NotNull
			private String level;

			public String getLevel() {
				return level;
			}

			public void setLevel(String level) {
				this.level = level;
			}
		}


		public Debug getDebug() {
			return debug;
		}


		public void setDebug(Debug debug) {
			this.debug = debug;
		}
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Log getLog() {
		return log;
	}
	public void setLog(Log log) {
		this.log = log;
	}
}
