package sim.vehi;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;

public class Part implements ScriptContext
{

	@Override
	public void setBindings(Bindings bindings, int scope)
	{
		switch (scope)
		{
			case ScriptContext.ENGINE_SCOPE:
				break;
		}
	}

	@Override
	public Bindings getBindings(int scope)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, Object value, int scope)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAttribute(String name, int scope)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeAttribute(String name, int scope)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAttributesScope(String name)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Writer getWriter()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer getErrorWriter()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWriter(Writer writer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setErrorWriter(Writer writer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Reader getReader()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReader(Reader reader)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<Integer> getScopes()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
