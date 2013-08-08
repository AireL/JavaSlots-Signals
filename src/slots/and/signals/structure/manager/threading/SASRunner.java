package slots.and.signals.structure.manager.threading;

import slots.and.signals.structure.Slot;

/**
 * Class to wrap invoke methods in for running in parallel. Holds priorities.
 * @author walfordt
 *
 */
public class SASRunner implements Runnable, Comparable<SASRunner> 
{

	private final Integer priorityLevel;
	
	private final Slot target;
	
	private final Object[] args;
	
	/**
	 * Constructor which sets the priority level
	 * @param priorityLevel		Priority of the request
	 * @param target			The targeted slot
	 * @param args				The passed arguments
	 */
	public SASRunner(Slot target, Object[] args)
	{
		this.target = target;
		this.priorityLevel = target.getPriority();
		this.args = args;
	}
	
	/**
	 * Method to run in the worker pool
	 */
	@Override
	public void run() {
		target.invoke(args);
	}

	/**
	 * Getter to get the priority level
	 * @return
	 */
	public Integer getPriority()
	{
		return this.priorityLevel;
	}
	
	/**
	 * Comparing target for priority queue
	 * @param compareTarget	The object to compare to
	 * @return	Returns 1 if the priority level is lower (higher priority), -1 if the priority level is higher, and 0 in all other cases
	 */
	@Override
	public int compareTo(SASRunner compareTarget) 
	{
		if (this.priorityLevel != null || compareTarget.getPriority() != null)
		{
			if (this.priorityLevel > ((SASRunner)compareTarget).getPriority())
			{
				return -1;
			}
			else if (this.priorityLevel < ((SASRunner)compareTarget).getPriority())
			{
				return 1;
			}
		}
		return 0;
	}

}
