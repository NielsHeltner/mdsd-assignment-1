package mdsd.model;

import java.net.URL;

/**
 * Meta model executor: executes metamodel
 * 
 * @author Niels
 */
public class MetaModelExecutor {
	
	private MetaModel metaModel;
	
	public MetaModelExecutor(MetaModel metaModel) {
		this.metaModel = metaModel;
		
		System.out.println("Meta model contains: ");
		System.out.println(metaModel);
	}
	
	/**
	 * Handles the URL request by routing it to the correct microservice
	 * @param request
	 */
	public void handleRequest(URL request) {
		System.out.println("");
		System.out.println("Incoming request: " + request);
		
		Microservice microservice = metaModel.getMicroservice(request.getAuthority());
		if (microservice == null) {
			System.out.println("Could not locate microservice at location: " + request.getAuthority());
		}
		else {
			System.out.println("Found microservice " + microservice.getName());
			
			Endpoint endpoint = microservice.getEndpoint(request.getPath());
			if (endpoint == null) {
				System.out.println("Microservice " + microservice.getName() + " does not contain endpoint " + request.getPath());
			}
			else {
				System.out.println("Found endpoint " + endpoint.getPath());
				System.out.println("Endpoint " + endpoint.getPath() + " expects " + endpoint.getParameters());
			}
		}
	}

}
