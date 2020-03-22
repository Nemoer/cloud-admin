#!/bin/bash

RUN_CMD="java"
RUN_CMD="$RUN_CMD $JAVA_OPTS"
RUN_CMD="$RUN_CMD -Djava.security.egd=file:/dev/./urandom"
RUN_CMD="$RUN_CMD -Duser.timezone=GMT+08"

RUN_CMD="$RUN_CMD -javaagent:/agent/skywalking-agent.jar"
RUN_CMD="$RUN_CMD -Dskywalking.agent.service_name=$SERVER_NAME"
RUN_CMD="$RUN_CMD -Dskywalking.collector.backend_service=$SKYWALKING_URL:11800"

RUN_CMD="$RUN_CMD -jar"
RUN_CMD="$RUN_CMD /app/app.jar"

RUN_CMD="$RUN_CMD --spring.profiles.active=$SPRING_PROFILES_ACTIVE"

echo $RUN_CMD
eval $RUN_CMD
