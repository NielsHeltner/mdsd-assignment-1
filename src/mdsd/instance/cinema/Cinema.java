package mdsd.instance.cinema;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import mdsd.model.MetaModelExecutor;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

public class Cinema {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		MetaModelExecutor executor = new MetaModelExecutor(new CinemaScript().getMetaModel());
		try {
			executor.handleRequest(new URL("http://localhost:5001/movies")); //legal microservice, legal endpoint, expects no parameters
			/*executor.handleRequest(new URL("http://localhost:5000/login"));//legal microservice, legal endpoint, expects two parameters
			executor.handleRequest(new URL("http://localhost:5002/qwerty"));//legal microservice, illegal endpoint
			executor.handleRequest(new URL("http://localhost:11/qwerty"));//illegal microservice
			*/
			new Thread(() -> {
				try {
					ServerSocket serverSocket = new ServerSocket(5000);
			        Socket clientSocket = serverSocket.accept();
			        
			        RawHttp http = new RawHttp();
			        RawHttpRequest request = http.parseRequest(clientSocket.getInputStream());
			        
			        System.out.println(request.getBody().get().asRawString(Charset.defaultCharset()));
			        
			        http.parseResponse("HTTP/1.1 200 OK\r\n" + 
			        					"Content-Type: text/plain\r\n").withBody(new StringBody("response")).writeTo(clientSocket.getOutputStream());;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();
			
			Socket socket = new Socket("localhost", 5000);

			RawHttp http = new RawHttp();
			RawHttpRequest request = http.parseRequest("POST / HTTP/1.1\r\n" + 
							"Host: localhost:5000/login\r\n").withBody(new StringBody("username=3"));
			request.writeTo(socket.getOutputStream());
			
			RawHttpResponse<?> response = http.parseResponse(socket.getInputStream());
			
			System.out.println(response.getBody().get().asRawString(Charset.defaultCharset()));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
