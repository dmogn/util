/*
* Utilites Library.
* Copyright (C) Dmitry Ognyannikov, 2005
* E-Mail: sirius_plus@yahoo.com , dmitry@oldportal.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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