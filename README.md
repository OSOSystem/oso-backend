# <img src="https://user-images.githubusercontent.com/19901781/46945965-8cb21700-d076-11e8-8c82-95af6b7388b3.png" width="80" height="80"> OSO Backend
[![Build Status](https://travis-ci.com/pwilken/OpenSourceTrackingSystem.svg?token=YUzfzW96QR4BesPEUUqv&branch=master)](https://travis-ci.com/pwilken/OpenSourceTrackingSystem)

Monolithic backend written in Kotlin for the OSO Project. 
It uses Gradle as dependency management and build tool.

Exposes a HTTP and TCP Port where different devices can communicate with it.
The backend then redirects emergencies to the registered Help-Providers.

# Development environment
As this is an open source project we want strongly encourage you to get involved. :relaxed:<br>
Here is a guide on how to setup a development environment primarily on a Linux/Unix system.
Windows should work with our Docker setup too though.
If something in this README is unclear, just let us know.

### Table of contents
* [Application](#application)
    * [Building](#building)
    * [Nginx](#nginx)
    * [Spring](#spring)
        * [Profiles](#profiles)
        * [Tasks](#tasks)
    * [Database](#database)
        * [PostgreSQL](#postgresql)
    * [Keycloak](#keycloak)
        * [Local](#local)
    * [Starting](#starting)
        * [Docker Compose](#docker-compose)
        * [By Hand](#by-hand)
* [Devices](#devices)
    * [Supported devices](#supported-devices)
    * [Configuration](#configuration)
* [Future](#future)

## Application
We use docker to build images and docker-compose to run the assembled images in containers in the production environment.

>To install the Docker Community Edition visit<br>
https://docs.docker.com/install/

>To install Docker Compose visit<br>
https://docs.docker.com/compose/install/

We don't want to force anyone to use docker.
Therefore docker is not necessary for this setup to work.
You can decide whatever fits you best.

### Building
The jar can be built by using the Gradle Wrapper which is included in this repository   
```
./gradlew bootJar
```

To build a docker image for the backend we provide this [Dockerfile](Dockerfile).
It will be used by ``docker-compose`` in the [Starting](#starting) section.

---

### Nginx
We use it primarily as reverse Proxy which supports HTTPS.
The Certificates are generated with Let's Encrypt.

---

### Spring
#### Profiles
When starting the application there are several application profiles which Spring can choose from. 
The [application.yml](src/main/resources/application.yml) is the default one which is always loaded into memory.
Properties defined in there can be overwritten by an another profile though.

There is an [application-local.yml](src/main/resources/application-local.yml) in this repository. 
This file is ignored by git because sensitive information should be stored there 
like your credentials for the database.

> **Note**: If you want to create a different profile, name it this way ``application-placeholder.yml``.<br>
The ``placeholder`` is then used by Spring as the profile name and can be loaded by passing this argument to the JVM <br>
``-Dspring.profile.active=placeholder``

#### Tasks
Tasks can be executed with Gradle. Spring provides several of them.

We extended the ``bootRun`` task in the ``build.gradle`` to active the ``local`` profile for Spring.
Furthermore we introduced a new task called ``bootRunDev`` which extends the ``bootRun`` task.
It gives us the possibility to attach a remote debugger to the running application. 

---

### Database
#### PostgreSQL
We use PostgreSQL as our primary data store.
The tables are generated automatically from the JPA Entities described in the source code. 

The [init.sh](postgres/init.sh) script creates the application and Keycloak database with a new owning user.
When adjusting the username and password make sure that it matches the one you stored in
[application-local.yml](src/main/resources/application-local.yml).

##### Docker
We provide a [Dockerfile](postgres/Dockerfile) which uses the ``postgres:alpine`` image as base and copies the
[init.sh](postgres/init.sh) on it. The script makes sure to initialize the database. 

A ``pgdata`` directory will be mounted to the container therefore a directory has to be created first.
```
mkdir pgdata
``` 

##### Linux/Unix
Here is an example using a Debian based distribution

Install the latest postgres version
```
sudo apt update
sudo apt install postgres
```

Make the script executable and start it
```
chmod +x postgres/init.sh
postgres/init.sh
```

Login as the new user and connect to the created database to check if everything is working as expected
```
psql -U username -h localhost -d oso
```

Alternatively you can download it from here 
https://www.postgresql.org/download/linux/

### Keycloak
We use Keycloak for identity and authorization management.
It is used for backend and frontend.

The currently support social logins are
* Github

#### Local
When starting a Keycloak instance and you want to adjust some settings you need to provide admin credentials 

**url**: http://localhost:8080/auth/admin<br>
**user**: developer<br>
**password**: ososystem


### Starting
#### Docker Compose
We have multiple compose files.
The [docker-compose.yml](docker-compose.yml) file provides the structure. 
In [docker-compose-dev.yml](docker-compose.dev.yml) you can adjust settings to your need.

As we want to be as transparent as possible we included the [docker-compose-prod.yml](docker-compose.prod.yml) file in this repository.
It is needed for our production server to work.

>**Note**: Automatic service discovery does not work for the default bridge network.
Therefore a new network bridged is created in order for the containers to communicate.

When making changes to the application the backend image has to be rebuild.
```
docker-compose build
```

To start the containers
```
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

> **Note**: The ``docker-compose.dev.yml`` is written as last because it extends base docker-compose file.
As the case may be it can even overwrite specific settings.

#### By Hand
After the installation of postgres the daemon should be running on your machine.

You can check if postgres is listening for incoming connections 
```
ss -an | grep 5432
```

After the building process the jar can be found in the ``build/libs`` folder. 
It can be started like any other java application
```
java -Dspring.profiles.active=local -jar build/libs/oso*.jar
```

# Devices
We aim to incorporate more devices as the development goes further.

## Supported devices
The currently supported devices are listed below
* [Smartphone (Android/iOS)](#smartphone-androidios)
* [Flic Button](#flic-button)
* [ReachFar Tracker RF-V18](#reachfar-tracker-rf-v18)

Any idea on which device we should look into next? Just write us.
If the requested device fits, we try to get our hands on it so we can make it work with the backend.

## Configuration
Every device has its own way to send data, it may use TCP with its own protocol or HTTP.
It may be that several configuration steps have to be done before a device can communicate with this backend. 

### Smartphone (Android/iOS)
We provide an app written in React Native so you are able to send emergencies with your Smartphone.

### Flic Button
The Flic Button works in conjunction with the Smartphone over Bluetooth.

We provide a seamless integration of the Flic Button in our Smartphone app. Thus the same app can be used.<br> 
By clicking on the Button for instance an emergency is automatically sent to the backend.

>For more information about the ReachFar Tracker visit<br>
https://flic.io/ 

### ReachFar Tracker RF-V18
Documentation is following...

>For more information about the ReachFar Tracker visit<br>
http://www.reachfargps.com/products/RF-V18.html

## Future
We try to build a platform where people in need have a tool to communicate effortlessly about needing help.
We encourage people to work with us so we can provide the best possible experience.
There is a lot more to come in the future. 
To name a few examples
* Support more devices
* Quick responses

# Contribute to the project
Everyone is welcome to contribute to this project.
Check out our issues tab and pick one issue out which seems fitting to you.
To get changes merged into the master branch you need to create a pull request.

# Stay in touch
Any questions or suggestions? Just write an E-Mail to [contact@ososystem.de](mailto:contact@ososystem.de)

If you want to get in touch with us in a more relaxed atmosphere, consider joining the discord server.<br>
:boom:[Instant Transmission](https://discord.gg/3rBUjtm):boom:

# License
This project is released under version 2.0 of the [Apache License](LICENSE.md).