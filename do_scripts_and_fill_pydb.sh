#!/bin/bash

for entry in ispyb-ejb/db/scripts/ahead/*; do
  echo "Running $entry"
  mysql -h localhost -P 3306 -upxadmin -ppxadmin pydb < "$entry"
done