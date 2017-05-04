package cop5556sp17;

import java.util.ArrayList;
import java.util.HashMap;

public class Scanner {
	/**
	 * Kind enum
	 */

	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), KW_IMAGE("image"), KW_URL("url"), KW_FILE(
				"file"), KW_FRAME("frame"), KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), SEMI(
						";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), RBRACE("}"), ARROW("->"), BARARROW(
								"|->"), OR("|"), AND("&"), EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(
										">="), PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), ASSIGN(
												"<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE(
														"convolve"), KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH(
																"screenwidth"), OP_WIDTH("width"), OP_HEIGHT(
																		"height"), KW_XLOC("xloc"), KW_YLOC(
																				"yloc"), KW_HIDE("hide"), KW_SHOW(
																						"show"), KW_MOVE(
																								"move"), OP_SLEEP(
																										"sleep"), KW_SCALE(
																												"scale"), EOF(
																														"eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}

	public static enum State {
		START, IN_IDENT, IN_DIGIT, AFTER_EQ, COMMENT;
	}

	public HashMap<String, String> addHashmapValues() {
		HashMap<String, String> keywords = new HashMap<String, String>();
		// adding keywords starts
		keywords.put("integer", "KW_INTEGER");
		keywords.put("boolean", "KW_BOOLEAN");
		keywords.put("image", "KW_IMAGE");
		keywords.put("url", "KW_URL");
		keywords.put("file", "KW_FILE");
		keywords.put("frame", "KW_FRAME");
		keywords.put("while", "KW_WHILE");
		keywords.put("if", "KW_IF");
		keywords.put("sleep", "OP_SLEEP");
		keywords.put("screenheight", "KW_SCREENHEIGHT");
		keywords.put("screenwidth", "KW_SCREENWIDTH");
		// adding keywords end

		// adding frame op keyword starts
		keywords.put("xloc", "KW_XLOC");
		keywords.put("yloc", "KW_YLOC");
		keywords.put("hide", "KW_HIDE");
		keywords.put("show", "KW_SHOW");
		keywords.put("move", "KW_MOVE");
		// adding frame op keyword ends

		// adding filter op keyword starts
		keywords.put("gray", "OP_GRAY");
		keywords.put("convolve", "OP_CONVOLVE");
		keywords.put("blur", "OP_BLUR");
		keywords.put("scale", "KW_SCALE");
		// adding filter op keyword ends

		// adding image op keyword starts
		keywords.put("width", "OP_WIDTH");
		keywords.put("height", "OP_HEIGHT");
		// adding image op keyword ends

		// adding boolean literal starts
		keywords.put("true", "KW_TRUE");
		keywords.put("false", "KW_FALSE");
		// adding boolean literal ends
		return keywords;
	}

	/**
	 * Thrown by Scanner when an illegal character is encountered
	 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}

	/**
	 * Thrown by Scanner when an int literal is not a value that can be
	 * represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
		public IllegalNumberException(String message) {
			super(message);
		}
	}

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;

		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}

	public class Token {
		public final Kind kind;
		public final int pos; // position in input array
		public final int length;

		// returns the text of this Token
		public String getText() {
			// TODO IMPLEMENT THIS
			String text = chars.substring(pos, pos + length);
			return text;
		}

		// returns a LinePos object representing the line and column of this
		// Token
		LinePos getLinePos() {
			LinePos linepos = Scanner.this.getLinePos(this);
			return linepos;
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		/**
		 * Precondition: kind = Kind.INT_LIT, the text can be represented with a
		 * Java int. Note that the validity of the input should have been
		 * checked when the Token was created. So the exception should never be
		 * thrown.
		 * 
		 * @return int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException {
			// TODO IMPLEMENT THIS
			int value = Integer.parseInt(chars.substring(pos, pos + length));
			return value;
		}

		public boolean isKind(Kind str) {
			// TODO Auto-generated method stub
			if(this.kind.equals(str))
				return true;
			else 
				return false;
		}

		//assignment code added 
		
		@Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }

		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }

		 

		  private Scanner getOuterType() {
		   return Scanner.this;
		  }
		
		//assignment code ended
	}

	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();

	}

	/**
	 * Initializes Scanner object by traversing chars and adding tokens to
	 * tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		int pos = 0;
		// TODO IMPLEMENT THIS!!!!

		HashMap<String, String> keywords = addHashmapValues();
		int length = chars.length();
		State state = State.START;
		int startPos = 0;
		int ch;
		lineNumber.add(0, 0);
		int i = 1;

		while (pos <= length) {
			ch = pos < length ? chars.charAt(pos) : -1;

			switch (state) {
			// START starts
			case START: {
				if (pos < length) {
					pos = skipWhiteSpace(pos);
					startPos = pos;
				}
				ch = pos < length ? chars.charAt(pos) : -1;

				// ch starts
				switch (ch) {
				case -1: {
					tokens.add(new Token(Kind.EOF, pos, 0));
					lineNumber.add(i, pos);
					pos++;
				}
					break;
				case '+': {
					tokens.add(new Token(Kind.PLUS, startPos, 1));
					pos++;
				}
					break;
				case '&': {
					tokens.add(new Token(Kind.AND, startPos, 1));
					pos++;
				}
					break;
				case '%': {
					tokens.add(new Token(Kind.MOD, startPos, 1));
					pos++;
				}
					break;
				case '*': {
					tokens.add(new Token(Kind.TIMES, startPos, 1));
					pos++;
				}
					break;
				case ';': {
					tokens.add(new Token(Kind.SEMI, startPos, 1));
					pos++;
				}
					break;
				case ',': {
					tokens.add(new Token(Kind.COMMA, startPos, 1));
					pos++;
				}
					break;
				case '(': {
					tokens.add(new Token(Kind.LPAREN, startPos, 1));
					pos++;
				}
					break;
				case ')': {
					tokens.add(new Token(Kind.RPAREN, startPos, 1));
					pos++;
				}
					break;
				case '{': {
					tokens.add(new Token(Kind.LBRACE, startPos, 1));
					pos++;
				}
					break;
				case '}': {
					tokens.add(new Token(Kind.RBRACE, startPos, 1));
					pos++;
				}
					break;
				case '=': {
					if ((pos + 1) == chars.length()) {
						throw new IllegalCharException("A single = is not allowed in the language. Error at character "
								+ ch + " at pos" + pos);
					} else {
						state = State.AFTER_EQ;
						pos++;
					}
				}
					break;
				case '0': {
					tokens.add(new Token(Kind.INT_LIT, startPos, 1));
					pos++;
				}
					break;
				case '/': {
					if ((pos + 1) != chars.length()) {
						if (chars.charAt(pos + 1) == '*') {
							state = State.COMMENT;
							pos += 1;
						} else {
							tokens.add(new Token(Kind.DIV, startPos, 1));
							pos += 1;
						}
					} else {
						tokens.add(new Token(Kind.DIV, startPos, 1));
						pos += 1;
					}
				}
					break;
				case '!': {
					if ((pos + 1) != chars.length()) {
						if (chars.charAt(pos + 1) == '=') {
							tokens.add(new Token(Kind.NOTEQUAL, startPos, (pos - startPos) + 2));
							pos += 2;
						} else {
							tokens.add(new Token(Kind.NOT, startPos, 1));
							pos += 1;
						}
					} else {
						tokens.add(new Token(Kind.NOT, startPos, 1));
						pos += 1;
					}
				}
					break;
				case '<': {
					if ((pos + 1) != chars.length()) {
						if (chars.charAt(pos+1) == '=') {
							tokens.add(new Token(Kind.LE, startPos, (pos - startPos) + 2));	
							pos+=2;
						} else if (chars.charAt(pos+1) == '-') {
							tokens.add(new Token(Kind.ASSIGN, startPos, (pos - startPos) + 2));
							pos+=2;
						} else {
							tokens.add(new Token(Kind.LT, startPos, 1));
							pos+=1;
						}
					} else {
						tokens.add(new Token(Kind.LT, startPos, 1));
						pos+=1;
					}
				
				}
					break;
				case '>': {
					if ((pos+1) != chars.length()) {
						if (chars.charAt(pos+1) == '=') {
							tokens.add(new Token(Kind.GE, startPos, (pos - startPos) + 2));
							pos+=2;
						} else {
							tokens.add(new Token(Kind.GT, startPos, 1));
							pos+=1;
						}
					} else {
						tokens.add(new Token(Kind.GT, startPos, 1));
						pos+=1;
					}
					
				}
					break;
				case '-': {
					if ((pos + 1) != chars.length()) {
						if (chars.charAt(pos + 1) == '>') {
							tokens.add(new Token(Kind.ARROW, startPos, (pos - startPos) + 2));
							pos += 2;
						} else {
							tokens.add(new Token(Kind.MINUS, startPos, 1));
							pos += 1;
						}
					} else {
						tokens.add(new Token(Kind.MINUS, startPos, 1));
						pos += 1;
					}
				}
					break;
				case '|': {
					if (pos < chars.length() - 2) {
						if (chars.charAt(pos + 1) == '-' && chars.charAt(pos + 2) == '>') {
							tokens.add(new Token(Kind.BARARROW, startPos, (pos - startPos) + 3));
							pos += 2;
						} else {
							tokens.add(new Token(Kind.OR, startPos, 1));
						}
					} else {
						tokens.add(new Token(Kind.OR, startPos, 1));
					}
					pos += 1;
				}
					break;
				case '\n': {
					lineNumber.add(i, pos);
					pos++;
					i++;
					state = State.START;
				}
					break;
				default: {
					if (Character.isDigit(ch)) {
						state = State.IN_DIGIT;
						pos++;
					} else if (Character.isJavaIdentifierStart(ch)) {
						state = State.IN_IDENT;
						pos++;
					} else {
						throw new IllegalCharException(
								"illegal char " + ch + " whose value is " + chars.charAt(pos) + " at pos" + pos);
					}
				}
				}
				// ch ends
			}
				break;
			// start ends

			// in digit starts
			case IN_DIGIT: {
				if (Character.isDigit(ch)) {
					pos++;
				} else {
					if (pos - startPos >= 10) {
						throw new IllegalNumberException(
								"The value exceeeds the maximum integer range permitted in java");
					} else {
						tokens.add(new Token(Kind.INT_LIT, startPos, (pos - startPos)));
						state = State.START;
					}
				}
			}
				break;
			// in digit ends

			// in ident starts
			case IN_IDENT: {
				if (Character.isJavaIdentifierPart(ch)) {
					pos++;
				} else {
					if (keywords.containsKey(chars.substring(startPos, pos))) {
						String value = keywords.get(chars.substring(startPos, pos));
						tokens.add(new Token(Kind.valueOf(value), startPos, (pos - startPos)));
					} else {
						tokens.add(new Token(Kind.IDENT, startPos, (pos - startPos)));
					}
					state = State.START;
				}

			}
				break;
			// in ident ends

			// after_eq starts
			case AFTER_EQ: {
				if (!(chars.charAt(pos) == '=')) {
					throw new IllegalCharException(
							"A single = is not allowed in the language. Error at character " + ch + " at pos" + pos);
				} else {
					tokens.add(new Token(Kind.EQUAL, startPos, (pos - startPos) + 1));
					state = State.START;
					pos++;
				}
			}
				break;
			// after_eq ends

			// comment starts
			case COMMENT: {
				try {
					while (!(chars.charAt(pos) == '*' && chars.charAt(pos + 1) == '/')) {
						if(chars.charAt(pos)=='\n'){
							lineNumber.add(i, pos);
							i++;
						}
						pos++;
					}
					state = State.START;
					pos = pos + 2;
				} catch (StringIndexOutOfBoundsException e) {
					state = State.START;
				}
			}
				break;
			// comment ends
			default:
				assert false;

			}

		}

		tokens.add(new Token(Kind.EOF,pos,0));
		return this;
	}

	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;
	ArrayList<Integer> lineNumber = new ArrayList<Integer>();

	/*
	 * Return the next token in the token list and update the state so that the
	 * next call will return the Token..
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}

	/*
	 * Return the next token in the token list without updating the state. (So
	 * the following call to next will return the same token.)
	 */
	public Token peek() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);
	}

	/**
	 * Returns a LinePos object containing the line and position in line of the
	 * given token.
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		// TODO IMPLEMENT THIS
		int position = t.pos;
		int line = 0;
		int posLine = 0;
		int start = 0;

		while (start < lineNumber.size()) {
			if (position <= lineNumber.get(start)) {
				if (start == 0) {
					line = start;
					posLine = position;
				} else {
					line = start - 1;
					if (line == 0) {
						posLine = position - lineNumber.get(start - 1);
					} else {
						posLine = position - lineNumber.get(start - 1) - 1;
					}
				}
				break;
			} else {
				start += 1;
			}

		}
		return new LinePos(line, posLine);
	}

	public int skipWhiteSpace(int pos) {
		
		while (((chars.charAt(pos) == ' ') || (chars.charAt(pos) == '\t') || (chars.charAt(pos) == '\r'))){
			pos++;
			if(pos>=chars.length()) break;
		}			
		return pos;
	}
}
