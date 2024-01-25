#!/bin/sh

# PID will return the process number of the Wildfly server ran by user ispybdev
PID=`ps -eaf | grep jboss | grep -v grep | grep java | grep ispybdev | awk '{print $2}'`
if [ "" != "$PID" ]; then
   echo "Stopping Wildfly dev server... ($PID)"
   kill -9 $PID
   echo "Sleeping for 10 seconds..."
   sleep 10
fi
echo "Done."
