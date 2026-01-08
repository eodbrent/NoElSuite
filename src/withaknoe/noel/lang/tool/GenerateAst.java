package withaknoe.noel.lang.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            //System.exit(64);
        }

        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Assign    : Token name, Expr value",
            "Binary    : Expr left, Token operator, Expr right",
            "Call      : Expr callee, Token paren, List<Expr> arguments",
            "Get       : Expr object, Token name",
            "Grouping  : Expr expression",
            "Literal   : Object value",
            "Logical   : Expr left, Token operator, Expr right",
            "Unary     : Token operator, Expr right",
            "Variable  : Token name"
        ));

        // One Primitive is good add: Line : Token name, Expr start, Expr end | Arc : Token name, Expr origin, Expr radius, Expr start, Expr end
        //                                    | Circle : Token name, Expr center, Expr radius
        defineAst(outputDir, "Stmt", Arrays.asList(
            "Block      : List<Stmt> statements",
            "Expression : Expr expression",
            "Function   : Token name, List<Token> params, List<Stmt> body",
            "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Primitive  : Token primitiveKeyword, Token name, List<Expr> properties",
            "Print      : Expr expression",
            "Return     : Token keyword, Expr value", // ~ will change when I get here. NoEl returns are handled much differently
            "Let        : Token name, Expr initializer",
            "While      : Expr condition, Stmt body"));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        // because intelliJ doesn't like the whitespace.
        writer.println("// @formatter:off");
        writer.println("package withaknoe.noel.lang;");
        writer.println("");
        writer.println("import java.util.List;");
        writer.println("");
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        // AST Classes
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        // accept() template
        writer.println();
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");
        writer.println("}");
        writer.println("// @formatter:on");

        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");

        // constructor
        writer.println("    " + className + "(" + fieldList + ") {");

        // parameters go in fields
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("    this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // the vbisitor stuff
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("        return visitor.visit" + className + baseName + "(this);");
        writer.println("    }");

        // now fields
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("    }");
    }

}
