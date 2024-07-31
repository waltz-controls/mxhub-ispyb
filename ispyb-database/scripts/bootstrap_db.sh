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
execute_sql "mysql $MYSQL_CONN -D $DB < ../schema/pydb_empty.sql" "Importing tables schema."

echo "Script completed successfully!"
