package demo.tpm.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDatetime {

	public static String formatDatetimr() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String dateInString = "7-Jun-2013";

        try {

            Date date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
		return dateInString;

    }
}
