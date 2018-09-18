package exceptions;

public class ValidatePasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ValidatePasswordException(String msg) {
		super(msg);
	}
	
}
