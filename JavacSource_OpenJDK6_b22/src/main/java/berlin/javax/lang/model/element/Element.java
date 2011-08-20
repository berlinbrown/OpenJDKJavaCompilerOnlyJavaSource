/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package berlin.javax.lang.model.element;


import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.List;
import java.util.Set;

import berlin.javax.lang.model.element.Modifier;
import berlin.javax.lang.model.type.*;
import berlin.javax.lang.model.util.*;


/**
 * Represents a program element such as a package, class, or method.
 * Each element represents a static, language-level construct
 * (and not, for example, a runtime construct of the virtual machine).
 * 
 * @author Joseph D. Darcy
 * @author Scott Seligman
 * @author Peter von der Ah&eacute;
 * @see Elements
 * @see TypeMirror
 * @since 1.6
 */
public interface Element {

    /**
     * Returns the type defined by this element.
     */
    TypeMirror asType();

    /**
     * Returns the {@code kind} of this element.
     *
     * @return the kind of this element
     */
    ElementKind getKind();

    /**
     * Returns the annotations that are directly present on this element.
     *
     * <p> To get inherited annotations as well, use
     * {@link Elements#getAllAnnotationMirrors(Element) getAllAnnotationMirrors}.
     *
     * @see ElementFilter
     *
     * @return the annotations directly present on this element;
     *          an empty list if there are none
     */
    List<? extends AnnotationMirror> getAnnotationMirrors();

    /**
     * Returns this element's annotation for the specified type if
     * such an annotation is present, else {@code null}.  The
     * annotation may be either inherited or directly present on this
     * element.
     * 
     * @param <A>  the annotation type
     * @param annotationType  the {@code Class} object corresponding to
     *          the annotation type
     * @return this element's annotation for the specified annotation
     *         type if present on this element, else {@code null}
     *
     * @see #getAnnotationMirrors()
     * @see java.lang.reflect.AnnotatedElement#getAnnotation
     * @see EnumConstantNotPresentException
     * @see AnnotationTypeMismatchException
     * @see IncompleteAnnotationException
     * @see MirroredTypeException
     * @see MirroredTypesException
     */
    <A extends Annotation> A getAnnotation(Class<A> annotationType);

    /**
     * Returns the modifiers of this element, excluding annotations.
     * Implicit modifiers, such as the {@code public} and {@code static}
     * modifiers of interface members, are included.
     *
     * @return the modifiers of this element, or an empty set if there are none
     */
    Set<Modifier> getModifiers();

    /**
     * Returns the simple (unqualified) name of this element.
     * The name of a generic type does not include any reference
     * to its formal type parameters.
     * For example, the simple name of the type element
     * {@code java.util.Set<E>} is {@code "Set"}.
     * If this element represents an unnamed package, an empty name is
     * returned.  If it represents a constructor, the name "{@code
     * <init>}" is returned.  If it represents a static initializer,
     * the name "{@code <clinit>}" is returned.  If it represents an
     * anonymous class or instance initializer, an empty name is
     * returned.
     *
     * @return the simple name of this element
     */
    Name getSimpleName();

    /**
     * Returns the innermost element
     * within which this element is, loosely speaking, enclosed.
     * <ul>
     * <li> If this element is one whose declaration is lexically enclosed
     * immediately within the declaration of another element, that other
     * element is returned.
     * <li> If this is a top-level type, its package is returned.
     * <li> If this is a package, {@code null} is returned.
     * <li> If this is a type parameter, {@code null} is returned.
     * </ul>
     *
     * @return the enclosing element, or {@code null} if there is none
     * @see Elements#getPackageOf
     */
    Element getEnclosingElement();

    /**
     * Returns the elements that are, loosely speaking, directly
     * enclosed by this element.
     *
     * @return the enclosed elements, or an empty list if none
     * @see Elements#getAllMembers
     * @jls3 8.8.9 Default Constructor
     * @jls3 8.9 Enums
     */
    List<? extends Element> getEnclosedElements();

    /**
     * Returns {@code true} if the argument represents the same
     * element as {@code this}, or {@code false} otherwise.
     */
    boolean equals(Object obj);

    /**
     * Obeys the general contract of {@link Object#hashCode Object.hashCode}.
     *
     * @see #equals
     */
    int hashCode();

    /**
     * Applies a visitor to this element.
     */
    <R, P> R accept(ElementVisitor<R, P> v, P p);
}
