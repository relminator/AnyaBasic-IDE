package net.phatcode.rel.ide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceProcessor
{
	private Lexer lexer = new Lexer();
	private List<Token> tokens;
	private List<String> types = new ArrayList<>();
	private List<String> variables = new ArrayList<>();
	private List<String> functions = new ArrayList<>();
	private Map<String, Token> symbols =new HashMap<>();  // global variables


	public Map<String, Token> getSymbols()
	{
		return symbols;
	}

	public SourceProcessor()
	{
		super();
	}
	
	public List<String> getTypes()
	{
		return types;
	}

	public List<String> getVariables()
	{
		return variables;
	}

	
	public List<String> getFunctions()
	{
		return functions;
	}

	
	public void tokenize( String source )
	{
	    types.clear();
	    functions.clear();
	    variables.clear();
	    symbols.clear();
	    
		tokens = lexer.tokenize(source);
	}
	
	public void printTokens()
	{
		symbols.forEach((key, value) -> System.out.println(key + " " + value));
	}
	
	public void processTokens()
	{
		for( int token = 0; token < tokens.size(); token++ )
		{
			Token next;
			switch( tokens.get(token).getType() )
			{
				case TYPE:
					next = tokens.get(++token);
					types.add(next.getTextOriginal());
					symbols.put(next.getTextOriginal(), next);
					//System.out.println("" + tokens.get(token).getText()); 
					//System.out.println("row = " + tokens.get(token).getRow()); 
					//System.out.println("col = " + tokens.get(token).getCol()); 
					break;
				case VAR:
					next = tokens.get(++token);
					variables.add(next.getTextOriginal());
					symbols.put(next.getTextOriginal(), next);
					break;
				case FUNCTION:
					next = tokens.get(++token);
					functions.add(next.getTextOriginal());
					symbols.put(next.getTextOriginal(), next);
					break;
			default:
				break;
			}
		}

	}
	
	public void reset()
	{
	    lexer.reset();
	    tokens.clear();
	    types.clear();
	    variables.clear();
	    functions.clear();
	    symbols.clear();
    
	}


}
