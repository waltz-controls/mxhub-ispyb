# Stage 1: Building the application
FROM maven:3.9.2-eclipse-temurin-17 as builder

# Set the working directory in the container
WORKDIR /src

# Copy the Java source code, pom.xml, and other build files into the image
COPY . /src

WORKDIR /src/dependencies

RUN mvn install -q --no-transfer-progress

WORKDIR /src

# Build the application
RUN mvn clean package -DskipTests -q --no-transfer-progress

# Stage 2: Setting up the TomEE server
FROM tomee:10-jre17-ubuntu

# Copy the WAR file from the builder stage to the TomEE webapps directory
COPY --from=builder /src/ispyb-ear/target/ispyb.ear /usr/local/tomee/webapps/

# Copy tomee.xml configuration file
COPY configuration/tomee.xml /usr/local/tomee/conf/

# Copy jdbc driver configuration file
COPY configuration/mariadb/mariadb-java-client-3.3.3.jar /usr/local/tomee/lib/

# Expose the port TomEE is running on
EXPOSE 8080

ENV JAVA_OPTS="-Dtomee.serialization.class.blacklist=- -Dtomee.serialization.class.whitelist=*"

# Start TomEE server
CMD ["catalina.sh", "run"]