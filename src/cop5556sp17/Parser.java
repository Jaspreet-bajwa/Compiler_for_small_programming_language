package cop5556sp17;

import static cop5556sp17.Scanner.Kind.*;

import java.util.ArrayList;
import java.util.HashMap;
import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.*;
import cop5556sp17.SymbolTable;

public class Parser {

	public HashMap<String, String> predictMap = new HashMap<>();
	public SymbolTable symtab = new SymbolTable();

	public void addHashMapValues() {
		predictMap.put("program", "IDENT");
		predictMap.put("paramDec", "KW_URL KW_FILE KW_INTEGER KW_BOOLEAN");
		predictMap.put("block", "LBRACE");
		predictMap.put("dec", "KW_INTEGER KW_BOOLEAN KW_IMAGE KW_FRAME");
		predictMap.put("statement",
				"OP_SLEEP KW_WHILE KW_IF IDENT OP_BLUR OP_GRAY OP_CONVOLVE KW_SHOW KW_HIDE KW_MOVE KW_XLOC KW_YLOC OP_WIDTH OP_HEIGHT KW_SCALE");
		predictMap.put("assign", "IDENT");
		predictMap.put("whileStatement", "KW_WHILE");
		predictMap.put("ifStatement", "KW_IF");
		predictMap.put("arrowOp", "ARROW BARARROW");
		predictMap.put("chainElem",
				"IDENT OP_BLUR OP_GRAY OP_CONVOLVE KW_SHOW KW_HIDE KW_MOVE KW_XLOC KW_YLOC OP_WIDTH OP_HEIGHT KW_SCALE");
		predictMap.put("filterOp", "OP_BLUR OP_GRAY OP_CONVOLVE");
		predictMap.put("frameOp", "KW_SHOW KW_HIDE KW_MOVE KW_XLOC KW_YLOC");
		predictMap.put("imageOp", "OP_WIDTH OP_HEIGHT KW_SCALE");
		predictMap.put("arg", "LPAREN ARROW BARARROW");
		predictMap.put("factor", "IDENT INT_LIT KW_TRUE KW_FALSE KW_SCREENWIDTH KW_SCREENHEIGHT LPAREN");
		predictMap.put("relOp", "LT LE GT GE EQUAL NOTEQUAL");
		predictMap.put("weakOp", "PLUS MINUS OR");
		predictMap.put("strongOp", "TIMES DIV AND MOD");
	}
	// jaspreet ends

	/**
	 * Exception to be thrown if a syntax error is detected in the input. You
	 * will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}

	/**
	 * Useful during development to ensure unimplemented routines are not
	 * accidentally called during development. Delete it when the Parser is
	 * finished.
	 *
	 */
	@SuppressWarnings("serial")
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		addHashMapValues();
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner. Check for EOF (i.e. no
	 * trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	Program parse() throws SyntaxException {
		Program pgm = null;
		pgm = program();
		matchEOF();
		return pgm;
	}

	Expression expression() throws SyntaxException {
		// TODO
		Expression e0 = null;
		Expression e1 = null;
		Token op = null;
		e0 = term();
		Token firstToken = e0.getFirstToken();
		while (predictMap.get("relOp").indexOf(t.kind.toString()) >= 0) {
			op = relOp();
			e1 = term();
			e0 = new BinaryExpression(firstToken, e0, op, e1);
		}
		return e0;

	}

	Token relOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("relOp").indexOf(t.kind.toString()) >= 0) {
			Token op = t;
			consume();
			return op;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Expression term() throws SyntaxException {
		// TODO
		Expression e0 = null;
		Expression e1 = null;
		Token op = null;
		e0 = elem();
		Token firstToken = e0.getFirstToken();
		while (predictMap.get("weakOp").indexOf(t.kind.toString()) >= 0) {
			op = weakOp();
			e1 = elem();
			e0 = new BinaryExpression(firstToken, e0, op, e1);
		}
		return e0;
	}

	Token weakOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("weakOp").indexOf(t.kind.toString()) >= 0) {
			Token op = t;
			consume();
			return op;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Expression elem() throws SyntaxException {
		// TODO
		Expression e0 = null;
		Expression e1 = null;
		Token op = null;
		e0 = factor();
		Token firstToken = e0.getFirstToken();
		while (predictMap.get("strongOp").indexOf(t.kind.toString()) >= 0) {
			op = strongOp();
			e1 = factor();
			e0 = new BinaryExpression(firstToken, e0, op, e1);
		}
		return e0;
	}

	Token strongOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("strongOp").indexOf(t.kind.toString()) >= 0) {
			Token op = t;
			consume();
			return op;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Expression factor() throws SyntaxException {
		Expression e = null;
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			e = new IdentExpression(t);
			consume();
		}
			break;
		case INT_LIT: {
			e = new IntLitExpression(t);
			consume();
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			e = new BooleanLitExpression(t);
			consume();
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			e = new ConstantExpression(t);
			consume();
		}
			break;
		case LPAREN: {
			consume();
			e = expression();
			match(RPAREN);
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal factor" + t.getText());
		}
		return e;
	}

	Block block() throws SyntaxException {
		// symtab.enterScope();
		Block block = null;
		ArrayList<Dec> listDec = new ArrayList<Dec>();
		ArrayList<Statement> listStmt = new ArrayList<Statement>();
		Dec dec = null;
		Statement stmt = null;
		if (t.isKind(LBRACE)) {
			Token firstToken = t;
			consume();
			while (!t.kind.toString().equalsIgnoreCase("EOF")) {
				while (predictMap.get("dec").indexOf(t.kind.toString()) >= 0) {
					dec = dec();
					listDec.add(dec);
				}
				while (predictMap.get("statement").indexOf(t.kind.toString()) >= 0) {
					stmt = statement();
					listStmt.add(stmt);
				}
				if (t.isKind(RBRACE)) {
					consume();
					block = new Block(firstToken, listDec, listStmt);
					return block;
				}
			}
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Program program() throws SyntaxException {
		// TODO
		Token nextToken = scanner.peek();
		Program prgm = null;
		ArrayList<ParamDec> listParamDec = new ArrayList<ParamDec>();
		Block block = null;
		Token firstToken = t;
		if (t.isKind(IDENT)) {
			consume();
			if (predictMap.get("block").indexOf(nextToken.kind.toString()) >= 0) {
				block = block();
				prgm = new Program(firstToken, listParamDec, block);
				return prgm;
			} else if (predictMap.get("paramDec").indexOf(nextToken.kind.toString()) >= 0) {
				ParamDec paramdec = null;
				paramdec = paramDec();
				listParamDec.add(paramdec);
				while (t.isKind(COMMA)) {
					consume();
					paramdec = paramDec();
					listParamDec.add(paramdec);
				}
				block = block();
				prgm = new Program(firstToken, listParamDec, block);
				return prgm;
			}
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	ParamDec paramDec() throws SyntaxException {
		// TODO
		ParamDec paramDec = null;
		if (predictMap.get("paramDec").indexOf(t.kind.toString()) >= 0) {
			Token firstToken = t;
			consume();
			if (t.isKind(IDENT)) {
				Token op = t;
				consume();
				paramDec = new ParamDec(firstToken, op);
				return paramDec;
			} else
				throw new SyntaxException("Illegal Character:" + t.getText());
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Dec dec() throws SyntaxException {
		// TODO
		Dec dec = null;
		if (predictMap.get("dec").indexOf(t.kind.toString()) >= 0) {
			Token firstToken = t;
			consume();
			if (t.isKind(IDENT)) {
				Token op = t;
				consume();
				dec = new Dec(firstToken, op);
				return dec;
			} else
				throw new SyntaxException("Illegal Character:" + t.getText());
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Statement statement() throws SyntaxException {
		Statement stmt = null;
		Expression expr = null;
		Token firstToken = null;
		if (t.isKind(OP_SLEEP)) {
			firstToken = t;
			consume();
			expr = expression();
			if (t.isKind(SEMI)) {
				consume();
				stmt = new SleepStatement(firstToken, expr);
				return stmt;
			}
			throw new SyntaxException("Illegal Character:" + t.getText());
		} else if (t.isKind(KW_WHILE)) {
			Block block = null;
			firstToken = t;
			consume();
			if (t.isKind(LPAREN)) {
				consume();
				expr = expression();
				if (t.isKind(RPAREN)) {
					consume();
					block = block();
					stmt = new WhileStatement(firstToken, expr, block);
					return stmt;
				}
				throw new SyntaxException("Illegal Character:" + t.getText());
			}
			throw new SyntaxException("Illegal Character:" + t.getText());
		} else if (t.isKind(KW_IF)) {
			Block block = null;
			firstToken = t;
			consume();
			if (t.isKind(LPAREN)) {
				consume();
				expr = expression();
				if (t.isKind(RPAREN)) {
					consume();
					block = block();
					stmt = new IfStatement(firstToken, expr, block);
					return stmt;
				}
				throw new SyntaxException("Illegal Character:" + t.getText());
			}
			throw new SyntaxException("Illegal Character:" + t.getText());
		} else if (predictMap.get("chainElem").indexOf(t.kind.toString()) >= 0 && !t.isKind(IDENT)) {
			Chain chain = null;
			chain = chain();
			if (t.isKind(SEMI)) {
				consume();
				return chain;
			}
			throw new SyntaxException("Illegal Character:" + t.getText());
		} else if (t.isKind(IDENT)) {
			Token nexttoken = scanner.peek();
			Chain chain = null;
			IdentLValue identLvalue = null;
			firstToken = t;
			if (nexttoken.isKind(ASSIGN)) {
				consume();
				identLvalue = new IdentLValue(firstToken);
				consume();
				expr = expression();
				if (t.isKind(SEMI)) {
					consume();
					stmt = new AssignmentStatement(firstToken, identLvalue, expr);
					return stmt;
				}
				throw new SyntaxException("Illegal Character:" + t.getText());
			} else if (!nexttoken.isKind(ASSIGN)) {
				chain = chain();
				/*
				 * op = arrowOp(); chainElem = chainElem(); chain = new
				 * BinaryChain(firstToken, chain, op, chainElem);
				 * while(predictMap.get("arrowOp").indexOf(t.kind.toString())>=0
				 * ){ op = arrowOp(); chainElem = chainElem(); chain = new
				 * BinaryChain(firstToken, chain, op, chainElem); }
				 */
				if (t.isKind(SEMI)) {
					consume();
					return chain;
				}
				throw new SyntaxException("Illegal Character:" + t.getText());
			}
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Chain chain() throws SyntaxException {
		Chain chain = null;
		ChainElem chainElem = null;
		Token firstToken = null, op = null;
		if (predictMap.get("chainElem").indexOf(t.kind.toString()) >= 0) {
			firstToken = t;
			chain = chainElem();
			op = arrowOp();
			chainElem = chainElem();
			chain = new BinaryChain(firstToken, chain, op, chainElem);
			while (predictMap.get("arrowOp").indexOf(t.kind.toString()) >= 0) {
				op = arrowOp();
				chainElem = chainElem();
				chain = new BinaryChain(firstToken, chain, op, chainElem);
			}
			return chain;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Token arrowOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("arrowOp").indexOf(t.kind.toString()) >= 0) {
			Token firstToken = t;
			consume();
			return firstToken;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Token filterOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("filterOp").indexOf(t.kind.toString()) >= 0) {
			Token op = t;
			consume();
			return op;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Token frameOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("frameOp").indexOf(t.kind.toString()) >= 0) {
			Token op = t;
			consume();
			return op;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Token imageOp() throws SyntaxException {
		// TODO Auto-generated method stub
		if (predictMap.get("imageOp").indexOf(t.kind.toString()) >= 0) {
			Token op = t;
			consume();
			return op;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	AssignmentStatement assign() throws SyntaxException {
		// TODO
		AssignmentStatement assign = null;
		IdentLValue ident = null;
		Expression expr = null;
		if (t.isKind(IDENT)) {
			Token firstToken = t;
			consume();
			ident = new IdentLValue(firstToken);
			if (t.isKind(ASSIGN)) {
				consume();
				expr = expression();
				assign = new AssignmentStatement(firstToken, ident, expr);
				return assign;
			}
			throw new SyntaxException("Illegal Character:" + t.getText());
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	ChainElem chainElem() throws SyntaxException {
		ChainElem chainElem = null;
		Token op = null;
		Tuple tuple = null;
		if (t.isKind(IDENT)) {
			chainElem = new IdentChain(t);
			consume();
			return chainElem;
		} else if (predictMap.get("filterOp").indexOf(t.kind.toString()) >= 0) {
			op = filterOp();
			tuple = arg();
			chainElem = new FilterOpChain(op, tuple);
			return chainElem;
		} else if (predictMap.get("frameOp").indexOf(t.kind.toString()) >= 0) {
			op = frameOp();
			tuple = arg();
			chainElem = new FrameOpChain(op, tuple);
			return chainElem;
		} else if (predictMap.get("imageOp").indexOf(t.kind.toString()) >= 0) {
			op = imageOp();
			tuple = arg();
			chainElem = new ImageOpChain(op, tuple);
			return chainElem;
		}
		throw new SyntaxException("Illegal Character:" + t.getText());
	}

	Tuple arg() throws SyntaxException {
		// TODO
		Tuple tuple = null;
		ArrayList<Expression> list = new ArrayList<Expression>();
		Expression expr1 = null;
		Token firstToken = t;
		if (t.isKind(LPAREN)) {
			consume();
			expr1 = expression();
			list.add(expr1);
			while (t.isKind(COMMA)) {
				Expression exp = null;
				consume();
				exp = expression();
				list.add(exp);
			}
			if (t.isKind(RPAREN)) {
				consume();
				tuple = new Tuple(firstToken, list);
				return tuple;
			} else {
				throw new SyntaxException("Illegal Character:" + t.getText());
			}
		} else {
			tuple = new Tuple(firstToken, list);
			return tuple;
		}
	}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.isKind(EOF)) {
			return t;
		} else {
			throw new SyntaxException("expected EOF");
		}
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.isKind(kind)) {
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		return null; // replace this statement
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}
