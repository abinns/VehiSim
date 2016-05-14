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

/**
 * Centralized, self-saving configuration class, handles saving
 * application-configuration state between application runs.
 * 
 * @author Sudo
 */
public class Config
{
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Gets the configuration from the specified file. If the configuration is
	 * invalid for some reason, returns a default configuration instead. Sets up
	 * a shutdown hook to handle saving the configuration out on shutdown.
	 * 
	 * @param p
	 *            the path attempt to load from
	 * @return the configuration specified, or a default one instead.
	 */
	public static Config get(Path p)
	{
		Config res = getOrDefFromFile(p);
		return addExportHook(p, res);
	}

	/**
	 * Returns the configuration passed, adds a shutdown hook to the runtime to
	 * export the config on JVM shutdown.
	 * 
	 * @param p
	 *            the path to export to
	 * @param config
	 *            the config to save out
	 * @return the config passed
	 */
	private static Config addExportHook(Path p, Config config)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			config.exportTo(p);
		}));
		return config;
	}

	/**
	 * Attempts to load a config from the specified file, handles the various
	 * problems, returns either the one from the path provided, or a new one.
	 * 
	 * @param p
	 *            the path to load from
	 * @return the config for that path
	 */
	private static Config getOrDefFromFile(Path p)
	{
		Config res;
		try
		{
			try
			{
				res = gson.fromJson(new FileReader(p.toFile()), Config.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e)
			{
				U.log("Unable to load config file " + p.toString() + "; using default config.", e);
				res = new Config();
			}
		} catch (Exception e)
		{
			U.e("Invalid config file, unable to load from " + p.toString() + "; overwriting with default config.", e);
			res = new Config();
		}
		return res;
	}

	/**
	 * Writes this config to the path provided
	 * 
	 * @param p
	 *            the path to export to
	 */
	private void exportTo(Path p)
	{
		U.writeToFile(gson.toJson(this), p);
	}

	private Filter classFilter;

	/**
	 * Creates the default config, used only internally if there is an issue
	 * loading the configuration from the system, or if
	 * {@link #forceDefault(Path)} is used.
	 */
	private Config()
	{
		this.classFilter = Filter.getDefault();
	}

	/**
	 * Returns the configured class filter.
	 * 
	 * @return
	 */
	public ClassFilter getFilter()
	{
		if (classFilter == null)
			classFilter = Filter.getDefault();
		return classFilter;
	}

	/**
	 * Loads a default config, setup to save to the specified path, adding a
	 * shutdown hook for the save in the process.
	 * 
	 * @param p
	 *            the path to export to
	 * @return a default configuration
	 */
	public static Config forceDefault(Path p)
	{
		Config res = new Config();
		return addExportHook(p, res);
	}
}
