package mdsd.model;

/**
 * Functional interface for creating an method
 * for deferred invocation.
 * @author Niels
 *
 * @param <P> the type of the parameters that will be passed to the method
 * @param <R> the return type of the method
 */
public interface Invocable<P, R> {
	
	R accept(P parameter);

}
