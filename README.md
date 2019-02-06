# <img src="https://user-images.githubusercontent.com/19901781/46945965-8cb21700-d076-11e8-8c82-95af6b7388b3.png" width="80" height="80"> OSO Backend
[![Build Status](https://travis-ci.com/OSOSystem/oso-backend.svg?branch=develop)](https://travis-ci.com/OSOSystem/oso-backend)

Monolithic backend written in Kotlin for the OSO Project. 
It uses Gradle as dependency management and build tool.

Exposes a HTTP and TCP Port where different devices can communicate with it.
The backend then redirects emergencies to the registered Help-Providers.

# Development environment
As this is an open source project we want to encourage you to get involved. :relaxed:<br>
Here is a guide on how to setup a development environment primarily on a Linux/Unix system.
Windows should work with our Docker setup too.
If something in this README is unclear, just let us know.

### Table of contents
* [Application](#application)
    * [Building](#building)
    * [Spring](#spring)
        * [Tasks](#tasks)
        * [REST Docs](#rest-docs)
    * [Database](#database)
        * [PostgreSQL](#postgresql)
    * [Keycloak](#keycloak)
        * [Local](#local)
    * [Starting](#starting)
        * [Docker Compose](#docker-compose)
        * [By Hand](#by-hand)

## Application
We use docker to build images and docker-compose to run the assembled images in containers in the production environment.

>To install the Docker Community Edition visit<br>
https://docs.docker.com/install/

>To install Docker Compose visit<br>
https://docs.docker.com/compose/install/

We don't want to force anyone to use docker.
Therefore docker is not necessary for this setup to work.
You can decide whatever fits you best.

>The image can be found under<br>
https://hub.docker.com/r/ososystem/backend

### Building
The jar can be built by using the Gradle Wrapper which is included in this repository   
```
./gradlew bootJar
```

To build a docker image for the backend we provide this [Dockerfile](Dockerfile).
It will be used by ``docker-compose`` in the [Starting](#starting) section.

---

### Spring
#### Tasks
Tasks can be executed with Gradle. Spring provides several of them.

We extended the ``bootRun`` task in the ``build.gradle`` to active the ``local`` profile for Spring.
Furthermore we introduced a new task called ``bootRunDev`` which extends the ``bootRun`` task.
It gives us the possibility to attach a remote debugger to the running application. 

#### REST Docs
When writing tests the documentation will be generated with Spring REST Docs.
There is a Gradle Task ``asciidoctor`` which assembles the generated snippets.
After the task is executed the documentation can be found under [asciidoc](src/main/asciidoc).

---

### Database
#### PostgreSQL
We use PostgreSQL as our primary data store.
The tables are generated automatically from the JPA Entities described in the source code. 

##### Docker
We provide a [Dockerfile](postgres/Dockerfile) which uses the ``postgres:alpine`` image as base and copies the
[init.sh](postgres/init.sh) on it. The script makes sure to create the needed users and databases.

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
