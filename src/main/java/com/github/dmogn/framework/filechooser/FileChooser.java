/*
 * FileChooser.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework.filechooser;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class FileChooser {

    java.io.File result = null;

    String defaultDirectory = "";

    String deafultFilename = "";

    // constructors:
    /**
     * Creates a new instance of FileChooser
     */
    public FileChooser() {
    }

    // methods:
    boolean showSaveAsDialog(javax.swing.JComponent parentComponent, FileChooserOption extensions[]) {
        javax.swing.JFileChooser ch = new javax.swing.JFileChooser();
        for (int i = 0; i < extensions.length; i++) {
            ch.addChoosableFileFilter(new CustomFileFilter(extensions[i]));
        }
        ch.setFileFilter(new CustomFileFilter(extensions[0]));

        if (ch.showSaveDialog(parentComponent) == javax.swing.JFileChooser.APPROVE_OPTION) {
            result = ch.getSelectedFile();
            return true;
        }
        return false;
    }

    boolean showOpenDialog(javax.swing.JComponent parentComponent, FileChooserOption extensions[]) {
        javax.swing.JFileChooser ch = new javax.swing.JFileChooser();
        for (int i = 0; i < extensions.length; i++) {
            ch.addChoosableFileFilter(new CustomFileFilter(extensions[i]));
        }
        ch.setFileFilter(new CustomFileFilter(extensions[0]));

        if (ch.showOpenDialog(parentComponent) == javax.swing.JFileChooser.APPROVE_OPTION) {
            result = ch.getSelectedFile();
            return true;
        }
        return false;
    }

    String getResult() {
        return result.getAbsolutePath();
    }

}
