/*
 * Clipboard.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 * Operations with system clipboard.
 * 
 * @author Dmitry Ognyannikov
 */
public class Clipboard {
  // constructors:
  protected Clipboard() {
  }

  // members:

  // methods:

  static public void copyObject(BaseObject object)
  {
    String xmlText = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, false);
    copyTextToClipboard(xmlText);
  }

  static public org.jdom.Element pasteElement()
  {
    String xmlText = pasteTextFromClipboard();
    return fromXMLText(xmlText);
  }

  public static org.jdom.Element fromXMLText(String xmlText)
  {
    if (com.oldportal.util.StringToolkit.findFirstFromToIgnoreCase(xmlText, "<?xml version=\"", 0, 2) != 0)
        return null;
    //java.nio.charset.Charset charset = java.nio.charset.Charset.forName("UTF8");
    //int size = xmlText.length();
    //com.oldportal.objectbuilder.document.source.SourceFormatter.toBytes(charset,xmlText);
    //int size2 = bytes.length;
    try{
      byte bytes[] = xmlText.getBytes("UTF8");

      java.io.File file = java.io.File.createTempFile("ob_", ".xml");
      java.io.FileOutputStream out = new java.io.FileOutputStream(file);
      out.write(bytes);
      out.close();

      java.io.FileInputStream in = new java.io.FileInputStream(file);
      org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
      org.jdom.Document doc = builder.build(in);
      in.close();

      file.delete();

      return doc.getRootElement();
    } catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }


  static public boolean pasteObject(BaseObject toObject)
  {
      if (toObject == null)
          return false;
    boolean result = false;
    try{
      com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().pushStack();

      BaseObject object = loadObject(pasteElement(), toObject);
      object.setParentObject(toObject);
      com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().resolveDependenciesLocal();
      com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().resetGUIDsForObjectTree(object.getGUID());
      result = pasteObject(object, toObject);
      // update parent objects tree phases with pasted object phase
      //TODO:
      //object.setPhase(object.getPhase());

      com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().popStack();

      if (!com.oldportal.framework.DocumentManager.getCurrentDocument().checkConsistence())
      {
        System.err.println("getDocument().checkConsistence() after paste fail!");
        return false;
      }
    } catch (Exception ex){
      ex.printStackTrace();
    }

    return result;
  }

  static public boolean pasteObject(BaseObject object, BaseObject toObject)
  {
    com.oldportal.framework.Module module = com.oldportal.framework.DocumentManager.getCurrentDocument().getModuleForObject(object);
    if (module == null)
      return false;

    return module.paste(object, toObject);
  }

  static public BaseObject loadObject(org.jdom.Element element, BaseObject parentObject)
  {
    /*com.oldportal.objectbuilder.document.Module module = com.oldportal.objectbuilder.document.DocumentManager.getDocument().getModuleForClass(element.getName());
    if (module == null)
      return null;//*/

    try{
      String className = element.getName();
      //Class objectClass = Class.forName(className);
      //java.lang.reflect.Constructor constructors[] = objectClass.getConstructors();
      /*com.oldportal.objectbuilder.document.Object parentObject2 = new com.oldportal.objectbuilder.document.Object(null);
      Object object = null;
      for (int i=0; i<constructors.length; i++)
      {
        if (constructors[i].getParameterTypes().length == 1)
        {
          Object objectArgs[] = new Object[1];
          objectArgs[0] = parentObject2;
          object = constructors[i].newInstance(objectArgs);
          break;
        }
      }//*/
      com.oldportal.framework.Module module = com.oldportal.framework.DocumentManager.getCurrentDocument().getModuleForClass(className);
      BaseObject object = module.loadObjectByName(className, parentObject);

      object.load(element);
      return object;
    }catch(Exception ex){
      return null;
    }
  }

  static public boolean haveData()
  {
    java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    return clipboard.isDataFlavorAvailable(java.awt.datatransfer.DataFlavor.stringFlavor);
  }

  public static String pasteTextFromClipboard()
  {
    String ret = null;

    java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();

    if (clipboard.isDataFlavorAvailable(java.awt.datatransfer.DataFlavor.stringFlavor))
    {
      java.awt.datatransfer.Transferable data = clipboard.getContents(null);
      try{
        ret = (String) data.getTransferData(java.awt.
            datatransfer.DataFlavor.stringFlavor);
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }

    return ret;
  }

  public static void copyTextToClipboard(String data)
  {
    java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    java.awt.datatransfer.StringSelection strData = new java.awt.datatransfer.StringSelection(data);
    clipboard.setContents(strData, strData);
  }


}
