/*
 * CustomFileFilter.java
 *
 * OldPortal Framework library.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework.filechooser;

import java.io.File;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class CustomFileFilter extends javax.swing.filechooser.FileFilter {
    // constructors:
    /** Creates a new instance of CustomFileFilter */
    public CustomFileFilter()
    {
        profile = new FileChooserOption();
    }

    public CustomFileFilter(FileChooserOption _profile)
    {
        profile = _profile;
    }

    public CustomFileFilter(String _description, String _extensions[])
    {
        profile = new FileChooserOption(_description, _extensions);
    }

    // members:
    FileChooserOption profile = null;

    // methods:
    public boolean accept(File f)
    {
        /** implement this javax.swing.filechooser.FileFilter abstract method*/
        String name = f.getName();
        if (f.isDirectory())
            return true;

        if (profile.extensions.size() == 0)//All files
            return true;

        for (int i=0; i<profile.extensions.size(); i++)
        {
            String extension = profile.extensions.get(i);
            if (extension.compareToIgnoreCase("")==0)
                break;

            int size = extension.length()+1;
            if (name.length() < size+1)
                continue;
            String ext = name.substring(name.length()-size);
            if (ext.compareToIgnoreCase("."+extension)==0)
                return true;
        }
        return false;
    }

    public String getDescription()
    {
        return profile.getDescription();
    }

    public void addExtension(String ext)
    {
        profile.addExtension(ext);
    }

    public void setDescription(String _description)
    {
        profile.setDescription(_description);
    }

    public FileChooserOption getProfile()
    {
        return profile;
    }

    public java.util.Vector<String> getExtensions()
    {
        return profile.getExtensions();
    }

    public String getFileWithExtension(java.io.File file)
    {
        java.util.Vector<String> extensions = getExtensions();
        String fileName = file.getAbsolutePath();
        boolean haveExtension = false;
        for (int i=0; i<extensions.size(); i++)
        {
            if (fileName.endsWith(extensions.get(i)))
            {
                haveExtension = true;
                break;
            }
        }
        if (haveExtension)
        {
            return fileName;
        }
        else
        {
            return fileName + "." + extensions.get(0);
        }
    }

}
