package application;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import backend.Config;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import util.U;

public class Driver
{
	public static void main(String... cheese)
	{
		// To be replaced with .get(...) when the default config is fleshed out
		// enough
		Config conf = Config.forceDefault(Paths.get(".").resolve("config.json"));

		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
		ScriptEngine engine = factory.getScriptEngine(conf.getFilter());
		Optional<CompiledScript> script = compile(Paths.get(".").resolve("testTank.js"), engine);

		ScriptContext context = new SimpleScriptContext();
		try
		{
			if (script.isPresent())
				script.get().eval(context);
			context.getBindings(ScriptContext.ENGINE_SCOPE).put("printPi", (Runnable) () -> System.out.println(Math.PI));
			U.p(context.getBindings(ScriptContext.ENGINE_SCOPE).entrySet());
			U.p(((ScriptObjectMirror) context.getBindings(ScriptContext.ENGINE_SCOPE).get("nashorn.global")).entrySet());
			engine.eval("doTick(); printPi(); tank.thing()", context);
		} catch (ScriptException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Optional<CompiledScript> compile(Path file, ScriptEngine engine)
	{
		try
		{
			return Optional.of(((Compilable) engine).compile(new FileReader(file.toFile())));
		} catch (FileNotFoundException | ScriptException e)
		{
			U.e("Could not compile " + file, e);
			return Optional.empty();
		}
	}
}
