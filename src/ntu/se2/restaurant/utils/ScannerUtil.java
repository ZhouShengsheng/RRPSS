package ntu.se2.restaurant.utils;

import java.util.Scanner;

public final class ScannerUtil {

	public static Scanner scanner = new Scanner(System.in);
	
	/**
	 * Call this method when the application exits.
	 */
	public static void close() {
		scanner.close();
	}
	
}
