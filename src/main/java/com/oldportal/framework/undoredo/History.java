/*
 * History.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */

package com.oldportal.framework.undoredo;

import com.oldportal.framework.BaseObject;

/**
 * History release Undo/Redo support for document.
 * 
 * @author Dmitry Ognyannikov
 */
public class History {
    // constructors:
    public History() {
    }

    // members:
    int currentActionIndex = -1;
    java.util.Vector<Action> actions = new java.util.Vector();

    // methods:

    public void clear()
    {
      currentActionIndex = -1;
      actions.clear();
    }

    public void onAddObject(BaseObject object, BaseObject toObject)
    {
      Action action = createAction(object);
      action.actionType = Action.ADD_OBJECT_ACTION;
      action.objectXMLData = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, false);

      addAction(action);
    }

    public void onDeleteObject(BaseObject object)
    {
      Action action = createAction(object);
      action.actionType = Action.DELETE_OBJECT_ACTION;
      action.objectXMLData = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, false);

      addAction(action);
    }

    /**
       Paste object from clipboard.
    */
    public void onPasteObject(BaseObject object, BaseObject toObject)
    {
      Action action = createAction(object);
      action.actionType = Action.PASTE_OBJECT_ACTION;
      action.objectXMLData = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, false);

      addAction(action);
    }

    /**
       Edit object properties(fields).
    */
    public void onEditObject(BaseObject object, BaseObject toObject)
    {
      Action action = createAction(object);
      action.actionType = Action.EDIT_OBJECT_ACTION;
      action.objectXMLData = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, true);

      addAction(action);
    }


    public void Undo()
    {
      if (!canUndo())
        return;

      Action action = actions.get(currentActionIndex);
      currentActionIndex--;

      if ((action.actionType == Action.ADD_OBJECT_ACTION) ||
          (action.actionType == Action.PASTE_OBJECT_ACTION))
      {
        com.oldportal.framework.Module module = (com.oldportal.framework.Module)com.oldportal.framework.DocumentManager.getCurrentDocument().getModuleForClass(action.objectClassName);
        com.oldportal.framework.BaseObject object = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.objectGUID);
        module.delete(object);
      }
      else if (action.actionType == Action.DELETE_OBJECT_ACTION)
      {
        org.jdom.Element element = com.oldportal.framework.Clipboard.fromXMLText(action.objectXMLData);
        com.oldportal.framework.Module module = com.oldportal.framework.DocumentManager.getCurrentDocument().getModuleForClass(action.objectClassName);
        com.oldportal.framework.BaseObject parentObject = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.parentObjectGUID);
        module.loadObjectByName(action.objectClassName, parentObject);
        com.oldportal.framework.BaseObject object = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.objectGUID);
        object.load(element);
        module.paste(object,parentObject);
      }
      else if (action.actionType == Action.EDIT_OBJECT_ACTION)
      {
        org.jdom.Element element = com.oldportal.framework.Clipboard.fromXMLText(action.objectXMLData);
        com.oldportal.framework.BaseObject object = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.objectGUID);
        // need save after while undo
        action.objectXMLData = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, true);
        object.load(element);
      }
    }

    public void Redo()
    {
      if (!canRedo())
        return;

      currentActionIndex++;
      Action action = actions.get(currentActionIndex);

      if ((action.actionType == Action.ADD_OBJECT_ACTION) ||
          (action.actionType == Action.PASTE_OBJECT_ACTION))
      {
        org.jdom.Element element = com.oldportal.framework.Clipboard.fromXMLText(action.objectXMLData);
        com.oldportal.framework.Module module = com.oldportal.framework.DocumentManager.getCurrentDocument().getModuleForClass(action.objectClassName);
        BaseObject parentObject = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.parentObjectGUID);
        module.loadObjectByName(action.objectClassName, parentObject);
        BaseObject object = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.objectGUID);
        object.load(element);
        module.paste(object,parentObject);
      }
      else if (action.actionType == Action.DELETE_OBJECT_ACTION)
      {
        com.oldportal.framework.Module module = (com.oldportal.framework.Module)com.oldportal.framework.DocumentManager.getCurrentDocument().getModuleForClass(action.objectClassName);
        BaseObject object = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.objectGUID);
        module.delete(object);
      }
      else if (action.actionType == Action.EDIT_OBJECT_ACTION)
      {
        org.jdom.Element element = com.oldportal.framework.Clipboard.fromXMLText(action.objectXMLData);
        BaseObject object = com.oldportal.framework.DocumentManager.getCurrentDocument().getRegistry().get(action.objectGUID);
        // need save before while redo
        action.objectXMLData = com.oldportal.framework.DocumentSerializerToolkit.xmlSaveObject(object, true);
        object.load(element);
      }
    }

    public boolean canUndo()
    {
      return currentActionIndex >= 0;
    }

    public boolean canRedo()
    {
      return currentActionIndex < actions.size()-1;
    }

    private Action createAction(BaseObject object)
    {
      Action action = new Action();

      //action.author = com.oldportal.framework.DocumentManager.getCurrentDocument().currentUser;
      action.objectClassName = object.getClass().toString();
      action.objectGUID = object.getGUID();
      action.parentObjectGUID = object.getParentObject().getGUID();

      return action;
    }

    private void addAction(Action action)
    {
      if (currentActionIndex < actions.size()-1)
      {
        actions.setSize(currentActionIndex+1);
      }

      actions.add(action);
      currentActionIndex++;
    }


}
