package net.phatcode.rel.ide;



public class Token
{

	public enum Type
	{
		PLUS,
		MINUS,
		ASTERISK,
		SLASH,
		PERCENT,
		CARET,
		LEFT_PAREN,
		RIGHT_PAREN,
        LEFT_BRACE,
        RIGHT_BRACE,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        NUMBER,
		STRING,
		KEYWORD,
		OR,
		AND,
		EQUAL,
		NOTEQUAL,
		LESS,
		GREATER,
		LESSEQUAL,
		GREATEREQUAL,
		ASSIGNMENT,
		NOT,
		PRINT,
		WRITE,
		DELAY,
		INPUT,
		START_BLOCK,
		END_BLOCK,
		WHILE,
		IF,
		THEN,
		ELSEIF,
		ELSE,
		FOR,
		TO,
		STEP,
		REPEAT,
		FREE,
		COMMA,
		COLON,
		COMMENT,
		FUNCTION,
		TYPE,
        DOT,
		SCREEN,
        SCREEN_FLIP,
        SCREEN_CLEAR,
        DRAW_LINE,
		DRAW_ELLIPSE,
		DRAW_STRING,
		SET_COLOR,
		SET_BLENDMODE,
		LOAD_IMAGE,
		DRAW_IMAGE,
		DRAW_TRANSFORMED_IMAGE,
        DRAW_SCROLLED_IMAGE,
        DRAW_FANCY_LINE,
		GET_SUB_IMAGE,
		SOUND_INIT,
		SOUND_LOAD,
		SOUND_PLAY,
		SOUND_PAUSE,
		SOUND_STOP,
		RETURN,
        SWAP,
		BREAK,
		EXIT,
		VAR,
		UNKNOWN
	}

	private Type type;
	private String text;
	private String textOriginal;
	private int row;
	private int col;
	
	Token( Type type, String text )
	{
		this.type = type;
		this.text = text.toLowerCase();
		this.textOriginal = text;
	}

	Token( Type type, String text, int row, int col )
	{
		this.type = type;
		this.text = text.toLowerCase();
		this.textOriginal = text;
		this.row = row;
		this.col = col;
	}

	public String toString()
	{
		return "Type: " + type + "--- Text: " + textOriginal;
	}

	public Type getType()
	{
		return type;
	}

	public String getText()
	{
		return text;
	}
	public int getRow()
	{
		return row;
	}

	public int getCol()
	{
		return col;
	}

	String getTextOriginal()
	{
		return textOriginal;
	}

}
