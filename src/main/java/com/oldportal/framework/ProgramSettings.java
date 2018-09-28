/*
 * ProgramSettings.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class ProgramSettings {
    // constructor
    public ProgramSettings(String programName, String programVersion) {
        try{
            settingsFile = getSettingsFile(programName, programVersion);
            if (!settingsFile.exists())
            {
                loadDefaultSettings();
                return;
            }
            // load XML settings file
            java.io.InputStream inStream = new java.io.FileInputStream(
                    settingsFile);
            org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
            builder.setIgnoringElementContentWhitespace(true);
            document = builder.build(inStream);
        }catch(Exception ex){
            loadDefaultSettings();
            ex.printStackTrace();
        }//*/
    }

    // members:
    public org.jdom.Document document = null;
    java.io.File settingsFile = null;

    // methods:

    public org.jdom.Element getNodeWithName(String nodeName)
    {
        return document.getRootElement().getChild(nodeName);
    }

    public void addOrReplaceNode(org.jdom.Element node)
    {
        document.getRootElement().removeChild(node.getName());
        document.getRootElement().addContent(node);
    }

    public void saveSettings()
    {
        try{
            if (!settingsFile.exists())
                settingsFile.createNewFile();

            org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(org.
                    jdom.output.Format.getCompactFormat());
            java.io.OutputStream outStream = new java.io.FileOutputStream(settingsFile);
            out.output(document, outStream);
            outStream.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void loadDefaultSettings()
    {
        org.jdom.Element rootElement = new org.jdom.Element("ApplicationSettings");
        document = new org.jdom.Document(rootElement);
    }

    java.io.File getSettingsFile(String programName, String programVersion)
    {
        String programSettingsFileName = "." + programName + "_" +
                                             programVersion + "_settings.xml";

        // add user home directory
        return new java.io.File(System.getProperty("user.home") + java.io.File.separator + programSettingsFileName);
    }
}
