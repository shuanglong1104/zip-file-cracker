package exceptions;

public class ConfigUncorrectException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ConfigUncorrectException(String msg) {
		super(msg);
	}
	
}
