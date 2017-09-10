package edu.ncsu.csc216.wolf_scheduler.course;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test class for Activity
 * @author Noah Benveniste
 */
public class ActivityTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for checkConflict() implementation in Activity
	 */
	@Test
	public void testCheckConflict() {
		Activity a1 = new Course("CSC216", "Programming Concepts - Java", "001", 4, "sesmith5", "MW", 1330, 1445);
	    Activity a2 = new Course("CSC216", "Programming Concepts - Java", "001", 4, "sesmith5", "TH", 1330, 1445);
	    
	    //Test checking for a conflict with two activities that DO NOT conflict
	    try {
	        a1.checkConflict(a2);
	        assertEquals("Incorrect meeting string for this Activity.", "MW 1:30PM-2:45PM", a1.getMeetingString());
	        assertEquals("Incorrect meeting string for possibleConflictingActivity.", "TH 1:30PM-2:45PM", a2.getMeetingString());
	    } catch (ConflictException e) {
	        fail("A ConflictException was thrown when two Activities at the same time on completely distinct days were compared.");
	    }
	    //Same test as above but objects swapped to check for commutativity of method
	    try {
	        a2.checkConflict(a1);
	        assertEquals("Incorrect meeting string for this Activity.", "MW 1:30PM-2:45PM", a1.getMeetingString());
	        assertEquals("Incorrect meeting string for possibleConflictingActivity.", "TH 1:30PM-2:45PM", a2.getMeetingString());
	    } catch (ConflictException e) {
	        fail("A ConflictException was thrown when two Activities at the same time on completely distinct days were compared.");
	    }
	    
	    //Test checking for a conflict with two activities that DO conflict
	    //Update a1 with the same meeting days and a start time that overlaps the end time of a2
	    a1.setMeetingDays("TH");
	    a1.setActivityTime(1445, 1530);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("TH 2:45PM-3:30PM", a1.getMeetingString());
	        assertEquals("TH 1:30PM-2:45PM", a2.getMeetingString());
	    }
	    //Test for conflict on a single day
	    a1.setMeetingDays("H");
	    a1.setActivityTime(1445, 1530);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 2:45PM-3:30PM", a1.getMeetingString());
	        assertEquals("TH 1:30PM-2:45PM", a2.getMeetingString());
	    }
	    //Test case where end time of one activity is the start time of the other
	    a2.setActivityTime(1530, 1630);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 2:45PM-3:30PM", a1.getMeetingString());
	        assertEquals("TH 3:30PM-4:30PM", a2.getMeetingString());
	    }
	    //Check for commutativity with the above case
	    try {
	        a2.checkConflict(a1);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 2:45PM-3:30PM", a1.getMeetingString());
	        assertEquals("TH 3:30PM-4:30PM", a2.getMeetingString());
	    }
	    
	    //Testing different combinations of overlapping time intervals
	    a1.setActivityTime(800, 1000);
	    a2.setActivityTime(900, 1100);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 9:00AM-11:00AM", a2.getMeetingString());
	    }
	    try {
	        a2.checkConflict(a1);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 9:00AM-11:00AM", a2.getMeetingString());
	    }
	    
	    a2.setActivityTime(700, 900);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 7:00AM-9:00AM", a2.getMeetingString());
	    }
	    try {
	        a2.checkConflict(a1);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 7:00AM-9:00AM", a2.getMeetingString());
	    }
	    
	    a2.setActivityTime(700, 1200);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 7:00AM-12:00PM", a2.getMeetingString());
	    }
	    try {
	        a2.checkConflict(a1);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 7:00AM-12:00PM", a2.getMeetingString());
	    }
	    
	    a2.setActivityTime(830, 900);
	    try {
	        a1.checkConflict(a2);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 8:30AM-9:00AM", a2.getMeetingString());
	    }
	    try {
	        a2.checkConflict(a1);
	        fail(); //ConflictException should have been thrown, but was not.
	    } catch (ConflictException e) {
	        //Check that the internal state didn't change during method call.
	        assertEquals("H 8:00AM-10:00AM", a1.getMeetingString());
	        assertEquals("TH 8:30AM-9:00AM", a2.getMeetingString());
	    } 
	}
}
