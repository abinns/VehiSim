package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jdk.nashorn.api.scripting.ClassFilter;

/**
 * Nicely importable/exportable classfilter, allows for allowing classes by
 * their .class as opposed to requiring a string parameter.
 * 
 * @author Sudo
 */
public class Filter implements ClassFilter
{
	private boolean		blacklist;
	private Set<String>	names;

	/**
	 * Used only internally.
	 */
	private Filter()
	{
		blacklist = false;
		this.names = new HashSet<>();
	}

	/**
	 * Sets this filter to behave as a whitelist
	 */
	public void setToWhiteList()
	{
		this.blacklist = false;
	}

	/**
	 * Sets this filter to behave as a blacklist
	 */
	public void setToBlackList()
	{
		this.blacklist = true;
	}

	/**
	 * Adds the specified class to the filter.
	 * 
	 * @param clazz
	 *            the class to add to the filter
	 */
	public void addClass(Class<?> clazz)
	{
		names.add(clazz.getName());
	}

	/**
	 * Removes the given class from the allowed classes.
	 * 
	 * @param clazz
	 *            the class to remove.
	 */
	public void removeClass(Class<?> clazz)
	{
		names.remove(clazz.getName());
	}

	/**
	 * Determines whether the specified classname should be allowed to be used
	 * by the script.
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
		if (names.contains(classname) ^ blacklist)
			return true;
		return false;
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

		filter.addClass(Map.class);
		filter.addClass(HashMap.class);
		filter.addClass(TreeMap.class);
		filter.addClass(LinkedHashMap.class);

		filter.addClass(List.class);
		filter.addClass(ArrayList.class);

		return filter;
	}
}
