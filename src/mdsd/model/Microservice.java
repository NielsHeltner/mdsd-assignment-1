package mdsd.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A microservice consists of a name, an internet location in the form of a URL,
 * and a collection of endpoints that it exposes.
 * 
 * @author Niels
 *
 */
public class Microservice {
	
	/**
	 * The name of this microservice.
	 */
	private String name;
	
	/**
	 * The URL where this microservice exposes itself.
	 */
	private URL url;
	
	/**
	 * The collection of endpoints this microservice contains.
	 */
	private List<Endpoint> endpoints;
	
	public Microservice(String name, String location, int port) throws MalformedURLException { // throws Exception for the fluent API to handle
		this.name = name;
		url = new URL(location + ":" + port); // should have a makeUrl method that verifies it's a correct url
		endpoints = new ArrayList<Endpoint>();
	}
	
	public void addEndpoint(Endpoint endpoint) {
		endpoints.add(endpoint);
	}

}
