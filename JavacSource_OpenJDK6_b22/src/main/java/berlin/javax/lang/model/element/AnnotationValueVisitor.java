/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
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

package berlin.javax.lang.model.element;

import java.util.List;

import berlin.javax.lang.model.type.TypeMirror;

/**
 * A visitor of the values of annotation type elements, using a variant of the
 * visitor design pattern. Unlike a standard visitor which dispatches based on
 * the concrete type of a member of a type hierarchy, this visitor dispatches
 * based on the type of data stored.
 * 
 * @author Joseph D. Darcy
 * @author Scott Seligman
 * @author Peter von der Ah&eacute;
 * @since 1.6
 */
public interface AnnotationValueVisitor<R, P> {
    /**
     * Visits an annotation value.
     */
    R visit(AnnotationValue av, P p);

    /**
     * A convenience method equivalent to {@code v.visit(av, null)}.
     */
    R visit(AnnotationValue av);

    /**
     * Visits a {@code boolean} value in an annotation.
     */
    R visitBoolean(boolean b, P p);

    /**
     * Visits a {@code byte} value in an annotation.
     */
    R visitByte(byte b, P p);

    /**
     * Visits a {@code char} value in an annotation.
     */
    R visitChar(char c, P p);

    /**
     * Visits a {@code double} value in an annotation.
     */
    R visitDouble(double d, P p);

    /**
     * Visits a {@code float} value in an annotation.
     * 
     */
    R visitFloat(float f, P p);

    /**
     * Visits an {@code int} value in an annotation.     
     */
    R visitInt(int i, P p);

    /**
     * Visits a {@code long} value in an annotation.
     */
    R visitLong(long i, P p);

    /**
     * Visits a {@code short} value in an annotation.
     */
    R visitShort(short s, P p);

    /**
     * Visits a string value in an annotation.
     */
    R visitString(String s, P p);

    /**
     * Visits a type value in an annotation.
     */
    R visitType(TypeMirror t, P p);

    /**
     * Visits an {@code enum} value in an annotation.
     */
    R visitEnumConstant(VariableElement c, P p);

    /**
     * Visits an annotation value in an annotation.
     */
    R visitAnnotation(AnnotationMirror a, P p);

    /**
     * Visits an array value in an annotation.     
     */
    R visitArray(List<? extends AnnotationValue> vals, P p);

    /**
     * Visits an unknown kind of annotation value. This can occur if the
     * language evolves and new kinds of value can be stored in an annotation.
     *
     */
    R visitUnknown(AnnotationValue av, P p);
}
