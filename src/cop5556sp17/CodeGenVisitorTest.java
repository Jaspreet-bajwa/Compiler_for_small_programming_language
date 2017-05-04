
package cop5556sp17;

import java.io.FileOutputStream;
import java.io.OutputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.Program;

public class CodeGenVisitorTest {

	static final boolean doPrint = true;
	static void show(Object s) {
		if (doPrint) {
			System.out.println(s);
		}
	}

	boolean devel = false;
	boolean grade = true;
	

	@Test
	public void emptyProg() throws Exception {
		//scan, parse, and type check the program
		
		//assignment 5 test cases
		//String input = "assignImageAndFrame url u {image i frame f frame f1\nu -> i -> f -> show;}";
		//String input = "emptyProg integer a, integer b { integer c integer d d<-a+b;}";	
		//String input = "testfile integer a{ integer c c<-10; a<-c+15;}";
		//String input = "compProg1 integer a, integer b, integer c, boolean bool0 { a <- 4;  b <- 5; boolean boolA  boolean boolB  boolA <- true;  boolB <- false;  if(boolA == true)  {boolean a a <- boolA; bool0 <- false;while(a != boolB){integer d  integer e c <- 3 + 5; d <- 10 - 1; c <- c * d; e <- d / 3; a <- boolB;if(c > d) {     c <- d;     if(c <= d)     {        boolA <- false;    }    if(boolA < boolB)     {        c <- 0;    }}} } if(c >= 1) {     /*boolB <- bool0 | true;*/} a <- 7;}";
		//String input = "assignParamNLocal integer int_foo, boolean bool_bar {int_foo <- 42;bool_bar <- false;  integer local_foo0  local_foo0 <- 5;    boolean local_bool0 local_bool0 <- true;}";                         
		//String input = "compProg2 integer x, integer y, integer z, boolean bool_1, boolean bool_2 { \nx <- 100; \ny <- x / 3 * 2; \nz <- y; \nbool_1 <- false; \nbool_2 <- true; \ninteger y \ny <- z + 20; \nz <- y; \nif(bool_2){ \nboolean bool_1 \nbool_1 <- bool_2; \n} \nif(bool_1) { \ninteger err \nerr <- 2333; \n} \ninteger pass_token \npass_token <- 0; \nwhile(pass_token != 4) { \ninteger local_1 \ninteger local_2 \nlocal_1 <- 45; \nlocal_2 <- 46; \nif(local_1 != local_2) {pass_token <- pass_token + 1;} \nif(local_1 == local_2) {pass_token <- pass_token + 1;} \nif(local_1 > local_2) {pass_token <- pass_token + 1;} \nif(local_1 >= 45) {pass_token <- pass_token + 1;} \nif(local_1 < local_2) {pass_token <- pass_token + 1;} \nif(46 <= local_2) {pass_token <- pass_token + 1;} \nif((local_1 > local_2)) {pass_token <- pass_token + 1;} \n} \n} ";
		//String input = "whileStatement1  integer y { integer x\tx <- 6;\ty <- x + 1; \twhile(x >= 2) {  \t\tx <- x - 1;\t}}";				
		//String input = "emptyProg {integer x integer y x <- 1; y <- x;}";	
		//String input = "identExprBooleanLocal  {boolean i boolean j i<-false; j <- i;}";
		//String input = "compProg0 { integer a0 a0<-0;if(a0 == 0){integer a00 integer b00 integer c00 integer d00 integer e00 e00 <- 5; d00 <- 4; c00 <- 3; b00 <- 2; a00 <- 1; if(a00 == 1){integer a01 integer b01 integer c01 integer d01 integer e01 e01 <- 55; d01 <- 44; c01 <- 33; b01 <- 22; a01 <- 11; }}}";
		//String input = "whileifwhileStatement0{\ninteger i \ninteger j \ninteger t \ni <-10; \nj <-1; \nt <-2; \nwhile (i > 0) {\ninteger k \nk <-i/j; \nif (k > 1) {\nwhile (t > 0) {t <- t-1;} \nj <- j+1;} \ni<-i-1;} \ni<-t;}";
		//String input = "whileifwhileStatement1{\ninteger i \ninteger j \ninteger t \ni <-10; \nj <-1; \nt <-2; \nwhile (i > 0) {\ninteger k \nk <-i/j; \nif (k > 1) {\ninteger t \nt <-j; \nwhile (t > 0) {t <- t-1;} \nj <- j+1;} \ni<-i-1;} \ni<-t;}";
		//String input = "ifwhileStatement0{\ninteger i \ninteger j \ni <-10; \nj <-1; \nif (i > 0) {\ninteger k \nk <-i/j; \nwhile (k > 1) {j <- j+1; \nk <-i/j;} \n} \ni<-j;}";
		//String input = "booleanComp2 { boolean a boolean b boolean c a<-true;b<-false; c<-a<b;c<-a<=b;c<-a>b;c<-a>=b;c<-a==b;c<-a!=b;a<-false; b<-true;c<-a<b;c<-a<=b;c<-a>b;c<-a>=b;c<-a==b;c<-a!=b;a<-true; b<-true;c<-a<b;c<-a<=b;c<-a>b;c<-a>=b;c<-a==b;c<-a!=b;a<-false; b<-false;c<-a<b;c<-a<=b;c<-a>b;c<-a>=b;c<-a==b;c<-a!=b;}";
		//String input = "whileStatement0{\ninteger i \ninteger j \ni <-4; \nwhile (i > 0) {\nj <- i;\ni<-i-1;} \ni<-9;}";
		//String input = "whileStatement2{\ninteger i \ninteger j \ni <-3; \nwhile (i > 0) {\ninteger j \nj <-i*2; \nwhile (j > i) {\nj<-j-1;} \ni<-i-1;} \ni<-0;}";
		//String input = "identExprBooleanLocal1  {boolean i boolean j i<-true; j <- i;}";
		//String input = "ifStatement0  {integer i \ninteger j \ni <-55; \nif (i == 55) {j <- 3;} \nif (i != 55){ j <- 1;}\n}";
		//String input = "ifStatement {integer i \ninteger j \ni <-55; \nif (i == 55) {j <- 3;}}";
		//String input = "ifStatement1{\ninteger i \ninteger j \ni <-56; \nif (i == 55) {j <- 3;} \nif (i != 55){ j <- 1;}\n}";
		//String input = "ifStatement2{\ninteger i \ninteger j \ni <-10; \nj <-0; \nif (i > 5) {j <- j+1; \nif (i > 7){ j <- j + 1; \nif (i > 8){ j <- j + 1;}\n}\n}\n}";
		//String input = "ifStatement3{\ninteger i \ninteger j \ni <-10; \nj <-0; \nif (i > 5) {j <- j+1; \nif (i > 7){ \ninteger i \ni <-7; j <- j + 1; \nif (i > 8){ j <- j + 1;}\n}\n}\n}";
		//String input = "ifStatement4 {integer local_int0\ninteger local_int1\nlocal_int0 <- 42;local_int1 <- 43;if(local_int0 == local_int1){integer local_int11 \n local_int11 <- 44;} if(local_int0 != local_int1){integer local_int22 \n local_int22 <- 45;}if(local_int0 != local_int1){integer local_int33 \n local_int33 <- 46;integer local_int44 \n local_int44 <- 47;}}";
		//String input = "ifwhileifStatement0{\ninteger i \ninteger j \ninteger k \nboolean b \ni <-10; \nj <-1; \nb <-true; \nif (b) {\nwhile (i > 0) {\nk <-i/j; \nif (k > 1) {j <- j+1;} \ni<-i-1;} \ni<-0;}\n}";
		//String input = "ifwhileifStatement1{\ninteger i \ninteger j \nboolean b \ni <-10; \nj <-1; \nb <-true; \nif (b) {\nwhile (i > 0) {\ninteger k \nk <-i/j; \nif (k > 1) {j <- j+1;} \ni<-i-1;} \ni<-0;}\n}";
		//String input = "add  {integer i \ninteger j \ni <-55; \nj <- 44; \nj <- i+j;\n}";
		//String input = "div  {integer i \ninteger j \ni <-33; \nj <- 3; \nj <- i/j;\n}";
		//String input = "times  {integer i \ninteger j \ni <-11; \nj <- 3; \nj <- i*j;\n}";
		//String input = "identExprLocal  {integer i integer j i<-55; j <- i;}";
		//String input = "whileifStatement0{\ninteger i \ninteger j \ni <-10; \nj <-1; \nwhile (i > 0) {\ninteger k \nk <-i/j; \nif (k > 1) {j <- j+1;} \ni<-i-1;} \ni<-0;}";
		  String input = "test integer a, integer b {a <- a;b <- b;b <- 7;a <- 12650;a -> b;b <- b;}";
		
		//assignment 6 test cases
		//String input = "subImage url u{ image i image j image k frame f u->i; \nu -> j; \n k <- i-j; k-> f-> show;}";
		//String input = "subImage url u {image i image j image k frame f \nu -> i; \nu -> j; \n k <- i-j; k -> f -> show;\n}";
		//String input = "addImage url u {image i image j image k frame f \nu -> i; \nu -> j; \n k <- i-j; k -> f -> show;  sleep 5; k <- k + i; k -> f -> show; \n}";
		//String input = "subImage2 url u {image i image j  frame f \nu -> i; \nu -> j; \n i <- i-j; i -> f -> show;\n}";
		//String input = "modImage url u {image i image j  frame f \nu -> j; \n i <- j%128; i -> f -> show;\n}";
		//String input = "divImage url u {image i image j  frame f \nu -> j; \n i <- j/2; i -> f -> show;\n}";
		//String input = "assignImage url u {image i image j frame f \nu -> i; j <- i;j -> f -> show;\n}";
		//String input = "assignImageAndFrame url u {image i image i1 frame f frame f1\nu -> i -> f -> show; frame f2  \ni -> scale (3) -> f2 -> show; \n i1 <- i; \n f2 <- f;\n}";
		//String input = "subImage url u {image i image j image k frame f \nu -> i; \nu -> j; \n k <- i-j; k -> f -> show;\n}";
		//String input = "readFromURLandWriteToFile2 url u, \nfile out \n{image i frame f \nu -> gray  -> i;\n i -> f -> show; \n i -> out;\n}";
		//String input = "readFromURLandWriteToFile3 url u, \nfile out \n{\nu -> gray  -> out;\n}";
		//String input = "sleepImg url u {image i frame f \nu -> i -> convolve -> f -> show;sleep 5;integer j j <- 42;\n}";
		//String input = "fibonacci integer n {\n   integer result result<-0;\n   if(n == 0){\n    result<- 0;\n  }\n  if(n == 1){\n    result<- 1;\n  }\n  if(n > 1){\n    integer fib1 fib1<- 0;\n    integer fib2 fib2<- 1;\n    integer idx idx<- 2;\n    while(idx < n){      integer tmp tmp<- fib1 + fib2;\n      fib1<- fib2;\n      fib2<- tmp;\n      idx<- idx + 1;\n    }\n    result<- fib1+fib2;\n  }\n}";
		//String input = "addImage url u {image i image j image k frame f \nu -> i; \nu -> j; \n k <- i-j; k -> f -> show;  sleep 5; k <- k + i; k -> f -> show; \n}";
		//String input = "booleanComp1 { boolean x  x<- true == false;boolean y  y<- true != false;boolean z  z<- true | false;z<- x|y;z<- x&y;}";
		//String input = "booleanComp2 { boolean x  x<- true;boolean y  y<- false;boolean z  z<- true < false;integer a  a<- 4;integer b  b<- 6;z<- ((a<b) | x) & y;z<- (a<b) | (x & y);}";
		//String input = "convolveImg url u {image i frame f \nu -> i -> convolve -> f -> show;\n}";
		//String input = "readFromURLandDisplay url u {image i frame f \nu -> i;i -> f -> show;\n}";
		//String input = "imgMove2 url u {image i frame f \nu -> i;i -> f -> show; \nsleep 5; \ninteger x \ninteger y \nx <- screenwidth;\ny <-  screenheight; \nf -> move (x*3/4,y/4) -> show;\n}";
		//String input = "subImage2 url u {image i image j  frame f \nu -> i; \nu -> j; \n i <- i-j; i -> f -> show;\n}";
		//String input = "booleanBinaryExpression2 \nboolean  b0, boolean b1{  \n boolean b3 boolean b4 b3 <- b0 & b1; b4 <- b0 | b1;}";
		//String input = "booleanBinaryExpression3 \nboolean  b0, boolean b1{  \n boolean b3 boolean b4 b3 <- b0 & b1; b4 <- b0 | b1;}";
		//String input = "modImage url u {image i image j  frame f \nu -> j; \n i <- j%128; i -> f -> show;\n}";
		//String input = "readFromURLandDisplayDisplay2 url u {image i frame f \nu -> i -> f -> show;\n}";
		//String input = "readFromURLandWriteToFile url u, \nfile out \n{image i frame f \nu -> i;i -> f -> show; \n i -> out;\n}";
		//String input = "blurImg url u {image i frame f \nu -> i -> blur -> f -> show;\n}";
		//String input = "grayImg url u {image i frame f \nu -> i -> gray -> f -> show;\n}";
		//String input = "divImage url u {image i image j  frame f \nu -> j; \n i <- j/2; i -> f -> show;\n}";
		//String input = "readFromURLandWriteScaledImageToFile url u, \nfile out \n{image i frame f \nu -> scale (3) -> i;i -> f -> show; \n i -> out;\n}";
		//String input = "barArrowGray url u {image i frame f \nu -> i |-> gray -> f -> show;\n}";
		//String input = "assignImage url u {image i image j frame f \nu -> i; j <- i;j -> f -> show;\n}";
		//String input = "scaleImage url u {image i frame f \nu -> i -> f -> show; frame f2  \ni -> scale (3) -> f2 -> show;\n}";
		//String input = "imgMove url u {image i frame f \nu -> i;i -> f -> show; \nsleep 5; \ninteger x \ninteger y \nf -> xloc -> x; \nf -> yloc -> y; \nf -> move (x+100,y-100) -> show;\n}";
		//String input = "imgMove url u {image i frame f \nu -> i;i -> f -> show; \ninteger x \ninteger y \nf -> xloc -> x; \nf -> yloc -> y; \nf -> move (x+100,y-100) -> show;\n }";
		//String input = "assignImageAndFrame url u {image i image i1 frame f frame f1\nu -> i -> f -> show; frame f2  \ni -> scale (3) -> f2 -> show; \n i1 <- i; \n f2 <- f;\n}";
		//String input = "booleanBinaryExpression \nboolean  b0, boolean b1{  \n boolean b3 boolean b4 b3 <- b0 & b1; b4 <- b0 | b1;}";
		
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		
		//generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel,grade,null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		
		//output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		
		//write byte code to file 
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);
		
		System.out.println(CodeGenUtils.bytecodeToString(bytecode));
		
		
		// directly execute bytecode
		
		//String [] args = new String[]{};		
		//String[] args = new String[]{"http://foxsportsuniversity.com/wp-content/uploads/2012/08/FLORIDA.png","/Users/JazBajwa/Downloads/pic.jpg"}; //create command line argument array to initialize params, none in this case
		String[] args = new String[]{"42","46","52","true","false"};
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}


}
