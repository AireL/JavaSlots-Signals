package slots.and.signals.structure.manager.support;

import java.util.List;

import slots.and.signals.structure.Slot;

/**
 * Generates a signal structure object for holding by the Handler
 * @author walfordt
 *
 */
public class SignalStructure {

	private final Class<?>[] invokerParameters;
	
	private List<Slot> registeredListeners;
	
	private final Class<?> returnParam;
	
	/**
	 * Generates a new signal structure type 
	 * 
	 * @param params		The parameters of the signal
	 * @param arrayList		The list of attached slots
	 * @param returnParam	The return type
	 */
	public SignalStructure(Class<?>[] params, List<Slot> arrayList, Class<?> returnParam) 
	{
		this.invokerParameters = params;
		this.registeredListeners = arrayList;
		this.returnParam = returnParam;
	}

	/**
	 * Getter to get the registered slots
	 * @return
	 */
	public List<Slot> getRegisteredSlots() 
	{
		return this.registeredListeners;
	}
	
	/**
	 * Getter to get the invoked paramaters
	 * @return
	 */
	public Class<?>[] getInvokerParameters()
	{
		return this.invokerParameters;
	}
	
	/**
	 * Getter to get the return object class
	 * @return
	 */
	public Class<?> getReturnType()
	{
		return this.returnParam;
	}

}
