package transformador;

import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.stmt.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static transformador.util.Transform.int2e;
import static transformador.util.Transform.string2e;

/** Visitor that combs through the body of a loop replacing any control breaking statement with specialized instructions */
class LoopBodyVisitor extends VisitadorAdvanced {
	private final int initialSwitchCounter = super.switchCounter;
	private final List<Expression> argList;
	private final List<Statement> continueStmts;
	private final String methodLabel;
	private boolean canReturn = false, canBreak = false, canContinue = false;
	private int loopLevel = 0;

	LoopBodyVisitor(List<Expression> argList, List<Statement> continueStmts, String methodLabel) {
		this.argList = argList;
		this.continueStmts = continueStmts;
		this.methodLabel = methodLabel;
	}

	protected boolean[] rbcArray() {
		return new boolean[] { canReturn, canBreak, canContinue };
	}

	private ReturnStmt generateControlledReturn(int code, Expression extra) {
		ArrayCreationExpr expr = new ArrayCreationExpr(
				T_OBJECT_ARRAY, 0, new ArrayInitializerExpr(new LinkedList<Expression>()));
		expr.getInitializer().getValues().add(
				new ArrayCreationExpr(T_OBJECT_ARRAY, 0,
						new ArrayInitializerExpr(Arrays.asList(
								int2e(code), extra))));
		expr.getInitializer().getValues().addAll(argList);
		return new ReturnStmt(expr);
	}

	/**
	 * Return statement visitor. Transforms return statements found within methods created from loops, and allows
	 * loops to work even with return statements within them.
	 */
	@Override
	public Node visit(ReturnStmt returnStmt, Object args) {
		canReturn = true;
		if (loopLevel != 0) return super.visit(returnStmt, args);
		return generateControlledReturn(Control.returnStmt.value,
				returnStmt.getExpr() != null ? returnStmt.getExpr() : new NullLiteralExpr());
	}

	/**
	 * Continue statement visitor. Transforms only statements found within loops.
	 */
	@Override
	public Node visit(ContinueStmt continueStmt, Object args) {
		canContinue = true;
		if (loopLevel != 0) return super.visit(continueStmt, args);
		if (continueStmt.getId() == null || continueStmt.getId().equals(methodLabel)) {
			return new BlockStmt(continueStmts);
		} else {
			return generateControlledReturn(Control.continueStmt.value, string2e(continueStmt.getId()));
		}
	}

	/**
	 * Break statement visitor. Transforms only statements found within loops but outside of {@code switch} statements
	 */
	@Override
	public Node visit(BreakStmt breakStmt, Object args) {
		if (switchCounter - initialSwitchCounter == 0 || breakStmt.getId() != null) {
			canBreak = true;
			if (loopLevel != 0) return super.visit(breakStmt, args);
			if (breakStmt.getId() == null || breakStmt.getId().equals(methodLabel)) {
				List<Expression> retArgs = new LinkedList<Expression>(argList);
				retArgs.add(0, new NullLiteralExpr());
				return new ReturnStmt(new ArrayCreationExpr(
						T_OBJECT_ARRAY, 0, new ArrayInitializerExpr(retArgs)));
			}
			return generateControlledReturn(Control.breakStmt.value, string2e(breakStmt.getId()));
		} else {
			return breakStmt;
		}
	}

	/** Loop statements go unmodified */
	@Override
	public Node visit(WhileStmt stmt, Object args) {
		loopLevel++;
		stmt.getBody().accept(this, args);
		loopLevel--;
		return stmt;
	}

	/** Loop statements go unmodified */
	@Override
	public Node visit(DoStmt stmt, Object args) {
		loopLevel++;
		stmt.getBody().accept(this, args);
		loopLevel--;
		return stmt;
	}

	/** Loop statements go unmodified */
	@Override
	public Node visit(ForStmt stmt, Object args) {
		loopLevel++;
		stmt.getBody().accept(this, args);
		loopLevel--;
		return stmt;
	}

	/** Loop statements go unmodified */
	@Override
	public Node visit(ForeachStmt stmt, Object args) {
		loopLevel++;
		stmt.getBody().accept(this, args);
		loopLevel--;
		return stmt;
	}

	/** Declarations go unmodified */
	@Override
	public Node visit(ClassOrInterfaceDeclaration classDeclaration, Object args) {
		return classDeclaration;
	}

	/** Declarations go unmodified */
	@Override
	public Node visit(MethodDeclaration methodDeclaration, Object args) {
		return methodDeclaration;
	}
}
