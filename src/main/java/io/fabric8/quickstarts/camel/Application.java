/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.quickstarts.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends RouteBuilder {

    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // @Override
    // public void configure() throws Exception {
    //     from("timer://foo?period=5000")
    //         .setBody().constant("Hello World")
    //         .log(">>> ${body}");
    // }
    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
            restConfiguration()
                .contextPath("/hello")
                    .apiProperty("api.title", "Camel REST API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiProperty("api.specification.contentType.json", "application/vnd.oai.openapi+json;version=3.0")
                    .apiProperty("api.specification.contentType.yaml", "application/vnd.oai.openapi;version=3.0")
                    .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

            rest("/get").description("REST service")
                .get("/").description("Hello World")
                    .route().routeId("hello-world")
                    // .to("sql:select distinct description from orders?" +
                    //     "dataSource=dataSource&" +
                    //     "outputClass=io.fabric8.quickstarts.camel.Book")
                    .setBody().constant("Hello World")   
                    .log(">>> ${body}"); 
                    .endRest()
                // .get("order/{id}").description("Details of an order by id")
                //     .route().routeId("order-api")
                //     .to("sql:select * from orders where id = :#${header.id}?" +
                //         "dataSource=dataSource&outputType=SelectOne&" +
                //         "outputClass=io.fabric8.quickstarts.camel.Order");
    }
}
