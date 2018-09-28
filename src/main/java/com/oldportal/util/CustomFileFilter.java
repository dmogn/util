/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
*/

package com.oldportal.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;


public class CustomFileFilter extends FileFilter
{

  public CustomFileFilter()
  {
   Description="";
   for (int i=0; i<Extensions.length; i++)
     Extensions[i]="";
  }

  public CustomFileFilter(String description)
  {
   this();
   Description = description;
  }

  private String Description;

  private String[] Extensions = new String[250];

  public boolean accept(File f)
  {
    String name = f.getName();
    if (f.isDirectory())
     return true;

    if (Extensions.length==0)//All files
     return true;

    for (int i=0; i<Extensions.length; i++)
    {
     if (Extensions[i].compareToIgnoreCase("")==0)
      break;

     int size=Extensions[i].length()+1;
     if (name.length() < size+1)
      continue;
     String ext = name.substring(name.length()-size);
     if (ext.compareToIgnoreCase("."+Extensions[i])==0)
      return true;
    }
    return false;
  }

  public String getDescription()
  {
    return Description;
  }

  public void addExtension(String ext)
  {
   for (int i=0; i<Extensions.length; i++)
     if (Extensions[i].compareToIgnoreCase("")==0)
     {
      Extensions[i] = ext;
      return;
     }
  }

  public void setDescription(String description)
  {
    Description=description;
  }
}