package mdsd.executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mdsd.executors.util.HttpUtil;
import mdsd.model.Endpoint;
import mdsd.model.Microservice;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;

/**
 * Executes a microservice model as a service exposed over a socket
 * that processes HTTP messages
 * @author Niels
 *
 */
public class HttpSocketMicroserviceExecutor {

	/**
	 * The microservice this executor uses as a model.
	 */
	private Microservice model;
	
	private ExecutorService pool;

	public HttpSocketMicroserviceExecutor(Microservice model) {
		this.model = model;
		pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	public void startService() {
		new Thread(() -> { // new thread to be able to have multiple servers in same JVM instance since .accept blocks
			try (ServerSocket serverSocket = new ServerSocket(model.getUrl().getPort())) {
				while (!serverSocket.isClosed()) {
					Socket clientSocket = serverSocket.accept();
					System.out.println("Microservice " + model.getName() + " accepting incoming request");
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
		}).start();
	}

	private void handleIncomingRequest(Socket clientSocket) {
		try {
			RawHttp http = new RawHttp();
			HttpUtil httpUtil = new HttpUtil();
			RawHttpRequest request = http.parseRequest(clientSocket.getInputStream()).eagerly();

			String path = request.getStartLine().getUri().getPath();
			if (verifyPath(path)) {
				Endpoint endpoint = model.getEndpoint(path);
				System.out.println("Found endpoint " + endpoint.getPath());
				if (endpoint.verify(request)) {
					Map<String, Object> parameters = httpUtil.toMap(httpUtil.getBody(request));
					httpUtil.sendResponse(clientSocket, endpoint.invoke(parameters));
				}
			} else {
				System.out.println("Microservice " + model.getName() + " does not contain endpoint " + path);
			}
			httpUtil.sendResponse(clientSocket, "hello from " + model.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean verifyPath(String path) {
		return model.getEndpoint(path) != null;
	}

}
