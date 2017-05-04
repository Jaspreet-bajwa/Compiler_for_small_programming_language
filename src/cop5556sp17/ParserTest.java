package cop5556sp17;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;

public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc 123 true false (abc>00)";// "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}

	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(true,abc)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//System.out.println(scanner);
		Parser parser = new Parser(scanner);
		parser.arg();
	} 

	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void testProgram() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 url abc { }";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}

	@Test
	public void testParamDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "boolean abcd198";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
	}
	
	@Test
	public void testBlock() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{frame abcd sleep false ;}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.block();
	} 
	
	@Test
	public void testDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "integer prog0";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.dec();
	}
	
	@Test
	public void testStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(true){}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.statement();
	} 

	@Test
	public void testAssign() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 <- abc";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.assign();
	} 
	
	@Test
	public void testChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 |-> blur -> abc";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
	}
	

	
	@Test
	public void testChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
	}
	

	
	@Test
	public void testExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.expression();
	}
	
	@Test
	public void testTerm() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.term();
	}
	
	@Test
	public void testElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.elem();
	} 

}
