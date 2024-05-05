FROM tomee:10-jre17-ubuntu

# Copy the WAR file from the builder stage to the TomEE webapps directory
COPY ispyb-ear/target/ispyb.ear /usr/local/tomee/webapps/

# Copy tomee.xml configuration file
COPY configuration/tomee.xml /usr/local/tomee/conf/

# Copy jdbc driver configuration file
COPY configuration/mariadb/mariadb-java-client-3.3.3.jar /usr/local/tomee/lib/

# Expose the port TomEE is running on
EXPOSE 8080

ENV JAVA_OPTS="-Dtomee.serialization.class.blacklist=- -Dtomee.serialization.class.whitelist=*"

# Start TomEE server
CMD ["catalina.sh", "run"]