package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Stream;

import jdk.nashorn.api.scripting.ClassFilter;

/**
 * Nicely importable/exportable classfilter, allows for allowing classes by
 * their .class as opposed to requiring a string parameter. Always allows
 * classes in the sim.api.* packages and sub-packages.
 * 
 * @author Sudo
 */
public class Filter implements ClassFilter
{
	/**
	 * Returns a normal whitelist filter, with some normal useful java classes
	 * whitelisted by default.
	 * 
	 * @return a default whitelist filter
	 */
	public static Filter getDefault()
	{
		Filter filter = new Filter();
		filter.setToWhiteList();

		filter.addClass(Set.class);
		filter.addClass(HashSet.class);
		filter.addClass(TreeSet.class);
		filter.addClass(ConcurrentSkipListSet.class);
		filter.addClass(CopyOnWriteArraySet.class);

		filter.addClass(Map.class);
		filter.addClass(ConcurrentNavigableMap.class);
		filter.addClass(HashMap.class);
		filter.addClass(ConcurrentHashMap.class);
		filter.addClass(TreeMap.class);
		filter.addClass(ConcurrentSkipListMap.class);
		filter.addClass(LinkedHashMap.class);

		filter.addClass(List.class);
		filter.addClass(ArrayList.class);
		filter.addClass(CopyOnWriteArrayList.class);

		filter.addClass(Math.class);
		filter.addClass(Stream.class);

		return filter;
	}

	/**
	 * Returns an empty, whitelisted filter.
	 * 
	 * @return an empty whitelist filter
	 */
	public static Filter getEmpty()
	{
		return new Filter();
	}

	private boolean blacklist;

	private Set<String> names;

	/**
	 * Used only internally.
	 */
	private Filter()
	{
		this.blacklist = false;
		this.names = new HashSet<>();
	}

	/**
	 * Adds the specified class to the filter.
	 * 
	 * @param clazz
	 *            the class to add to the filter
	 */
	public void addClass(Class<?> clazz)
	{
		this.names.add(clazz.getName());
	}

	/**
	 * Determines whether the specified classname should be allowed to be used
	 * by the script. Always allows anything in the sim.api.* classes and
	 * packages.
	 */
	@Override
	public boolean exposeToScripts(String classname)
	{
		/*
		 * @formatter:off
		 * | Present in Set |  Whitelist/Blacklist   | --Exposed?-- |
		 * |     TRUE       |     Blacklist (T)      |    FALSE     |
		 * |     TRUE       |     Whitelist (F)      |    TRUE      |
		 * |     FALSE      |     Blacklist (T)      |    TRUE      |
		 * |     FALSE      |     Whitelist (F)      |    FALSE     |
		 * @formatter:on
		 *  results in XOR being the perfect match. For once. *hue hue hue*
		 */
		if (classname.startsWith("sim.api."))
			return true;
		if (this.names.contains(classname) ^ this.blacklist)
			return true;
		return false;
	}

	/**
	 * Removes the given class from the allowed classes.
	 * 
	 * @param clazz
	 *            the class to remove.
	 */
	public void removeClass(Class<?> clazz)
	{
		this.names.remove(clazz.getName());
	}

	/**
	 * Sets this filter to behave as a blacklist
	 */
	public void setToBlackList()
	{
		this.blacklist = true;
	}

	/**
	 * Sets this filter to behave as a whitelist
	 */
	public void setToWhiteList()
	{
		this.blacklist = false;
	}
}
