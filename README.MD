Concourse CCTray
=================

pre-requisites
--------------

* Maven
* Java
* Concourse (can be on a different server)

Installation
------------

To create the jar use `mvn clean install` 

Running for a single Team
-------------------------

To run for a single team usage, modify application.properties and use the above installation steps.

OR

run the application with the following command:

Windows (CMD): 
```cmd
java -jar ConcourseCCtray.java ^
-Dconccourse.username={username} ^
-Dconcourse.password={password} ^
-Dconcourse.team={team} ^
-Dconcourse.url={url}
```

Bash
```bash
java -jar ConcourseCCtray.java \
-Dconccourse.username={username} \
-Dconcourse.password={password} \
-Dconcourse.team={team} \
-Dconcourse.url={url}
```

Running for multiple Teams 
--------------------------

To run for a multiple team usage.

run the application with the following command:

`java -jar ConcourseCCtray.java`

Getting the CCtray
------------------

### Using Curl ###

`curl --user {username}:{password} http://localhost:8080/cctray.xml?team={team}&url={concourseURL}`

If username and password, team, or url are not passed it will use that defined in application properties.

### Using nevergreen ###

Point nevergreen to `http://localhost:8080/cctray.xml?team={team}&url={concourseURL}` and set the username and 
password to the same as your concourse username and password.

If username and password, team, or url are not passed it will use that defined in application properties.