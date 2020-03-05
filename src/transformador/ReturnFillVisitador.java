package transformador;

import japa.parser.ast.Node;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.visitor.ModifierVisitorAdapter;

import static transformador.util.Generate.runtimeException;
import static transformador.util.Util.isEnding;

public class ReturnFillVisitador extends ModifierVisitorAdapter<Void> {
	@Override
	public Node visit(MethodDeclaration n, Void arg) {
		if (n.getType().equals(new VoidType()) || n.getBody() == null || n.getBody().getStmts() == null)
			return super.visit(n, arg);
		Statement lastStatement = n.getBody().getStmts().get(n.getBody().getStmts().size() - 1);
		if (!isEnding(lastStatement))
			n.getBody().getStmts().add(runtimeException("The compiler doesn't know that this statement is unreachable"));
		return n;
	}
}
