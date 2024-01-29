package fr.istic.vv;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

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
