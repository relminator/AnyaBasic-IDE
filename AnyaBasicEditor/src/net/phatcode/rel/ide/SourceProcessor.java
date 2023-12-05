/********************************************************************
 *  SourceProcessor.java
 * 
 *  Richard Eric Lope BSN RN
 *  http://rel.phatcode.net
 *  
 * License MIT: 
 * Copyright (c) 2023 Richard Eric Lope 

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. (As clarification, there is no
 * requirement that the copyright notice and permission be included in binary
 * distributions of the Software.)

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 *******************************************************************/

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
	    if(tokens != null ) tokens.clear();
	    types.clear();
	    variables.clear();
	    functions.clear();
	    symbols.clear();
    
	}


}
