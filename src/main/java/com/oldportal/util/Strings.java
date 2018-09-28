/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
*/

package com.oldportal.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

/**
 * Vector (array) of strings.
 * @author Dmitry Ognyannikov
 */
public class Strings implements Serializable {
    // constructors:
    public Strings() {
    }
  
    public Strings(final Strings src)
    {
        strings = (Vector)src.strings.clone();
    }
  
    public Strings(final String src)
    {
        loadFromString(src);
    }

    public Strings(Vector<String> src)
    {
        strings = (Vector<String>)src.clone();
    }


  // members:
  private Vector<String> strings = new Vector<String>();

  // methods:

  public String get(int index)
  {
          return (String)strings.get(index);
  }

  public void insert(String str, int index)
  {
    strings.insertElementAt(str, index);
  }

  public void add(String str)
  {
    strings.add(str);
  }

  public void clear()
  {
    strings.clear();
  }

  public int size()
  {
    return strings.size();
  }

  public void removeElementAt(int index)
  {
    strings.removeElementAt(index);
  }

  public String toString()
  {
   if (size()==0)
    return new String();
   String str=get(0);
   for (int i=1; i<size(); i++)
   {
    str+="\n";
    str+=get(i);
   }
   return str;
  }

  /*public boolean loadFromFile(String fileName)
  {
  try{
    return loadFromFile(new FileInputStream(fileName));
  } catch(FileNotFoundException e)
  { return false; }
  }

  public boolean loadFromFile(InputStream stream)
  {
   try{
   clear();
   if (stream.available()<2)
    return true;
   String str[]=new String[1];
   while(ServiceFunctions.ReadString(stream, str, "UTF8"))
   {
     add(str[0]);
   }
   if (strings.size()<1)
    return false;
   else
    return true;
   }
   catch(Exception e){
   return false;
   }
  }//*/

  public void loadFromString(String str)
  {
   clear();
   String newstr = new String("");
   for (int i=0; i<str.length(); i++)
   {
     int c = str.charAt(i);
     if (c=='\n')
     {
      add(newstr);
      newstr = new String("");
     }
     else
      newstr += (char)c;
   }
   if (newstr.length()>0)
    add(newstr);
  }

  /*public boolean saveToFile(String fileName, int textMode)
  {
		try{
			 File file = new File(fileName);
			 if (file != null || file.exists())
				 file.delete();
			 FileOutputStream out = new FileOutputStream(fileName);
			 boolean ret = saveToFile(out, textMode);
			 out.close();
			 return ret;
		} catch (Exception e){
			e.printStackTrace();
		return false; }
  }

  public boolean saveToFile(OutputStream stream, int textMode)
  {
		try{
		 for (int i=0; i<size(); i++)
		 {
			ServiceFunctions.WriteString(stream, get(i), "UTF-8", textMode);
		 }
			return true;
		} catch (Exception e){
			e.printStackTrace();
		return false; }
  }//*/

	// implement interfaces:
  private void writeObject(java.io.ObjectOutputStream out)
     throws IOException
  {
    out.writeObject(strings);
  }

  private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException
  {
    strings = (Vector)in.readObject();
  }

  protected Object clone()
  {
    return new Strings(this);
  }

  public Vector<String> getStringsVector()
  {
      return strings;
  }

}