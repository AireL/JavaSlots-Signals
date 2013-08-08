package slots.and.signals.structure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import slots.and.signals.exceptions.InvalidMethodException;
import slots.and.signals.exceptions.InvalidReturnTypeException;
import slots.and.signals.structure.manager.SASHandler;

/**
 * A class to handle receiving of method processing requests from outside the project. This class registers with a listener object, that then calls the specified
 * method whenever the signal is broadcast. Note, before nulling this class, you MUST unregister the slot, otherwise this class will be held in memory and still be
 * invoked.
 * 
 * If you are having trouble with the slot method failing to attach to a signal as the signal is not registered before the slot, enclose the signal object in static 
 * brackets outside the class runtime methods, to ensure it is generated at runtime. As signal registering depends solely on the SASHandler static methods, this will
 * flow, and ensure you don't have any issues.
 * 
 * @author walfordt
 */
public class Slot {
	
	protected final SlotMethod slotMethod;
	
	protected final Object targetObject;
	
	protected final Method targetMethod;
	
	protected final String targetString;
	
	protected Integer priority = null;
	
	protected final Class<?> returnType;
	
	/**
	 * Generates a new Slot object
	 * 
	 * @param targetObject	The object which contains the entered method
	 * @param targetMethod	The method name which should be called on triggering
	 * @param targetString	The target name of the signal to align to 
	 * @param parameters	The class types of the parameters taken by the target method
	 * @param returnType	The class of the return type returned by the method
	 * @throws InvalidMethodException 		Thrown if the method cannot be found
	 * @throws InvalidReturnTypeException 	Thrown if the return type does not match the signal's return type
	 * @throws IllegalArgumentException 	Thrown if the arguments do not match the signal's arguments
	 */
	public Slot (Object targetObject, String targetMethod, String targetString, Class<?>[] parameters, Class<?> returnType) throws InvalidMethodException, IllegalArgumentException, InvalidReturnTypeException
	{
		try {
			this.targetMethod = targetObject.getClass().getMethod(targetMethod, parameters);
			this.slotMethod = null;
			this.targetObject = targetObject;
			this.targetString = targetString;
			this.returnType = returnType;

		} catch (SecurityException e) {
			throw new InvalidMethodException("Error: This thread does not have access to this method");
		} catch (NoSuchMethodException e) {
			throw new InvalidMethodException("Error: The specified method of name: "
				+ targetMethod + " with the specified parameters " +
				"does not exist");
		}
		SASHandler.registerSlot(this, this.targetString, parameters);
	}
	
	/**
	 * Generates a new Slot object
	 * 
	 * @param st				The class that implements the SlotMethod interface. Note that this attaches to one method, and means you cannot attach another signal using a different method
	 * @param targetString		The name of the signal to bind to
	 * @param parameters		The class types of the parameters the method takes
	 * @param returnType		The class of the return type the method returns
	 * @throws IllegalArgumentException 	Thrown if the arguments do not match the signal's arguments
	 * @throws InvalidReturnTypeException 	Thrown if the return type does not match the signal's return type
	 */
	public Slot (SlotMethod st, String targetString, Class<?>[] parameters, Class<?> returnType) throws IllegalArgumentException, InvalidReturnTypeException
	{
		this.slotMethod = st;
		this.targetObject = null;
		this.targetMethod = null;
		this.targetString = targetString;
		this.returnType = returnType;
		SASHandler.registerSlot(this, this.targetString, parameters);
	}
	
	/**
	 * 
	 * Generates a new Slot object
	 * 
	 * @param st				The class that implements the SlotMethod interface. Note that this attaches to one method, and means you cannot attach another signal using a different method
	 * @param targetString		The name of the signal to bind to
	 * @param parameters		The class types of the parameters the method takes
	 * @param returnType		The class of the return type the method returns
	 * @throws IllegalArgumentException 	Thrown if the arguments do not match the signal's arguments
	 * @throws InvalidReturnTypeException 	Thrown if the return type does not match the signal's return type
	 */
	public Slot (SlotMethod st, String targetString, Object[] parameters, Object returnType) throws IllegalArgumentException, InvalidReturnTypeException
	{
		this.slotMethod = st;
		this.targetObject = null;
		this.targetMethod = null;
		this.targetString = targetString;
		Class<?>[] params = new Class<?>[parameters.length];
		for (int i = 0; i < params.length; i++)
		{
			params[i] = parameters[i].getClass();
		}
		this.returnType = returnType.getClass();
		SASHandler.registerSlot(this, this.targetString, params);		
	}
	
	/**
	 * Generates a new Slot object
	 * 
	 * @param targetObject	The object which contains the entered method
	 * @param targetMethod	The method name which should be called on triggering
	 * @param targetString	The target name of the signal to align to 
	 * @param parameters	The class types of the parameters taken by the target method
	 * @param returnType	The class of the return type returned by the method
	 * @throws InvalidMethodException 		Thrown if the method cannot be found
	 * @throws InvalidReturnTypeException 	Thrown if the return type does not match the signal's return type
	 * @throws IllegalArgumentException 	Thrown if the arguments do not match the signal's arguments
	 */
	public Slot (Object targetObject, String targetMethod, String targetString, Class<?>[] parameters, int priority) throws InvalidMethodException, IllegalArgumentException, InvalidReturnTypeException
	{
		try {
			this.targetMethod = targetObject.getClass().getMethod(targetMethod, parameters);
			this.slotMethod = null;
			this.targetObject = targetObject;
			this.targetString = targetString;
			this.returnType = null;
			this.priority = priority;

		} catch (SecurityException e) {
			throw new InvalidMethodException("Error: This thread does not have access to this method");
		} catch (NoSuchMethodException e) {
			throw new InvalidMethodException("Error: The specified method of name: "
				+ targetMethod + " with the specified parameters " +
				"does not exist");
		}
		SASHandler.registerSlot(this, this.targetString, parameters);
	}
	
	/**
	 * Generates a new Slot object
	 * 
	 * @param st				The class that implements the SlotMethod interface. Note that this attaches to one method, and means you cannot attach another signal using a different method
	 * @param targetString		The name of the signal to bind to
	 * @param parameters		The class types of the parameters the method takes
	 * @param priority			The priority of this slot in relation to others
	 * @throws IllegalArgumentException 	Thrown if the arguments do not match the signal's arguments
	 * @throws InvalidReturnTypeException 	Thrown if the return type does not match the signal's return type
	 */
	public Slot (SlotMethod st, String targetString, Class<?>[] parameters, Integer priority) throws IllegalArgumentException, InvalidReturnTypeException
	{
		this.slotMethod = st;
		this.targetObject = null;
		this.targetMethod = null;
		this.targetString = targetString;
		this.returnType = null;
		this.priority = priority;
		SASHandler.registerSlot(this, this.targetString, parameters);
	}
	
	/**
	 * Called by the SASHandler when it is triggered, this runs the method after checking the necessary objects.
	 * NOTE: This is NOT threadsafe, and could possibly trigger concurrency problems if not correctly used. If you want a (more) threadsafe implementation, try SynchroSlot (
	 * 
	 * @param varargs	The arguments passed by the signal
	 * @return	The return value of the method
	 */
	public Object invoke(Object[] varargs)
	{
		Object returnVal = null;
		try 
		{
			if (slotMethod == null)
			{
				try {
					if (this.targetMethod.isAccessible() != true)
					{
						this.targetMethod.setAccessible(true);
					}
					returnVal = this.targetMethod.invoke(targetObject, varargs);
					
					if (returnVal == null && this.returnType != null)
					{
						throw new InvalidReturnTypeException("Error: The return type is not expected to be null");
					}
					else
					{
						if (this.returnType != null && !this.returnType.isInstance(returnVal))
						{
							throw new InvalidReturnTypeException("Error: The return type: "
								    + returnVal.getClass() + " does not match the expected return type: "
								    + this.returnType);
						}
					}
				} catch (IllegalAccessException e) {
					throw new InvalidMethodException("Error: This thread does not have access to this method");
				} catch (InvocationTargetException e) {
					throw new InvalidMethodException("Error: The specified method of name: "
					    + this.targetMethod.getName() + " with the specified parameters " +
					    "does not exist");
				}
			}
			else
			{
				returnVal = this.slotMethod.invoke(varargs);
				if (returnVal == null && this.returnType != null)
				{
					throw new InvalidReturnTypeException("Error: The return type is not expected to be null");
				}
				else
				{
					if (!this.returnType.isInstance(returnVal))
					{
						throw new InvalidReturnTypeException("Error: The return type: "
							    + returnVal.getClass() + " does not match the expected return type: "
							    + this.returnType);
					}
				}
			}
		}
		catch (InvalidMethodException e)
		{
			e.printStackTrace();
		} catch (InvalidReturnTypeException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	/**
	 * Gets the return type of this slot
	 * @return	The return type of the method
	 */
	public Object getReturnType() {
		return this.returnType;
	}
	
	/**
	 * Deregisters this slot from the signal it is attached to. Note this function MUST be called before the object is nulled in code, otherwise it will act as a 
	 * memory leak. It will also still continue to call the method. (Java has no destructors, so thats all she wrote folks)
	 */
	public void deregisterSlot()
	{
		SASHandler.unRegisterSlot(this, targetString);
	}
	
	/**
	 * Getter to return the priority of this slot
	 * @return
	 */
	public Integer getPriority()
	{
		return this.priority;
	}
	
	/**
	 * Setter to set the priority of this slot 
	 * @param priority
	 */
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

}
