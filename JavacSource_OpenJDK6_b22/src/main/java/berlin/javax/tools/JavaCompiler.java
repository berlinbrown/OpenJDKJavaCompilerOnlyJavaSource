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

package berlin.javax.tools;

import java.io.File;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import berlin.javax.annotation.processing.Processor;

/**
 * Interface to invoke Java&trade; programming language compilers from programs.
 * 
 * <p>
 * The compiler might generate diagnostics during compilation (for example,
 * error messages). If a diagnostic listener is provided, the diagnostics will
 * be supplied to the listener. If no listener is provided, the diagnostics will
 * be formatted in an unspecified format and written to the default output,
 * which is {@code System.err} unless otherwise specified. Even if a diagnostic
 * listener is supplied, some diagnostics might not fit in a {@code Diagnostic}
 * and will be written to the default output.
 * 
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @see DiagnosticListener
 * @see Diagnostic
 * @see JavaFileManager
 * @since 1.6
 */
public interface JavaCompiler extends Tool, OptionChecker {

    /**
     * Creates a future for a compilation task with the given components and
     * arguments. The compilation might not have completed as described in the
     * CompilationTask interface.
     * 
     * <p>
     * If a file manager is provided, it must be able to handle all locations
     * defined in {@link StandardLocation}.
     */
    CompilationTask getTask(Writer out, JavaFileManager fileManager,
            DiagnosticListener<? super JavaFileObject> diagnosticListener, Iterable<String> options,
            Iterable<String> classes, Iterable<? extends JavaFileObject> compilationUnits);

    /**
     * Gets a new instance of the standard file manager implementation for this
     * tool.
     */
    StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> diagnosticListener,
            Locale locale, Charset charset);

    /**
     * Interface representing a future for a compilation task. The compilation
     * task has not yet started. To start the task, call the {@linkplain #call
     * call} method.
     */
    interface CompilationTask extends Callable<Boolean> {

        /**
         * Sets processors (for annotation processing). This will bypass the
         * normal discovery mechanism.       
         */
        void setProcessors(Iterable<? extends Processor> processors);

        /**
         * Set the locale to be applied when formatting diagnostics and other
         * localized data.
         * 
         * @param locale
         *            the locale to apply; {@code null} means apply no locale
         * @throws IllegalStateException
         *             if the task has started
         */
        void setLocale(Locale locale);

        /**
         * Performs this compilation task. The compilation may only be performed
         * once. Subsequent calls to this method throw IllegalStateException.
         * 
         * @return true if and only all the files compiled without errors; false
         *         otherwise
         */
        Boolean call();
    }
}
