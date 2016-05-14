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

public class Filter implements ClassFilter
{
	private boolean		blacklist;
	private Set<String>	names;

	public Filter()
	{
		blacklist = false;
		this.names = new HashSet<>();
	}

	public void setToWhiteList()
	{
		this.blacklist = false;
	}

	public void setToBlackList()
	{
		this.blacklist = true;
	}

	public void addClass(Class<?> clazz)
	{
		names.add(clazz.getName());
	}

	@Override
	public boolean exposeToScripts(String classname)
	{
		// XOR finally useful *hue hue hue*
		if (names.contains(classname) ^ blacklist)
			return true;
		return false;
	}

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
