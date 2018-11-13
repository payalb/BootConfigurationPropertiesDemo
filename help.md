Spring Boot @ConfigurationProperties is letting developer maps the entire file into an object easily.

1. Simple Properties file
Normally, you use the @Value annotation to inject the .properties value one by one, this is good for small and simple structure .properties files.

global.properties
email=test@mkyong.com
thread-pool=12
Copy
1.1 @Value example.

GlobalProperties.java
@Component
@PropertySource("classpath:global.properties")
public class GlobalProperties {

    @Value("${thread-pool}")
    private int threadPool;

    @Value("${email}")
    private String email;
    
    //getters and setters

}
Copy
1.2 @ConfigurationProperties example.

GlobalProperties.java
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@PropertySource("classpath:global.properties")
@ConfigurationProperties
public class GlobalProperties {

    private int threadPool;
    private String email;

    //getters and setters

}
Copy

 
2. Complex Properties file
2.1 Review a complex structure .properties file below, how you map the values via @Value annotation?

application.properties
#Logging
logging.level.org.springframework.web=ERROR
logging.level.com.mkyong=DEBUG

#Global
email=test@mkyong.com
thread-pool=10

#App
app.menus[0].title=Home
app.menus[0].name=Home
app.menus[0].path=/
app.menus[1].title=Login
app.menus[1].name=Login
app.menus[1].path=/login

app.compiler.timeout=5
app.compiler.output-folder=/temp/

app.error=/error/
Copy
or the equilvent in YAML.

application.yml
logging:
  level:
    org.springframework.web: ERROR
    com.mkyong: DEBUG
email: test@mkyong.com
thread-pool: 10
app:
  menus:
    - title: Home
      name: Home
      path: /
    - title: Login
      name: Login
      path: /login
  compiler:
    timeout: 5
    output-folder: /temp/
  error: /error/
Copy
Note
@ConfigurationProperties supports both .properties and .yml file.
2.2 @ConfigurationProperties come to rescue, create a @ConfigurationProperties bean like this :

AppProperties.java
package com.mkyong;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("app") // prefix app, find app.* values
public class AppProperties {

    private String error;
    private List<Menu> menus = new ArrayList<>();
    private Compiler compiler = new Compiler();

    public static class Menu {
        private String name;
        private String path;
        private String title;

        //getters and setters

        @Override
        public String toString() {
            return "Menu{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    public static class Compiler {
        private String timeout;
        private String outputFolder;

        //getters and setters

        @Override
        public String toString() {
            return "Compiler{" +
                    "timeout='" + timeout + '\'' +
                    ", outputFolder='" + outputFolder + '\'' +
                    '}';
        }

    }

    //getters and setters
}
Copy
GlobalProperties.java
package com.mkyong;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties // no prefix, find root level values.
public class GlobalProperties {

    private int threadPool;
    private String email;

	//getters and setters
}
Copy

 
3. Demo
3.1 Tests to make sure the .properties values are mapped to the object correctly.

WelcomeController.java
package com.mkyong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    private AppProperties app;
    private GlobalProperties global;

    @Autowired
    public void setApp(AppProperties app) {
        this.app = app;
    }

    @Autowired
    public void setGlobal(GlobalProperties global) {
        this.global = global;
    }

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {

        String appProperties = app.toString();
        String globalProperties = global.toString();

        logger.debug("Welcome {}, {}", app, global);

        model.put("message", appProperties + globalProperties);
        return "welcome";
    }

}
Copy
SpringBootWebApplication.java
package com.mkyong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

}
Copy
3.1 Start Spring Boot with mvn spring-boot:run and access the default / controller, review the console :

Console
Welcome 
	AppProperties{error='/error/', 
		menus=[
		    Menu{name='Home', path='/', title='Home'}, 
		    Menu{name='Login', path='/login', title='Login'}
		], 
		compiler=Compiler{timeout='5', outputFolder='/temp/'}}, 
		
	GlobalProperties{threadPool=10, email='test@mkyong.com'}
Copy
4. @ConfigurationProperties Validation
4.1 This @ConfigurationProperties support JSR-303 bean validation – javax.validation

GlobalProperties.java
package com.mkyong;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties
public class GlobalProperties {

    @Max(5)
    @Min(0)
    private int threadPool;

    @NotEmpty
    private String email;

    //getters and setters
}
Copy
4.2 Try start Spring Boot again, and review the console :

Console
***************************
APPLICATION FAILED TO START
***************************

Description:

Binding to target GlobalProperties{threadPool=10, email='test@mkyong.com'} failed:

    Property: target.threadPool
    Value: 10
    Reason: must be less than or equal to 5



 

 


 
