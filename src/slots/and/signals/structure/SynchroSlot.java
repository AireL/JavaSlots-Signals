package slots.and.signals.structure;

import java.lang.reflect.InvocationTargetException;

import slots.and.signals.exceptions.InvalidMethodException;
import slots.and.signals.exceptions.InvalidReturnTypeException;

/**
 * A class to handle receiving of method processing requests from outside the project. This class registers with a listener object, that then calls the specified
 * method whenever the signal is broadcast. This version is (more) threadsafe
 * 
 * @author walfordt
 */
public class SynchroSlot extends Slot
{

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
	public SynchroSlot (Object targetObject, String targetMethod,
			String targetString, Class<?>[] parameters, Class<?> returnType)
			throws Exception {
		super(targetObject, targetMethod, targetString, parameters, returnType);
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
	public SynchroSlot (SlotMethod st, String targetString, Class<?> returnType, Class<?>[] parameters) throws Exception
	{
		super(st,targetString,parameters,returnType);
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
	public SynchroSlot (SlotMethod st, String targetString, Object returnType, Class<?>[] parameters) throws Exception
	{
		super(st,targetString,parameters,returnType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * This implementation is more threadsafe - however this is not guaranteed due to the JVM's inherent handling of threads
	 */
	@Override
	public synchronized Object invoke(Object[] varargs)
	{
		Object returnVal = null;
		try 
		{
			if (slotMethod == null)
			{
				try {
					returnVal = this.targetMethod.invoke(targetObject, varargs);
					
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
}
