#!/bin/sh
set -e
if [ -n "$DT_AGENT_PATH" ];then
    java $JAVA_OPTS -ea -agentpath:$DT_AGENT_PATH -Djava.security.egd=file:/dev/./urandom -jar build/libs/license-plate-recognition-1.0.jar
else
    java $JAVA_OPTS -ea -Djava.security.egd=file:/dev/./urandom -jar build/libs/license-plate-recognition-1.0.jar
fi
