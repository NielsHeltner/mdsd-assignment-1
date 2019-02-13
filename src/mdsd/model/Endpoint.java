package mdsd.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an endpoint for a microservice. Consists of a valid HttpMethod, a collection of 
 * expected parameter names and their types, and a response type.
 * 
 * @author Niels
 *
 */
public class Endpoint {
	
	/**
	 * The name of this endpoint, which corresponds to the path in the URL.
	 */
	private String name;
	
	/**
	 * The valid HttpMethod for this endpoint.
	 */
	private HttpMethod method;
	
	/**
	 * The parameters this endpoint expects to receive. Consists of key-value pairs
	 * of the parameter's name and the parameter's type.
	 */
	private Map<String, Class<?>> parameters;
	
	/**
	 * The type of this endpoint's response.
	 * Defaults to Void.class if none is given.
	 */
	private Class<?> responseType;
	
	/**
	 * Construct
	 * @param name the name of this endpoint
	 */
	public Endpoint(String name) {
		this.name = name;
		parameters = new HashMap<String, Class<?>>();
		responseType = Void.class;
	}
	
	public void setHttpMethod(HttpMethod method) {
		this.method = method;
	}
	
	public void setResponseType(Class<?> responseType) {
		this.responseType = responseType;
	}
	
	public void addParameter(String name, Class<?> type) { // throw new ArgumentException if either is null
		parameters.put(name, type);
	}
	
	public String getName() {
		return name;
	}
	
	public HttpMethod getHttpMethod() {
		return method;
	}
	
	public Class<?> getResponseType() {
		return responseType;
	}
	
	/**
	 * Gets the read-only collection of key-value pairs that describe the expected parameters
	 * for this endpoint.
	 * @return
	 */
	public Map<String, Class<?>> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

}
