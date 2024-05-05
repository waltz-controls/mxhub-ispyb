#!/usr/bin/env bash

set -e

# MySQL connection parameters
HOST="127.0.0.1"
PORT="3306"
USERNAME="root"
PASSWORD="root"  # Consider securing this better, e.g., using an environment variable or a secured option file
MYSQL_CONN="-h $HOST -P $PORT -u $USERNAME -p$PASSWORD"

# Spinner function for visual feedback
spinner() {
    local pid=$1
    local delay=0.75
    local spinstr='|/-\\'
    while [ "$(ps a | awk '{print $1}' | grep $pid)" ]; do
        local temp=${spinstr#?}
        printf " [%c]  " "$spinstr"
        local spinstr=$temp${spinstr%"$temp"}
        sleep $delay
        printf "\b\b\b\b\b\b"
    done
    printf "    \b\b\b\b"
}

# Function to execute MySQL command with spinner
execute_sql() {
    echo "$2"
    echo "Running: $1"
    bash -c "$1" &
    pid=$!
    spinner $pid
    wait $pid
    echo " Done."
    echo
}

# Database configuration
DB="ispyb_build"

echo "Starting database setup..."

# Drop and create database
execute_sql "mysql $MYSQL_CONN -e \"DROP DATABASE IF EXISTS $DB; CREATE DATABASE $DB; SET GLOBAL log_bin_trust_function_creators = ON;\"" "Dropping and creating database $DB."

# User creation and permissions setup
execute_sql "mysql $MYSQL_CONN -e \"CREATE USER 'pxuser'@'%' IDENTIFIED BY 'pxuser'; GRANT ALL PRIVILEGES ON *.* TO 'pxuser'@'%';\"" "Creating user pxuser and setting permissions."
execute_sql "mysql $MYSQL_CONN -e \"CREATE USER 'pxadmin'@'%' IDENTIFIED BY 'pxadmin'; GRANT ALL PRIVILEGES ON *.* TO 'pxadmin'@'%'; FLUSH PRIVILEGES;\"" "Creating user pxadmin and setting permissions."

# Import schema SQL files
execute_sql "mysql $MYSQL_CONN -D $DB < ../schema/1_tables.sql" "Importing tables schema."
execute_sql "mysql $MYSQL_CONN -D $DB < ../schema/2_lookups.sql" "Importing lookups schema."
execute_sql "mysql $MYSQL_CONN -D $DB < ../schema/3_data.sql" "Importing base data."

if [ -z "${NO_USERPORTAL_DATA}" ]; then
    execute_sql "mysql $MYSQL_CONN -D $DB < ../schema/4_data_user_portal.sql" "Importing User Portal Data."
fi

# Additional imports and grants
execute_sql "mysql $MYSQL_CONN -D $DB < ../schema/5_routines.sql" "Importing routines."
execute_sql "mysql $MYSQL_CONN -D $DB < ../grants/ispyb_acquisition.sql" "Setting acquisition grants."
execute_sql "mysql $MYSQL_CONN -D $DB < ../grants/ispyb_processing.sql" "Setting processing grants."
execute_sql "mysql $MYSQL_CONN -D $DB < ../grants/ispyb_web.sql" "Setting web grants."
execute_sql "mysql $MYSQL_CONN -D $DB < ../grants/ispyb_import.sql" "Setting import grants."

# Get this scripts dir
dir=$(dirname $(realpath ${0}))

# Checking for missed updates
arr=$(${dir}/missed_updates.sh)
if [ -n "$arr" ]; then
    echo "Running missed updates:"
    for sql_file in ${arr[@]}; do
        execute_sql "mysql $MYSQL_CONN -D ${DB} < 'schema/updates/${sql_file}'" "Applying update from ${sql_file}."
    done
else
    echo "No new schema/updates/*.sql files to apply."
fi

echo "Script completed successfully!"
