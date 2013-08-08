package slots.and.signals.structure.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import slots.and.signals.exceptions.InvalidReturnTypeException;
import slots.and.signals.exceptions.SignalNameInUseException;
import slots.and.signals.structure.Signal;
import slots.and.signals.structure.Slot;
import slots.and.signals.structure.manager.support.SignalStructure;
import slots.and.signals.structure.manager.threading.SASRunner;

/**
 * Handles the passing of values between threads and projects
 * @author walfordt
 *
 */
public class SASHandler {

	private static int MAX_THREADS = 10;
	private static int MIN_THREADS = 5;
	private static long THREAD_TIMEOUT = 500;
	private static boolean USE_WORKER_THREADS = true;
	
	private static HashMap<String, SignalStructure> linker = new HashMap<String,SignalStructure>();
	private static BlockingQueue<Runnable> threadQueue = new PriorityBlockingQueue<Runnable>(MAX_THREADS);
	private static ThreadPoolExecutor exec;
	static {
		if (USE_WORKER_THREADS)
		{
			exec = new ThreadPoolExecutor(MIN_THREADS, MAX_THREADS, THREAD_TIMEOUT, TimeUnit.MILLISECONDS, threadQueue);
		}

	}
		
	/**
	 * Registers a signal with the handler
	 * 
	 * @param signal	The signal to register
	 */
	public static void registerSignal(Signal signal) throws SignalNameInUseException
	{
		if (linker.containsKey(signal.getSignalName()))
		{
			throw new SignalNameInUseException("Error: This signal name is currently in use");
		}
		SignalStructure struct = new SignalStructure(signal.getParams(), new ArrayList<Slot>(), signal.getReturnParam());
		linker.put(signal.getSignalName(), struct);
	}
	
	/**
	 * Deregisters a signal with the handler
	 * 
	 * @param signal	The signal to deregister
	 */
	public static void unRegisterSignal(Signal signal)
	{
		linker.remove(signal);
	}
	
	/**
	 * Registers a slot with the handler, attached to the specified signal
	 * 
	 * @param slot			The slot to register
	 * @param targetSignal	The name of the target signal to register to
	 * @param params		The parameters the slot takes on its invoke method
	 * @throws IllegalArgumentException		Thrown if the specified parameters are different from the signal
	 * @throws InvalidReturnTypeException	Thrown if the specified return type is different from the signal
	 */
	public static void registerSlot(Slot slot, String targetSignal, Class<?>[] params) throws IllegalArgumentException, InvalidReturnTypeException
	{
		SignalStructure struct = linker.get(targetSignal);
		if (params == null)
		{
			if (struct.getInvokerParameters() != null)
			{
				throw new IllegalArgumentException("Error: The argument is not expected to be null");
			}
		}
		else
		{
			if (struct.getInvokerParameters().length != params.length)
			{
				throw new IllegalArgumentException
				    ("Error: The entered number of arguments: "
				    + params.length + " does not equal the expected number: "
				    + struct.getInvokerParameters().length);
			}
			for (int i = 0; i < params.length; i++)
			{
				if (struct.getInvokerParameters()[i] != params[i])
				{
					throw new IllegalArgumentException("Error: The entered object of class: " + 
						struct.getInvokerParameters()[i] + " does not match the expected class: "
					    + params[i]);
				}
			}
		}
		
		if (slot.getReturnType() == null)
		{
			if (struct.getReturnType() != null)
			{
				throw new InvalidReturnTypeException("Error: The argument is not expected to be null");
			}
		}
		else
		{
			if (slot.getReturnType() != struct.getReturnType())
			{
				throw new InvalidReturnTypeException("Error: The return type: "
				    + slot.getReturnType() + " does not match the expected return type: "
				    + struct.getReturnType());
			}		
		}
		linker.get(targetSignal).getRegisteredSlots().add(slot);
	}
	
	/**
	 * deregisters a slot from the specified target signal
	 * 
	 * @param slot			The slot to deregister
	 * @param targetSignal	The target signal
	 */
	public static void unRegisterSlot(Slot slot, String targetSignal)
	{
		linker.get(targetSignal).getRegisteredSlots().remove(slot);
	}
	
	/**
	 * Invokes all the slots attached to the invoking signal, with the vararg parameters.
	 * 
	 * Note: If USE_WORKER_THREADS is set to true, and the signal returns no values, the handler will attempt to thread each invoke using the threadpool.
	 * @param signal
	 * @param varargs
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<?> invoke(Signal signal, Object[] varargs)
	{
		List returnVal = new ArrayList();
		if (USE_WORKER_THREADS && signal.isVoid() && linker.get(signal.getSignalName()).getRegisteredSlots().size() > 1)
		{
			for (Slot slot : linker.get(signal.getSignalName()).getRegisteredSlots())
			{
				exec.execute(new SASRunner(slot, varargs));
			}
		}
        else
		{
			for (Slot slot : linker.get(signal.getSignalName()).getRegisteredSlots())
			{
				Object o = slot.invoke(varargs);
				returnVal.add(o);
			}
		}
		return returnVal;
	}	
	
	/**
	 * Shuts down the SASHandler, clearing all the lists and stopping the thread pool
	 * 
	 */
	public static void stopHandler()
	{
		exec.shutdown();
		linker.clear();		
	}
}
