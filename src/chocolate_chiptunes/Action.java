package chocolate_chiptunes;

import java.lang.reflect.Method;

public class Action {

	private Object oldValue;
	private Object newValue;
	private Object objectAffected;
	private Method objectMethodToInvoke;
	private Method controllerMethodToInvoke;
	
	Action(Object oldValue, Object newValue, Object objectAffected, Method objectMethodToInvoke, Method controllerMethodToInvoke) {
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.objectAffected = objectAffected;
		this.objectMethodToInvoke = objectMethodToInvoke;
		this.controllerMethodToInvoke = controllerMethodToInvoke;
	}
	
	public Object getOldValue() {
		return oldValue;
	}
	
	public Object getNewValue() {
		return newValue;
	}
	
	public Object getObjectAffected() {
		return objectAffected;
	}
	
	public Method getObjectMethodToInvoke() {
		return objectMethodToInvoke;
	}
	
	public Method getControllerMethodToInvoke() {
		return controllerMethodToInvoke;
	}
}
