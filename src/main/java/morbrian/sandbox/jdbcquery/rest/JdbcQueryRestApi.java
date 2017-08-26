package morbrian.sandbox.jdbcquery.rest;

import morbrian.sandbox.jdbcquery.QueryController;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;


@Path("/database") @RequestScoped public class JdbcQueryRestApi {

  private static Set<String> SUPPORTED_SCHEMAS =
      Collections.unmodifiableSet(new HashSet(Arrays.asList("public")));
  private static Set<String> SUPPORTED_CATEGORIES =
      Collections.unmodifiableSet(new HashSet(Arrays.asList("sample")));
  @Inject QueryController controller;
  @Inject private Logger log;

  @GET @Path("/fetch/{schema}/{category}") @Produces(MediaType.APPLICATION_JSON)
  public String fetchDataCategory(@PathParam("schema") final String schema,
      @PathParam("category") final String category) {

    if (!SUPPORTED_SCHEMAS.contains(schema)) {
      return "Unsupported Schema: " + schema;
    }

    if (!SUPPORTED_CATEGORIES.contains(category)) {
      return "Unsupported Category: " + category;
    }

    Executors.defaultThreadFactory().newThread(new Runnable() {
      @Override public void run() {
        controller.fetchData(schema, category);
      }
    }).start();

    return "Fetching Data For: " + schema + "." + category;
  }

}

