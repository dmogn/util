/*
 * FileChooser.java
 *
 * OldPortal Framework library.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework.filechooser;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class FileChooser {
    // constructors:
    /** Creates a new instance of FileChooser */
    public FileChooser() {
    }

    // members:

    java.io.File result = null;

    String defaultDirectory = "";

    String deafultFilename = "";

    // methods:

    boolean showSaveAsDialog(javax.swing.JComponent parentComponent, FileChooserOption extensions[])
    {
        javax.swing.JFileChooser ch = new javax.swing.JFileChooser();
        for (int i=0; i<extensions.length; i++)
        {
            ch.addChoosableFileFilter(new CustomFileFilter(extensions[i]));
        }
        ch.setFileFilter(new CustomFileFilter(extensions[0]));

        if (ch.showSaveDialog(parentComponent) == javax.swing.JFileChooser.APPROVE_OPTION) {
            result = ch.getSelectedFile();
            return true;
        }
        return false;
    }

    boolean showOpenDialog(javax.swing.JComponent parentComponent, FileChooserOption extensions[])
    {
        javax.swing.JFileChooser ch = new javax.swing.JFileChooser();
        for (int i=0; i<extensions.length; i++)
        {
            ch.addChoosableFileFilter(new CustomFileFilter(extensions[i]));
        }
        ch.setFileFilter(new CustomFileFilter(extensions[0]));

        if (ch.showOpenDialog(parentComponent) == javax.swing.JFileChooser.APPROVE_OPTION) {
            result = ch.getSelectedFile();
            return true;
        }
        return false;
    }

//    boolean showSelectDirectoryDialog(javax.swing.JComponent parentComponent)
//    {
//        //TODO:
//        return false;
//    }

    String getResult()
    {
        return result.getAbsolutePath();
    }

}
