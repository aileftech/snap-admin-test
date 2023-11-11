# SnapAdmin - Auth Test project

This is the auth test project containing a blueprint on how to implement a Spring Security configuration for [SnapAdmin](https://github.com/aileftech/snap-admin).

This repo contains:
 * A basic Spring Boot app with entity definitions and an `import.sql` file that creates a sample database, including users and roles
 * End-to-end tests performed with Selenium on the SnapAdmin web interface, to check that the routes are protected properly

The repo doesn't contain contollers, repositories, etc... because everything is generated at runtime.

Please refer to the [main README](https://github.com/aileftech/snap-admin-test) for instructions on how to run.