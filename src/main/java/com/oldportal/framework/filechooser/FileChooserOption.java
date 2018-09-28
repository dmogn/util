/*
 * FileChooserOption.java
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
public class FileChooserOption {
    // constructors:
    /** Creates a new instance of FileChooserOption */
    public FileChooserOption() {
    }

    public FileChooserOption(String _description, String _extensions[]) {
        description = _description;
        for (int i=0; i<_extensions.length; i++)
        {
            addExtension(_extensions[i]);
        }
    }

    // members:

    String description = "";

    java.util.Vector<String> extensions = new java.util.Vector<String>();

    // methods:

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String _description)
    {
        description = _description;
    }

    public void addExtension(String ext)
    {
        for (int i=0; i<extensions.size(); i++)
            if (extensions.get(i).equals(ext))
                return;

        extensions.add(ext);
    }

    public java.util.Vector<String> getExtensions()
    {
        return extensions;
    }

}
