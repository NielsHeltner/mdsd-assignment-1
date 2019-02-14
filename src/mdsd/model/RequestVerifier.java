package mdsd.model;

import rawhttp.core.RawHttpRequest;

public class RequestVerifier {
	
	private RawHttpRequest request;
	private Microservice microservice;
	
	public RequestVerifier(RawHttpRequest request, Microservice microservice) {
		this.request = request;
		this.microservice = microservice;
	}
	
	public boolean verify() {
		String path = request.getStartLine().getUri().getPath();
        Endpoint endpoint = microservice.getEndpoint(path);
        if (endpoint == null) {
        	System.out.println("Microservice " + microservice.getName() + " does not contain endpoint " + path);
        }
        else {
        	System.out.println("Found endpoint " + endpoint.getPath());
        	
        	HttpMethod method = HttpMethod.valueOf(request.getStartLine().getMethod());
        	if (endpoint.getHttpMethod() != method) {
        		System.out.println("Endpoint " + endpoint.getPath() + " supports http method " + endpoint.getHttpMethod() + " but received " + method);
        	}
        	else {
        		// parse body and check parameters
        	}
        }
        
        return true;
	}
	
	private boolean verifyPath() {
		String path = request.getStartLine().getUri().getPath();
        Endpoint endpoint = microservice.getEndpoint(path);
        return endpoint != null;
	}

}
