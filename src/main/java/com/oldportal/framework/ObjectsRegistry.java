/*
 * ObjectsRegistry.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework;

/**
 * Support restore objects dependencies in document.
 * 
 * @author Dmitry Ognyannikov
 */
public class ObjectsRegistry {
    // constructors:
    /**
     * Creates a new instance of ObjectsRegistry
     */
    public ObjectsRegistry() {
    }

    // members:
    private java.util.HashMap<String, BaseObject> map = new java.util.HashMap<String, BaseObject>();

    private java.util.Vector<ObjectsRegistry> registersStack = new java.util.Vector<ObjectsRegistry>();


    // methods:
    public BaseObject get(GUID guid)
  {
    for (int i=registersStack.size()-1; i>=0; i--)
      if (registersStack.get(i).containsObject(guid))
        return registersStack.get(i).get(guid);

    return map.get(guid.toString());
  }

  public void add(BaseObject object)
  {
    if (registersStack.size() > 0)
    {
      registersStack.get(registersStack.size()-1).add(object);
    }
    else
    {
      map.put(object.getGUID().toString(), object);
    }

  }

  public void remove(GUID guid){
    if (registersStack.size() > 0)
    {
      registersStack.get(registersStack.size()-1).remove(guid);
    }
    else
    {
      map.remove(guid.toString());
    }
  }

  public boolean containsObject(GUID guid){
    for (int i=registersStack.size()-1; i>=0; i--)
      if (registersStack.get(i).containsObject(guid))
        return true;

    if ((guid == null) || guid.isEmpty())
      return false;
    return map.containsKey(guid.toString());
  }


  public void clear()
  {
    registersStack.clear();
    map.clear();
  }

  public int size()
  {
    return map.size();
  }

  public java.util.Collection values()
  {
    return map.values();
  }

  public void resetGUIDsForObjectTree(GUID guid){
    if (registersStack.size() > 0)
    {
      registersStack.get(registersStack.size()-1).resetGUIDsForObjectTree(guid);
    }
    else
    {
      BaseObject object = get(guid);
      if (object == null)
        return;

      java.lang.Object array[] = map.values().toArray();
      resetGUIDsForObjectTree(object, array);
    }
  }

  private void resetGUIDsForObjectTree(BaseObject object, java.lang.Object array[])
  {
    for (int i=0; i<array.length; i++)
    {
      BaseObject childObject = (BaseObject)array[i];
      if (childObject.getParentObject() == (Object)object)
      {
        resetGUIDsForObjectTree(childObject, array);
      }
    }
    object.setGUID(GUID.generateNew());
  }

  /**
   * Resolve dependencies in current stack layer.
   * For operations with custom objects (copy, paste, etc).
  */
  public void resolveDependenciesLocal()
  {
    if (registersStack.size() > 0)
    {
      registersStack.get(registersStack.size()-1).resolveDependenciesLocal();
    }
    else
    {
      java.lang.Object objects[] = values().toArray();
      for (int i=0; i<objects.length; i++)
      {
        BaseObject object = (BaseObject)objects[i];
        object.resolveDependencies();
      }
    }
  }

  public void pushStack()
  {
    ObjectsRegistry newRegister = new ObjectsRegistry();
    registersStack.add(newRegister);
  }

  public void popStack()
  {
    ObjectsRegistry oldRegister = registersStack.get(registersStack.size()-1);
    ObjectsRegistry parentRegister;
    if (registersStack.size() > 1)
      parentRegister = registersStack.get(registersStack.size()-2);
    else
      parentRegister = this;

    // move all new elements to parent register
    java.lang.Object array[] = oldRegister.map.values().toArray();
    for (int i=0; i<array.length; i++)
    {
      BaseObject childObject = (BaseObject)array[i];
      if (!parentRegister.containsObject(childObject.getGUID()))
      {
        parentRegister.add(childObject);
      }
    }

    oldRegister.clear();

    registersStack.remove(registersStack.size()-1);
  }



  public boolean testRegistry()
  {
    boolean ret = true;
    /*java.util.HashMap map = new java.util.HashMap();
    for (int i=0; i<10; i++)
    {
      map.put(Integer.toString(i), Integer.toString(i+1));
    }
    for (int i=0; i<10; i++)
    {
      boolean b1 = map.containsKey(Integer.toString(i));
      boolean b2 = map.containsValue(Integer.toString(i+1));
      ret &= b1;
      ret &= b2;
    }//*/
    return ret;
  }
}
