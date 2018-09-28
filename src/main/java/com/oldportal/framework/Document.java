/*
 * Document.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

import com.oldportal.framework.undoredo.*;

/**
 * Document class in MDI architecture.
 * 
 * @author Dmitry Ognyannikov
 */
public class Document extends BaseObject {
    // constructors:
    /** Creates a new instance of Document */
    public Document() {
        super(null, null);
    }

    // members:
    private java.util.Vector<DocumentView> views = new java.util.Vector<DocumentView>();
    private ObjectsRegistry register = new ObjectsRegistry();
    protected java.util.Vector<Module> modules = new java.util.Vector<Module>();
    private History history = new History();

    private String documentPath = "";
    private String name = "";

    private boolean isModified = false;

    // methods:

    public void addView(DocumentView view)
    {
        for (int i=0; i<views.size(); i++)
        {
            if (views.get(i)==view)
                return;
        }
        views.add(view);
    }

    public void removeView(DocumentView view)
    {
        for (int i=views.size()-1; i>=0; i--)
        {
            if (views.get(i)==view)
                views.remove(i);
        }
    }

    public final DocumentView[] getViews()
    {
        DocumentView retViews[] = new DocumentView[views.size()];
        for (int i=0; i<views.size();i++)
        {
            retViews[i] = views.get(i);
        }
        return retViews;
    }

    public final Module[] getModules()
    {
        Module retModules[] = new Module[modules.size()];
        for (int i=0; i<modules.size();i++)
        {
            retModules[i] = modules.get(i);
        }
        return retModules;
    }


    /** Get registry for document objects. */
    public final ObjectsRegistry getRegistry()
    {
        return register;
    }

    public String getDocumentPath()
    {
        return documentPath;
    }

    public void setDocumentPath(String path)
    {
        documentPath = path;
        try{
          java.io.File file = new java.io.File(documentPath);
          setName(file.getName());
        } catch (Exception ex)
        {
          //setName("");
        }
    }

    public boolean isModified()
    {
        return isModified;
    }

    public void setModified()
    {
        isModified = true;
    }

    public void setModified(boolean _isModified)
    {
        isModified = _isModified;
    }

    public void load(org.jdom.Element element)
    {
        // check class type:
        if (element.getName() != "MDI_Framework_Document")
        {
            // TODO get MainFrame and pass to message dialog.
            javax.swing.JFrame frame = null;
            javax.swing.JOptionPane.showMessageDialog(frame, "Invalid document format error", "Invalid document format.", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        String uri = element.getNamespaceURI();
        if (element.getNamespaceURI() != "oldportal.com")
        {
            javax.swing.JFrame frame = null;
            javax.swing.JOptionPane.showMessageDialog(frame, "Invalid document format error", "Invalid document format.", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }


        // load own members:

        // load shared members GUIDs:

        // load attributes:
        //version = Integer.parseInt(element.getAttributeValue("version"));

        // load strings:

        resolveDependencies();

        checkConsistence();
    }

    public org.jdom.Element save(boolean attributesOnly)
    {
        org.jdom.Element ret = new org.jdom.Element("MDI_Framework_Document", org.jdom.Namespace.getNamespace("oldportal.com"));

        // save attributes:
        //ret.setAttribute("version", Integer.toString(version));

        // save strings:

        return ret;
    }

    public void resolveDependencies()
    {
        java.lang.Object objects[] = register.values().toArray();
        for (int i=0; i<objects.length; i++)
        {
            BaseObject object = (BaseObject)objects[i];
            object.resolveDependencies();
        }
    }

    public boolean checkConsistence()
    {
        return true;
    }

    final public Module getModuleForClass(String className)
    {
        for (int i=0; i<modules.size(); i++)
        {
            if (modules.get(i).isSupportedClass(className))
                return modules.get(i);
        }
        return null;
    }

    final public Module getModuleForObject(BaseObject object)
    {
        for (int i=0; i<modules.size(); i++)
        {
            if (modules.get(i).isSupportedObject(object))
                return modules.get(i);
        }
        return null;
    }

    final public History getHistory()
    {
        return history;
    }

    public String getDocumentDirectory()
    {
        return new java.io.File(documentPath).getParent();
    }


    public void notifyObservers(Object object)
    {
        ;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String _name)
    {
        name = _name;
    }

}
