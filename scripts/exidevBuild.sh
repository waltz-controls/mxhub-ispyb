#!/bin/bash

site=DESY
environment=development
exi_project_path=/home/ispybdev/projects/EXI

cd ${exi_project_path}

# Get latest data from repository
git fetch origin
# Pull latest changes from repository
git pull

# Build EXI for production (minified js)
grunt

echo "Finish build for EXI ${site} ${environment} environment."
echo "There is no need to deploy since Apache Virtual host is already pointing to:"
echo ${exi_project_path}
