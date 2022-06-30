# com.xcale

[Maven](http://maven.apache.org/) base project using [Spring Boot
](https://projects.spring.io/spring-boot/)

### Build-Time Dependencies

The tool chain required to build this project consists of:

- JDK 17
- Maven
- Lombok

The generated POM files implicitly invoke [JUnit5](http://junit.org) based unit tests using Maven's _surefire_ plugin.
In other words, a command line like

    mvn install

implicitly invokes unit tests. The build will break if any unit tests fail. 
Document in URL: http://localhost:8080/XCale/swagger-ui.html 
