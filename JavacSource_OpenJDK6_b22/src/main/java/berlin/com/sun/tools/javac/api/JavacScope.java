/*
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved. DO NOT
 * ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
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

package berlin.com.sun.tools.javac.api;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Iterator;

import berlin.com.sun.source.tree.Tree;
import berlin.com.sun.source.util.SourcePositions;
import berlin.com.sun.source.util.TreePath;
import berlin.com.sun.source.util.Trees;
import berlin.com.sun.tools.javac.code.Scope;
import berlin.com.sun.tools.javac.code.Symbol.ClassSymbol;
import berlin.com.sun.tools.javac.comp.Attr;
import berlin.com.sun.tools.javac.comp.AttrContext;
import berlin.com.sun.tools.javac.comp.Enter;
import berlin.com.sun.tools.javac.comp.Env;
import berlin.com.sun.tools.javac.comp.MemberEnter;
import berlin.com.sun.tools.javac.comp.Resolve;
import berlin.com.sun.tools.javac.tree.JCTree;
import berlin.com.sun.tools.javac.tree.TreeCopier;
import berlin.com.sun.tools.javac.tree.TreeInfo;
import berlin.com.sun.tools.javac.tree.TreeMaker;
import berlin.com.sun.tools.javac.tree.JCTree.JCClassDecl;
import berlin.com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import berlin.com.sun.tools.javac.tree.JCTree.JCExpression;
import berlin.com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import berlin.com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import berlin.com.sun.tools.javac.util.Context;
import berlin.com.sun.tools.javac.util.List;
import berlin.com.sun.tools.javac.util.Log;
import berlin.javax.lang.model.element.Element;
import berlin.javax.lang.model.element.ExecutableElement;
import berlin.javax.lang.model.element.TypeElement;
import berlin.javax.tools.JavaFileObject;

import static berlin.com.sun.source.tree.Tree.Kind.*;

/**
 * Provides an implementation of Scope.
 * @author Jonathan Gibbons;
 */
public class JavacScope implements berlin.com.sun.source.tree.Scope {
    protected final Env<AttrContext> env;

    /** Creates a new instance of JavacScope */
    JavacScope(Env<AttrContext> env) {
        env.getClass(); // null-check
        this.env = env;
    }

    public JavacScope getEnclosingScope() {
        if (env.outer != null && env.outer != env)
            return new JavacScope(env.outer);
        else {
            // synthesize an outermost "star-import" scope
            return new JavacScope(env) {
                public boolean isStarImportScope() {
                    return true;
                }

                public JavacScope getEnclosingScope() {
                    return null;
                }

                public Iterable<? extends Element> getLocalElements() {
                    return env.toplevel.starImportScope.getElements();
                }
            };
        }
    }

    public TypeElement getEnclosingClass() {
        // hide the dummy class that javac uses to enclose the top level
        // declarations
        return (env.outer == null || env.outer == env ? null : env.enclClass.sym);
    }

    public ExecutableElement getEnclosingMethod() {
        return (env.enclMethod == null ? null : env.enclMethod.sym);
    }

    public Iterable<? extends Element> getLocalElements() {
        return env.info.getLocalElements();
    }

    public Env<AttrContext> getEnv() {
        return env;
    }

    public boolean isStarImportScope() {
        return false;
    }

    public boolean equals(Object other) {
        if (other instanceof JavacScope) {
            JavacScope s = (JavacScope) other;
            return (env.equals(s.env) && isStarImportScope() == s.isStarImportScope());
        } else
            return false;
    }

    public int hashCode() {
        return env.hashCode() + (isStarImportScope() ? 1 : 0);
    }

    public String toString() {
        return "JavacScope[env=" + env + ",starImport=" + isStarImportScope() + "]";
    }
}
