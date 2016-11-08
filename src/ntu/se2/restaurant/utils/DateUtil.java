package ntu.se2.restaurant.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil 
{
	static SimpleDateFormat FORMAT;
	static Calendar calendar;
	
	public static Date setYear(Date date)
	{
		calendar = Calendar.getInstance(); //get the time
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 2016);
		return date = calendar.getTime();
	}
	
	public static Date addAnHour (Date date)
	{
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY,1);
		return date = calendar.getTime();
	}
	
	public static Date stringToDateTime(String dateStr) throws ParseException {
		FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return FORMAT.parse(dateStr);
	}
	
	public static String getTime(Date date)
	{
		FORMAT = new SimpleDateFormat("HH:mm");
		return FORMAT.format(date);
	}
	
	public static Date formatStringTimetoDate(String time)
	{
		FORMAT = new SimpleDateFormat("HH:mm");
		Date date;
		try {
			date = FORMAT.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}
	
	public static String getDate(Date date)
	{
		FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		String s = FORMAT.format(date);
		return s;
	}
	
	public static String getDateTime(Date date)
	{
		FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String s = FORMAT.format(date);
		return s;
	}
	
	public static String formatStringDate(String date) throws ParseException
	{
		FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = FORMAT.parse(date);
		return FORMAT.format(date1);
	}

	public static Date formatStringToDate(String date)
	{
		FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return FORMAT.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
