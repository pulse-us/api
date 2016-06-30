# PULSE Broker

Pulse Broker

# Installation instructions

## Clone the repository

```sh
$ git clone https://github.com/pulse/api.git
```

## Build the jar file

#### Command Line
1. Go to api/pulse/broker/
2. Run `gradlew build` on the command line

#### Eclipse 
1. Right click the broker project -> Gradle (STS) -> Refresh Dependencies
2. Right click the broker project -> Run As -> Gradle (STS) Build

## Deploy the Web App

Have the mock webapp running 

1. On the command line go to broker/build/libs/
2. Run command: `java -jar mock-0.0.1-SNAPSHOT.jar`