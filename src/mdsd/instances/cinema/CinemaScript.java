package mdsd.instances.cinema;

import static mdsd.model.HttpMethod.*;

import mdsd.dsl.internal.MicroserviceBuilder;

/**
 * An instance of the meta model, populated using 
 * the internal DSL.
 * @author Niels
 *
 */
public class CinemaScript extends MicroserviceBuilder {

	@Override
	protected void build() {
		microservice("LOGIN_SERVICE", "localhost", 5000).
	    endpoint("login").method(POST).
	        parameter("username", String.class).parameter("password", String.class).
	        response(Boolean.class). // was login successful?
	        function((p) -> {
	        	int i = 2 + 2;
	        	return "Hello from deferred invocation. received username " + p.get("username");
	        }).
	    endpoint("logout").
	        parameter("username", String.class).
	        response(Boolean.class). // was logout successful?

		microservice("MOVIES_SERVICE", "localhost", 5001).
		    endpoint("movies").method(GET).
		        response(String[].class). // collection of movie descriptions
		    endpoint("movies/:movieId").method(GET).
		        parameter("movieId", Integer.class).
		        response(String.class). // single movie description corresponding to movieId
	
		microservice("BOOKING_SERVICE", "localhost", 5002).
		    endpoint("book").method(POST).
		        parameter("username", String.class).parameter("movieId", Integer.class).
		        response(Integer.class) // bookingId
		;
	}

}
