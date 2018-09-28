/*
 * Module.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 * Edit operations with set of document objects.
 * 
 * @author Dmitry Ognyannikov
 */
public interface Module {
    // methods:


    // General
    public String getModuleName();
    public String getModuleDescription();

    // IO
    //public boolean canLoadFrom(org.jdom.Element element);
    //public BaseObject loadFrom(org.jdom.Element element);
    public boolean isSupportedObject(BaseObject object);
    public boolean isSupportedClass(String className);
    public BaseObject loadObjectByName(String className, BaseObject parentObject);

    // GUI support
    //public boolean canPasteTo(javax.swing.tree.DefaultMutableTreeNode node, org.jdom.Element element);
    //public boolean pasteTo(javax.swing.tree.DefaultMutableTreeNode node, org.jdom.Element element);

    public boolean editProperties(BaseObject object);

  /**
   * Clear and fill tree node.
   * @param node DefaultMutableTreeNode
   */
  //public void updateNode(javax.swing.tree.DefaultMutableTreeNode node, com.oldportal.objectbuilder.DocumentView view);
  public void setView(DocumentView view);

  // menu items:
  //public javax.swing.JMenuItem[] getAddItems(javax.swing.tree.DefaultMutableTreeNode node);
  //public javax.swing.JMenuItem[] getGlobalItems(javax.swing.tree.DefaultMutableTreeNode node);
  //public javax.swing.JComponent getTreeObjectComponent(com.oldportal.objectbuilder.document.Object _object, boolean selected, boolean expanded, boolean hasFocus);

  // clipboard operations:
  public boolean canCut(BaseObject object);
  public boolean canCopy(BaseObject object);
  public boolean canPaste(String objectClassName, BaseObject toObject);
  public boolean canDelete(BaseObject object);
  public boolean paste(BaseObject object, BaseObject toObject);
  public void cut(BaseObject object);
  public void delete(BaseObject object);


}
