/*
 * MainFrame.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

/**
 * The Main Frame for document views.
 *
 * @author Dmitry Ognyannikov
 */
public class MainFrame extends javax.swing.JFrame {

    // constructors:
    /**
     * Creates a new instance of MainFrame
     */
    public MainFrame() {
        DocumentManager.setMainFrame(this);
        this.addWindowListener(new MainFrame_windowAdapter(this));
    }

    // methods:
    /**
     * Must be overwritten by end application designer.
     */
    public Factory getFactory() {
        return null;
    }

    /**
     * Set controls and menus configuration for activated view. Must be
     * overwritten by end application designer.
     */
    public void setControlsForSelectedView(DocumentView view) {
        ;
    }

    /**
     * Set controls and menus configuration for activate view and for edit
     * selected object. Must be overwritten by end application designer.
     */
    public void setControlsForSelectedObject(BaseObject object, DocumentView view) {
        ;
    }

    /**
     * Set controls for no seleted Views (if all documents closed). Must be
     * overwritten by end application designer.
     */
    public void setDefaultControls() {
        ;
    }

    final void onWindowActivatedInternal(WindowEvent e) {
        onWindowActivated(e);
    }

    public void onWindowActivated(WindowEvent e) {
        ;
    }

    final void onWindowDeactivatedInternal(WindowEvent e) {
        onWindowDeactivated(e);
    }

    public void onWindowDeactivated(WindowEvent e) {
        ;
    }

    final void onWindowClosingInternal(WindowEvent e) {
        onWindowClosing(e);
        // close documents before close applications:
        Document docs[] = DocumentManager.getDocuments();
        for (int i = 0; i < docs.length; i++) {
            // close document:
            DocumentManager.closeDocument(docs[i]);
        }
    }

    public void onWindowClosing(WindowEvent e) {
        ;
    }

    public void onWindowOpened(WindowEvent e) {

    }

    final void onWindowOpenedInternal(WindowEvent e) {
        onWindowOpened(e);
    }

}

class MainFrame_windowAdapter extends WindowAdapter {

    private MainFrame adaptee;

    MainFrame_windowAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void windowActivated(WindowEvent e) {
        adaptee.onWindowActivatedInternal(e);
    }

    public void windowClosing(WindowEvent e) {
        adaptee.onWindowClosingInternal(e);
    }

    public void windowDeactivated(WindowEvent e) {
        adaptee.onWindowDeactivatedInternal(e);
    }

    public void windowOpened(WindowEvent e) {
        adaptee.onWindowOpenedInternal(e);
    }
}
