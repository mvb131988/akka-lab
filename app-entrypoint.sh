#!/bin/bash

nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:${DEBUG_PORT} \
 -DCLUSTERHOST=${CLUSTER_HOST} \
 -DCLUSTERPORT=${CLUSTER_PORT} \
 /tmp/host.jar > /tmp/log.txt 2>&1 &

while true; do sleep 1000; done