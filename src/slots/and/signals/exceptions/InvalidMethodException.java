package slots.and.signals.exceptions;

/**
 * An exception to handle a slot defining an invalid method.
 * @author walfordt
 *
 */
public class InvalidMethodException extends Exception {

	private static final long serialVersionUID = 2159720051947126865L;

	/**
	 * Generates a new exception
	 * @param message	The message to pass back with the exception
	 */
	public InvalidMethodException(String message) 
	{
		super(message);
	}

}
