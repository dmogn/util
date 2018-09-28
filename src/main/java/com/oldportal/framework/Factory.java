/*
 * Factory.java
 *
 * OldPortal Framework library.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 * Must be overwrited by end application designer.
 * @author Dmitry Ognyannikov
 */
public interface Factory {
    DocumentView createDefaultView(Document document);
    Document createDocument();
    void saveDocument(Document document);
    void saveAsDocument(Document document);
}
