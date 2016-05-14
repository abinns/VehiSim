package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class U
{
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

	public static void e(String txt, Throwable t)
	{
		U.printWithTag("ERROR", U.addErrTxt(txt, t));
	}

	public static void log(String txt)
	{
		U.printWithTag("LOG", txt);
	}

	public static void log(String txt, Throwable t)
	{
		U.printWithTag("LOG", U.addErrTxt(txt, t));
	}

	public static void p(Object obj)
	{
		U.printWithTag("OUTPUT", obj.toString());
	}

	public static void p(String string)
	{
		U.printWithTag("OUTPUT", string);
	}

	private static void printWithTag(String tag, String message)
	{
		StringBuilder s = new StringBuilder();
		s.append("[").append(tag).append("]");
		s.append(message);
		System.err.println(s.toString());
	}

	private static void w(String txt, Throwable t)
	{
		U.printWithTag("WARNING", U.addErrTxt(txt, t));
	}

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
