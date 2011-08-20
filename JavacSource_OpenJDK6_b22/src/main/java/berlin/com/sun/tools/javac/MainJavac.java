/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
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

/*
 * With modifications by berlin brown (berlin.brown at gmail.com)
 */

package berlin.com.sun.tools.javac;

import java.io.PrintWriter;
import java.lang.reflect.*;

/**
 * The programmatic interface for the Java Programming Language compiler, javac.
 */
public class MainJavac {

    /*
     * With modifications by berlin brown (berlin.brown at gmail.com)
     */
    
    static {
        ClassLoader loader = MainJavac.class.getClassLoader();
        if (loader != null)
            loader.setPackageAssertionStatus("com.sun.tools.javac", true);
    }

    /**
     * Main entry point for javac compiler.
     * 
     * @param args
     *            The command line parameters.
     */
    public static void main(String[] args) throws Exception {
        
        if (args.length > 0 && args[0].equals("-Xjdb")) {
            final String[] newargs = new String[args.length + 2];
            final Class<?> c = Class.forName("com.sun.tools.example.debug.tty.TTY");
            final Method method = c.getDeclaredMethod("main", new Class[] { args.getClass() });
            method.setAccessible(true);
            System.arraycopy(args, 1, newargs, 3, args.length - 1);
            newargs[0] = "-connect";
            newargs[1] = "com.sun.jdi.CommandLineLaunch:options=-esa -ea:com.sun.tools...";
            newargs[2] = "com.sun.tools.javac.Main";
            method.invoke(null, new Object[] { newargs });
        } else {
            final int res = compile(args);
            System.out.println("Compile complete with : status=" + res);
            System.exit(res);
        } // End of the if - else //
    }

    /**
     * Programmatic interface to the Java Programming Language compiler, javac.
     */
    public static int compile(String[] args) {
        final berlin.com.sun.tools.javac.main.Main compiler = new berlin.com.sun.tools.javac.main.Main("javac");
        return compiler.compile(args);
    }

    /**
     * Programmatic interface to the Java Programming Language compiler, javac.
     */
    public static int compile(String[] args, PrintWriter out) {
        final berlin.com.sun.tools.javac.main.Main compiler = new berlin.com.sun.tools.javac.main.Main("javac", out);
        return compiler.compile(args);
    }
}
