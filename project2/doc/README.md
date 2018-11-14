## Distributed Computing - Assignment 1
Jaeseok Huh, 2015005241, Department of Computer Science and Engineering  
jaeseok@hanyang.ac.kr


### Environment
Ubuntu 14.04 LTS
JDK,JRE-7

### How to compile

```
$ ./build.sh
```

### How to run
```
$ ./run_rmiregistry.sh
```
Wait for a few seconds. Then, start new session.

```
$ ./run_server.sh
```

### Design Structure
```
/
	doc/
		README.md  # report
	run_client.sh
	run_rmiregistry.sh
	run-server.sh
	src/
		client/
			Client.java
		schedule/
			CalendarEvent.java   # Class for storing event
			CalenaarService.java # Interface for both client and server
		server/
			Server.java
```

*Client* serves as an user interface for accessing data in *Server*.  
Since the range of *GregorianCalendar* is 0-11, *Client* makes the month of input to fit in by reducing it by 1. *Client* tries to locate a registry and look up for the name of pre-determined interface, "CalendarService".

*schedule/CalendarEvent* provides a class, which serves as an instance for events. It contains the description(*desc*) and *id* (auto-increment). Also, a function to check whether its event overlaps with other event or not is implemented.

*schedule/CalendarSerivce* serves as an interface, which is implemented by *server*. *Client* looks up for it through rmiregistry and *Server* binds to it. Since relations among them are susceptible to exceptions (due to network instability, etc), in those cases, *RemoteException* should be thrown.

*server/Server*	initializes variables for storing data, and exports and bind the implementation of the common interface shared with client. Please note that the result of *retrieveSchedule* consist of *CalendarEvent*.

