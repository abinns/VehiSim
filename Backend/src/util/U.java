package util;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class U
{

	private static final PrintStream out;

	static
	{
		out = System.err;
	}

	/**
	 * Given a piece of text and a {@link Throwable}, this handles adding the
	 * full stack trace of that error to the existing error message. If the
	 * throwable is null, returns the passed text.
	 * 
	 * @param txt
	 *            the pre-existing error message
	 * @param t
	 *            the passed throwable, ignored if null.
	 * @return the original error message, plus a full stack trace of the passed
	 *         error. If null error, returns input message.
	 */
	private static String addErrTxt(String txt, Throwable t)
	{
		if (t == null)
			return txt;
		StringBuilder s = new StringBuilder(txt);
		s.append("\n").append(t.getClass().getName()).append(": ").append(t.getMessage());
		for (StackTraceElement cur : t.getStackTrace())
			s.append("\n").append(cur.toString());
		return s.toString();
	}

	/**
	 * Prints the given error text, along with the stack tracr from the provided
	 * error. If throwable is null, it is ignored.
	 * 
	 * @param txt
	 *            the text to print
	 * @param t
	 *            the error that caused the problem
	 */
	public static void e(String txt, Throwable t)
	{
		U.printWithTag("ERROR", U.addErrTxt(txt, t));
	}

	/**
	 * Prints the given text out as a log message.
	 * 
	 * @param txt
	 *            the text to print
	 */
	public static void log(String txt)
	{
		log(txt, null);
	}

	/**
	 * Prints the given message as a log, along with the full stack trace of the
	 * given error. Error ie ignored if null.
	 * 
	 * @param txt
	 *            the log message to print
	 * @param t
	 *            the error message to be printed
	 */
	public static void log(String txt, Throwable t)
	{
		U.printWithTag("LOG", U.addErrTxt(txt, t));
	}

	/**
	 * Prints the given object to output, using the object's {@link #toString()}
	 * method.
	 * 
	 * @param obj
	 *            the object to print
	 */
	public static void p(Object obj)
	{
		U.printWithTag("OUTPUT", obj.toString());
	}

	/**
	 * Basic date-time formatter, formats in the type:
	 * 
	 * <pre>
	 * 1980 Jan 01 01:23:45.678 AM
	 * </pre>
	 * 
	 * Assumes default ZoneId and Locale.
	 */
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM dd hh:mm:ss.SSS a").withZone(ZoneId.systemDefault()).withLocale(Locale.getDefault());

	private static void printWithTag(String tag, String message)
	{
		StringBuilder s = new StringBuilder();
		s.append("[").append(formatter.format(Instant.now())).append("]");
		s.append("[").append(tag).append("]");
		s.append(message);
		U.out.println(s.toString());
	}

	/**
	 * Prints the given warning text, as well as the stack trace of the
	 * specified error. If the error is null, it is ignored.
	 * 
	 * @param txt
	 *            The text to print
	 * @param t
	 *            the error to print the stack trace of
	 */
	private static void w(String txt, Throwable t)
	{
		U.printWithTag("WARNING", U.addErrTxt(txt, t));
	}

	/**
	 * Attempts to write the given text to disk, though only printing a warning
	 * if unable.
	 * 
	 * @param txt
	 *            the text to write
	 * @param path
	 *            the path to the file to put it.
	 */
	public static void writeToFile(String txt, Path path)
	{
		try
		{
			Files.write(path, txt.getBytes());
		} catch (IOException e)
		{
			U.w("Unable to write data to file " + path, e);
		}
	}
}
