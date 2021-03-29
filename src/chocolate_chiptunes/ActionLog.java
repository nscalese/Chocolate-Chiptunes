package chocolate_chiptunes;

import java.util.LinkedList;
import java.util.ListIterator;

public class ActionLog {
	
	private LinkedList<Action> ActionLog;
	private ListIterator<Action> ActionList;
	
	ActionLog() {
		ActionLog = new LinkedList<Action>();
		ActionList = ActionLog.listIterator();
	}
	
	public void AddAction(String oldValue, String newValue, Object objectAffected) {
		//Remove any actions down the list if they exist (this only happens if you undo a bunch of actions and then perform a brand new action while you're not at the front of the list)
		while(ActionList.hasNext()) {
			ActionList.next();
			ActionList.remove();
			ActionList.previous();
		}
		
		//Add the new action
		ActionList.add(new Action(oldValue, newValue, objectAffected));
	}
	
	public void Undo() {
		Action prevAction = ActionList.previous();
	}
	
	public void Redo() {
		Action nextAction = ActionList.next();
	}
	
}
