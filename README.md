# <img src="https://user-images.githubusercontent.com/19901781/46945965-8cb21700-d076-11e8-8c82-95af6b7388b3.png" width="80" height="80"> OSO Backend
[![Build Status](https://travis-ci.com/OSOSystem/oso-backend.svg?branch=develop)](https://travis-ci.com/OSOSystem/oso-backend)

Monolithic backend written in Kotlin for the OSO Project. 
It uses Gradle as dependency management and build tool.

Exposes a REST and TCP interface where different devices can send emergencies to it.
The backend is responsible for redirecting these emergencies to a group of people called help-providers
who then can take care of it.

What is exactly an emergency in our domain? Check out our [Glossary](https://github.com/OSOSystem/oso-docs/wiki/Domain).

# Development environment
As this is an open source project we want to encourage you to get involved. :relaxed:<br>
Here is a guide on how to setup a development environment.
If something in this README is unclear, just let us know.

### Table of contents
* [Application](#application)
    * [Building](#building)
        * [API Documentation](#api-documentation)
    * [Starting](#starting)
    * [Debugging](#debugging)
* [PostgreSQL](#postgresql)
* [Keycloak](#keycloak)

## Application
We use docker and docker-compose to build images and run these assembled images in containers.

To install the Docker Community Edition visit<br>
https://docs.docker.com/install/

To install Docker Compose visit<br>
https://docs.docker.com/compose/install/

---

### Building
Before an docker image can be assembled a jar has to exist.

The jar can be built by using the Gradle wrapper [gradlew](gradlew) which is included in this repository.   

The task is executed like this 

on Windows 
```
gradlew bootJar
```

on Linux/Unix 
```
./gradlew bootJar
```

Successful? Now build the image/s with docker-compose
```
docker-compose build
```

#### API Documentation
A documentation can be generated with Spring REST Docs based on our written tests.
The Gradle task to do this is ``asciidoctor``.
After the task is executed the documentation can be found under the path ``build/asciidoc/html5/index.html``.

### Starting
To start the application it is necessary that the Postgres service is running.
This dependency is expressed in our compose files with many other things.

We have multiple of these compose files which we are going to briefly discuss
* The [docker-compose.yml](docker-compose.yml) file provides the basic structure of every service. 
* Within the [docker-compose-dev.yml](docker-compose.dev.yml) you can adjust settings to your need as this file is not tracked by git.
It basically extends the [docker-compose.yml](docker-compose.yml).

>**Note**: Automatic service discovery does not work for the default bridge network.
Therefore a new network bridged is created in order for the containers to communicate.

To start the containers just type the following
```
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

> **Note**: The ``docker-compose.dev.yml`` is written as last because it extends the base docker-compose file.

### Debugging
A socket is listening on port ``8000`` for attaching a remote debugger to it.

When using IntelliJ for instance, you can just simply add a new remote configuration with the following settings
 
**Host**: localhost<br>
**Port**: 8000<br>
**Debugger Mode**: Attach to remote JVM

Now set a breakpoint and you are ready to go!

## PostgreSQL
We use PostgreSQL as our primary data store.

>**Note**: Currently the tables are generated automatically from the JPA Entities described in the source code. 

### Docker
We provide another [Postgres Dockerfile](postgres/Dockerfile) which uses the ``postgres:alpine`` image as base and copies the
[init.sh](postgres/init.sh) on it. The script makes sure to create the needed users and databases.

## Keycloak
We use Keycloak for user authentication and authorization.<br>
There is a [oso-realm.json](oso-realm.json) in this repository which provides a basic configuration.

When the keycloak instance is started the admin interface is accessible at ``http://localhost:8080/auth/admin``<br>

The default credentials are<br>
**user**: developer<br>
**password**: ososystem

This section will be further expanded in the future as we are still trying some things out with Keycloak.<br>
It is a really complex and powerful FOSS.

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
