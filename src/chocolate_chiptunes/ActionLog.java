package chocolate_chiptunes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.ListIterator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;

public class ActionLog {
	
	private LinkedList<Action> ActionLog;
	private ListIterator<Action> ActionList;
	private Controller controller;
	
	ActionLog(Controller controller) {
		ActionLog = new LinkedList<Action>();
		ActionList = ActionLog.listIterator();
		this.controller = controller;
	}

	public void emptyLog(){
		ActionLog.clear();
		ActionList = ActionLog.listIterator();
	}
	
	public void AddAction(Object oldValue, Object newValue, Object objectAffected, Method objectMethodToInvoke, Method controllerMethodToInvoke) {
		//Remove any actions down the list if they exist (this only happens if you undo a bunch of actions and then perform a brand new action while you're not at the front of the list)
		while(ActionList.hasNext()) {
			ActionList.next();
			ActionList.remove();
			
			if(ActionList.hasPrevious())
				ActionList.previous();
		}
		
		//Add the new action
		ActionList.add(new Action(oldValue, newValue, objectAffected, objectMethodToInvoke, controllerMethodToInvoke));
	}
	
	public void Undo() {
		if(ActionList.hasPrevious()) {
			//Grab the previous action from the action list
			Action prevAction = ActionList.previous();
			
			//Instantiate the objected affected and the old value as objects
			Object object = prevAction.getObjectAffected(),
					value = prevAction.getOldValue();
			
			//Grab its class type
			String objectClassName = object.getClass().getName(),
					valueClassName = value.getClass().getName();
			
			//Throw the invocations into a try block to prevent the application from crashing
			try {					
				//Cast the object affected to it's determined type
				object = Class.forName(objectClassName).cast(object);
				
				//Cast the old value to it's determined type
				value = Class.forName(valueClassName).cast(value);

				//Invoke the method associated with the object (if not null)
				if(prevAction.getObjectMethodToInvoke() != null)
					prevAction.getObjectMethodToInvoke().invoke(object, value);
				
				//Invoke the method associated with the controller
				prevAction.getControllerMethodToInvoke().invoke(controller, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException | ClassNotFoundException e) {
				System.out.println("An unexpected error has occured.");
				e.printStackTrace();
			}	
		}
	}
	
	public void Redo() {
		if(ActionList.hasNext()) {
			//Grab the previous action from the action list
			Action nextAction = ActionList.next();
			
			//Instantiate the objected affected and the new value as objects
			Object object = nextAction.getObjectAffected(),
					value = nextAction.getNewValue();
			
			//Grab its class type
			String objectClassName = object.getClass().getName(),
					valueClassName = value.getClass().getName();
			
			//Throw the invocations into a try block to prevent the application from crashing
			try {					
				//Cast the object affected to it's determined type
				object = Class.forName(objectClassName).cast(object);
				
				//Cast the new value to it's determined type
				value = Class.forName(valueClassName).cast(value);
				
				//Invoke the method associated with the object (if not null)
				if(nextAction.getObjectMethodToInvoke() != null)
					nextAction.getObjectMethodToInvoke().invoke(object, value);
				
				//Invoke the method associated with the controller
				nextAction.getControllerMethodToInvoke().invoke(controller, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException | ClassNotFoundException e) {
				System.out.println("An unexpected error has occured.");
				e.printStackTrace();
			}	
		}
	}
}
