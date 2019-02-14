package mdsd.model;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

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
	
	public void request(URL url, HttpMethod method) {
		try {
			String location = url.getHost();
			int port = url.getPort();
			String path = url.getPath();
			
			Socket socket = new Socket(location, port);
	
			RawHttp http = new RawHttp();
			RawHttpRequest request = http.parseRequest(method + " " + path + " HTTP/1.1\r\n" + 
							"Host: " + location + ":"+ port + "\r\n").withBody(new StringBody("username=3"));
			request.writeTo(socket.getOutputStream());
			
			RawHttpResponse<?> response = http.parseResponse(socket.getInputStream());
			
			System.out.println(response.getBody().get().asRawString(Charset.defaultCharset()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
