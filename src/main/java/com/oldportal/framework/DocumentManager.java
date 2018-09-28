/*
 * DocumentManager.java
 *
 * OldPortal Framework library.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 * Manage documents in MDI application framework.
 * @author Dmitry Ognyannikov
 */
public class DocumentManager {
    // constructors:
    /** Creates a new instance of DocumentManager */
    public DocumentManager() {
    }

    // members:

    private static java.util.Vector<Document> documents = new java.util.Vector<Document>();

    private static Document currentDocument = null;

    private static DocumentView currentView = null;

    private static ViewsFramework framework = null;

    private static MainFrame mainFrame = null;

    private static Factory factory = null;

    // methods:
    public static void init(ViewsFramework _framework, Factory _factory)
    {
        framework = _framework;
        factory = _factory;
    }

    /**
    * Return active document.
    * @return Document
    */
    public static Document getCurrentDocument()
    {
        return currentDocument;
    }

    public static void setCurrentDocument(Document document)
    {
        // check is document added.
        if (document != null)
        {
            boolean isAdded = false;
            for (int i = 0; i < documents.size(); i++)
                if (documents.get(i) == document)
                    isAdded = true;

            if (!isAdded)
                addDocument(document);
        }
        currentDocument = document;
    }

    public static Document[] getDocuments()
    {
        Document[] docs = new Document[documents.size()];
        for (int i=0; i<documents.size(); i++)
        {
            docs[i] = documents.get(i);
        }
        return docs;
    }

    public static DocumentView getCurrentView()
    {
        return currentView;
    }

    public static void setCurrentView(DocumentView view)
    {
        framework.setSelectedComponent(view);
        currentView = view;
    }

    public static MainFrame getMainFrame()
    {
        return mainFrame;
    }

    public static void setMainFrame(MainFrame frame)
    {
        mainFrame = frame;
    }

    public static void closeAllDocuments()
    {
        for (int i=documents.size()-1; i>=0; i--)
        {
            closeDocument(documents.get(i));
        }
    }

    public static void addDocument(Document document)
    {
        for (int i=0; i<documents.size(); i++)
            if (documents.get(i) == document)
                return;

        documents.add(document);
    }

    public static void addDocumentView(Document document)
    {
      if (document.getViews().length == 0) { // create default view & add to framework
        DocumentView view = factory.createDefaultView(document);
        framework.addTab(document.getName(), view);
        Module modules[] = document.getModules();
        for (int i = 0; i < modules.length; i++) {
          modules[i].setView(view);
        }
        framework.setSelectedComponent(view);
        setCurrentDocument(document);
        view.init();
      }
    }

    /**
    * find path and filename.
    * open document.
    */
    public static boolean openDocument(com.oldportal.framework.filechooser.FileChooserOption extensions[], String defaultDirectory)
    {
      try{
          javax.swing.JFileChooser ch = new javax.swing.JFileChooser();
          com.oldportal.framework.filechooser.FileChooserOption defaultExtension = null;
          for (int i=0; i<extensions.length; i++)
          {
            com.oldportal.framework.filechooser.CustomFileFilter filter = new com.oldportal.framework.filechooser.CustomFileFilter(extensions[i]);
            if (i==0)
              ch.setFileFilter(filter);

            ch.addChoosableFileFilter(filter);
          }

          if (defaultDirectory != null && !defaultDirectory.equals(""))
          {
              java.io.File currentDir = new java.io.File(defaultDirectory);
              if (currentDir.exists())
                  ch.setCurrentDirectory(currentDir);
          }

          if (ch.showOpenDialog(framework) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = ch.getSelectedFile();
            return openDocument(file);
          }
          return false;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
    }

    /**
      * open document.
      */
      public static boolean openDocument(java.io.File file)
      {
        try{
              Document document = factory.createDocument();

              if (!DocumentSerializerToolkit.load_xml(document, file))
                return false;

              document.setDocumentPath(file.getAbsolutePath());

              addDocument(document);

              addDocumentView(document);

              return true;
          }
          catch (Exception ex) {
            ex.printStackTrace();
            return false;
          }
      }


    /**
    * find path and filename.
    * save document.
    */
  public static void saveDocument(com.oldportal.framework.filechooser.FileChooserOption extensions[], String defaultDirectory)
  {
    try{
      if (currentDocument == null)
        return;

      if (currentDocument.getDocumentPath().equals("")) {
        saveAsDocument(extensions, defaultDirectory);
        return;
      }

      java.io.File file = new java.io.File(currentDocument.getDocumentPath());
      if (!file.exists())
          file.createNewFile();

      DocumentSerializerToolkit.save_xml(currentDocument, file);

      currentDocument.setModified(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Close (without save) old document.
   * Create copy of the document.
   * Save new document.
   */
  public static void saveAsDocument(com.oldportal.framework.filechooser.FileChooserOption extensions[], String defaultDirectory)
  {
    try{
      if (currentDocument == null)
        return;
      if (extensions.length < 1)
          throw new IllegalArgumentException("No avaliable file extension for Save As dialog");
      javax.swing.JFileChooser ch = new javax.swing.JFileChooser();
      com.oldportal.framework.filechooser.CustomFileFilter filter = new com.oldportal.framework.filechooser.CustomFileFilter(extensions[0]);
      ch.setFileFilter(filter);

      for (int i=1; i<extensions.length; i++)
      {
          com.oldportal.framework.filechooser.CustomFileFilter filter_additional =
            new com.oldportal.framework.filechooser.CustomFileFilter(extensions[i]);
          ch.addChoosableFileFilter(filter_additional);
      }

      if (defaultDirectory != null || !defaultDirectory.equals(""))
      {
          java.io.File currentDir = new java.io.File(defaultDirectory);
          if (currentDir.exists())
              ch.setCurrentDirectory(currentDir);
      }


      if (ch.showSaveDialog(framework) == javax.swing.JFileChooser.APPROVE_OPTION) {
        java.io.File f = ch.getSelectedFile();
        String selectedFile = null;
        if (ch.getFileFilter() instanceof com.oldportal.framework.filechooser.CustomFileFilter)
        {
            com.oldportal.framework.filechooser.CustomFileFilter selectedFilter =
                    (com.oldportal.framework.filechooser.CustomFileFilter) ch.
                    getFileFilter();
            selectedFile = selectedFilter.getFileWithExtension(f);
        }
        else
            selectedFile = f.getAbsolutePath();

        currentDocument.setDocumentPath(selectedFile);

        java.io.File file = new java.io.File(selectedFile);
        if (!file.exists())
          file.createNewFile();

        DocumentSerializerToolkit.save_xml(currentDocument, file);

        currentDocument.setModified(false);

        for (int i=0; i<framework.getComponentCount(); i++)
        {
            DocumentView view = (DocumentView)framework.getComponentAt(i);
            framework.setTitleAt(i, view.document.getName());
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Check document for modified.
   * Close document.
   * Create new document.
   */
  public static void closeDocument(Document document)
  {
    //TODO: Check document for modified.
    closeDocumentViews(document);
    documents.remove(document);
    if (document == currentDocument)
    {
        if (documents.size() > 0)
        {
            setCurrentDocument(documents.get(0));
        }
        else
            setCurrentDocument(null);
    }
  }

  public static void closeDocumentViews(Document document)
  {
    java.awt.Component views[] = framework.getComponents();
    for (int i=0; i<views.length; i++)
    {
      if (views[i] instanceof DocumentView)
      {
        DocumentView view = (DocumentView)views[i];
        if (view.document == document)
          framework.remove(view);
      }
    }
  }

  public static void newDocument()
  {
    Document document = factory.createDocument();

    if (currentDocument == null)
      currentDocument = document;

    addDocument(document);

    addDocumentView(document);
  }

  public static void setCurrentDocumentModified()
  {
      if (getCurrentDocument() != null)
          getCurrentDocument().setModified();
  }

}
