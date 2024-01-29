# Code of your exercise

Put here all the code created for this exercise

Visiteur qui calcule la complexité cyclomatique de toutes les méthodes :

```java
public class CyclomaticComplexityPrinter extends VoidVisitorWithDefaults<Void> {
    PrintWriter out;
    String currentClassName;
    int nbNodes, nbEdges, nbEndNodes;

    public CyclomaticComplexityPrinter(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }

        out.flush();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        currentClassName = declaration.getFullyQualifiedName().orElse("[Anonymous]");
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        nbNodes = 0;
        nbEdges = 0;
        nbEndNodes = 1;

        declaration.getBody().get().accept(this, arg);

        int cc = nbEdges - nbNodes + 2 * nbEndNodes;

        // System.out.println("  " + declaration.getNameAsString() + ": " + nbNodes + " nodes; " + nbEdges + " edges; " + nbEndNodes + " end nodes; " + "CC = " + cc);
        String parameters = declaration.getParameters().stream().map(p -> p.toString()).collect(Collectors.joining("; "));
        out.println(currentClassName + "," + declaration.getNameAsString() + "," + parameters + "," + cc);
    }

    public void visit(BlockStmt stmt, Void arg) {
        List<Node> nodes = stmt.getChildNodes();
        nbNodes += nodes.size();
        // System.out.println("    stmt children: " + nodes.size());

        for (Node node : nodes) {
            if (node instanceof IfStmt || node instanceof ForStmt || node instanceof ForEachStmt || node instanceof WhileStmt) {
                nbEdges += 2;
            } else {
                nbEdges++;
            }

            if (node instanceof ReturnStmt) {
                nbEndNodes++;
            }

            node.accept(this, arg);
        }
    }

}
```

Méthode `main` appelant ce visiteur :

```java
public static void main(String[] args) throws IOException {
    if(args.length != 2) {
        System.err.println("Should provide the path to the source code and the output CSV path");
        System.exit(1);
    }

    File file = new File(args[0]);
    if(!file.exists() || !file.isDirectory() || !file.canRead()) {
        System.err.println("Provide a path to an existing readable directory");
        System.exit(2);
    }

    File outputFile = new File(args[1]);
    outputFile.createNewFile();

    SourceRoot root = new SourceRoot(file.toPath());
    CyclomaticComplexityPrinter printer = new CyclomaticComplexityPrinter(new PrintWriter(outputFile));
    root.parse("", (localPath, absolutePath, result) -> {
        result.ifSuccessful(unit -> unit.accept(printer, null));
        return SourceRoot.Callback.Result.DONT_SAVE;
    });
}
```