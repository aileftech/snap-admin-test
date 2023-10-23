# Spring Boot Admin Panel - Test project

This is a sample project used for testing [spring-boot-database-admin](https://github.com/aileftech/spring-boot-database-admin).
This same code with slight variations is also used to run the live-demo at http://dbadmin.ailef.tech/admin (when it's up).

This repo contains:
 * A basic Spring Boot app with entity definitions and an `import.sql` file that creates a sample database
 * End-to-end tests performed with Selenium on the Spring Boot Database Admin web interface, using the data from the sample database

The repo doesn't contain contollers, repositories, etc... because everything is generated at runtime.

## 📋 Prerequisites
Before you begin running this project, please ensure that you have the following software and tools installed:

### [Apache Maven](https://maven.apache.org/download.cgi)
Apache Maven is a powerful project management and build automation tool. It is used for building and managing Java projects, including this one.

### [Docker](https://www.docker.com)
It's not mandatory to have Docker installed, but for ease of use we provide a docker-compose file that will start a MySQL instance. If you don't want
to use Docker, you will need to customize the `application.properties` file in order to connect to your own database.


## 🛠️ Run the Program

  - Clone the Repository.

    ```sh
    git clone https://github.com/aileftech/spring-boot-database-admin.git
    ```
  - Navigate to the Repository Folder.

    ```sh
    cd spring-boot-database-admin-test
    ```
  - Create & Run Docker Container: 
        
    ```sh
    docker-compose -f docker-compose.yaml up
    ```

    If you decided not to use Docker, check the `application.properties` file and configure the datasource properly. You can also use an H2
    embedded database which doesn't require starting a server.

  - Run the following command in the terminal: 

    ```sh
    mvn spring-boot:run or 'mvn spring-boot:start' to run in background.
    ```

## 🖥️ Accessing Admin Panel

Once the application is up and running, you can access the admin interface by opening your web browser and navigating to the following URL:

[http://localhost:8080/admin](http://localhost:8080/admin)


## 📝 Tests

To execute the tests for this application, make sure the application is running, and then run the following command in your terminal:

```shell
mvn test
```

Make sure you are running on a "clean" instance of the database, i.e. you have not changed its content as it could otherwise interfere with the
correct execution of the tests.
