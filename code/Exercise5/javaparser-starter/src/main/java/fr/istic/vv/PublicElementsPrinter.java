package fr.istic.vv;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
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
