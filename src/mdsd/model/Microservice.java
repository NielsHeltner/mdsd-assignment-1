package mdsd.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * Construct
	 * @param name the name of the microservice
	 * @param location the internet address
	 * @param port the port
	 * @throws MalformedURLException thrown if the given location and port 
	 * do not form a valid URL
	 */
	public Microservice(String name, String location, int port) throws MalformedURLException { // throws Exception for the fluent API to handle
		this.name = name;
		url = new URL(location + ":" + port); // should have a makeUrl method that verifies it's a correct url
		endpoints = new ArrayList<Endpoint>();
	}
	
	public void addEndpoint(Endpoint endpoint) {
		endpoints.add(endpoint);
	}
	
	public String getName() {
		return name;
	}
	
	public URL getUrl() {
		return url;
	}
	
	/**
	 * Gets a read-only collection of exposed Endpoints.
	 * @return
	 */
	public List<Endpoint> getEndpoints() {
		return Collections.unmodifiableList(endpoints);
	}

}
