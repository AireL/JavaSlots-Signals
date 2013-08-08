package slots.and.signals.structure;

import java.util.List;

import slots.and.signals.exceptions.SignalNameInUseException;
import slots.and.signals.structure.manager.SASHandler;

/**
 * This class is designed to register an intent to broadcast data for calling on a method/methods outside of the current project/thread. THe class registers with
 * the listener, then can be activated by calling the 'invoke' function.
 * 
 * If you are having trouble with the slot method failing to attach to a signal as the signal is not registered before the slot, enclose the signal object in static 
 * brackets outside the class runtime methods, to ensure it is generated at runtime. As signal registering depends solely on the SASHandler static methods, this will
 * flow, and ensure you don't have any issues.
 * 
 * @author walfordt
 */
public class Signal {

	private final Class<?>[] parameters;
	
	private final String signalName;
	
	private final Class<?> returnParameter;
	
	private final boolean isVoid;
	
	/**
	 * Creates a new Signal object
	 * 
	 * @param signalName	The name you want to attribute to the signal. Note this must be unique
	 * @param varargs		The arguments that are being passed by this signal
	 * @param returnParam	The expected return value type (this can be null)
	 */
	public Signal(String signalName, Class<?>[] varargs, Class<?> returnParam)
	{
		Class<?>[] params = varargs;
		this.parameters = params;
		this.signalName = signalName;
		this.returnParameter = returnParam;
		if (returnParam == null)
		{
			isVoid = true;
		}
		else
		{
			isVoid = false;
		}
		try {
			SASHandler.registerSignal(this);
		} catch (SignalNameInUseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new signals object
	 * 
	 * @param signalName	The name you want to attribute to the signal. Note this must be unique
	 * @param varargs		The arguments that are being passed by this signal
	 * @param returnParam	The expected return value in object form (this can be null)
	 */
	public Signal(String signalName, Object[] varargs, Object returnParam)
	{
		Class<?>[] params = null;
		if (varargs != null)
		{
			params = new Class<?>[varargs.length];
			for (int i = 0; i < varargs.length; i++)
			{
				params[i] = varargs[i].getClass();
			}
		}
		this.parameters = params;
		this.signalName = signalName;
		this.returnParameter = returnParam.getClass();
		if (returnParameter == null)
		{
			isVoid = true;
		}
		else
		{
			isVoid = false;
		}

		try {
			SASHandler.registerSignal(this);
		} catch (SignalNameInUseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter for the signal name
	 * @return
	 */
	public String getSignalName() 
	{
		return this.signalName;
	}
	
	/**
	 * Getter for whether the signal is void (used for performance optimisation)
	 * @return
	 */
	public boolean isVoid()
	{
		return this.isVoid;
	}

	/**
	 * Getter for the parameter list
	 * @return
	 */
	public Class<?>[] getParams() 
	{
		return this.parameters;
	}
	
	/**
	 * Getter for the return class
	 * @return
	 */
	public Class<?> getReturnParam()
	{
		return this.returnParameter;
	}

	/**
	 * Invokes the attached methods to this signal
	 * 
	 * @param varargs		The arguments being passed in
	 * @return		The expected return object (or null if how it is set)
	 * @throws IllegalArgumentException		Thrown if the argument parameters are not as expected
	 */
	public List<?> invoke(Object[] varargs) throws IllegalArgumentException 
	{
		if (varargs == null)
		{
			if (this.parameters != null)
			{
				throw new IllegalArgumentException("Error: The argument is not expected to be null");
			}
		}
		else 
		{
			if (varargs.length != parameters.length)
			{
				throw new IllegalArgumentException
				    ("Error: The entered number of arguments: "
				    + varargs.length + " does not equal the expected number: "
				    + parameters.length);
			}
			for (int i = 0; i < parameters.length; i++)
			{
				if (!parameters[i].isInstance(varargs[i]))
				{
					throw new IllegalArgumentException("Error: The entered object of class: " + 
				        varargs[i].getClass() + " does not match the expected class: "
				        + parameters[i]);
				}
			}
		}
		return SASHandler.invoke(this, varargs);
	}
	
	/**
	 * unregisters a signal from the SAS Handler. This means no more slots can register, and any current slot attached is deregistered.
	 */
	public void unRegisterSignal()
	{
		SASHandler.unRegisterSignal(this);
	}
}
