package edu.ncsu.csc216.wolf_scheduler.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.ncsu.csc216.wolf_scheduler.course.Activity;

/**
 * Class that handles the writing of activities to an output file
 * @author Noah Benveniste
 */
public class ActivityRecordIO {

	/**
	 * Writes the given list of Courses to an output file
	 * @param fileName The name of the file to be written to
	 * @param activities An array list of Activities to be written to the file
	 * @throws IOException if the file cannot be saved
	 */
	public static void writeActivityRecords(String fileName, ArrayList<Activity> activities) throws IOException {
		//Initialize print stream object to write to file
		PrintStream fileWriter = new PrintStream(new File(fileName));
	
		//Index through the array list, printing one course per line
		for (int i = 0; i < activities.size(); i++) {
		    fileWriter.println(activities.get(i).toString());
		}
	
		//Once all courses have been printed into the file, close the print stream
		fileWriter.close();
	}

}
