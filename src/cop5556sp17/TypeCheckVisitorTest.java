/**  Important to test the error cases in case the
 * AST is not being completely traversed.
 * 
 * Only need to test syntactically correct programs, or
 * program fragments.
 */

package cop5556sp17;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.AST.ASTNode;
//import cop5556sp17.AST.Dec;
//import cop5556sp17.AST.IdentExpression;
//import cop5556sp17.AST.Program;
//import cop5556sp17.AST.Statement;
//import cop5556sp17.Parser.SyntaxException;
//import cop5556sp17.Scanner.IllegalCharException;
//import cop5556sp17.Scanner.IllegalNumberException;
//import cop5556sp17.TypeCheckVisitor.TypeCheckException;

public class TypeCheckVisitorTest {
	

//	@Rule
//	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testAssignmentBoolLit0() throws Exception{
		//String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		//String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		//String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
//		String input = "abc url x, file y, boolean k {}";
		//String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		//String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
	
	@Test
	public void testAssignmentBoolLit1() throws Exception{
		//String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		//String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		//String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
		String input = "abc url x, file y, boolean k {}";
		//String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		//String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		//String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
	
	
	@Test
	public void testAssignmentBoolLit2() throws Exception{
		String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		//String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		//String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
		//String input = "abc url x, file y, boolean k {}";
		//String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		//String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		//String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
	
	
	@Test
	public void testAssignmentBoolLit3() throws Exception{
		//String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		//String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
		// input = "abc url x, file y, boolean k {}";
		//String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		//String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		//String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
	
	@Test
	public void testAssignmentBoolLit4() throws Exception{
		//String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		//String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		//String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
		//String input = "abc url x, file y, boolean k {}";
		String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		//String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		//String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
	
	@Test
	public void testAssignmentBoolLit5() throws Exception{
		//String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		//String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
		//String input = "abc url x, file y, boolean k {}";
		//String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		//String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		//String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
	
	@Test
	public void testAssignmentBoolLit6() throws Exception{
		//String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";
		//String input = "p integer a, integer b {image img1 image img2 if(img1 != img2) {image a a <- img1; } if(a != b) {boolean a a <- img1 != img2; }}";
		//String input = "testFrame url u1, url u2, file file1 {frame fra1 frame fra2 image img fra1 -> move (screenheight, screenwidth) -> xloc; img -> fra2; img -> file1;}";
		//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
		//String input = "prog {\n boolean x \n scale(1234) -> width; \n  integer y \n scale(y) -> scale(y + 10);}" ;
		//String input = "prog  boolean y , file x {\n integer z \n scale(100) -> width; blur -> y; convolve -> blur -> gray |-> gray -> width;}";
		//String input = "abc\n{integer x\nif(true){integer x}\n}";
		//String input = "prog1  file file1, integer itx, boolean b1{ integer ii1 boolean bi1 \n image IMAGE1 frame fram1 sleep itx+ii1; while (b1){if(bi1)\n{sleep ii1+itx*2;}}\nfile1->blur |->gray;fram1 ->yloc;\n IMAGE1->blur->scale (ii1+1)|-> gray;\nii1 <- 12345+54321;}";
		//String input ="abc integer x, integer y {}";
		//String input = "p \nurl y {\n  image i\n  y->i;\n}";
		//String input = "program { image first image last if(true) { integer first image second last->second->first;}}";
		//String input = "abc url x, file y, boolean k {}";
		//String input = "p {integer x integer y integer z \nz <- x + y;\nz <- x-y;\nx <- z/y;\nz <- x / y;\nx <- z*z;\n}";
		String input = "p {integer x integer y boolean z \nz <- x < y; z <- x > y; z <- 33 <= 44; z <- 33 >= 55;\n}";
		//String input = "p {frame x frame y boolean z \nz <- x == y;\nz <- x != y;}";
		//String input = "p {integer x x <- 42;}";
		//String input = "abc  {integer x}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	} 
/*
	@Test
	public void testAssignmentBoolLitError0() throws Exception{
		String input = "p {boolean x\nboolean y\nx <- true;\ny <- false;}";//"p {integer x \n if(true) \n{boolean y \ny <- 3;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}		
*/


}
