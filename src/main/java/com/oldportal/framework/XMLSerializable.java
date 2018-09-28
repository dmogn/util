/*
 * XMLSerializable.java
 *
 * Created on 30 ������ 2005 �., 16:36
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.oldportal.framework;

/**
 * Interface for XML input/output for obect.
 * Serialization of class data to XML node.
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
   * Сохраняет данные класса и его предков в XML элемент, который можно встроить в какой-либо документ.
   */
  public org.jdom.Element save(boolean attributesOnly);
  /**
   * Restore references to shared classe via search class by class GUID.
   * Восстанавливает ссылки на разделяемые классы через поиск по их GUID.
   */
  public void resolveDependencies();

}
