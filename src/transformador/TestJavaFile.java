package transformador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;

@RunWith(Parameterized.class)
public class TestJavaFile {
	private static final String TRANSFORMED = "_trans";
	private static final String DOT_JAVA = ".java";
	private final String fileName;

	public TestJavaFile(String fileName) {
		this.fileName = fileName;
	}

	@Parameters(name="{index}: {0}")
	public static Collection<String> paramGen() {
		return findJavaPrograms(new File("src/Ejemplos").getAbsolutePath());
	}

	/**
	 * Recursively finds Java source files that are not transformed. For extra security, it returns
	 * the absolute paths.
	 * @param fileName File or directory to be checked.
	 * @return List of Java source files contained in the input file name or the input file name.
	 */
	private static List<String> findJavaPrograms(String fileName) {
		List<String> res = new ArrayList<String>();
		File f = new File(fileName);
		if (f.isDirectory()) {
			for (String s : f.list())
				res.addAll(findJavaPrograms(f.getAbsolutePath() + File.separator + s));
		} else if (fileName.endsWith(DOT_JAVA) && !fileName.endsWith(TRANSFORMED + DOT_JAVA)) {
			String path = f.getAbsolutePath();
			res.add(path.substring(0, path.lastIndexOf(DOT_JAVA)));
		}
		return res;
	}

	/**
	 * Transforms a java file, then compiles the original and the transformed program and runs them.
	 * Fails when the transformation fails, the compilation fails, or the output and exit codes of the
	 * executions are different.
	 */
	@Test
	public void TestFile() throws IOException, ParseException, InterruptedException {
		System.out.println("Begin test for " + fileName);
		// Abrimos el fichero original (un ".java")
		File original = new File(this.fileName + DOT_JAVA);

		// Parseamos el fichero original. Se crea una unidad de compilaci�n (un AST).
		CompilationUnit cu = JavaParser.parse(original);

		// Recorremos el AST
		new Visitador().visit(cu, null);

		// Le cambiamos el nombre a la clase principal
		// (le a�adimos "_trans" para que coincida con el nombre del fichero)
		TypeDeclaration clazz = cu.getTypes().get(0);
		clazz.setName(clazz.getName() + TRANSFORMED);

		// Convertimos en un String el programa transformado
		String transformado = cu.toString();

		// Escribimos en un fichero el programa transformado
		File destino = new File(this.fileName + TRANSFORMED + DOT_JAVA);
		FileWriter fw = new FileWriter(destino);
		fw.write(transformado);
		fw.close();

		// Compilamos el programa original y el transformado
		Process compOrig = Runtime.getRuntime().exec("javac -d bin " + original.getPath());
		Process compNew  = Runtime.getRuntime().exec("javac -d bin " + destino.getPath());
		compOrig.waitFor();
		compNew.waitFor();
		if (compOrig.exitValue() != 0) throw new RuntimeException("Ha fallado la compilación de la versión original de " + this.fileName);
		if (compNew.exitValue() != 0) throw new RuntimeException("Ha fallado la compilación de la versión transformada de " + this.fileName);
		// Ejecutamos ambos programas y comparamos el output
		String canonicalName = cu.getPackage().getName() + "." + original.getName().substring(0, original.getName().lastIndexOf(".java"));
		ProcessBuilder runOriB = new ProcessBuilder("java", "-cp", "bin", canonicalName);
		ProcessBuilder runNewB = new ProcessBuilder("java", "-cp", "bin", canonicalName + TRANSFORMED);
		final Process runOri = runOriB.start();
		final Process runNew = runNewB.start();
		final boolean[] killed = new boolean[] { false };
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO: if the first one doesn't end, then the second doesn't have to end
					// TODO: if the first one ends, the second must end
					Thread.sleep(5000);
					runOri.destroy();
					runNew.destroy();
					killed[0] = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		runOri.waitFor();
		runNew.waitFor();
		if (killed[0]) throw new RuntimeException("Maximum runtime exceeded");
		diffLines(runOri.getInputStream(), runNew.getInputStream());
		diffLines(runOri.getErrorStream(), runNew.getErrorStream());
		if (runOri.exitValue() != runNew.exitValue())
			throw new RuntimeException("The return codes did not match: original is " + runOri.exitValue() +
				" and transformed is " + runNew.exitValue());
		System.out.println("Test para " + this.fileName + " OK");
	}

	/** Checks if two InputStream objects match */
	private void diffLines(InputStream original, InputStream transformed) throws IOException {
		String resOrig = stream2string(original);
		String resTran = stream2string(transformed);

		if (!resOrig.equals(resTran)) {
			System.out.println("Salida del programa iterativo:");
			System.out.println(resOrig);
			System.out.println("Salida del programa recursivo:");
			System.out.println(resTran);
			throw new RuntimeException("La salida de los programas no coincide");
		}
	}

	/** Reads an InputStream until it closes, and returns the whole input as a string. */
	private String stream2string(InputStream stream) throws IOException {
		StringBuilder builder = new StringBuilder();
		String buffer;
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		// When the stream closes and there is no more content to read, reader.readLine() will be null
		while ((buffer = reader.readLine()) != null) {
			builder.append(buffer).append('\n');
		}
		return builder.toString();
	}
}