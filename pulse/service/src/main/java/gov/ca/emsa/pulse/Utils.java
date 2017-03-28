package gov.ca.emsa.pulse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
	public static boolean checkDobFormat(String dob){
		Pattern p = Pattern.compile("\\d{4}([01]\\d([0-3]\\d([0-2]\\d([0-5]\\d([0-5]\\d)?)?)?)?)?([+-]\\d{4})?$");
		Matcher m = p.matcher(dob);
		return m.matches();
	}
}
