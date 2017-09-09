package edu.ncsu.csc216.wolf_scheduler.course;

/**
 * Interface whose behavior allows for the checking of conflicts between Activity objects.
 * @author Noah Benveniste
 */
public interface Conflict {
	
	/**
	 * Method that is used to check for scheduling conflicts between Activities.
	 * @param possibleConflictingActivity The Activity to check for conflicts with
	 * @throws ConflictException if the two Activities have any overlap in their meeting days and times
	 */
	void checkConflict(Activity possibleConflictingActivity) throws ConflictException;
}
