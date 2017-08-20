#!/bin/sh

export JAVA_HOME=/usr/java/latest

sed -i s/\$\{DB_SERVICE_NAME\}/${DB_SERVICE_NAME}/ /opt/webserver/conf/Catalina/localhost/ROOT.xml
sed -i s/\$\{POSTGRESQL_DATABASE\}/${POSTGRESQL_DATABASE}/ /opt/webserver/conf/Catalina/localhost/ROOT.xml
sed -i s/\$\{POSTGRESQL_USER\}/${POSTGRESQL_USER}/ /opt/webserver/conf/Catalina/localhost/ROOT.xml
sed -i s/\$\{POSTGRESQL_PASSWORD\}/${POSTGRESQL_PASSWORD}/ /opt/webserver/conf/Catalina/localhost/ROOT.xml
