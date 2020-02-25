package transformador;

import java.lang.instrument.ClassDefinition;
import java.util.LinkedList;
import java.util.List;

import iter2rec.transformation.loop.Loop;
import iter2rec.transformation.loop.While;
import iter2rec.transformation.variable.LoopVariables;
import iter2rec.transformation.variable.Variable;
import japa.parser.ast.*;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.ModifierVisitorAdapter;

public class Visitador extends ModifierVisitorAdapter<Object>
{
	/********************************************************/
	/********************** Atributos ***********************/
	/********************************************************/
	
	// Usamos un contador para numerar los mŽtodos que creemos
	int contador=1;  
	// Variable usada para conocer la lista de mŽtodos visitados
	LinkedList<MethodDeclaration> previousMethodDeclarations = new LinkedList<MethodDeclaration>();
	// Variable usada para saber cu‡l es el œltimo mŽtodo visitado (el que estoy visitando ahora)
	MethodDeclaration methodDeclaration;
	// Variable usada para conocer la lista de clases visitadas
	LinkedList<ClassOrInterfaceDeclaration> previousClassDeclarations = new LinkedList<ClassOrInterfaceDeclaration>();
	// Variable usada para saber cu‡l es la œltima clase visitada (la que estoy visitando ahora)	
	ClassOrInterfaceDeclaration classDeclaration;

	/********************************************************/
	/*********************** Metodos ************************/
	/********************************************************/

	// Visitador de clases
	// Este visitador no hace nada, simplemente registra en una lista las clases que se van visitando
	public Node visit(ClassOrInterfaceDeclaration classDeclaration, Object args)
	{
		this.previousClassDeclarations.add(classDeclaration);
		this.classDeclaration = classDeclaration;
		Node newClassDeclaration = super.visit(classDeclaration, args);
		this.previousClassDeclarations.removeLast();
		this.classDeclaration = this.previousClassDeclarations.isEmpty() ? null : this.previousClassDeclarations.getLast();
		
		return newClassDeclaration;
	}
	// Visitador de mŽtodos
	// Este visitador no hace nada, simplemente registra en una lista los mŽetodos que se van visitando	
	public Node visit(MethodDeclaration methodDeclaration, Object args)
	{
		this.previousMethodDeclarations.add(methodDeclaration);
		this.methodDeclaration = methodDeclaration;
		Node newMethodDeclaration = super.visit(methodDeclaration, args);
		this.previousMethodDeclarations.removeLast();
		this.methodDeclaration = this.previousMethodDeclarations.isEmpty() ? null : this.previousMethodDeclarations.getLast();

		return newMethodDeclaration;
	}
	
	// Visitador de sentencias "do while"
	public Node visit(DoStmt whileStmt, Object args)
	{
		//Creamos el If recursivo 
		IfStmt newIf = new IfStmt();
		newIf.setCondition(whileStmt.getCondition());
		//Object
		ClassOrInterfaceType objType = new ClassOrInterfaceType();
		objType.setName("Object");
		// Creamos un objeto Loop que sirve para examinar bucles
		Loop loop = new While(null, null, whileStmt);
		// El objeto Loop nos calcula la lista de variables declaradas en el método y usadas en el bucle (la intersección)
		List<Variable> variables = loop.getUsedVariables(methodDeclaration);
		// Creamos un objeto LoopVariables que sirve para convertir la lista de variables en lista de argumentos y parámetros
		LoopVariables loopVariables = new LoopVariables(variables);
		// El objeto LoopVariables nos calcula la lista de argumentos del método 
		List<Expression> arguments = loopVariables.getArgs();
		//Declaración del segundo método
		MethodDeclaration methodDeclaration2 = new MethodDeclaration();
		methodDeclaration2.setName("method"+contador);
		methodDeclaration2.setModifiers(methodDeclaration.getModifiers());
		String nameMethod = methodDeclaration2.getName();
		contador++;
		
		
		//Mantener el típo del método en la nueva llamada
		ClassOrInterfaceType object = new ClassOrInterfaceType();
		object.setName("Object");
		ReferenceType type = new ReferenceType();
		type.setArrayCount(1);
		type.setType(object);
		
		methodDeclaration2.setType(type);
		methodDeclaration2.setParameters(loopVariables.getParameters());
		
		// Añadimos el body del do while al principio del metodo
		
		BlockStmt cuerpoMetodo = new BlockStmt();
		List<Statement> stmtsMetodo = new LinkedList<Statement>();
		MethodCallExpr callMethod = new MethodCallExpr();
		callMethod.setName(nameMethod);
		callMethod.setArgs(arguments);
		stmtsMetodo.add(whileStmt.getBody());
		ReturnStmt returnIf = new ReturnStmt();
		returnIf.setExpr(callMethod);
		
		List<Statement> methodBody = new LinkedList<Statement>();
		
		
		methodBody.add(returnIf);
		
		
		BlockStmt blockIf2 = new BlockStmt();
		blockIf2.setStmts(methodBody);
		
		// Creamos el if recursivo dentro del nuevo método
		IfStmt recIf = new IfStmt(whileStmt.getCondition(), blockIf2, null);
		stmtsMetodo.add(recIf);
		cuerpoMetodo.setStmts(stmtsMetodo);
		methodDeclaration2.setBody(cuerpoMetodo);
		
		
		//Añadimos el nuevo metodo a la clase
		this.classDeclaration.getMembers().add(methodDeclaration2);
		
		ReturnStmt returnStmt = new ReturnStmt();
		ArrayInitializerExpr arrayInitializerExpr = new ArrayInitializerExpr();
		arrayInitializerExpr.setValues(arguments);
		
		ArrayCreationExpr arrayCreationExpr = new ArrayCreationExpr();
		arrayCreationExpr.setType(objType);
		arrayCreationExpr.setArrayCount(1);
		arrayCreationExpr.setInitializer(arrayInitializerExpr);
		returnStmt.setExpr(arrayCreationExpr);
		stmtsMetodo.add(returnStmt);
		
		
		return newIf;
	}
	
	// Visitador de sentencias "while"
	public Node visit(WhileStmt whileStmt, Object args)
	{
		
		/**************************/
		/******** LLAMADOR ********/
		/**************************/		
		
		// Creamos un objeto Loop que sirve para examinar bucles
		Loop loop = new While(null, null, whileStmt);
		// El objeto Loop nos calcula la lista de variables declaradas en el método y usadas en el bucle (la intersección)
		List<Variable> variables = loop.getUsedVariables(methodDeclaration);
		// Creamos un objeto LoopVariables que sirve para convertir la lista de variables en lista de argumentos y parámetros
		LoopVariables loopVariables = new LoopVariables(variables);
		// El objeto LoopVariables nos calcula la lista de argumentos del método 
		List<Expression> arguments = loopVariables.getArgs();
		//Creamos el If recursivo 
		IfStmt newIf = new IfStmt();
		newIf.setCondition(whileStmt.getCondition());
		
		//*** If ***
		BlockStmt blockIf = new BlockStmt();
		newIf.setThenStmt(blockIf);
		List<Statement> bodyIf = new LinkedList<Statement>();
				
		//while(***condition***)
		Expression condition = whileStmt.getCondition();
		
		//method_x()
		MethodCallExpr methodCall = new MethodCallExpr();
		methodCall.setName("method"+contador);
		methodCall.setArgs(arguments);
		
		//Object
		ClassOrInterfaceType objType = new ClassOrInterfaceType();
		objType.setName("Object");
		
		//Object[]
		ReferenceType refType = new ReferenceType();
		refType.setArrayCount(1);
		refType.setType(objType);
		
		//Object[] result = this.metodo_x()
		NameExpr resultExpr = new NameExpr("result");
		List<VariableDeclarator> vars = new LinkedList<VariableDeclarator>();
		VariableDeclarator var = new VariableDeclarator();
		VariableDeclaratorId varId = new VariableDeclaratorId();
		varId.setName(resultExpr.getName());
		vars.add(var);
		var.setId(varId);
		var.setInit(methodCall);
		
		//Object[] result = this.metodo_x()
		VariableDeclarationExpr varDecExpr = new VariableDeclarationExpr();
		varDecExpr.setType(refType);
		varDecExpr.setVars(vars);
		
		//Object[] result = this.metodo_x()
		ExpressionStmt exprStmt = new ExpressionStmt();
		exprStmt.setExpression(varDecExpr);
		
		//*** If ***  
		bodyIf.add(exprStmt);
		
		List<Type> types = loopVariables.getReturnTypes();
		List<String> names = loopVariables.getReturnNames();
		
		// Bucle para recorrer todas las variables
		int i = 0;
		while(i < vars.size()) {
			//result[i]
			ArrayAccessExpr expr = new ArrayAccessExpr();
			expr.setName(new NameExpr("result"));
			expr.setIndex(new IntegerLiteralExpr(i+""));
			//Casting para cada variable
			CastExpr cast = new CastExpr();
			cast.setType(getWrapper(variables.get(i).getType()));
			cast.setExpr(expr);
			
			bodyIf.add(new ExpressionStmt(variables.get(i).getAssignationExpr(cast)));
			i++;
		}
				
		blockIf.setStmts(bodyIf);
		newIf.setThenStmt(blockIf);
		
		/**************************/
		/********* METODO *********/
		/**************************/
		
		//Declaración del segundo método
		MethodDeclaration methodDeclaration2 = new MethodDeclaration();
		methodDeclaration2.setName("method"+contador);
		methodDeclaration2.setModifiers(methodDeclaration.getModifiers());
		String nameMethod = methodDeclaration2.getName();
		contador++;
		
		
		//Mantener el típo del método en la nueva llamada
		ClassOrInterfaceType object = new ClassOrInterfaceType();
		object.setName("Object");
		ReferenceType type = new ReferenceType();
		type.setArrayCount(1);
		type.setType(object);
		
		methodDeclaration2.setType(type);
		methodDeclaration2.setParameters(loopVariables.getParameters());
		
		// Añadimos el body del do while al principio del metodo
		
		BlockStmt cuerpoMetodo = new BlockStmt();
		List<Statement> stmtsMetodo = new LinkedList<Statement>();
		MethodCallExpr callMethod = new MethodCallExpr();
		callMethod.setName(nameMethod);
		callMethod.setArgs(arguments);
		stmtsMetodo.add(whileStmt.getBody());
		ReturnStmt returnIf = new ReturnStmt();
		returnIf.setExpr(callMethod);
		
		List<Statement> methodBody = new LinkedList<Statement>();
		
		
		methodBody.add(returnIf);
		
		
		BlockStmt blockIf2 = new BlockStmt();
		blockIf2.setStmts(methodBody);
		
		// Creamos el if recursivo dentro del nuevo método
		IfStmt recIf = new IfStmt(whileStmt.getCondition(), blockIf2, null);
		stmtsMetodo.add(recIf);
		cuerpoMetodo.setStmts(stmtsMetodo);
		methodDeclaration2.setBody(cuerpoMetodo);
		
		
		//Añadimos el nuevo metodo a la clase
		this.classDeclaration.getMembers().add(methodDeclaration2);
		
		ReturnStmt returnStmt = new ReturnStmt();
		ArrayInitializerExpr arrayInitializerExpr = new ArrayInitializerExpr();
		arrayInitializerExpr.setValues(arguments);
		
		ArrayCreationExpr arrayCreationExpr = new ArrayCreationExpr();
		arrayCreationExpr.setType(objType);
		arrayCreationExpr.setArrayCount(1);
		arrayCreationExpr.setInitializer(arrayInitializerExpr);
		returnStmt.setExpr(arrayCreationExpr);
		stmtsMetodo.add(returnStmt);
		
		
		return newIf;
	}

	// Dada un tipo, 
	// Si es un tipo primitivo, devuelve el wrapper correspondiente 
	// Si es un tipo no primitivo, lo devuelve
	private Type getWrapper(Type type)
	{
		if (!(type instanceof PrimitiveType))
			return type;

		PrimitiveType primitiveType = (PrimitiveType) type;
		String primitiveName = primitiveType.getType().name();
		String wrapperName = primitiveName;

		if (wrapperName.equals("Int"))
			wrapperName = "Integer";
		else if (wrapperName.equals("Char"))
			wrapperName = "Character";

		return new ClassOrInterfaceType(wrapperName);
	}
	// Dada una sentencia, 
	// Si es una œnica instrucci—n, devuelve un bloque equivalente 
	// Si es un bloque, lo devuelve
	private BlockStmt blockWrapper(Statement statement)
	{
		if (statement instanceof BlockStmt)
			return (BlockStmt) statement;

		BlockStmt block = new BlockStmt();
		List<Statement> blockStmts = new LinkedList<Statement>();
		blockStmts.add(statement);

		block.setStmts(blockStmts);

		return block;
	}
}