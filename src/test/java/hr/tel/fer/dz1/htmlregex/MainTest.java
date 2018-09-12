package hr.tel.fer.dz1.htmlregex;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MainTest {
	
	static private String regexTag = "<.+>";
	static private String regexExtendedTag = "<[^>]*>";
	static private String regexIP = " (([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])($| )";
	static private String regexEmail = " (\\p{Alnum})+(_?|\\.?)(\\p{Alnum})+@(\\p{Alpha})+\\.(\\p{Alpha})+($| )";
	static private String all = ".*";
	

	@Test
	public void testTagTrue() {
		String stringForTest = "gldk<title> bla bla bla ";
		Pattern pattern = Pattern.compile(regexTag);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());
	}
	

	@Test
	public void testTagFalse() {
		String stringForTest = "gldk<title bla bla bla <";
		Pattern pattern = Pattern.compile(regexTag);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testExtendedTagTrue() {
		String stringForTest = "gldk<title atribut='blue'> bla bla bla ";
		Pattern pattern = Pattern.compile(regexExtendedTag);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());
	}
	
	@Test
	public void testExtendedTag2True() {
		String stringForTest = "gldk<title atribut='blue' atribut2='red'> bla bla bla ";
		Pattern pattern = Pattern.compile(regexExtendedTag);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());
	}
	
	@Test
	public void testExtendedTagFalse() {
		String stringForTest = "gldk<title atribut='blue' atribut2='red' bla bla bla ";
		Pattern pattern = Pattern.compile(regexExtendedTag);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());
	}
	
	@Test
	public void testIPTrue() {
		String stringForTest = "gldk<title> bla  12.234.2.3 bla bla <";
		Pattern pattern = Pattern.compile(regexIP);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());	
	}
	
	@Test
	public void testMissingNumberIPFalse() {
		String stringForTest = "gldk<title> bla 3.3.33.  bla bla <";
		Pattern pattern = Pattern.compile(regexIP);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testToMuchNumbersIPFalse() {
		String stringForTest = "gldk<title> bla 3.3.33.2.1  bla bla <";
		Pattern pattern = Pattern.compile(regexIP);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testToBigNumberIPFalse() {
		String stringForTest = "gldk<title> bla 3.3.332.2  bla bla <";
		Pattern pattern = Pattern.compile(regexIP);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testOrdinaryEmailTrue() {
		String stringForTest = "gldk<title> bla bla bla < salica@mail.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());	
	}
	
	@Test
	public void testEmailWithDotTrue() {
		String stringForTest = "gldk<title> bla bla bla < salica.casa@mail.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());	
	}
	
	@Test
	public void testEmailWithUnderlineTrue() {
		String stringForTest = "gldk<title> bla bla bla < salica_casa@mail.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());	
	}
	
	@Test
	public void testEmailWithNumbersTrue() {
		String stringForTest = "gldk<title> bla bla bla < salica3.casa4@mail.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());	
	}
	
	@Test
	public void testEmailWithNumbersFalse() {
		String stringForTest = "gldk<title> bla bla bla < salica.casa4@mail2.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testEmailWithoutMonkeyFalse() {
		String stringForTest = "gldk<title> bla bla bla < salica.casa4mail.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testEmailWithSignsFalse() {
		String stringForTest = "gldk<title> bla bla bla < salica.casa4@mai-l.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}


	@Test
	public void testEmailWithoutNameFalse() {
		String stringForTest = "gldk<title> bla bla bla < @mail.com";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testEmailWithoutEndFalse() {
		String stringForTest = "gldk<title> bla bla bla < salica@mail.";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testEmailWithoutEnd2False() {
		String stringForTest = "gldk<title> bla bla bla < salica@";
		Pattern pattern = Pattern.compile(regexEmail);
		Matcher matcher = pattern.matcher(stringForTest);
		assertFalse(matcher.find());	
	}
	
	@Test
	public void testAllTrue() {
		String stringForTest = "gldk<title> bla bla bla < @mail.com";
		Pattern pattern = Pattern.compile(all);
		Matcher matcher = pattern.matcher(stringForTest);
		assertTrue(matcher.find());	
	}
}
