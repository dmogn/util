/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
*/

package com.oldportal.util.net;

import javax.net.ssl.SSLSession;

/**
 * The stub for SSL hostname verification. "Permit All" mode.
 * 
 * @author Dmitry Ognyannikov
 */
public class HostnameVerifier implements javax.net.ssl.HostnameVerifier {
    public HostnameVerifier() {
    }

    public boolean verify(String string, SSLSession sSLSession) {
        return true;
    }
}