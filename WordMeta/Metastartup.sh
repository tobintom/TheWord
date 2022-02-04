#!/bin/sh
while ! nc -z Eureka-server 3000 ; do
    echo "Waiting for the Eureka Server"
    sleep 3
done
java -Xshareclasses -Xmx=100m -Xss512k -XX:MaxRam=200m -jar /opt/lib/WordMeta-service-1.0.jar
