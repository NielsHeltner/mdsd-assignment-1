package mdsd.instance.cinema;

import mdsd.dsl.MicroserviceBuilder;
import static mdsd.model.HttpMethod.*;

public class CinemaScript extends MicroserviceBuilder {

	@Override
	protected void build() {
		microservice("LOGIN_SERVICE", "localhost", 5000).
	    endpoint("login").method(POST).
	        parameter("username", String.class).parameter("password", String.class).
	        response(Boolean.class). // was login successful?
	        function((p) -> {
	        	System.out.println("Hello from stored method / deferred invocation");
	        	return "it worked!";
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
