# SnapAdmin - Test project

This isthe main test project used for testing [SnapAdmin](https://github.com/aileftech/snap-admin).
This same code with slight variations is also used to run the live-demo at http://dbadmin.ailef.tech/admin (when it's up).

This repo contains:
 * A basic Spring Boot app with entity definitions and an `import.sql` file that creates a sample database
 * End-to-end tests performed with Selenium on the SnapAdmin web interface, using the data from the sample database

The repo doesn't contain contollers, repositories, etc... because everything is generated at runtime.

Please refer to the [main README](https://github.com/aileftech/snap-admin-test) for instructions on how to run.