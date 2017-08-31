#!/bin/bash

export JAVA_HOME=/usr/java/latest

resolve() {
    #
    # Check variable is set, exit with error if not, otherwise return value.
    # Params:
    # 1: string name of variable to check.
    # return: value of named variable
    # usage: NEWVAR=$(resolve VARNAME)
    #
    local CHECK=`echo $1`
    if [ -z ${!CHECK} ]; then echo "ERROR: env_var $CHECK value is required"; exit 1; fi

    echo ${!CHECK}
}

sed "s/\${DATA_DB_SERVICE_HOST}/$(resolve DATA_DB_SERVICE_HOST)/" /usr/local/src/ROOT.xml \
| sed "s/\${DATA_DB_SERVICE_PORT}/$(resolve DATA_DB_SERVICE_PORT)/" \
| sed "s/\${DATA_DB_USERNAME}/$(resolve DATA_DB_USERNAME)/" \
| sed "s/\${DATA_DB_PASSWORD}/$(resolve DATA_DB_PASSWORD)/" \
| sed "s/\${DATA_DB_NAME}/$(resolve DATA_DB_NAME)/" \
| sed "s/\${DATA_DB_MAX_TOTAL}/$(resolve DATA_DB_MAX_TOTAL)/" \
| sed "s/\${DATA_DB_MAX_IDLE}/$(resolve DATA_DB_MAX_IDLE)/" \
| sed "s/\${DATA_DB_MAX_WAIT}/$(resolve DATA_DB_MAX_WAIT)/" \
| sed "s/\${LOB_DB_SERVICE_HOST}/$(resolve LOB_DB_SERVICE_HOST)/" \
| sed "s/\${LOB_DB_SERVICE_PORT}/$(resolve LOB_DB_SERVICE_PORT)/" \
| sed "s/\${LOB_DB_USERNAME}/$(resolve LOB_DB_USERNAME)/" \
| sed "s/\${LOB_DB_PASSWORD}/$(resolve LOB_DB_PASSWORD)/" \
| sed "s/\${LOB_DB_NAME}/$(resolve LOB_DB_NAME)/" \
| sed "s/\${LOB_DB_MAX_TOTAL}/$(resolve LOB_DB_MAX_TOTAL)/" \
| sed "s/\${LOB_DB_MAX_IDLE}/$(resolve LOB_DB_MAX_IDLE)/" \
| sed "s/\${LOB_DB_MAX_WAIT}/$(resolve LOB_DB_MAX_WAIT)/" \
> /opt/webserver/conf/Catalina/localhost/ROOT.xml
