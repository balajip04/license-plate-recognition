# README.md

license-plate-recognition is a Java WebApp built using Spring boot and Docker

# Configuration
Edit ~/.gradle/gradle.properties configuration
```
#Start the gradle daemon
org.gradle.daemon=true
```

# Build
license-plate-recognition uses Gradle as the built tool
`./gradlew build`

# Run
`./gradlew bootRun`
If your app is running on the command line then you can open another terminal and run
`.gradlew compileJava` 
and it will restart your application

# Test
`./gradlew clean test`

# API Docs

license-plate-recognition uses [Swagger](http://swagger.io/) for API documentation.
