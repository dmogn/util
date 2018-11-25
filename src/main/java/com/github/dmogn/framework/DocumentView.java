/*
 * DocumentView.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

/**
 * View class in MDI architecture.
 *
 * @author Dmitry Ognyannikov
 */
public class DocumentView extends javax.swing.JPanel {

    Document document = null;

    // constructors:
    /**
     * Creates a new instance of DocumentView
     */
    public DocumentView(Document _document) {
        document = _document;
        _document.addView(this);

        // init events:
        addComponentListener(new View_componentAdapter(this));
    }

    // methods:
    final void onSelectedInternal(ComponentEvent e) {
        DocumentManager.setCurrentView(this);
        DocumentManager.setCurrentDocument(document);
        DocumentManager.getMainFrame().setControlsForSelectedView(this);
        onSelected(e);
    }

    public void onSelected(ComponentEvent e) {
        ;
    }

    public void init() {
        ;
    }
}

class View_componentAdapter extends ComponentAdapter {

    private DocumentView adaptee;

    View_componentAdapter(DocumentView adaptee) {
        this.adaptee = adaptee;
    }

    public void componentShown(ComponentEvent e) {
        adaptee.onSelectedInternal(e);
    }
}
