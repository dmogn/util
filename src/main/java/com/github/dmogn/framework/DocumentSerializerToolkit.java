/*
 * DocumentSerializerToolkit.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework;

/**
 * I/O functionality for document.
 *
 * @author Dmitry Ognyannikov
 */
public class DocumentSerializerToolkit {

    // constructors:
    /**
     * Creates a new instance of DocumentSerializerToolkit
     */
    public DocumentSerializerToolkit() {
    }

    // methods:
    static public boolean load_xml(Document document, java.io.File file) {
        try {
            java.io.InputStream inStream = new java.io.FileInputStream(file);
            org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
            //builder.setValidation(true);
            builder.setIgnoringElementContentWhitespace(true);
            org.jdom.Document xmlDocument = builder.build(inStream);
            DocumentManager.setCurrentDocument(document);
            document.load(xmlDocument.getRootElement());
            inStream.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    static public boolean save_xml(Document document, java.io.File file) {
        try {
            org.jdom.Document xmlDocument = new org.jdom.Document(document.save(false));
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(org.jdom.output.Format.getCompactFormat());
            java.io.OutputStream outStream = new java.io.FileOutputStream(file);
            out.output(xmlDocument, outStream);
            outStream.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*static public Document load_comressed_xml(Document document, java.io.File file)
  {
    try{
      java.io.InputStream inStream = new com.jcraft.jzlib.ZInputStream(new java.io.FileInputStream(file));
      org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
      builder.setValidation(true);
      builder.setIgnoringElementContentWhitespace(true);
      Document doc = new Document();
      org.jdom.Document xmlDocument = builder.build(inStream);
      DocumentManager.setCurrentDocument(document);
      doc.load(xmlDocument.getRootElement());
      inStream.close();
      return doc;
    }
    catch (Exception ex){
      ex.printStackTrace();
      return null;
    }
  }

  static public boolean save_comressed_xml(Document document, java.io.File file)
  {
    try{
      org.jdom.Document xmlDocument = new org.jdom.Document(document.save(false));
      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(org.
          jdom.output.Format.getCompactFormat());
      java.io.OutputStream outStream = new com.jcraft.jzlib.ZOutputStream(new java.io.FileOutputStream(file), 6);
      out.output(xmlDocument, outStream);
      outStream.close();
      return true;
    }
    catch (Exception ex){
      ex.printStackTrace();
      return false;
    }
  }//*/
    static final public String xmlSaveObject(BaseObject object, boolean attributesOnly) {
        org.jdom.Element root = object.save(attributesOnly);
        org.jdom.Document doc = new org.jdom.Document(root);
        org.jdom.output.XMLOutputter outputter = new org.jdom.output.XMLOutputter();
        return outputter.outputString(doc);
    }


    /*static public org.jdom.Element saveString(String fieldName, String string)
  {
    org.jdom.Element element = new org.jdom.Element(fieldName);
    org.jdom.Element elementString = new org.jdom.Element("EncodedString");
    element.addContent(elementString);
    string = encodeString(string);
    org.jdom.CDATA data = new org.jdom.CDATA(string);
    elementString.addContent(data);
    return element;
  }//*/

 /*static public String loadString(org.jdom.Element element)
  {
    if (element == null)
      return "";

    org.jdom.Element encodedStringCDATA = element.getChild("EncodedString");
    String encodedString = encodedStringCDATA.getText();
    return decodeString(encodedString);
  }//*/

 /*static private String encodeString(String str)
  {
    StringBuilder ret = new StringBuilder((int)(str.length()*1.1));
    for (int i=0; i<str.length(); i++)
    {
      char c = str.charAt(i);
      if (c == '\\')
      {
        ret.append("\\\\");
      }
      else if (c == ']')
      {
        ret.append("\\]");
      }
      else if (c == '\t')
      {
        ret.append("\\t");
      }
      else if (c == '\r')
      {
        ret.append("\\r");
      }
      else if (c == '\n')
      {
        ret.append("\\n");
      }
      else if (c == ' ')
      {
        ret.append("\\ ");
      }
      else
      {
        ret.append(c);
      }
    }
    return ret.toString();
  }//*/

 /*static private String decodeString(String str)
  {
    StringBuilder ret = new StringBuilder(str.length());
    for (int i=0; i<str.length(); i++)
    {
      char c = str.charAt(i);
      if (c == '\\')
      {
        if (i > str.length()-2)
        {
          return ret.toString();//error
        }
        else
        {
          char c2 = str.charAt(i+1);
          if (c2 == '\\')
            ret.append('\\');
          else if (c2 == ']')
            ret.append(']');

          else if (c2 == 't')
            ret.append('\t');
          else if (c2 == 'r')
            ret.append('\r');
          else if (c2 == 'n')
            ret.append('\n');
          else if (c2 == ' ')
            ret.append(' ');
          else
          {
            continue;//error - unknown symbol
          }
          i++;
        }
      }
      else
      {
        ret.append(c);
      }
    }
    return ret.toString();
  }//*/
    public static void testXMLSerialize() {
        try {
            // save:
            org.jdom.Element root = new org.jdom.Element("Document");
            org.jdom.Document xmlDocument = new org.jdom.Document(root);
            java.io.File file = new java.io.File("C:\\\\temp.xml");
            java.io.OutputStream outStream = new java.io.FileOutputStream(file);
            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(org.jdom.output.Format.getCompactFormat());
            out.output(xmlDocument, outStream);
            outStream.close();

            // load:
            java.io.InputStream inStream = new java.io.FileInputStream(file);
            org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
            //builder.setValidation(true);
            builder.setIgnoringElementContentWhitespace(true);
            org.jdom.Document xmlDocument2 = builder.build(inStream);
            inStream.close();
            System.out.println(" true  load!!");

            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

}
