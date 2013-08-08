package slots.and.signals.exceptions;

/**
 * An exception to handle the registering of multiple signals.
 * @author walfordt
 *
 */
public class SignalNameInUseException extends Exception 
{
	private static final long serialVersionUID = 3992010944490914840L;

	/**
	 * Generates a new Signal Name In Use exception
	 * @param message the message to throw
	 */
	public SignalNameInUseException(String message) {
		super(message);
	}
}
