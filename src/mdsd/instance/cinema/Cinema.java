package mdsd.instance.cinema;

import java.net.MalformedURLException;
import java.net.URL;

import mdsd.model.MetaModelExecutor;

public class Cinema {
	
	public static void main(String[] args) throws MalformedURLException {
		MetaModelExecutor executor = new MetaModelExecutor(new CinemaScript().getMetaModel());
		executor.handleRequest(new URL("http://localhost:5001/movies"));
	}

}
