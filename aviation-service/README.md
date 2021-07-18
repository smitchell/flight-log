# RUNNING THE AVIATION SERVICE

This service needs to be run using Spring Boot. 

The application depends on an external MySQL database provided by the user. Your database credentials can be
set in an system environment variable. Run the server from a separate terminal outside of the IDE, otherwese 
the SPRING_APPLICATION_JSON variable will break the Junit tests that use H2.

Run the application as shown below, substituting your MySQL information.
```
export SPRING_APPLICATION_JSON='{"spring": {"datasource": {"url":"jdbc:mysql://[YOUR DB]/[YOUR SCHEMA]", "username":[YOUR USER],"password":[YOUR PW]}}}'
mvn spring-boot:run
```

