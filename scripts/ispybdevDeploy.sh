#!/bin/bash

wildflydev_deployment_path=/opt/wildfly-dev/standalone/deployments
ispyb_java_project_path=/home/ispybdev/projects/ISPyB

# When a new ear file is copied to Wildfly it automatilly re-deploy it
cp ${ispyb_java_project_path}/ispyb-ear/target/ispyb.ear ${wildflydev_deployment_path}

echo "EAR file deployed to ${wildflydev_deployment_path}:"
echo "${ispyb_java_project_path}/ispyb-ear/target/ispyb.ear"
echo "No need to restart Wildfly server since the new ear file is automatically re-deployed (unless server is currently stopped)"
