package mdsd.model;

public interface Invocable<P, R> {
	
	R accept(P parameter);

}
