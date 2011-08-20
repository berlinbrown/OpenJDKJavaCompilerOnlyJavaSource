/*
 * Copyright (c) 2001, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 only, as published by
 * the Free Software Foundation. Oracle designates this particular file as
 * subject to the "Classpath" exception as provided by Oracle in the LICENSE
 * file that accompanied this code.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License version 2 for more
 * details (a copy is included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA or
 * visit www.oracle.com if you need additional information or have any
 * questions.
 */

package berlin.com.sun.tools.javac.tree;

import berlin.com.sun.tools.javac.tree.JCTree.JCAnnotation;
import berlin.com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import berlin.com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import berlin.com.sun.tools.javac.tree.JCTree.JCAssert;
import berlin.com.sun.tools.javac.tree.JCTree.JCAssign;
import berlin.com.sun.tools.javac.tree.JCTree.JCAssignOp;
import berlin.com.sun.tools.javac.tree.JCTree.JCBinary;
import berlin.com.sun.tools.javac.tree.JCTree.JCBlock;
import berlin.com.sun.tools.javac.tree.JCTree.JCBreak;
import berlin.com.sun.tools.javac.tree.JCTree.JCCase;
import berlin.com.sun.tools.javac.tree.JCTree.JCCatch;
import berlin.com.sun.tools.javac.tree.JCTree.JCClassDecl;
import berlin.com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import berlin.com.sun.tools.javac.tree.JCTree.JCConditional;
import berlin.com.sun.tools.javac.tree.JCTree.JCContinue;
import berlin.com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import berlin.com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import berlin.com.sun.tools.javac.tree.JCTree.JCErroneous;
import berlin.com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import berlin.com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import berlin.com.sun.tools.javac.tree.JCTree.JCForLoop;
import berlin.com.sun.tools.javac.tree.JCTree.JCIdent;
import berlin.com.sun.tools.javac.tree.JCTree.JCIf;
import berlin.com.sun.tools.javac.tree.JCTree.JCImport;
import berlin.com.sun.tools.javac.tree.JCTree.JCInstanceOf;
import berlin.com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
import berlin.com.sun.tools.javac.tree.JCTree.JCLiteral;
import berlin.com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import berlin.com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import berlin.com.sun.tools.javac.tree.JCTree.JCModifiers;
import berlin.com.sun.tools.javac.tree.JCTree.JCNewArray;
import berlin.com.sun.tools.javac.tree.JCTree.JCNewClass;
import berlin.com.sun.tools.javac.tree.JCTree.JCParens;
import berlin.com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import berlin.com.sun.tools.javac.tree.JCTree.JCReturn;
import berlin.com.sun.tools.javac.tree.JCTree.JCSkip;
import berlin.com.sun.tools.javac.tree.JCTree.JCSwitch;
import berlin.com.sun.tools.javac.tree.JCTree.JCSynchronized;
import berlin.com.sun.tools.javac.tree.JCTree.JCThrow;
import berlin.com.sun.tools.javac.tree.JCTree.JCTry;
import berlin.com.sun.tools.javac.tree.JCTree.JCTypeApply;
import berlin.com.sun.tools.javac.tree.JCTree.JCTypeCast;
import berlin.com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import berlin.com.sun.tools.javac.tree.JCTree.JCUnary;
import berlin.com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import berlin.com.sun.tools.javac.tree.JCTree.JCWhileLoop;
import berlin.com.sun.tools.javac.tree.JCTree.JCWildcard;
import berlin.com.sun.tools.javac.tree.JCTree.LetExpr;
import berlin.com.sun.tools.javac.tree.JCTree.TypeBoundKind;
import berlin.com.sun.tools.javac.tree.JCTree.Visitor;
import berlin.com.sun.tools.javac.util.List;

/**
 * A subclass of Tree.Visitor, this class defines a general tree scanner
 * pattern.
 */
public class TreeScanner extends Visitor {

    /**
     * Visitor method: Scan a single node.
     */
    public void scan(JCTree tree) {
        if (tree != null)
            tree.accept(this);
    }

    /**
     * Visitor method: scan a list of nodes.
     */
    public void scan(List<? extends JCTree> trees) {
        if (trees != null)
            for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
                scan(l.head);
    }

    /*
     * **************************************************************************
     * * Visitor methods
     * *********************************************************
     * *****************
     */

    public void visitTopLevel(JCCompilationUnit tree) {
        scan(tree.packageAnnotations);
        scan(tree.pid);
        scan(tree.defs);
    }

    public void visitImport(JCImport tree) {
        scan(tree.qualid);
    }

    public void visitClassDef(JCClassDecl tree) {
        scan(tree.mods);
        scan(tree.typarams);
        scan(tree.extending);
        scan(tree.implementing);
        scan(tree.defs);
    }

    public void visitMethodDef(JCMethodDecl tree) {
        scan(tree.mods);
        scan(tree.restype);
        scan(tree.typarams);
        scan(tree.params);
        scan(tree.thrown);
        scan(tree.defaultValue);
        scan(tree.body);
    }

    public void visitVarDef(JCVariableDecl tree) {
        scan(tree.mods);
        scan(tree.vartype);
        scan(tree.init);
    }

    public void visitSkip(JCSkip tree) {
    }

    public void visitBlock(JCBlock tree) {
        scan(tree.stats);
    }

    public void visitDoLoop(JCDoWhileLoop tree) {
        scan(tree.body);
        scan(tree.cond);
    }

    public void visitWhileLoop(JCWhileLoop tree) {
        scan(tree.cond);
        scan(tree.body);
    }

    public void visitForLoop(JCForLoop tree) {
        scan(tree.init);
        scan(tree.cond);
        scan(tree.step);
        scan(tree.body);
    }

    public void visitForeachLoop(JCEnhancedForLoop tree) {
        scan(tree.var);
        scan(tree.expr);
        scan(tree.body);
    }

    public void visitLabelled(JCLabeledStatement tree) {
        scan(tree.body);
    }

    public void visitSwitch(JCSwitch tree) {
        scan(tree.selector);
        scan(tree.cases);
    }

    public void visitCase(JCCase tree) {
        scan(tree.pat);
        scan(tree.stats);
    }

    public void visitSynchronized(JCSynchronized tree) {
        scan(tree.lock);
        scan(tree.body);
    }

    public void visitTry(JCTry tree) {
        scan(tree.body);
        scan(tree.catchers);
        scan(tree.finalizer);
    }

    public void visitCatch(JCCatch tree) {
        scan(tree.param);
        scan(tree.body);
    }

    public void visitConditional(JCConditional tree) {
        scan(tree.cond);
        scan(tree.truepart);
        scan(tree.falsepart);
    }

    public void visitIf(JCIf tree) {
        scan(tree.cond);
        scan(tree.thenpart);
        scan(tree.elsepart);
    }

    public void visitExec(JCExpressionStatement tree) {
        scan(tree.expr);
    }

    public void visitBreak(JCBreak tree) {
    }

    public void visitContinue(JCContinue tree) {
    }

    public void visitReturn(JCReturn tree) {
        scan(tree.expr);
    }

    public void visitThrow(JCThrow tree) {
        scan(tree.expr);
    }

    public void visitAssert(JCAssert tree) {
        scan(tree.cond);
        scan(tree.detail);
    }

    public void visitApply(JCMethodInvocation tree) {
        scan(tree.meth);
        scan(tree.args);
    }

    public void visitNewClass(JCNewClass tree) {
        scan(tree.encl);
        scan(tree.clazz);
        scan(tree.args);
        scan(tree.def);
    }

    public void visitNewArray(JCNewArray tree) {
        scan(tree.elemtype);
        scan(tree.dims);
        scan(tree.elems);
    }

    public void visitParens(JCParens tree) {
        scan(tree.expr);
    }

    public void visitAssign(JCAssign tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitAssignop(JCAssignOp tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitUnary(JCUnary tree) {
        scan(tree.arg);
    }

    public void visitBinary(JCBinary tree) {
        scan(tree.lhs);
        scan(tree.rhs);
    }

    public void visitTypeCast(JCTypeCast tree) {
        scan(tree.clazz);
        scan(tree.expr);
    }

    public void visitTypeTest(JCInstanceOf tree) {
        scan(tree.expr);
        scan(tree.clazz);
    }

    public void visitIndexed(JCArrayAccess tree) {
        scan(tree.indexed);
        scan(tree.index);
    }

    public void visitSelect(JCFieldAccess tree) {
        scan(tree.selected);
    }

    public void visitIdent(JCIdent tree) {
    }

    public void visitLiteral(JCLiteral tree) {
    }

    public void visitTypeIdent(JCPrimitiveTypeTree tree) {
    }

    public void visitTypeArray(JCArrayTypeTree tree) {
        scan(tree.elemtype);
    }

    public void visitTypeApply(JCTypeApply tree) {
        scan(tree.clazz);
        scan(tree.arguments);
    }

    public void visitTypeParameter(JCTypeParameter tree) {
        scan(tree.bounds);
    }

    @Override
    public void visitWildcard(JCWildcard tree) {
        scan(tree.kind);
        if (tree.inner != null)
            scan(tree.inner);
    }

    @Override
    public void visitTypeBoundKind(TypeBoundKind that) {
    }

    public void visitModifiers(JCModifiers tree) {
        scan(tree.annotations);
    }

    public void visitAnnotation(JCAnnotation tree) {
        scan(tree.annotationType);
        scan(tree.args);
    }

    public void visitErroneous(JCErroneous tree) {
    }

    public void visitLetExpr(LetExpr tree) {
        scan(tree.defs);
        scan(tree.expr);
    }

    public void visitTree(JCTree tree) {
        assert false;
    }
}
