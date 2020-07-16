## Spring Boot Coding Dojo

Welcome to the Spring Boot Coding Dojo implementation of Yuksel Ozalpdemir!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The implementation had quite a few problems making it a non-production ready product.

### The task

The task was to make it production-grade. Refactoring and other changes where allowed to achieve the goal.

### The solution

The main goal of the task was to make the application production-grade. It is a very simple application with only one feature, and it should be kept like this without knowing new requirements.
I could have added swagger support for user testing and documenting of the api endpoints, but this would take extra time and it is also not required for a production-grade application. The actual purpose of the application its end users are currently unknown.

**This is what I did to make the application production-grade:**

- Restructured the code (moved to packages)
- Code cleanup (refactoring and fixes)
- Constants and database configuration moved to yaml configuration file
- Exception handling added
- Application Logging added
- Unit tests added for service code
- Integration tests added for the controller endpoints

**There are still some improvements possible:**

- All of the changes above, could be made better (more logging, more testing), but again, without knowing the actual purpose of the application, it is not wise to spent a lot of time.
- The OpenWeather API token could be moved from the application.yml to external (by passing the parameter from commandline while starting the app)
- The entity data or field names could be protected by not exposing those in the api response. This can be done by using DTO’s.
- More JavaDoc could be added (this is not something specific for production grade applications but a general task)
- Development configuration and production configuration could be more separated (dev should have its own database configuration)
- More unit tests could be added (for the controllers)
- Add SSL support
- Add Swagger support

### How to use the application

The application can be started with the command:

> mvn spring-boot:run
> On production:
> mvn spring-boot:run -Dspring.profiles.active=prod

Example Api Call:

> http://{host}:{port}/weather?city=London

### Footnote

It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
