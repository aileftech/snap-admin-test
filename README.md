# Spring Boot Admin Panel - Test project

This is a sample project used for testing [spring-boot-database-admin](https://github.com/aileftech/spring-boot-database-admin).
This same code with slight variations is also used to run the live-demo at http://dbadmin.ailef.tech/admin (when it's up).

This repo contains:
 * A basic Spring Boot app with entity definitions and an `import.sql` file that creates a sample database
 * End-to-end tests performed with Selenium on the Spring Boot Database Admin web interface, using the data from the sample database

The repo doesn't contain contollers, repositories, etc... because everything is generated at runtime.

To run this project:

```
git clone https://github.com/aileftech/spring-boot-database-admin-test
cd spring-boot-database-admin-test
mvn spring-boot:run # or 'mvn spring-boot:start' to run in background
```

The web interface should then be available at [http://localhost:8080/admin](http://localhost:8080/admin).

While you have the application running, execute `mvn test` to run the tests.
