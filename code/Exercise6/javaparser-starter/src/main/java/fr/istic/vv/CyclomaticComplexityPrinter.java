package fr.istic.vv;

import java.util.List;

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


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityPrinter extends VoidVisitorWithDefaults<Void> {
    int nbNodes, nbEdges, nbEndNodes;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
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
        System.out.println("  " + declaration.getNameAsString() + ": CC = " + cc);
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
