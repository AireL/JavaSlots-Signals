package slots.and.signals.structure;

/**
 * Interface to allow easier methods of registering single slots
 * @author walfordt
 */
public interface SlotMethod 
{
	/**
	 * Called by the SASHandler when it is triggered, this is the method that will be run.
	 * 
	 * @param varargs	The arguments passed by the signal
	 * @return	The return value of the method
	 */
	public Object invoke(Object[] varargs);
}
