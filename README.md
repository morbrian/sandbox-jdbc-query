# Simple template for basic JDBC query capability.

## Table of Contents

* [Configuration Overview](#configuration-overview)
* [Install jdbc-query Web App](#install-jdbc-query-web-app)


## Configuration Overview

Assumes database is configured in target app server.

       
## Install jdbc-query Web App

The `jdbc-query` build produces a WAR artifact called `jdbc-query.war`. This 
must be copied to the tomcat webapps deployment directory.

1. Copy `jdbc-query.war` to `$CATALINA_HOME/webapps`

        cp ./target/jdbc-query.war $CATALINA_HOME/webapps

## Requesting Fetch

1. A very simple GET REST request will initiate a fetch.

        http://localhost:8080/jdbc-query/api/rest/database/fetch/public/tablenames


