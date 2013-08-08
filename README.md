JavaSlots-Signals
=================

Java implementation of Slots and Signals

------------------------------------------

Small module that simulates the Slots and Signals functionality from Qt. This module uses reflection, but encapsulates
functionality with significant error checking to manager inherent flaws in Java. Written in 1.6, but should be
compatible with later versions.

The module is capable of working across threads, and returning responses to the signals (returned as a list of 
objects). The standard spiel about thread safety still applies - this won't cover up poor thread management (sorry!)

The module also implements a thread pool if desired to optimise multiple notifications to a signal, if the signal
doesn't return anything. A warning is flagged if an invoke method returns an object when none is expected, but won't 
take anything down.

This module was written as a futile gesture against excessive event driven code (Yes, people use multiple nested
anonymous classes extending listeners and it makes me sad). Its simple, effective and fast. It _does_ use reflection. 
Fair warning.
