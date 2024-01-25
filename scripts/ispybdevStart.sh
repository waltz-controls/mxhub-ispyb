#!/bin/sh

# Here we set the JBOSS_HOME to the wildfly dev path
# so it does not have a conflict with the one configured in /etc/profile.d/wildfly_home.sh (testing by default) 
export JBOSS_HOME=/opt/wildfly-dev
export PATH=$PATH:$JBOSS_HOME/bin

# PID will return the process number of the Wildfly server ran by user ispybdev
PID=`ps -eaf | grep jboss | grep -v grep | grep java | grep ispybdev | awk '{print $2}'`
if [ "" != "$PID" ]; then
   echo "Stopping Wildfly dev server... ($PID)"
   kill -9 $PID
   sleep 10
fi

echo "Starting Wildfly dev server..."
#/opt/wildfly-dev/bin/standalone.sh -b 0.0.0.0 -Djboss.socket.binding.port-offset=300 -Djavax.net.debug=true -Djavax.net.ssl.keyStore=/opt/jdk1.8.0_321/jre/lib/security/cacerts -Djavax.net.ssl.keyStorePassword=changeit -Djavax.net.ssl.keyStoreType=JKS -Dcom.sun.net.ssl.checkRevocation=false -Dtrust_all_cert=true &
/opt/wildfly-dev/bin/standalone.sh -b 0.0.0.0 -Djboss.socket.binding.port-offset=300 &
