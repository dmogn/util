package com.oldportal.framework.undoredo;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class Action {
    // constructors:
    public Action() {
    }

    // members:
    static final int ADD_OBJECT_ACTION = 1;
    static final int DELETE_OBJECT_ACTION = 2;
    static final int PASTE_OBJECT_ACTION = 3;
    static final int EDIT_OBJECT_ACTION = 4;

    int actionType;
    com.oldportal.framework.GUID objectGUID;
    com.oldportal.framework.GUID parentObjectGUID;
    String objectXMLData;
    String objectClassName;
    //com.oldportal.objectbuilder.document.User author;

    // methods:

}
