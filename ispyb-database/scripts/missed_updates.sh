#!/usr/bin/env bash

# MySQL connection parameters
HOST="127.0.0.1"
PORT="3306"
USERNAME="root"
PASSWORD="root"  # Consider securing this better, e.g., using an environment variable or a secured option file
MYSQL_CONN="-h $HOST -P $PORT -u $USERNAME -p$PASSWORD"

# Get the database name
if [ -z "${DB}" ]
then
  DB="ispyb_build"
fi

# Get this scripts dir
dir=$(dirname $(realpath ${0}))

# Get the project'r root dir
project_root=$(dirname "${dir}")

# Load some function definitions in case we need them
source ${dir}/functions.sh

# Get a list of all update files
all_sql_files=`cd ${project_root}/schema/updates && ls *.sql && cd ../..`

# Get a list of all update files recorded in the SchemaStatus table
done_sql_files=`mysql ${MYSQL_CONN} -D $DB --skip-column-names --silent --raw -e "SELECT scriptName FROM SchemaStatus WHERE schemaStatus = 'DONE' ORDER BY recordTimeStamp;"`

# Make a list of all update files not recorded in the SchemaStatus table
arr=()
while read -r sql_file; do
  contains "$done_sql_files" "$sql_file"
  if [[ $? -ne 0 ]]
  then
    echo "$sql_file"
  fi
done <<< "$all_sql_files"
