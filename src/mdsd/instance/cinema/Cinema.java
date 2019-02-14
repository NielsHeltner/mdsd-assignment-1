package mdsd.instance.cinema;

import java.net.MalformedURLException;
import java.net.URL;

import mdsd.model.MetaModelExecutor;

public class Cinema {
	
	public static void main(String[] args) {
		MetaModelExecutor executor = new MetaModelExecutor(new CinemaScript().getMetaModel());
		try {
			executor.handleRequest(new URL("http://localhost:5001/movies")); //legal microservice, legal endpoint, expects no parameters
			executor.handleRequest(new URL("http://localhost:5000/login"));//legal microservice, legal endpoint, expects two parameters
			executor.handleRequest(new URL("http://localhost:5002/qwerty"));//legal microservice, illegal endpoint
			executor.handleRequest(new URL("http://localhost:11/qwerty"));//illegal microservice
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
