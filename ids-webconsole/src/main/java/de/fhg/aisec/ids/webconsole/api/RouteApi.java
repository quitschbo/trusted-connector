/*-
 * ========================LICENSE_START=================================
 * ids-webconsole
 * %%
 * Copyright (C) 2018 Fraunhofer AISEC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package de.fhg.aisec.ids.webconsole.api;

import de.fhg.aisec.ids.api.Result;
import de.fhg.aisec.ids.api.policy.PAP;
import de.fhg.aisec.ids.api.router.*;
import de.fhg.aisec.ids.webconsole.WebConsoleComponent;
import de.fhg.aisec.ids.webconsole.api.data.ValidationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST API interface for "data pipes" in the connector.
 *
 * <p>This implementation uses Camel Routes as data pipes, i.e. the API methods allow inspection of
 * camel routes in different camel contexts.
 *
 * <p>The API will be available at http://localhost:8181/cxf/api/v1/routes/<method>.
 *
 * @author Julian Schuette (julian.schuette@aisec.fraunhofer.de)
 */
@Path("/routes")
@Api("Routes")
public class RouteApi {
  private static final Logger LOG = LoggerFactory.getLogger(RouteApi.class);

  /**
   * Returns map from camel context to list of camel routes.
   *
   * <p>Example:
   *
   * <p>{"camel-1":["Route(demo-route)[[From[timer://simpleTimer?period\u003d10000]] -\u003e
   * [SetBody[simple{This is a demo body!}], Log[The message contains ${body}]]]"]}
   *
   * @return The resulting route objects
   */
  @GET
  @Path("list")
  @ApiOperation(
    value = "Returns map from camel context to list of camel routes.",
    response = RouteObject.class,
    responseContainer = "List"
  )
  @Produces(MediaType.APPLICATION_JSON)
  public List<RouteObject> list() {
    return WebConsoleComponent.getRouteManagerOrThrowSUE().getRoutes();
  }

  @GET
  @Path("/get/{id}")
  @ApiOperation(value = "Get a Camel route", response = RouteObject.class)
  @Produces(MediaType.APPLICATION_JSON)
  public RouteObject get(@ApiParam(value = "Route ID") @PathParam("id") String id) {
    RouteManager rm = WebConsoleComponent.getRouteManagerOrThrowSUE();
    RouteObject oRoute = rm.getRoute(id);
    if (oRoute == null) {
      throw new NotFoundException("Route not found");
    }
    return oRoute;
  }

  @GET
  @Path("/getAsString/{id}")
  @ApiOperation(
    value = "Gets a textual representation of a Camel route.",
    response = RouteObject.class
  )
  @Produces(MediaType.TEXT_PLAIN)
  public String getAsString(@ApiParam(value = "Route ID") @PathParam("id") String id) {
    RouteManager rm = WebConsoleComponent.getRouteManagerOrThrowSUE();
    String routeAsString = rm.getRouteAsString(id);
    if (routeAsString == null) {
      throw new NotFoundException("Route not found");
    }
    return routeAsString;
  }

  /** Stop a route based on an id. */
  @GET
  @Path("/startroute/{id}")
  @ApiOperation(value = "Stops a route")
  @Produces(MediaType.APPLICATION_JSON)
  public Result startRoute(@PathParam("id") String id) {
    try {
      WebConsoleComponent.getRouteManagerOrThrowSUE().startRoute(id);
      return new Result();
    } catch (Exception e) {
      LOG.warn(e.getMessage(), e);
      return new Result(false, e.getMessage());
    }
  }

  @POST
  @Path("/save/{id}")
  @ApiOperation(value = "Save changes to a route")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Result saveRoute(@PathParam("id") String id, String routeDefinition) {
    try {
      WebConsoleComponent.getRouteManagerOrThrowSUE().saveRoute(id, routeDefinition);
      return new Result();
    } catch (Exception e) {
      LOG.warn(e.getMessage(), e);
      return new Result(false, e.getMessage());
    }
  }

  @PUT
  @Path("/add")
  @ApiOperation(value = "Adds a new route")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Result addRoute(String routeDefinition) {
    try {
      WebConsoleComponent.getRouteManagerOrThrowSUE().addRoute(routeDefinition);
      return new Result();
    } catch (Exception e) {
      LOG.warn(e.getMessage(), e);
      return new Result(false, e.getMessage());
    }
  }

  /** Stop a route based on its id. */
  @GET
  @Path("/stoproute/{id}")
  @ApiOperation(value = "Stops a route")
  @Produces(MediaType.APPLICATION_JSON)
  public Result stopRoute(@PathParam("id") String id) {
    try {
      WebConsoleComponent.getRouteManagerOrThrowSUE().stopRoute(id);
      return new Result();
    } catch (Exception e) {
      LOG.warn(e.getMessage(), e);
      return new Result(false, e.getMessage());
    }
  }

  /** Get runtime metrics of a route */
  @GET
  @ApiOperation(value = "Get runtime metrics of a route")
  @Path("/metrics/{id}")
  public RouteMetrics getMetrics(@PathParam("id") String routeId) {
    return WebConsoleComponent.getRouteManagerOrThrowSUE().getRouteMetrics().get(routeId);
  }

  /** Get aggregated runtime metrics of all routes */
  @GET
  @ApiOperation(value = "Get aggregated runtime metrics of all routes")
  @Path("/metrics")
  public RouteMetrics getMetrics() {
    return aggregateMetrics(
        WebConsoleComponent.getRouteManagerOrThrowSUE().getRouteMetrics().values());
  }

  /**
   * Aggregates metrics of several rules
   *
   * @param currentMetrics List of RouteMetrics to process
   * @return The aggregated RouteMetrics object
   */
  private RouteMetrics aggregateMetrics(Collection<RouteMetrics> currentMetrics) {
    RouteMetrics metrics = new RouteMetrics();
    metrics.setCompleted(currentMetrics.stream().mapToLong(RouteMetrics::getCompleted).sum());
    metrics.setFailed(currentMetrics.stream().mapToLong(RouteMetrics::getFailed).sum());
    metrics.setFailuresHandled(
        currentMetrics.stream().mapToLong(RouteMetrics::getFailuresHandled).sum());
    metrics.setInflight(currentMetrics.stream().mapToLong(RouteMetrics::getInflight).sum());
    metrics.setMaxProcessingTime(
        currentMetrics.stream().mapToLong(RouteMetrics::getMaxProcessingTime).max().orElse(0));
    // This is technically nonsense, as average values of average values are not really
    // the average values of the single elements, but it's the best aggregation we can get.
    metrics.setMeanProcessingTime(
        (long)
            currentMetrics
                .stream()
                .mapToLong(RouteMetrics::getMeanProcessingTime)
                .filter(i -> i >= 0)
                .average()
                .orElse(.0));
    metrics.setMinProcessingTime(
        currentMetrics.stream().mapToLong(RouteMetrics::getMinProcessingTime).min().orElse(0));
    metrics.setCompleted(currentMetrics.stream().mapToLong(RouteMetrics::getCompleted).sum());
    return metrics;
  }

  /**
   * Retrieve list of supported components (aka protocols which can be addressed by Camel)
   *
   * @return List of supported protocols
   */
  @GET
  @Path("/components")
  @Produces(MediaType.APPLICATION_JSON)
  public List<RouteComponent> getComponents() {
    RouteManager rm = WebConsoleComponent.getRouteManagerOrThrowSUE();
    return rm.listComponents();
  }

  /** Retrieve list of currently installed endpoints (aka URIs to/from which routes exist) */
  @GET
  @Path("/list_endpoints")
  public Map<String, String> listEndpoints() {
    return WebConsoleComponent.getRouteManagerOrThrowSUE().listEndpoints();
  }

  @GET
  @Path("/validate/{routeId}")
  @Produces(MediaType.APPLICATION_JSON)
  public ValidationInfo validate(@PathParam("routeId") String routeId) {
    PAP pap = WebConsoleComponent.getPolicyAdministrationPoint();
    RouteVerificationProof rvp = pap.verifyRoute(routeId);
    ValidationInfo vi = new ValidationInfo();
    vi.valid = rvp.isValid();
    if (!rvp.isValid()) {
      vi.counterExamples = rvp.getCounterExamples();
    }
    return vi;
  }

  @GET
  @Path("/prolog/{routeId}")
  @Produces(MediaType.TEXT_PLAIN)
  public String getRouteProlog(@PathParam("routeId") String routeId) {
    return WebConsoleComponent.getRouteManagerOrThrowSUE().getRouteAsProlog(routeId);
  }
}
