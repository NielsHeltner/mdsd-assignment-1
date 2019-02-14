package mdsd.instance.cinema;

import java.net.MalformedURLException;
import java.net.URL;

import mdsd.model.MetaModelExecutor;

public class Cinema {
	
	public static void main(String[] args) {
		MetaModelExecutor executor = new MetaModelExecutor(new CinemaScript().getMetaModel());
		try {
			executor.handleRequest(new URL("http://localhost:5001/movies"));
			executor.handleRequest(new URL("http://localhost:5000/login"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
