# pulse-api

The PULSE API

# Installation instructions

## Clone the repository

```sh
$ git clone https://github.com/pulse-admin/api.git
```

## Configure

```sh
$ cd pulse/broker/src/main/resources/META-INF
$ cp persistence.template.xml persistence.xml
$ nano persistence.xml
$ # Set up database un/pw information, as well as access URL
$ cd ../
$ cp application.properties.template application.properties
$ nano application.properties
$ # Set up database un/pw, server port, spring security password, etc.
```

## Build the jar file

### Command Line

```sh
$ cd pulse/broker
$ gradlew build
```

### Eclipse
1. Right click the mock project -> Gradle (STS) -> Refresh Dependencies
2. Right click the mock project -> Run As -> Gradle (STS) Build

## Deploy the Broker

```sh
$ cd pulse/broker
$ gradlew bootRun
```
