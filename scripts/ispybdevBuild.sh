#!/bin/bash

site=DESY
environment=development
maven_settings_file=/home/ispybdev/scripts/settings.xml
ispyb_java_project_path=/home/ispybdev/projects/ISPyB

# This has to be done only one time
# mvn -s ${maven_settings_file} initialize 

cd ${ispyb_java_project_path}

# Get latest data from repository
git fetch origin
# Pull latest changes from repository
git pull

# Build the ISPyB Java project
mvn -s ${maven_settings_file} -Dispyb.site=${site} -Dispyb.env=${environment} clean install

echo "Finish build for ISPyB Java ${site} ${environment} environment."
echo "Deployable ear file available at:"
echo "${ispyb_java_project_path}/ispyb-ear/target/ispyb.ear"
