package cop5556sp17;

import static cop5556sp17.Scanner.Kind.EQUAL;
import static cop5556sp17.Scanner.Kind.GE;
import static cop5556sp17.Scanner.Kind.GT;
import static cop5556sp17.Scanner.Kind.LE;
import static cop5556sp17.Scanner.Kind.NOTEQUAL;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;

	// ass 5 starts
	public int argSlot = 0;
	public int slotNumber = 1;
	FieldVisitor fv;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.getName();
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object",
				new String[] { "java/lang/Runnable" });
		cw.visitSource(sourceFileName, null);

		// generate constructor code
		// get a MethodVisitor
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		// Create label at start of code
		Label constructorStart = new Label();
		mv.visitLabel(constructorStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering <init>");
		// generate code to call superclass constructor
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		// visit parameter decs to add each as field to the class
		// pass in mv so decs can add their initialization code to the
		// constructor.
		ArrayList<ParamDec> params = program.getParams();
		for (ParamDec dec : params)
			dec.visit(this, mv);
		mv.visitInsn(RETURN);
		// create label at end of code
		Label constructorEnd = new Label();
		mv.visitLabel(constructorEnd);
		// finish up by visiting local vars of constructor
		// the fourth and fifth arguments are the region of code where the local
		// variable is defined as represented by the labels we inserted.
		mv.visitLocalVariable("this", classDesc, null, constructorStart, constructorEnd, 0);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, constructorStart, constructorEnd, 1);
		// indicates the max stack size for the method.
		// because we used the COMPUTE_FRAMES parameter in the classwriter
		// constructor, asm
		// will do this for us. The parameters to visitMaxs don't matter, but
		// the method must
		// be called.
		mv.visitMaxs(1, 1);
		// finish up code generation for this method.
		mv.visitEnd();
		// end of constructor

		// create main method which does the following
		// 1. instantiate an instance of the class being generated, passing the
		// String[] with command line arguments
		// 2. invoke the run method.
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering main");
		mv.visitTypeInsn(NEW, className);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "([Ljava/lang/String;)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "run", "()V", false);
		mv.visitInsn(RETURN);
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
		mv.visitLocalVariable("instance", classDesc, null, mainStart, mainEnd, 1);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// create run method
		mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		mv.visitCode();
		Label startRun = new Label();
		mv.visitLabel(startRun);
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering run");
		program.getB().visit(this, null);
		mv.visitInsn(RETURN);
		Label endRun = new Label();
		mv.visitLabel(endRun);
		mv.visitLocalVariable("this", classDesc, null, startRun, endRun, 0);
		// TODO visit the local variables
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, startRun, endRun, 1);
		mv.visitMaxs(1, 1);
		mv.visitEnd(); // end of run method

		cw.visitEnd();// end of class

		// generate classfile and return it
		return cw.toByteArray();
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		if (assignStatement.getVar().dec instanceof ParamDec) {
			mv.visitVarInsn(ALOAD, 0);
		}
		assignStatement.getE().visit(this, arg);
		CodeGenUtils.genPrint(DEVEL, mv, "\nassignment: " + assignStatement.var.getText() + "=");
		CodeGenUtils.genPrintTOS(GRADE, mv, assignStatement.getE().typeName);
		assignStatement.getVar().visit(this, arg);
		if (assignStatement.getE().typeName.equals(TypeName.IMAGE)
				&& assignStatement.getVar().dec.typeName.equals(TypeName.IMAGE)) {
			mv.visitVarInsn(ALOAD, assignStatement.getVar().dec.getSlotNumber());
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "copyImage",
					"(Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;", false);
			mv.visitVarInsn(ASTORE, assignStatement.getVar().dec.getSlotNumber());
		}
		return null;
	}

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {

		binaryChain.getE0().visit(this, "L");

		if (binaryChain.getE0().typeName.equals(TypeName.URL)) {
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageIO", "readFromURL",
					"(Ljava/net/URL;)Ljava/awt/image/BufferedImage;", false);
		} else if (binaryChain.getE0().typeName.equals(TypeName.FILE)) {
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageIO", "readFromFile",
					"(Ljava/io/File;)Ljava/awt/image/BufferedImage;", false);
		}

		binaryChain.getE1().visit(this, "R");

		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		// TODO Implement this
		binaryExpression.getE0().visit(this, arg);
		binaryExpression.getE1().visit(this, arg);

		Label beginBinary = new Label();
		Label binaryEnd = new Label();
		switch (binaryExpression.getOp().kind) {
		case PLUS:
			if (binaryExpression.getE0().typeName.equals(TypeName.IMAGE)
					&& binaryExpression.getE1().typeName.equals(TypeName.IMAGE)) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "add",
						"(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;",
						false);
			} else {
				mv.visitInsn(IADD);
			}
			break;
		case MINUS:
			if (binaryExpression.getE0().typeName.equals(TypeName.IMAGE)
					&& binaryExpression.getE1().typeName.equals(TypeName.IMAGE)) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "sub",
						"(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;",
						false);
			} else {
				mv.visitInsn(ISUB);
			}
			break;
		case TIMES:
			if (binaryExpression.getE0().typeName.equals(TypeName.IMAGE)
					&& binaryExpression.getE1().typeName.equals(TypeName.IMAGE)) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "mul",
						"(Ljava/awt/image/BufferedImage;I)Ljava/awt/image/BufferedImage;", false);
			} else {
				mv.visitInsn(IMUL);
			}
			break;
		case DIV:
			if (binaryExpression.getE0().typeName.equals(TypeName.IMAGE)
					&& binaryExpression.getE1().typeName.equals(TypeName.INTEGER)) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "div",
						"(Ljava/awt/image/BufferedImage;I)Ljava/awt/image/BufferedImage;", false);
			} else {
				mv.visitInsn(IDIV);
			}
			break;

		case OR:
			mv.visitInsn(IOR);
			break;

		case AND:
			mv.visitInsn(IAND);
			break;

		case MOD:
			if (binaryExpression.getE0().typeName.equals(TypeName.IMAGE)
					&& binaryExpression.getE1().typeName.equals(TypeName.INTEGER)) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "mod",
						"(Ljava/awt/image/BufferedImage;I)Ljava/awt/image/BufferedImage;", false);
			} else {
				mv.visitInsn(IREM);
			}
			break;

		case LT:
			mv.visitJumpInsn(IF_ICMPLT, beginBinary);
			mv.visitInsn(ICONST_0);
			break;

		case LE:
			mv.visitJumpInsn(IF_ICMPLE, beginBinary);
			mv.visitInsn(ICONST_0);
			break;

		case GE:
			mv.visitJumpInsn(IF_ICMPGE, beginBinary);
			mv.visitInsn(ICONST_0);
			break;

		case EQUAL:
			mv.visitJumpInsn(IF_ICMPNE, beginBinary);
			mv.visitInsn(ICONST_0);

			break;

		case NOTEQUAL:
			mv.visitJumpInsn(IF_ICMPNE, beginBinary);
			mv.visitInsn(ICONST_0);

			break;

		case GT:
			mv.visitJumpInsn(IF_ICMPGT, beginBinary);
			mv.visitInsn(ICONST_0);

			break;

		default:
			break;
		}

		if (binaryExpression.getOp().kind.equals(LE) || binaryExpression.getOp().kind.equals(Kind.LT)
				|| binaryExpression.getOp().kind.equals(GE) || binaryExpression.getOp().kind.equals(EQUAL)
				|| binaryExpression.getOp().kind.equals(NOTEQUAL) || binaryExpression.getOp().kind.equals(GT)) {
			mv.visitJumpInsn(GOTO, binaryEnd);
			mv.visitLabel(beginBinary);
			mv.visitLdcInsn(true);
			mv.visitLabel(binaryEnd);
		}

		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Implement this
		Label l1 = new Label();
		mv.visitLabel(l1);
		for (int i = 0; i < block.getDecs().size(); i++) {
			block.getDecs().get(i).visit(this, arg);
		}
		for (int i = 0; i < block.getStatements().size(); i++) {
			block.getStatements().get(i).visit(this, arg);
			if (block.getStatements().get(i) instanceof BinaryChain) {
				mv.visitInsn(POP);
			}
		}
		Label l2 = new Label();
		mv.visitLabel(l2);
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		// TODO Implement this
		
		if (booleanLitExpression.getValue()) {
			mv.visitInsn(ICONST_1);
		} else {
			mv.visitInsn(ICONST_0);
		}
		return null;
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		// assert false : "not yet implemented";
		// for screen width
		if (constantExpression.getFirstToken().kind.equals(Kind.KW_SCREENWIDTH)) {
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFrame", "getScreenWidth", "()I", false);
		} else if (constantExpression.getFirstToken().kind.equals(Kind.KW_SCREENHEIGHT)) {
			// for screen height
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFrame", "getScreenHeight", "()I", false);
		}

		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		// TODO Implement this
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitLabel(l1);
		declaration.setSlotNumber(slotNumber++);
		if (declaration.typeName.equals(TypeName.FRAME)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ASTORE, declaration.getSlotNumber());
		} else if (declaration.typeName.equals(TypeName.IMAGE)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ASTORE, declaration.getSlotNumber());
		}
		mv.visitLabel(l2);
		// slotNumber += 1;
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		// assert false : "not yet implemented";

		filterOpChain.getArg().visit(this, arg);

		if (filterOpChain.getFirstToken().kind.equals(Kind.OP_BLUR)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFilterOps", "blurOp",
					"(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;",
					false);
		} else if (filterOpChain.getFirstToken().kind.equals(Kind.OP_CONVOLVE)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFilterOps", "convolveOp",
					"(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;",
					false);
		} else if (filterOpChain.getFirstToken().kind.equals(Kind.OP_GRAY)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFilterOps", "grayOp",
					"(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;",
					false);
		}

		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		// assert false : "not yet implemented";
		Kind kind = frameOpChain.getFirstToken().kind;

		frameOpChain.getArg().visit(this, arg);

		if (kind.equals(Kind.KW_SHOW)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "cop5556sp17/PLPRuntimeFrame", "showImage",
					"()Lcop5556sp17/PLPRuntimeFrame;", false);
		} else if (kind.equals(Kind.KW_HIDE)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "cop5556sp17/PLPRuntimeFrame", "hideImage",
					"()Lcop5556sp17/PLPRuntimeFrame;", false);
		} else if (kind.equals(Kind.KW_MOVE)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "cop5556sp17/PLPRuntimeFrame", "moveFrame",
					"(II)Lcop5556sp17/PLPRuntimeFrame;", false);
		} else if (kind.equals(Kind.KW_XLOC)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "cop5556sp17/PLPRuntimeFrame", "getXVal", "()I", false);
		} else if (kind.equals(Kind.KW_YLOC)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "cop5556sp17/PLPRuntimeFrame", "getYVal", "()I", false);
		}

		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {

		if (identChain instanceof Chain && arg.equals("L")) {
			if (identChain.typeName.equals(TypeName.URL)) {
				mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
				mv.visitFieldInsn(GETFIELD, className, identChain.getFirstToken().getText(), "Ljava/net/URL;");
			} else if (identChain.typeName.equals(TypeName.FILE)) {
				mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
				mv.visitFieldInsn(GETFIELD, className, identChain.getFirstToken().getText(), "Ljava/io/File;");
			} else if (identChain.typeName.equals(TypeName.INTEGER) || identChain.typeName.equals(TypeName.BOOLEAN)) {
				if (identChain.dec instanceof ParamDec) {
					mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
					mv.visitFieldInsn(GETFIELD, className, identChain.getFirstToken().getText(),
							identChain.typeName.getJVMTypeDesc());
				} else {
					mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
				}
			} else if (identChain.typeName.equals(TypeName.IMAGE)) {
				mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
			} else {
				mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
			}
			mv.visitInsn(DUP);
		} else if (identChain instanceof ChainElem && arg.equals("R")) { 
			if (identChain.typeName.equals(TypeName.INTEGER)) {
				mv.visitVarInsn(ISTORE, identChain.dec.getSlotNumber());
			} else if (identChain.typeName.equals(TypeName.FILE)) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, className, identChain.firstToken.getText(),
						identChain.dec.typeName.getJVMTypeDesc());
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageIO", "write",
						"(Ljava/awt/image/BufferedImage;Ljava/io/File;)Ljava/awt/image/BufferedImage;", false);
				mv.visitVarInsn(ASTORE, identChain.dec.getSlotNumber());
			} else if (identChain.typeName.equals(TypeName.FRAME)) {
				mv.visitInsn(ACONST_NULL);
				mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFrame", "createOrSetFrame",
						"(Ljava/awt/image/BufferedImage;Lcop5556sp17/PLPRuntimeFrame;)Lcop5556sp17/PLPRuntimeFrame;",
						false);
				mv.visitVarInsn(ASTORE, identChain.dec.getSlotNumber());
				mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
			} else if (identChain.typeName.equals(TypeName.IMAGE)) {
				mv.visitVarInsn(ASTORE, identChain.dec.getSlotNumber());
				mv.visitVarInsn(ALOAD, identChain.dec.getSlotNumber());
			}
		}

		return identChain.dec.getSlotNumber();
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		// TODO Implement this
		Label startIdentExpression = new Label();
		Label endIdentExpression = new Label();
		mv.visitLabel(startIdentExpression);

		String type = "";

		if (identExpression.dec.typeName.equals(TypeName.INTEGER)) {
			type = "I";
		} else if (identExpression.dec.typeName.equals(TypeName.BOOLEAN)) {
			type = "Z";
		} else {
			type = identExpression.dec.typeName.getJVMTypeDesc();
		}

		if (identExpression.dec instanceof ParamDec) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, className, identExpression.getFirstToken().getText(), type);
		} else {
			if (identExpression.typeName.equals(TypeName.INTEGER)
					|| identExpression.typeName.equals(TypeName.BOOLEAN)) {
				mv.visitVarInsn(ILOAD, identExpression.dec.getSlotNumber());
			} else {
				mv.visitVarInsn(ALOAD, identExpression.dec.getSlotNumber());
			}
		}
		mv.visitLabel(endIdentExpression);
		mv.visitEnd();
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		// TODO Implement this
		Label startIdentLvalue = new Label();
		Label endIdentLvalue = new Label();
		mv.visitLabel(startIdentLvalue);

		String type = "";
		if (identX.dec.typeName.equals(TypeName.INTEGER)) {
			type = "I";
		} else if (identX.dec.typeName.equals(TypeName.BOOLEAN)) {
			type = "Z";
		} else {
			type = identX.dec.typeName.getJVMTypeDesc();
		}

		if (identX.dec instanceof ParamDec) {
			mv.visitFieldInsn(PUTFIELD, className, identX.getText(), type);
		} else if (identX.dec.typeName.equals(TypeName.INTEGER) || identX.dec.typeName.equals(TypeName.BOOLEAN)) {
			mv.visitVarInsn(ISTORE, identX.dec.getSlotNumber());
		} else {
			mv.visitVarInsn(ASTORE, identX.dec.getSlotNumber());
		}
		mv.visitLabel(endIdentLvalue);
		mv.visitEnd();
		return null;

	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		// TODO Implement this
		ifStatement.getE().visit(this, arg);
		Label after = new Label();
		mv.visitJumpInsn(IFEQ, after);
		ifStatement.getB().visit(this, arg);
		mv.visitLabel(after);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {

		imageOpChain.getArg().visit(this, arg);

		if (imageOpChain.getFirstToken().kind.equals(Kind.OP_WIDTH)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "getWidth", "()I", false);
		} else if (imageOpChain.getFirstToken().kind.equals(Kind.OP_HEIGHT)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "getHeight", "()I", false);
		} else if (imageOpChain.getFirstToken().kind.equals(Kind.KW_SCALE)) {
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageOps", "scale",
					"(Ljava/awt/image/BufferedImage;I)Ljava/awt/image/BufferedImage;", false);
		}
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		// TODO Implement this
		mv.visitIntInsn(BIPUSH, intLitExpression.value);
		return null;
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		// TODO Implement this
		// For assignment 5, only needs to handle integers and boolean

		paramDec.slotNumber = argSlot;

		if (paramDec.typeName.equals(TypeName.INTEGER)) {
			fv = cw.visitField(0, paramDec.getIdent().getText(), paramDec.typeName.getJVMTypeDesc(), null, null);
			fv.visitEnd();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(argSlot);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "I");

		} else if (paramDec.typeName.equals(TypeName.BOOLEAN)) {
			fv = cw.visitField(0, paramDec.getIdent().getText(), paramDec.typeName.getJVMTypeDesc(), null, null);
			fv.visitEnd();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(argSlot);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "Z");

		} else if (paramDec.typeName.equals(TypeName.FILE)) {
			fv = cw.visitField(0, paramDec.getIdent().getText(), paramDec.typeName.getJVMTypeDesc(), null, null);
			fv.visitEnd();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/io/File");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(argSlot);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "Ljava/io/File;");

		} else if (paramDec.typeName.equals(TypeName.URL)) {
			fv = cw.visitField(0, paramDec.getIdent().getText(), paramDec.typeName.getJVMTypeDesc(), null, null);
			fv.visitEnd();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(argSlot);
			// mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeImageIO", "getURL",
					"([Ljava/lang/String;I)Ljava/net/URL;", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "Ljava/net/URL;");
		}

		argSlot += 1;
		return null;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		sleepStatement.getE().visit(this, arg);
		mv.visitInsn(I2L);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V", false);
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		// assert false : "not yet implemented";
		for (int i = 0; i < tuple.getExprList().size(); i++) {
			tuple.getExprList().get(i).visit(this, arg);
			if (tuple.getExprList().get(i) instanceof ConstantExpression) {
				mv.visitVarInsn(ALOAD, 1);
			}
		}
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		// TODO Implement this
		Label guard = new Label();
		Label body = new Label();

		mv.visitJumpInsn(GOTO, guard);
		mv.visitLabel(guard);
		whileStatement.getE().visit(this, arg);
		mv.visitJumpInsn(IFNE, body);
		mv.visitLabel(body);
		whileStatement.getB().visit(this, arg);
		return null;
	}

}
