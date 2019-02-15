package mdsd.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.StringBody;

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
	 * Lookup table of endpoints this microservice contains.
	 */
	private Map<String, Endpoint> endpoints;

	private ExecutorService pool;
	
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
		url = new URL("http://" + location + ":" + port); // should have a makeUrl method that verifies it's a correct url
		endpoints = new HashMap<String, Endpoint>();
		
		pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		new Thread(() -> { // to be able to have multiple servers in same JVM instance
			startService();
		}).start();
	}
	
	private void startService() {
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())) {
			while (!serverSocket.isClosed()) {
		        Socket clientSocket = serverSocket.accept();
		        System.out.println("Microservice " + name + " accepting incoming request");
				pool.execute(() -> {
					handleIncomingRequest(clientSocket);
				});
			}
			try {
				pool.shutdown();
				pool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleIncomingRequest(Socket clientSocket) {
		try {
	        RawHttp http = new RawHttp();
	        HttpUtil httpUtil = new HttpUtil();
	        RawHttpRequest request = http.parseRequest(clientSocket.getInputStream()).eagerly();

	        String path = request.getStartLine().getUri().getPath();
	        if (verifyPath(path)) {
	        	Endpoint endpoint = getEndpoint(path);
	        	System.out.println("Found endpoint " + endpoint.getPath());
	        	if (endpoint.verify(request)) {
	        		Map<String, Object> parameters = httpUtil.toMap(httpUtil.getBody(request));
	        		sendResponse(http, clientSocket, endpoint.invoke(parameters));
	        	}
	        }
	        else {
	        	System.out.println("Microservice " + name + " does not contain endpoint " + path);
	        }
	        sendResponse(http, clientSocket, "hello from " + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendResponse(RawHttp http, Socket clientSocket, Object content) throws IOException {
		http.parseResponse("HTTP/1.1 200 OK\r\n" + 
				"Content-Type: text/plain\r\n")
			.withBody(new StringBody(content.toString()))
			.writeTo(clientSocket.getOutputStream());
	}
	
	private boolean verifyPath(String path) {
		return getEndpoint(path) != null;
	}
	
	public void addEndpoint(Endpoint endpoint) {
		endpoints.put(endpoint.getPath(), endpoint);
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
	public Collection<Endpoint> getEndpoints() {
		return Collections.unmodifiableCollection(endpoints.values());
	}
	
	/**
	 * Get a specific endpoint
	 * @param path the path corresponding to the endpoint to retrieve
	 * @return
	 */
	public Endpoint getEndpoint(String path) {
		if (!path.substring(0, 1).equals("/")) {
			path = "/" + path;
		}
		return endpoints.get(path);
	}
	
	@Override
	public String toString() {
		return "Microservice{name = " + name + ", url = " + url + ", endpoints = " + endpoints.keySet() + "}";
	}

}
