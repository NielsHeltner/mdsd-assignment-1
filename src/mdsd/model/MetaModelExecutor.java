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
	}
	
	/**
	 * Handles the URL request by routing it to the correct microservice
	 * @param request
	 */
	public void handleRequest(URL request) {
		String path = request.getPath();
		System.out.println(path);

		Microservice a = metaModel.getMicroservice(request);
		
		Endpoint b = a.getEndpoint(path);
		
		System.out.println(a != null);
	}

}
