#!/bin/bash
mkdir build
javac -d build src/client/Client.java src/schedule/CalendarEvent.java src/schedule/CalendarService.java src/server/Server.java
cd ..
