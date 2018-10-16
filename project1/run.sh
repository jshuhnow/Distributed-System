#!/bin/bash
rmiregistry &
java server.Server &
java client.Client 
