package com.oldportal.util.net;

import javax.net.ssl.SSLSession;

/**
 * Заглушка верификатора имени хоста SSL соединения.
 *
 * @author Dmitry Ognyannikov
 * @version 3.4
 */
public class HostnameVerifier implements javax.net.ssl.HostnameVerifier {
    public HostnameVerifier() {
    }

    public boolean verify(String string, SSLSession sSLSession) {
        return true;
    }
}