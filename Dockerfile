# Start with a base image containing Java runtime 21
FROM eclipse-temurin:21-jre

# Install necessary packages for downloading and managing Tomcat
RUN apt-get update && apt-get install -y wget tar

# Define environment variables for Tomcat version and installation directory
ENV TOMCAT_VERSION 10.0.0-M1
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Download and install Tomcat
RUN wget https://downloads.apache.org/tomee/tomee-10.0.0-M1/apache-tomee-$TOMCAT_VERSION-webprofile.tar.gz -O /tmp/tomcat.tar.gz \
    && tar -xvf /tmp/tomcat.tar.gz -C /usr/local \
    && mv /usr/local/apache-tomee-webprofile-$TOMCAT_VERSION $CATALINA_HOME \
    && rm /tmp/tomcat.tar.gz \
    && chmod +x $CATALINA_HOME/bin/catalina.sh

# Copy the EAR file from the builder stage to the Tomcat webapps directory
COPY ispyb-ear/target/ispyb.ear $CATALINA_HOME/webapps/

# Copy tomee.xml configuration file to the Tomcat conf directory
COPY configuration/tomee.xml $CATALINA_HOME/conf/

# Copy JDBC driver to the Tomcat lib directory
COPY configuration/mariadb/mariadb-java-client-3.3.3.jar $CATALINA_HOME/lib/

# Expose the port Tomcat is running on
EXPOSE 8080

# Environment variable for Java options, include serialization configs
ENV JAVA_OPTS="-Dtomee.serialization.class.blacklist=- -Dtomee.serialization.class.whitelist=*"

# Start Tomcat server
CMD ["catalina.sh", "run"]
