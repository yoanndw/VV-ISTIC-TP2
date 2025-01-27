# Code of your exercise

Put here all the code created for this exercise

Visiteur qui affiche les attributs qui n'ont pas de getter :

```java
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {
    private PrintWriter out;
    private Set<String> getters = new HashSet<>();

    public PublicElementsPrinter(PrintWriter out) {
        this.out = out;
    }

    private String fieldToGetter(String field) {
        StringBuilder getter = new StringBuilder("get").append(String.valueOf(field.charAt(0)).toUpperCase())
                .append(field.substring(1));
        return getter.toString();
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitClassDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;

        this.out.flush();
        this.getters.clear();

        this.out.println("Classe: " + declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        for (BodyDeclaration<?> field : declaration.getFields()) {
            field.accept(this, arg);
        }

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitClassDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitClassDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        // this.out.println("  " + declaration.getDeclarationAsString(true, true));

        String methodName = declaration.getNameAsString();
        if (methodName.startsWith("get")) {
            // this.out.println("Getter found " + methodName);
            this.getters.add(methodName);
        }
    }

    @Override
    public void visit(FieldDeclaration fieldDeclaration, Void arg) {
        // this.out.println("Var: " + fieldDeclaration);
        for (VariableDeclarator varDeclarator : fieldDeclaration.getVariables()) {
            // this.out.println("Var declarator: " + varDeclarator.getNameAsString());

            String getter = this.fieldToGetter(varDeclarator.getNameAsString());
            // this.out.println("Var: " + varDeclarator.getNameAsString() + ", " + getter);
            if (!this.getters.contains(getter)) {
                this.out.println("  " + varDeclarator.getNameAsString());
            }
        }

        this.out.flush();
    }

}

```

Utilisation du visiteur :

```java
public static void main(String[] args) throws IOException {
    if (args.length == 0) {
        System.err.println("Should provide the path to the source code");
        System.exit(1);
    }

    File file = new File(args[0]);
    if (!file.exists() || !file.isDirectory() || !file.canRead()) {
        System.err.println("Provide a path to an existing readable directory");
        System.exit(2);
    }

    File outFile = new File("no-getter_report.txt");
    outFile.createNewFile();
    PrintWriter outFileWriter = new PrintWriter(outFile);

    SourceRoot root = new SourceRoot(file.toPath());

    PublicElementsPrinter printer = new PublicElementsPrinter(outFileWriter);
    // PublicElementsPrinter printer = new PublicElementsPrinter(new PrintWriter(System.out));

    root.parse("", (localPath, absolutePath, result) -> {
        result.ifSuccessful(unit -> unit.accept(printer, null));
        return SourceRoot.Callback.Result.DONT_SAVE;
    });
}
```

La sortie du programme est disponible [ici](./javaparser-starter/no-getter_report.txt).