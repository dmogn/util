/*
 * BaseObject.java
 *
 * OldPortal Framework library.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 * Parent class for all document objects.
 * @author Dmitry Ognyannikov
 */
public class BaseObject implements XMLSerializable, Comparable {
    // constructors:
    /** Creates a new instance of BaseObject */
    public BaseObject(BaseObject _parentObject) {
        parentObject = _parentObject;
        guid = guid.generateNew();
    }

    public BaseObject(BaseObject _parentObject, Document _parentDocument) {
        parentObject = _parentObject;
        parentDocument = _parentDocument;
        guid = guid.generateNew();
    }

    // members:
    private BaseObject parentObject = null;
    private GUID parentObjectGUID = null;
    private Document parentDocument = null;

    /** Object unique identifier */
    GUID guid = null;

    // methods:

    public void clear()
    {
    }

    public void load(org.jdom.Element element)
    {
        clear();

        if (element.getName()!="BaseObject")
            return;

        // load own members:

        // load shared members GUIDs:
        try{
            parentObjectGUID = GUID.fromString(element.getAttributeValue("parentObject"));
        } catch (Exception ex) {
            parentObjectGUID = null;
        }

        // load attributes:
        setGUID(GUID.fromString(element.getAttributeValue("guid")));

        // load strings:
  }

  public org.jdom.Element save(boolean attributesOnly)
  {
    org.jdom.Element ret = new org.jdom.Element("BaseObject");

    // save shared members:
    if (parentObject != null)
      ret.setAttribute("parentObject", parentObject.getGUID().toString());


    // save attributes:
    ret.setAttribute("guid", guid.toString());

    // save strings:

    return ret;
  }

  public void resolveDependencies()
  {
    if (DocumentManager.getCurrentDocument().getRegistry().containsObject(parentObjectGUID))
      parentObject = DocumentManager.getCurrentDocument().getRegistry().get(parentObjectGUID);
    else
      parentObject = null;
  }

  public final GUID getGUID()
  {
    return guid;
  }

  public final void setGUID(GUID _guid)
  {
    DocumentManager.getCurrentDocument().getRegistry().remove(this.guid);
    guid = _guid;
    DocumentManager.getCurrentDocument().getRegistry().add(this);
  }

  public BaseObject getParentObject()
  {
    return parentObject;
  }

  public void setParentObject(BaseObject object)
  {
    if (object!=null)
    {
      parentObject = object;
    }
    else
    {
      parentObject = null;
    }
    parentObjectGUID = null;
  }

  public void onRemoved()
  {
    Document document = DocumentManager.getCurrentDocument();
    document.getRegistry().remove(guid);
    java.lang.Object objects[] = document.getRegistry().values().toArray();
    for (int i=0; i<objects.length; i++)
    {
      BaseObject object = (BaseObject)objects[i];
      if ((object.getParentObject() != null)&&(object.getParentObject().equals(this)))
        object.onRemoved();
    }
  }

  public final boolean isObjectParent(BaseObject object)
  {
    if (getParentObject() == null)
      return false;

    BaseObject parent = getParentObject();
    while (parent != null)
    {
      if (parent == object)
        return true;
      parent = parent.getParentObject();
    }
    return false;
  }

  public int compareTo(java.lang.Object o) {
    int nRes = toString().compareTo(o.toString());
    if (nRes != 0)
      return nRes;
    return guid.compareTo(((BaseObject)o).getGUID());
  }

  public boolean equals(Object obj)
  {
      if (!(obj instanceof BaseObject))
          return false;
      return guid.equals(((BaseObject)obj).guid);
  }

  protected java.lang.Object clone()
  {
      //TODO: создать объект того же типа, этот в XML, тот из XML, вернуть тот. Универсальный способ вроде.
      return null;
  }

}
