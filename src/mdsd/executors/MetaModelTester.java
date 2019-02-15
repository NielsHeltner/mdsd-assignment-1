package mdsd.executors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import mdsd.executors.util.HttpUtil;
import mdsd.model.HttpMethod;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

/**
 * Contains functionality for interacting with the meta model 
 * and simulating network data from a client
 * 
 * @author Niels
 */
public class MetaModelTester {

	private HttpUtil httpUtil;
	
	public MetaModelTester() {
		httpUtil = new HttpUtil();
	}
	
	/**
	 * Simulates network data for communicating with a microservice over a network
	 * @param url the url to reach the desired microservice
	 * @param method the http method to invoke on the microservice
	 * @param parameters any parameters to pass to the microservice
	 */
	public void request(URL url, HttpMethod method, Map<String, Object> parameters) {
		try (Socket socket = new Socket()) {
			String location = url.getHost();
			int port = url.getPort();
			String path = url.getPath();
			
			System.out.println("");
			System.out.println("Connecting to " + location + ":" + port + path);
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

}
