package exceptions;

public class InvalidStatusChange extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidStatusChange(String message) {
		super(message);
	}
}
