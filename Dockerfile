FROM jboss/wildfly:17.0.1.Final

# Copies the standalone.xml from the configuration directory into the WildFly configuration directory
COPY configuration/standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Copies the MySQL module files from the local directory to the WildFly modules directory
COPY configuration/mysql /opt/jboss/wildfly/modules/system/layers/base/com/mysql

# Copies the EAR file from the target directory to the WildFly deployments directory
COPY ispyb-ear/target/ispyb.ear /opt/jboss/wildfly/standalone/deployments/

# Expose the port WildFly will run on
EXPOSE 8080

# Set the default command for the container (starts WildFly)
ENTRYPOINT ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]