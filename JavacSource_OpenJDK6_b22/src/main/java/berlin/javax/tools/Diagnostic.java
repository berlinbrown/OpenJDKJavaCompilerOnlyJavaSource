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

package berlin.javax.tools;

import java.util.Locale;

/**
 * Interface for diagnostics from tools.  A diagnostic usually reports
 * a problem at a specific position in a source file.  However, not
 * all diagnostics are associated with a position or a file.
 * 
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public interface Diagnostic<S> {

    /**
     * Kinds of diagnostics, for example, error or warning.
     */
    enum Kind {
        /**
         * Problem which prevents the tool's normal completion.
         */
        ERROR,
        /**
         * Problem which does not usually prevent the tool from
         * completing normally.
         */
        WARNING,
        /**
         * Problem similar to a warning, but is mandated by the tool's
         * specification. 
         */
        MANDATORY_WARNING,
        /**
         * Informative message from the tool.
         */
        NOTE,
        /**
         * Diagnostic which does not fit within the other kinds.
         */
        OTHER,
    }

    /**
     * Used to signal that no position is available.
     */
    public final static long NOPOS = -1;

    /**
     * Gets the kind of this diagnostic, for example, error or
     * warning.
     * @return the kind of this diagnostic
     */
    Kind getKind();

    /**
     * Gets the source object associated with this diagnostic.
     */
    S getSource();

    /**
     * Gets a character offset from the beginning of the source object
     * associated with this diagnostic that indicates the location of
     * the problem.  In addition, the following must be true:
     */
    long getPosition();

    /**
     * Gets the character offset from the beginning of the file
     * associated with this diagnostic that indicates the start of the
     * problem.
     */
    long getStartPosition();

    /**
     * Gets the character offset from the beginning of the file
     * associated with this diagnostic that indicates the end of the
     * problem.
     */
    long getEndPosition();

    /**
     * Gets the line number of the character offset returned by
     * {@linkplain #getPosition()}.
     */
    long getLineNumber();

    /**
     * Gets the column number of the character offset returned by
     * {@linkplain #getPosition()}.
     */
    long getColumnNumber();

    /**
     * Gets a diagnostic code indicating the type of diagnostic.  The
     * code is implementation-dependent and might be {@code null}.     
     */
    String getCode();

    /**
     * Gets a localized message for the given locale.  The actual
     * message is implementation-dependent.  If the locale is {@code
     * null} use the default locale.
     */
    String getMessage(Locale locale);
}
