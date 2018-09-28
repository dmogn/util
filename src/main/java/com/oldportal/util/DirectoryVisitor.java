package com.oldportal.util;

import java.io.File;
/**
 *
 *
 * @author Dmitry Ognyannikov
 * @version 1.0
 */
public class DirectoryVisitor {
    // constructors:
    public DirectoryVisitor() {
    }

    // members:

    // methods:

    protected boolean onDirectoryEnter(File file)
    {
        return true;// enter to directory
    }

    protected boolean onFile(File file)
    {
        return true;// continue list files
    }

    public static boolean listDirectory(File directory, DirectoryVisitor visitor)
    {
        if (!directory.exists())
            throw new IllegalArgumentException("File directory not exist");
        if (!directory.isDirectory())
            throw new IllegalArgumentException("File directory is not directory");

        if (!visitor.onDirectoryEnter(directory))
            return false;// not enter to directory

        File files[] = directory.listFiles();

        for (int i=0; i<files.length; i++)
        {
            if (files[i].isFile())
            {
                if (!visitor.onFile(files[i]))
                    return false;
            }
            else if (files[i].isDirectory())
            {
                if (!listDirectory(files[i], visitor))
                    return false;
            }
        }
        return true;
    }


}
