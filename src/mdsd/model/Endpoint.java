package mdsd.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Endpoint {
	
	private HttpMethod method;
	
	private Map<String, Class<?>> parameters;
	
	private Class<?> responseType = Void.class;
	
	public Endpoint() {
		parameters = new HashMap<String, Class<?>>();
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
	
	public HttpMethod getHttpMethod() {
		return method;
	}
	
	public Class<?> getResponseType() {
		return responseType;
	}
	
	public Map<String, Class<?>> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

}
