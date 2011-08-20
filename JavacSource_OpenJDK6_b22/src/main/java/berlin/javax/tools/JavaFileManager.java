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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static berlin.javax.tools.JavaFileObject.Kind;

/**
 * File manager for tools operating on Java&trade; programming language
 * source and class files.  In this context, <em>file</em> means an
 * abstraction of regular files and other sources of data.
 *
 * <p>When constructing new JavaFileObjects, the file manager must
 * determine where to create them.  For example, if a file manager
 * manages regular files on a file system, it would most likely have a
 * current/working directory to use as default location when creating
 * or finding files.  A number of hints can be provided to a file
 * manager as to where to create files.  Any file manager might choose
 * to ignore these hints.
 *
 * <p>Some methods in this interface use class names.  Such class
 * names must be given in the Java Virtual Machine internal form of
 * fully qualified class and interface names.  For convenience '.'
 * and '/' are interchangeable.  The internal form is defined in
 * chapter four of the
 * <a href="http://java.sun.com/docs/books/vmspec/2nd-edition/jvms-maintenance.html">Java
 * Virtual Machine Specification</a>.
 * 
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @see JavaFileObject
 * @see FileObject
 * @since 1.6
 */
public interface JavaFileManager extends Closeable, Flushable, OptionChecker {

    /**
     * Interface for locations of file objects.  Used by file managers
     * to determine where to place or search for file objects.
     */
    interface Location {
        /**
         * Gets the name of this location.
         *
         * @return a name
         */
        String getName();

        /**
         * Determines if this is an output location.  An output
         * location is a location that is conventionally used for
         * output.
         *
         * @return true if this is an output location, false otherwise
         */
        boolean isOutputLocation();
    }

    /**
     * Gets a class loader for loading plug-ins from the given
     * location.  For example, to load annotation processors, a
     * compiler will request a class loader for the {@link
     * StandardLocation#ANNOTATION_PROCESSOR_PATH
     * ANNOTATION_PROCESSOR_PATH} location.
     */
    ClassLoader getClassLoader(Location location);

    /**
     * Lists all file objects matching the given criteria in the given
     * location.  List file objects in "subpackages" if recurse is
     * true.
     *   
     */
    Iterable<JavaFileObject> list(Location location,
                                  String packageName,
                                  Set<Kind> kinds,
                                  boolean recurse)
        throws IOException;

    /**
     * Infers a binary name of a file object based on a location.  The
     * binary name returned might not be a valid JLS binary name.
     */
    String inferBinaryName(Location location, JavaFileObject file);

    /**
     * Compares two file objects and return true if they represent the
     * same underlying object.
     *
     */
    boolean isSameFile(FileObject a, FileObject b);

    /**
     * Handles one option.  If {@code current} is an option to this
     * file manager it will consume any arguments to that option from
     * {@code remaining} and return true, otherwise return false.
     *
     * @param current current option
     * @param remaining remaining options
     * @return true if this option was handled by this file manager,
     * false otherwise
     * @throws IllegalArgumentException if this option to this file
     * manager is used incorrectly
     * @throws IllegalStateException if {@link #close} has been called
     * and this file manager cannot be reopened
     */
    boolean handleOption(String current, Iterator<String> remaining);

    /**
     * Determines if a location is known to this file manager.
     *
     * @param location a location
     * @return true if the location is known
     */
    boolean hasLocation(Location location);

    /**
     * Gets a {@linkplain JavaFileObject file object} for input
     * representing the specified class of the specified kind in the
     * given location.     
     */
    JavaFileObject getJavaFileForInput(Location location,
                                       String className,
                                       Kind kind)
        throws IOException;

    /**
     * Gets a {@linkplain JavaFileObject file object} for output
     * representing the specified class of the specified kind in the
     * given location.          
     */
    JavaFileObject getJavaFileForOutput(Location location,
                                        String className,
                                        Kind kind,
                                        FileObject sibling)
        throws IOException;

    /**
     * Gets a {@linkplain FileObject file object} for input
     * representing the specified <a href="JavaFileManager.html#relative_name">relative
     * name</a> in the specified package in the given location.   
     */
    FileObject getFileForInput(Location location,
                               String packageName,
                               String relativeName)
        throws IOException;

    /**
     * Gets a {@linkplain FileObject file object} for output
     * representing the specified <a href="JavaFileManager.html#relative_name">relative   
     */
    FileObject getFileForOutput(Location location,
                                String packageName,
                                String relativeName,
                                FileObject sibling)
        throws IOException;

    /**
     * Flushes any resources opened for output by this file manager
     * directly or indirectly.  Flushing a closed file manager has no
     * effect.
     */
    void flush() throws IOException;

    /**
     * Releases any resources opened by this file manager directly or
     * indirectly. 
     */
    void close() throws IOException;
}
