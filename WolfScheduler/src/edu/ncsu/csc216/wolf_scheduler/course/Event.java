package edu.ncsu.csc216.wolf_scheduler.course;

/**
 * Object class that represents an event
 * @author Noah Benveniste
 */
public class Event extends Activity {

	/** The number of weeks the event repeats */
	private int weeklyRepeat;
	/** Details describing the event */
	private String eventDetails;
	
	/**
	 * Constructs an event object
	 * @param title The title of the event
	 * @param meetingDays A string made up of the first letters of the days the event takes place on
	 * @param startTime Start time of the Event
	 * @param endTime End time of the Event
	 * @param weeklyRepeat The number of weeks the event repeats for
	 * @param eventDetails Details describing the event
	 */
	public Event(String title, String meetingDays, int startTime, int endTime, int weeklyRepeat, String eventDetails) {
		super(title, meetingDays, startTime, endTime);
		setWeeklyRepeat(weeklyRepeat);
		setEventDetails(eventDetails);
	}

	/**
	 * Get the number of weeks the event repeats
	 * @return the weeklyRepeat
	 */
	public int getWeeklyRepeat() {
		return weeklyRepeat;
	}

	/**
	 * Set the number of weeks the event repeats
	 * @param weeklyRepeat the weeklyRepeat to set
	 * @throws IllegalArgumentException if the input is less than 1 or greater than 4
	 */
	public void setWeeklyRepeat(int weeklyRepeat) {
		if (weeklyRepeat < 1 || weeklyRepeat > 4) {
			throw new IllegalArgumentException("Invalid weekly repeat.");
		}
		this.weeklyRepeat = weeklyRepeat;
	}

	/**
	 * Get the event details
	 * @return the eventDetails
	 */
	public String getEventDetails() {
		return eventDetails;
	}

	/**
	 * Set the event details
	 * @param eventDetails the eventDetails to set
	 * @throws IllegalArgumentException if the input is null
	 */
	public void setEventDetails(String eventDetails) {
		if (eventDetails == null) {
			throw new IllegalArgumentException("Invalid event details.");
		}
		this.eventDetails = eventDetails;
	}
	
	/**
	 * Sets the meeting days for an Event
	 * @param meetingDays the meetingDays to set
	 * @throws IllegalArgumentException if the input is null or an empty string, if the 
	 * input has characters other than u,m,t,w,h,f,s
	 */
	@Override
	public void setMeetingDays(String meetingDays) {
		//Check that the input isn't null or an empty string
		if (meetingDays == null || meetingDays.equals("")) {
			throw new IllegalArgumentException();
		}
		
		//Check for invalid characters
		for (int i = 0; i < meetingDays.length(); i++) {
			if (meetingDays.charAt(i) != 'U' && meetingDays.charAt(i) != 'M' && meetingDays.charAt(i) != 'T' && 
					meetingDays.charAt(i) != 'W' && meetingDays.charAt(i) != 'H' && 
					meetingDays.charAt(i) != 'F' && meetingDays.charAt(i) != 'S') {
				throw new IllegalArgumentException();
			}
		}
		
		super.setMeetingDays(meetingDays);
	}
	
	/**
	 * Checks if two Events are duplicates of one another. An Event is a duplicate of another
	 * if it has the same title as the other.
	 * @param activity The Activity to compare
	 * @return true if the input Activity is an Event and it has the same title as the Event being
	 * compared, false if the input Activity is not an Event or it is an Event and it does not
	 * have the same title as the Event being compared
	 */
	@Override
	public boolean isDuplicate(Activity activity) {
		//Check that the input is an Event
		if (activity instanceof Event) {
			//Cast the activity to an Event
			Event event = (Event)activity;
			//Check that the events being compared do not have the same title
			return this.getTitle().equals(event.getTitle());
		} else {
			//Return false if the Activity is not an Event
			return false;
		}
	}
	
	/**
	 * Creates and returns a string array containing two empty strings for fields that Event does not
	 * have, as well as the Event title and meetingString
	 * @return the short string array
	 */
	@Override
	public String[] getShortDisplayArray() {
		return new String[] {"", "", this.getTitle(), this.getMeetingString()};
	}

	/**
	 * Creates and returns a string array containing two empty strings for fields that Event does not have, title,
	 * another two empty strings, meetingString and eventDetails.
	 * @return the long string array
	 */
	@Override
	public String[] getLongDisplayArray() {
		return new String[] {"", "", this.getTitle(), "", "", this.getMeetingString(), "" + this.getEventDetails()};
	}

	
	/** Overridden getMeetingString, toString */
	
	
	/**
	 * Overrides the super method so that the weeklyRepeat value is appended to the string
	 * @return the meetingString
	 */
	@Override
	public String getMeetingString() {
		return super.getMeetingString() + " (every " + this.getWeeklyRepeat() + " weeks)";
	}

	/**
	 * Returns a comma separated list of the Event's state as a string
	 * @return the comma separated list
	 */
	@Override
	public String toString() {
		return this.getTitle() + "," + this.getMeetingDays() + "," + this.getStartTime() + "," +
				this.getEndTime() + "," + this.getWeeklyRepeat() + "," + this.getEventDetails();
	}
	
}
