package edu.ncsu.csc216.wolf_scheduler.scheduler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import edu.ncsu.csc216.wolf_scheduler.course.Activity;
import edu.ncsu.csc216.wolf_scheduler.course.ConflictException;
import edu.ncsu.csc216.wolf_scheduler.course.Course;
import edu.ncsu.csc216.wolf_scheduler.course.Event;
import edu.ncsu.csc216.wolf_scheduler.io.ActivityRecordIO;
import edu.ncsu.csc216.wolf_scheduler.io.CourseRecordIO;

/**
 * Class that handles the creation and manipulation of a schedule of course and a catalog of
 * available courses
 * @author Noah Benveniste
 * @author Sarah Heckman
 */
public class WolfScheduler {
	
	/** Fields */
	
	/** Catalog of courses to be chosen from */
	private ArrayList<Course> courseCatalog;
	/** The student's schedule */
	private ArrayList<Activity> schedule;
	/** The title of the schedule */
	private String title;
	
	/** Default Schedule Name */
	public static final String DEFAULT_SCHEDULE_NAME = "My Schedule";
	
	/** Constructor */
	
	/**
	 * Constructor for WolfScheduler object. Initializes course catalog and schedule array lists
	 * and attempts to populate the course catalog with courses from the input file
	 * @param inFile the name of the file to be read
	 * @throws IllegalArgumentException if the input file cannot be read
	 */
	public WolfScheduler(String inFile) {
		//Create the course catalog
		ArrayList<Course> c = new ArrayList<Course>();
		this.courseCatalog = c;
		
		//Create the schedule
		ArrayList<Activity> s = new ArrayList<Activity>();
		this.schedule = s;
		
		//Set the default schedule name
		this.title = DEFAULT_SCHEDULE_NAME;
		
		//Try to add the courses from the input file to the course catalog
		try {
			this.courseCatalog = CourseRecordIO.readCourseRecords(inFile);
		} catch (FileNotFoundException e ) {
			throw new IllegalArgumentException("Cannot find file.");
		}
	}

	/** Methods */
	
	/**
	 * Attempts to retrieve a course from the course catalog based on an input name and
	 * section number
	 * @param name the name of the course
	 * @param section the section for the course
	 * @return the course object corresponding to the input if it exists in the course
	 * catalog or null if the course does not exist
	 */
	public Course getCourseFromCatalog(String name, String section) {
		for (int i = 0; i < this.courseCatalog.size(); i++) {
			//Check if the currently indexed course in the catalog has the same name and
			//section as the input name and section
			if (this.courseCatalog.get(i).getName().equals(name) && 
					this.courseCatalog.get(i).getSection().equals(section)) {
				//If true, return the course
				return this.courseCatalog.get(i);
			}
		}
		//If no matches were found while indexing the course catalog, return null
		return null;
	}
	
	/**
	 * Checks if a given course (identified by name and section) can be added to the schedule
	 * and then adds it if allowed
	 * @param name the name of the course
	 * @param section the section for the course
	 * @return true if it can be added (i.e. it is not already in the schedule), false if it 
	 * does not exist in the course catalog
	 * @throws IllegalArgumentException if the course is already in the schedule
	 */
	public boolean addCourse(String name, String section) {
		//First, check that the course actually exists in the catalog
		if (this.getCourseFromCatalog(name, section) == null) {
			//Return false if it does not exist
			return false;
		}
		//Next, check that a course with the same name does not already exist in the schedule
		for (int i = 0; i < this.schedule.size(); i++) {
			//Check if the currently indexed course in the schedule has the same name as the input
			if (this.schedule.get(i).isDuplicate(this.getCourseFromCatalog(name, section))) {
				//If true, throw an exception
				throw new IllegalArgumentException("You are already enrolled in " + name);
			}
			
			//Check for conflicts with other activities in the schedule
			try {
				this.getCourseFromCatalog(name, section).checkConflict(this.schedule.get(i));
			} catch (ConflictException e) {
				throw new IllegalArgumentException("The course cannot be added due to conflict.");
			}
		}
		//If the course passed the above two tests, add it to the schedule (the index of 
		//the added element changes dynamically - if a course is being added to an empty
		//schedule, the schedule size will be 0 and the course must be assigned to index
		//0 etc)
		this.schedule.add(this.schedule.size(), this.getCourseFromCatalog(name, section));
		//If the loop executes without throwing an exception, the course does not already
		//exist in the schedule
		return true;
	}
	
	/**
	 * Adds a user-defined event to the schedule if an event with the same title does not already exist
	 * @param title The title of the event
	 * @param meetingDays The days the event takes place
	 * @param startTime The start time of the event
	 * @param endTime The end time of the event
	 * @param weeklyRepeat The number of weeks the event repeats for
	 * @param eventDetails Details describing the event
	 * @throws IllegalArgumentException if the user attempts to add an event with the same title as another event already in the schedule
	 */
	public void addEvent(String title, String meetingDays, int startTime, int endTime, int weeklyRepeat, String eventDetails) {
		//Try to create the event
		Event newEvent = new Event(title, meetingDays, startTime, endTime, weeklyRepeat, eventDetails);
		//Check that an event with the same title does not already exist
		for (int i = 0; i < this.schedule.size(); i++) {
			//Check if the currently indexed activity in the schedule is an event that has
			//the same title
			if (this.schedule.get(i).isDuplicate(newEvent)) {
				//If true, throw an exception
				throw new IllegalArgumentException("You have already created an event called " + newEvent.getTitle());
			}
			
			//Check for conflicts with other activities in the schedule
			try {
				newEvent.checkConflict(this.schedule.get(i));
			} catch (ConflictException e) {
				throw new IllegalArgumentException("The event cannot be added due to conflict.");
			}
		}
		//Add the event if it does not already exist and it does not conflict
		this.schedule.add(this.schedule.size(), newEvent);
	}
	
	/**
	 * Checks if an activity at a given index can be removed from the student's schedule and
	 * then removes it if allowed
	 * @param idx The index of the activity to be removed from the schedule
	 * @return true if the course can be removed (i.e. it is in the schedule), false if it
	 * cannot be removed (it is not in the schedule)
	 */
	public boolean removeActivity(int idx) {
		//Checks to see if the passed index is in bounds for the array. If it is, remove the
		//element at that index and return true. If not, return false.
		if (this.schedule.size() > idx) {
			this.schedule.remove(idx);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Creates a new empty ArrayList and assigns it to the schedule field, resetting
	 * the schedule to empty
	 */
	public void resetSchedule() {
		//Create a new empty array list of courses
		ArrayList<Activity> newEmptySchedule = new ArrayList<Activity>();
		//Set the schedule field of the object to the newly created empty schedule
		this.schedule = newEmptySchedule;
	}
	
	/**
	 * Creates and returns a 2D string array representation of the course catalog containing
	 * information about the courses' name, section, title and meeting info
	 * @return a 2D string array of the course catalog if there are courses in the catalog,
	 * or an empty string array otherwise
	 */
	public String[][] getCourseCatalog() {
		//The number of rows is determined by the number of courses in the catalog
		int numRow = this.courseCatalog.size();
		//If there are no courses in the catalog, return an empty array
		if (numRow == 0) {
			return new String[0][0];
		}
		//Four columns: name, section, title, meetingString
		int numCol = 4;
		//Create the string array with the one row per course in the catalog
		String[][] catStr = new String[numRow][numCol];
		//Index through the course catalog, finding the relevant data for each course
		//and then add it to the array in the proper index
		for (int i = 0; i < numRow; i++) {
			//Add the course name
			catStr[i][0] = this.courseCatalog.get(i).getName();
			//Add the course section
			catStr[i][1] = this.courseCatalog.get(i).getSection();
			//Add the course title
			catStr[i][2] = this.courseCatalog.get(i).getTitle();
			//Add the meetingString
			catStr[i][3] = this.courseCatalog.get(i).getMeetingString();
			//Next iteration of loop adds data for next course in the catalog in the
			//next row of the array
		}
		return catStr;
	}

	/**
	 * Creates and returns a 2D string array representation of the schedule containing
	 * information about the activities it contains
	 * @return a 2D string array of the schedule if there are activities in the schedule,
	 * or an empty string array otherwise
	 */
	public String[][] getScheduledActivities() {
		//The number of rows is determined by the number of activities in the schedule
		int numRow = this.schedule.size();
		//If there are no activities in the schedule, return an empty array
		if (numRow == 0) {
			return new String[0][0];
		}
		//Three columns: name, section, title, meetingString
		int numCol = 4;
		//Create the string array with the one row per activity in the schedule
		String[][] schedStr = new String[numRow][numCol];
		//Index through the schedule, finding the relevant data for each course
		//and then add it to the array in the proper index
		for (int i = 0; i < numRow; i++) {
			//Get the activity information for short display
			schedStr[i] = this.schedule.get(i).getShortDisplayArray();
			//Next iteration of loop adds data for next course in the catalog in the
			//next row of the array
		}
		return schedStr;
	}

	/**
	 * Creates and returns a 2D string array representation of the schedule containing
	 * information about the courses' name, section, title, credits, instructorId and meetingDays
	 * @return a 2D string array of the schedule if there are courses in the schedule,
	 * or an empty string array otherwise
	 */
	public String[][] getFullScheduledActivities() {
		//The number of rows is determined by the number of courses in the schedule
		int numRow = this.schedule.size();
		//If there are no courses in the schedule, return an empty array
		if (numRow == 0) {
			return new String[0][0];
		}
		//Seven columns: name, section, title, credits, instructorId, meetingDays, eventDetails
		int numCol = 7;
		//Create the string array with the one row per course in the schedule
		String[][] fullSchedStr = new String[numRow][numCol];
		//Index through the schedule, finding the relevant data for each course
		//and then add it to the array in the proper index
		for (int i = 0; i < numRow; i++) {
			//Get the activity information for long display
			fullSchedStr[i] = this.schedule.get(i).getLongDisplayArray();
			//Next iteration of loop adds data for next course in the catalog in the
			//next row of the array
		}
		return fullSchedStr;
	}

	/**
	 * Gets the title for the schedule
	 * @return the schedule's title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Sets the title for the schedule
	 * @param title the user's desired title for the schedule
	 * @throws IllegalArgumentException if the input is null
	 */
	public void setTitle(String title) {
		if (title == null) {
			throw new IllegalArgumentException("Title cannot be null.");
		} else {
			this.title = title;
		}
	}

	/**
	 * Exports the schedule to a specified file
	 * @param fileName the name of the file the user wishes to export to
	 * @throws IllegalArgumentException if the specified file cannot be written to
	 */
	public void exportSchedule(String fileName) {
		try {
			ActivityRecordIO.writeActivityRecords(fileName, this.schedule);
		} catch (IOException e) {
			throw new IllegalArgumentException("The file cannot be saved.");
		}
	}
}
