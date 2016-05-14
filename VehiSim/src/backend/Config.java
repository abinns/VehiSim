package backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jdk.nashorn.api.scripting.ClassFilter;
import util.U;

public class Config
{

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Config get(Path p)
	{
		Config res = getOrDefFromFile(p);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			res.exportTo(p);
		}));
		return res;
	}

	private static Config getOrDefFromFile(Path p)
	{
		Config res;
		try
		{
			res = gson.fromJson(new FileReader(p.toFile()), Config.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e)
		{
			U.log("Unable to load config file " + p.toString() + "; using default config.", e);
			res = new Config();
		}
		return res;
	}

	private void exportTo(Path p)
	{
		U.writeToFile(gson.toJson(this), p);
	}

	private Filter filter;

	/**
	 * Creates the default config, used only internally if there is an issue
	 * loading the configuration from the system.
	 */
	private Config()
	{
		this.filter = Filter.getDefault();
	}

	public ClassFilter getFilter()
	{
		if (filter == null)
			filter = Filter.getDefault();
		return filter;
	}

	public static Config forceDefault(Path p)
	{
		Config res = new Config();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			res.exportTo(p);
		}));
		return res;
	}
}
