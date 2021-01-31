package excel.builder210.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.poi.ss.formula.eval.EvaluationException;
import org.junit.Test;

import excel.builder210.main.DateParser;

public class DateParserTest {
	
	@Test
	public void illegalArgument() {
		assertEquals(DateParser.parse("20190604171702"), new Date(2019, 6, 4));
		assertEquals(DateParser.parse("20180604171702"), new Date(2018, 6, 4));
		assertEquals(DateParser.parse("20180704171702"), new Date(2018, 7, 4));
		assertEquals(DateParser.parse("20180612171702"), new Date(2018, 6, 12));
	}
	

}
