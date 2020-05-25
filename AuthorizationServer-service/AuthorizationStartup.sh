#!/bin/sh
while ! nc -z Eureka-server 3000 ; do
    echo "Waiting for the Eureka Server"
    sleep 3
done
java -jar /opt/lib/AuthorizationServer-service-1.0.jar
