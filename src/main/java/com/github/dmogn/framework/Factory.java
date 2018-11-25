/*
 * Factory.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework;

/**
 * Must be overwrited by end application designer.
 *
 * @author Dmitry Ognyannikov
 */
public interface Factory {

    public DocumentView createDefaultView(Document document);

    public Document createDocument();

    public void saveDocument(Document document);

    public void saveAsDocument(Document document);
}
