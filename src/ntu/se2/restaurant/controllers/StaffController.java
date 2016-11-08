package ntu.se2.restaurant.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import ntu.se2.restaurant.models.Staff;
import ntu.se2.restaurant.utils.DataFilePath;

public class StaffController {

	// Singleton.
	private static StaffController instance = null;
	
	// Fields.
	private ArrayList<Staff> staffList;
	private int currentStaffIndex;
	
	private StaffController() {
		staffList = new ArrayList<>();
		currentStaffIndex = 0;
		loadData();
	}
	
	/**
	 * Get shared singleton instance.
	 * 
	 * @return
	 */
	public static StaffController sharedInstance() {
		if (instance == null) {
			instance = new StaffController();
		}
		return instance;
	}
	
	/**
	 * Load data from file.
	 * 
	 */
	private void loadData() {
		try {
			Scanner sc=new Scanner(new BufferedReader(new FileReader(DataFilePath.STAFF_PATH)));
			sc.nextLine();
			while(sc.hasNext())
			{
				String current[] = sc.nextLine().split(",");
				Staff staff = new Staff(current[0],current[1],current[2],current[3]);
				staffList.add(staff);
			}
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Get current staff on duty.
	 * 
	 * @return
	 */
	public Staff getCurrentStaff() {
		if (staffList.size() == 0) {
			return null;
		}
		return staffList.get(currentStaffIndex);
	}
	
	/**
	 * Get staff by his or her id.
	 * 
	 * @return
	 */
	public Staff getStaffById(String id) {
		for (int i = 0; i < staffList.size(); i++) {
			Staff staff = staffList.get(i);
			if (staff.getStaffID().equals(id)) {
				return staff;
			}
		}
		return null;
	}

}
