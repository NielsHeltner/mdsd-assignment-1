package mdsd.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

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
	private HttpUtil httpUtil;
	
	public MetaModelExecutor(MetaModel metaModel) {
		this.metaModel = metaModel;
		httpUtil = new HttpUtil();
		
		System.out.println("Meta model contains: ");
		System.out.println(metaModel);
	}
	
	public void request(URL url, HttpMethod method, Map<String, Object> parameters) {
		try (Socket socket = new Socket()) {
			String location = url.getHost();
			int port = url.getPort();
			String path = url.getPath();
			
			socket.connect(new InetSocketAddress(location, port));
	
			RawHttp http = new RawHttp();
			RawHttpRequest request = http.parseRequest(method + " " + path + " HTTP/1.1\r\n" + 
							"Host: " + location + ":"+ port + "\r\n");
			if (parameters != null && !parameters.isEmpty()) {
				request = request.withBody(new StringBody(httpUtil.toString(parameters)));
			}
			request.writeTo(socket.getOutputStream());
			
			RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
			System.out.println("Response: " + httpUtil.getBody(response));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void request(URL url, HttpMethod method) {
		request(url, method, null);
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
