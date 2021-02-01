package excel.builder210.main;

import java.util.Date;

public class DateParser {
	
	public static Date parse(String date){
		return parse(date, false);
	}
	
	//sdfgertgr
	public static Date parse(String date, boolean b) {
		int y = 0, m = 0, d = 0;
		if(b) {
			if(date.length()!=7)throw new IllegalArgumentException();
			y = Integer.parseInt(date.substring(3, 7));
			m = Integer.parseInt(date.substring(0, 2));
			return new Date(y, m, 0);
		}else {
			if(date.length()!=14)throw new IllegalArgumentException();
			y = Integer.parseInt(date.substring(0, 4));
			m = Integer.parseInt(date.substring(4, 6));
			d = Integer.parseInt(date.substring(6, 8));
			return new Date(y-1900, m-1, d);
		}
	}
	
}
