
<h1 align="center">
  SerbUber Web Application
  <br>
</h1>

<p align="center">
  • <a href="#-project-setup-and-commands">FTN, Novi Sad, 2022</a>
  •
</p>


## 👨‍💻 Developers
    • Zorica Vuković        SW21-2019
    • Anastasija Samčović   SW44-2019
    • Srđan Đurić           SW63-2019

## 🚀 Project setup

#### <span style="vertical-align: middle">:warning:</span> *Pre requirements:*

- Installed Node.js
- Angular version 14+
- JDK version 17
- PostgreSQL
- Python version 3 and later

#### <span style="vertical-align: middle">:floppy_disk:</span> *How to run backend:*

- Open serbUber backend app in IntelliJ IDE as Maven project
- Click on reload project(Maven will update and download all dependencies)
- Click on run button to run server
- Open locust.py script in prefered IDE and run command locust -f locustfile.py --headless -u 1 -r 1

#### <span style="vertical-align: middle">:floppy_disk:</span> *How to run frontend:*

- Open angular-app in wanted IDE (VSCode, WebStorm etc.)
- Run npm install in terminal to install all needed dependencies
- Run ng serve in terminal to start application

#### <span style="vertical-align: middle">:floppy_disk:</span> *How to run tests:*

- Selenium, unit and integration tests are located in src/test/
- Backend tests are being run by clicking right click on a test class and selecting run option
- Frontend tests are being run by running ng test in terminal

<br>

## 🤝 Useful to know:
- SerbUber application is using Google maps and many other APIs, stable internet connection is needed
- There are three types of user roles to login:
    - Admin (admin@gmail.com)
    - Regular user (ana@gmail.com)
    - Driver (mile@gmail.com)
- Password for all users is: sifra123
- PostgreSQL configuration is set in application properties

## 🏗️ Project structure:
- BACKEND
    - main
        - java
            - config (project configuration classes)
            - controller (application endpoints are located here)
            - dto (data transfer objects)
            - exception (global exception handler and all exception types)
            - model (all entity classes and enumerations)
            - repository (classes with queries for reaching data from DB)
            - request (Types of object that are received from frontend)
            - service (all bussines logic)
            - util (helping functions and constants)
        - resources
            - application.properties
            - data-postgres.sql (script for populating database)
            - staticly stored images
            - spring email configuration
    - test
        - selenium (including selenium tests)
        - server (including integration tests, unit tests and repository tests)
- FRONTEND
    - enviroments (global constants that are needed across application)
    - modules
        - admin (components used only by admins)
        - auth (security based components)
        - driver (components used only by drivers)
        - material (imports for angular material)
        - regular_user (components used only by regular users)
        - root (starting point for frontend)
        - shared (components used by multiple user roles, interceptors, pipes)
        - user (components that are generly used by all user roles)
* Note* All user based elements are consistring of pages, components and services


## 📎 Useful links:

- PostgreSQL: https://www.postgresql.org/
- Maven: https://maven.apache.org/
- Angular: https://angular.io/
- Zorica Vuković: https://www.linkedin.com/in/zorica-vukovic-937b34149/
- Anastasija Samčović: https://www.linkedin.com/in/anastasija-samcovic/
- Srđan Đurić: https://www.linkedin.com/in/srdjan-djuric/


