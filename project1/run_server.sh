#!/bin/bash
cd build
rmiregistry &
sleep 1
java server.Server &
cd ..
