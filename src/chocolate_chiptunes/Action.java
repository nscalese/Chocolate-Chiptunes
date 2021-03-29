package chocolate_chiptunes;

public class Action {

	private String oldValue;
	private String newValue;
	private Object objectAffected;
	
	Action(String oldValue, String newValue, Object objectAffected) {
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.objectAffected = objectAffected;
	}
	
	public String getOldValue() {
		return oldValue;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
	public Object getObjectAffected() {
		return objectAffected;
	}
}
