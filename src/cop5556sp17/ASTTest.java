package cop5556sp17;

import static cop5556sp17.Scanner.Kind.*;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.AST.*;

public class ASTTest {

	static final boolean doPrint = true;

	static void show(Object s) {
		if (doPrint) {
			System.out.println(s);
		}
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();
/*
	@Test
	public void testProgram() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc boolean sum , url jaspreet , url jaspreet {image abc}"; // "abc {integer cbv sleep abc; while (true){abc<-true;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class, ast.getClass());
	}

	@Test
	public void testParamDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "boolean abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.paramDec();
		assertEquals(ParamDec.class, ast.getClass());
	} */

	
	@Test
	public void testBlock() throws IllegalCharException, IllegalNumberException, SyntaxException {
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height -> x; frame f i -> scale (x) -> f;}";
		String input = "abc\n{integer x\nif(true){integer x}\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.parse();
		assertEquals(Program.class, ast.getClass());
	} 
	
	
	/*
	@Test
	public void testDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "frame abd345";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.dec();
		assertEquals(Dec.class, ast.getClass());
	}

	@Test
	public void testStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sleep screenheight;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(SleepStatement.class, ast.getClass());
	}

	@Test
	public void testSleepStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sleep (123);";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(SleepStatement.class, ast.getClass());
	}

	@Test
	public void testAssignmentStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sum <- (true);";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(AssignmentStatement.class, ast.getClass());
	}
	
	@Test
	public void testChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "scale -> abc |-> c123;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(BinaryChain.class, ast.getClass());
	}

	@Test
	public void testIdentChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abcd";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(IdentChain.class, ast.getClass());
	}

	@Test
	public void testFilterOpChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur true abc 123 true false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(FilterOpChain.class, ast.getClass());
	}

	@Test
	public void testFrameOpChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "xloc (abc , 1234)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(FrameOpChain.class, ast.getClass());
	}

	@Test
	public void testImageOpChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height -> x; frame f i -> scale (x) -> f;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(ImageOpChain.class, ast.getClass());
	}

	@Test
	public void testBinaryChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abcd |-> c1234";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chain();
		assertEquals(BinaryChain.class, ast.getClass());
		BinaryChain be = (BinaryChain) ast;
		assertEquals(IdentChain.class, be.getE0().getClass());
		assertEquals(IdentChain.class, be.getE1().getClass());
		assertEquals(BARARROW, be.getArrow().kind);
	}

	@Test
	public void testWhileStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(true){a -> b;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(WhileStatement.class, ast.getClass());
	}

	@Test
	public void testIfStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(true){a -> b;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(IfStatement.class, ast.getClass());
	}

	@Test
	public void testIdentExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abcd123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testIntLitExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "12345";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}

	@Test
	public void testBooleanLitExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "true false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BooleanLitExpression.class, ast.getClass());
	}

	@Test
	public void testConstantExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "screenheight";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(ConstantExpression.class, ast.getClass());
	}

	@Test
	public void testBinaryExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(true) == (false)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
	} */

	/*
	@Test
	public void testTuple() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  ( 3+4 , 5 ) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		Tuple s = parser.arg();
		Tuple ts = s;
		System.out.println(ts.getExprList().get(1).firstToken.intVal());
		//System.out.println(ts.getExprList().get(1).firstToken.kind);
		System.out.println(ts.getExprList().get(0).firstToken.intVal());
		assertEquals(Tuple.class, s.getClass());
		//assertEquals(Expected:3, ts.getExprList().get(0).firstToken.intVal());
	} */
	
	/*
	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}

	@Test
	public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	} */
}
