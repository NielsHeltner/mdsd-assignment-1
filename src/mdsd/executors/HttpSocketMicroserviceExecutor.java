package mdsd.executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mdsd.executors.util.HttpUtil;
import mdsd.model.Endpoint;
import mdsd.model.Microservice;
import rawhttp.core.RawHttpRequest;

/**
 * Executes a microservice model as a service exposed over a socket that
 * processes HTTP messages. Expects to be subclassed, and override the
 * verify method to define how a request is verified.
 *
 * @author Niels
 *
 */
public abstract class HttpSocketMicroserviceExecutor {

    /**
     * The microservice this executor uses as a model.
     */
    private final Microservice model;

    private ServerSocket serverSocket;

    private ExecutorService pool;

    public HttpSocketMicroserviceExecutor(Microservice model) {
        this.model = model;
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void startService() {
        new Thread(() -> { // new thread to be able to have multiple servers in the same terminal since .accept blocks
            try (ServerSocket serverSocket = new ServerSocket(model.getUrl().getPort())) {
                this.serverSocket = serverSocket;
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Microservice " + model.getName() + " accepting incoming request");
                    pool.execute(() -> {
                        handleIncomingRequest(clientSocket);
                    });
                }
            }
            catch (SocketException e) {
                System.out.println(model.getName() + " socket closed.");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    pool.shutdown();
                    pool.awaitTermination(10, TimeUnit.SECONDS);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopService() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleIncomingRequest(Socket clientSocket) {
    	try {
	        HttpUtil httpUtil = new HttpUtil();
	        RawHttpRequest request = httpUtil.parseRequest(clientSocket);
    		if (verify(request)) {
    	        String path = request.getStartLine().getUri().getPath();
		        Endpoint endpoint = model.getEndpoint(path);
		
		        Map<String, Object> parameters = httpUtil.toMap(httpUtil.getBody(request));
		
		        httpUtil.sendResponse(clientSocket, endpoint.invoke(parameters));
    		}
    		httpUtil.sendResponse(clientSocket, "Illegal request");
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    protected abstract boolean verify(RawHttpRequest request);

    protected Microservice getModel() {
        return model;
    }

}
