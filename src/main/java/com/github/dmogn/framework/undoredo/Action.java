/*
 * Action.java
 *
 * OldPortal Framework Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.framework.undoredo;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class Action {

    static final int ADD_OBJECT_ACTION = 1;
    static final int DELETE_OBJECT_ACTION = 2;
    static final int PASTE_OBJECT_ACTION = 3;
    static final int EDIT_OBJECT_ACTION = 4;

    int actionType;
    com.github.dmogn.framework.GUID objectGUID;
    com.github.dmogn.framework.GUID parentObjectGUID;
    String objectXMLData;
    String objectClassName;
    //com.oldportal.objectbuilder.document.User author;

    // constructors:
    public Action() {
    }

    // methods:
}
