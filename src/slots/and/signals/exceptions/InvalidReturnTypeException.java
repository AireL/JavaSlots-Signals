package slots.and.signals.exceptions;

/**
 * An exception to handle an attempt to return an invalid type.
 * @author walfordt
 */
public class InvalidReturnTypeException extends Exception {

	private static final long serialVersionUID = 9164441002446319485L;

	/**
	 * Generates a new exception
	 * @param message	The message to pass back with the exception
	 */
	public InvalidReturnTypeException(String message) 
	{
		super(message);
	}

}
