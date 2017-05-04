package cop5556sp17;

import cop5556sp17.AST.*;
import cop5556sp17.AST.Type.TypeName;
import static cop5556sp17.Scanner.Kind.*;
import java.util.ArrayList;

public class TypeCheckVisitor implements ASTVisitor {

	public SymbolTable symtab = new SymbolTable();

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String message) {
			super(message);
		}
	}

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Chain chain = binaryChain.getE0();
		chain.visit(this, arg);
		ChainElem chainElem = binaryChain.getE1();
		chainElem.visit(this, arg);
		if (chain.typeName.equals(TypeName.URL) && binaryChain.getArrow().isKind(ARROW)
				&& chainElem.typeName.equals(TypeName.IMAGE)) {
			binaryChain.typeName = TypeName.IMAGE;
		} else if (chain.typeName.equals(TypeName.FILE) && binaryChain.getArrow().isKind(ARROW)
				&& chainElem.typeName.equals(TypeName.IMAGE)) {
			binaryChain.typeName = TypeName.IMAGE;
		} else if (chain.typeName.equals(TypeName.FRAME) && binaryChain.getArrow().isKind(ARROW)
				&& (chainElem.getFirstToken().isKind(KW_XLOC) || chainElem.getFirstToken().isKind(KW_YLOC))) {
			binaryChain.typeName = TypeName.INTEGER;
		} else if (chain.typeName.equals(TypeName.FRAME) && binaryChain.getArrow().isKind(ARROW)
				&& (chainElem.getFirstToken().isKind(KW_SHOW) || chainElem.getFirstToken().isKind(KW_HIDE)
						|| chainElem.getFirstToken().isKind(KW_MOVE))) {
			binaryChain.typeName = TypeName.FRAME;
		} else if (chain.typeName.equals(TypeName.IMAGE) && binaryChain.getArrow().isKind(ARROW)
				&& (chainElem.getFirstToken().isKind(OP_WIDTH) || chainElem.getFirstToken().isKind(OP_HEIGHT))) {
			binaryChain.typeName = TypeName.INTEGER;
		} else if (chain.typeName.equals(TypeName.IMAGE) && binaryChain.getArrow().isKind(ARROW)
				&& chainElem.typeName.equals(TypeName.FRAME)) {			
			binaryChain.typeName = TypeName.FRAME;			
		} else if (chain.typeName.equals(TypeName.IMAGE) && binaryChain.getArrow().isKind(ARROW)
				&& chainElem.typeName.equals(TypeName.FILE)) {
			binaryChain.typeName = TypeName.NONE;
		} else if (chain.typeName.equals(TypeName.IMAGE)
				&& (binaryChain.getArrow().isKind(ARROW) || binaryChain.getArrow().isKind(BARARROW))
				&& (chainElem.getFirstToken().isKind(OP_GRAY) || chainElem.getFirstToken().isKind(OP_BLUR)
						|| chainElem.getFirstToken().isKind(OP_CONVOLVE))) {
			binaryChain.typeName = TypeName.IMAGE;
		} else if (chain.typeName.equals(TypeName.IMAGE) && binaryChain.getArrow().isKind(ARROW)
				&& chainElem.getFirstToken().isKind(KW_SCALE)) {
			binaryChain.typeName = TypeName.IMAGE;
		} else if (chain.typeName.equals(TypeName.IMAGE) && binaryChain.getArrow().isKind(ARROW)
				&& (chainElem instanceof IdentChain)) {
			if(chainElem.typeName.equals(TypeName.IMAGE))
				binaryChain.typeName = TypeName.IMAGE;
			else if (chainElem.typeName.equals(TypeName.INTEGER))
				binaryChain.typeName = TypeName.INTEGER;
		} else if(chain.typeName.equals(TypeName.INTEGER) && binaryChain.getArrow().isKind(ARROW)
				&& (chainElem.typeName.equals(TypeName.INTEGER))){
				binaryChain.typeName = TypeName.INTEGER;
		} else {
			throw new TypeCheckException("Exception occurred in Binary Chain");
		}

		return binaryChain;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub

		Expression e0 = binaryExpression.getE0();
		e0.visit(this, arg);
		Expression e1 = binaryExpression.getE1();
		e1.visit(this, arg);

		if (e0.typeName.equals(TypeName.INTEGER) && e1.typeName.equals(TypeName.INTEGER)) {
			if (binaryExpression.getOp().kind.equals(PLUS) || binaryExpression.getOp().kind.equals(MINUS)
					|| binaryExpression.getOp().kind.equals(TIMES) || binaryExpression.getOp().kind.equals(DIV)
					|| binaryExpression.getOp().kind.equals(MOD)) {
				binaryExpression.typeName = TypeName.INTEGER;
			} else if (binaryExpression.getOp().kind.equals(LT) || binaryExpression.getOp().kind.equals(GT)
					|| binaryExpression.getOp().kind.equals(LE) || binaryExpression.getOp().kind.equals(GE)
					|| binaryExpression.getOp().kind.equals(EQUAL) || binaryExpression.getOp().kind.equals(NOTEQUAL)
					|| binaryExpression.getOp().kind.equals(AND) || binaryExpression.getOp().kind.equals(OR)) {
				binaryExpression.typeName = TypeName.BOOLEAN;
			}
		} else if (e0.typeName.equals(TypeName.IMAGE) && e1.typeName.equals(TypeName.IMAGE)) {
			if (binaryExpression.getOp().kind.equals(PLUS) || binaryExpression.getOp().kind.equals(MINUS)
					|| binaryExpression.getOp().kind.equals(MOD)) {
				binaryExpression.typeName = TypeName.IMAGE;
			} else if (binaryExpression.getOp().kind.equals(EQUAL) || binaryExpression.getOp().kind.equals(NOTEQUAL)
					|| binaryExpression.getOp().kind.equals(AND) || binaryExpression.getOp().kind.equals(OR)) {
				binaryExpression.typeName = TypeName.BOOLEAN;
			}
		} else if (e0.typeName.equals(TypeName.INTEGER) && e1.typeName.equals(TypeName.IMAGE)) {
			if (binaryExpression.getOp().kind.equals(TIMES)) {
				binaryExpression.typeName = TypeName.IMAGE;
			}
		} else if (e0.typeName.equals(TypeName.IMAGE) && e1.typeName.equals(TypeName.INTEGER)) {
			if (binaryExpression.getOp().kind.equals(TIMES) || binaryExpression.getOp().kind.equals(MOD)
					|| binaryExpression.getOp().kind.equals(DIV)) {
				binaryExpression.typeName = TypeName.IMAGE;
			}
		} else if (e0.typeName.equals(TypeName.BOOLEAN) && e1.typeName.equals(TypeName.BOOLEAN)) {
			if (binaryExpression.getOp().kind.equals(LT) || binaryExpression.getOp().kind.equals(GT)
					|| binaryExpression.getOp().kind.equals(LE) || binaryExpression.getOp().kind.equals(GE)
					|| binaryExpression.getOp().kind.equals(EQUAL) || binaryExpression.getOp().kind.equals(NOTEQUAL)
					|| binaryExpression.getOp().kind.equals(AND) || binaryExpression.getOp().kind.equals(OR)) {
				binaryExpression.typeName = TypeName.BOOLEAN;
			}
		} else if(e0.typeName.equals(e1.typeName)){
			binaryExpression.typeName = TypeName.BOOLEAN;
		} else {
			throw new TypeCheckException("Exception occurred at Binary Expression");
		}
		return binaryExpression;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		ArrayList<Dec> dec = block.getDecs();
		for (int i = 0; i < dec.size(); i++) {
			visitDec(dec.get(i), arg);
		}
		for (int i = 0; i < block.getStatements().size(); i++) {
			Statement stat = block.getStatements().get(i);
			stat.visit(this, arg);
		}
		symtab.leaveScope();
		return block;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		booleanLitExpression.typeName = TypeName.BOOLEAN;
		return booleanLitExpression;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		filterOpChain.getArg().visit(this, arg);
		if (filterOpChain.getArg().getExprList().size() == 0) {
			filterOpChain.typeName = TypeName.IMAGE;
		} else {
			throw new TypeCheckException("Exception occurred in filter op chain");
		}
		return filterOpChain;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		frameOpChain.getArg().visit(this, arg);
		if (frameOpChain.getFirstToken().isKind(KW_SHOW) || frameOpChain.getFirstToken().isKind(KW_HIDE)) {
			if (frameOpChain.getArg().getExprList().size() == 0) {
				frameOpChain.typeName = TypeName.NONE;
			}
		} else if (frameOpChain.getFirstToken().isKind(KW_XLOC) || frameOpChain.getFirstToken().isKind(KW_YLOC)) {
			if (frameOpChain.getArg().getExprList().size() == 0) {
				frameOpChain.typeName = TypeName.INTEGER;
			}
		} else if (frameOpChain.getFirstToken().isKind(KW_MOVE)) {
			if (frameOpChain.getArg().getExprList().size() == 2) {
				frameOpChain.typeName = TypeName.NONE;
			}
		} else
			throw new TypeCheckException("Exception occurred in frame op chain");

		return frameOpChain;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = null;
		dec = symtab.lookup(identChain.getFirstToken().getText());
		if (dec != null) {
			identChain.dec = dec;
			identChain.typeName = dec.typeName;
			return identChain;
		} else {
			throw new TypeCheckException("Exception occurred");
		}
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = null;
		dec = symtab.lookup(identExpression.getFirstToken().getText());
		if (dec != null) {
			identExpression.typeName = dec.typeName;
			identExpression.dec = dec;
		} else {
			throw new TypeCheckException("Exception occurred at Ident Expression");
		}
		return identExpression;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp = ifStatement.getE();
		exp.visit(this, arg);
		ifStatement.getB().visit(this, arg);
		if (exp.typeName.equals(TypeName.BOOLEAN)) {
			return ifStatement;
		} else {
			throw new TypeCheckException("Exception occurred in if");
		}
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		intLitExpression.typeName = TypeName.INTEGER;
		return intLitExpression;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp = sleepStatement.getE();
		exp.visit(this, arg);
		if (exp.typeName.equals(TypeName.INTEGER)) {
			return sleepStatement;
		} else {
			throw new TypeCheckException("Exception occurred in sleep statement");
		}
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp = whileStatement.getE();
		exp.visit(this, arg);
		whileStatement.getB().visit(this, arg);
		if (exp.typeName.equals(TypeName.BOOLEAN)) {
			return whileStatement;
		} else {
			throw new TypeCheckException("Exception occurred in while");
		}
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		// TODO Auto-generated method stub
		declaration.typeName = Type.getTypeName(declaration.getFirstToken());
		boolean insertOp = symtab.insert(declaration.getIdent().getText(), declaration);
		if (insertOp) { 
			return declaration;
		} else {
			throw new TypeCheckException("Exception occurred in visit dec. Variable declared twice.");
		}
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// TODO Auto-generated method stub
		ParamDec paramDec;
		int size = program.getParams().size();
		for (int j = 0; j < size; j++) {
			paramDec = program.getParams().get(j);
			visitParamDec(paramDec, arg);
		}
		visitBlock(program.getB(), arg);
		return program;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		IdentLValue identLvalue = (IdentLValue) visitIdentLValue(assignStatement.getVar(), arg);
		Expression exp = assignStatement.getE();
		exp.visit(this, arg);
		if (identLvalue.dec.typeName.equals(exp.typeName)) {
			return assignStatement;
		} else {
			throw new TypeCheckException("Exception occurred in assignment statement");
		}
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = null;
		dec = symtab.lookup(identX.getFirstToken().getText());
		if (dec != null) {
			identX.dec = dec;
			return identX;
		} else {
			throw new TypeCheckException("Exception occurred in identLValue");
		}
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		paramDec.typeName = Type.getTypeName(paramDec.getFirstToken());
		boolean insertOp = symtab.insert(paramDec.getIdent().getText(), paramDec);
		if (insertOp) { 
			return paramDec;
		} else {
			throw new TypeCheckException("Exception occurred in visit param dec. Variable declared twice.");
		} 
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		// TODO Auto-generated method stub
		constantExpression.typeName = TypeName.INTEGER;
		return constantExpression;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		imageOpChain.getArg().visit(this, arg);
		if (imageOpChain.getFirstToken().isKind(OP_WIDTH) || imageOpChain.getFirstToken().isKind(OP_HEIGHT)) {
			if (imageOpChain.getArg().getExprList().size() == 0) {
				imageOpChain.typeName = TypeName.INTEGER;
			}
		} else if (imageOpChain.getFirstToken().isKind(KW_SCALE)) {
			if (imageOpChain.getArg().getExprList().size() == 1) {
				imageOpChain.typeName = TypeName.IMAGE;
			}
		} else
			throw new TypeCheckException("Exception occurred in image op chain");

		return imageOpChain;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression exp;
		for (int i = 0; i < tuple.getExprList().size(); i++) {
			exp = tuple.getExprList().get(i);
			exp.visit(this, arg);
			if (!exp.typeName.equals(TypeName.INTEGER)) {
				throw new TypeCheckException("exception occurred at visit tuple");
			}
		}
		return tuple;
	}

}
