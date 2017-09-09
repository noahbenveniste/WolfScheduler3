package edu.ncsu.csc216.wolf_scheduler.course;

/**
 * A checked exception that is thrown by the Conflict interface in the case of an Activity conflict
 * @author Noah Benveniste
 */
public class ConflictException extends Exception {

	/** ID used for serialization. */
	private static final long serialVersionUID = 1L;
	/** Default return message for ConflictException */
	private static final String DEFAULT_MSG = "Schedule conflict.";
	
	/**
	 * Constructor for ConflictException given a custom error message.
	 */
	public ConflictException(String customMsg) {
		super(customMsg);
	}
	
	/**
	 * Constructor for ConflictException given no custom error message;
	 * Uses the default message "Schedule conflict.".
	 */
	public ConflictException() {
		this(DEFAULT_MSG);
	}
}
