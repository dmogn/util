/*
 * XMLSerializable.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework;

/**
 * Interface for XML input/output for obect. Serialization of class data to XML
 * node.
 *
 * @author Dmitry Ognyannikov
 */
public interface XMLSerializable {
    // methods:

    /**
     * Load class data (and parent classes data) from XML node (element).
     * Загружает данные класса и его предков из XML элемента.
     */
    public void load(org.jdom.Element element);

    /**
     * Save class data (and parent classes data) to XML node (element).
     * Сохраняет данные класса и его предков в XML элемент, который можно
     * встроить в какой-либо документ.
     */
    public org.jdom.Element save(boolean attributesOnly);

    /**
     * Restore references to shared classe via search class by class GUID.
     * Восстанавливает ссылки на разделяемые классы через поиск по их GUID.
     */
    public void resolveDependencies();

}
