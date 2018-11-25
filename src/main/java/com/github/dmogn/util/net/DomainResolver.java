/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.util.net;

import javax.naming.directory.*;
import java.net.*;

/**
 * DNS network operations.
 *
 * @author Dmitry Ognyannikov
 */
public class DomainResolver {

    public static String getIpForDomain(String domain) {
        // JNDI DNS example:
//        Hashtable env = new Hashtable();
//        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
//        env.put("java.naming.provider.url",    "dns://server1.sun.com/sun.com");
//
//        DirContext ictx = new InitialDirContext(env);
//        Attributes attrs1 = ictx.getAttributes("host1", new String[] {"A"});
//        Attributes attrs2 = ictx.getAttributes("host2", new String[] {"A"});
        try {
            InetAddress address = InetAddress.getByName(domain);

            return address.getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.warn("getIpForDomain(), domain: " + domain + " exception:", ex);
            return null;
        }
    }

    public static String getIpForDomainWithSelectedDNS(String domain, String dnsServerIP) {
        try {
            // JNDI DNS example:
            DirContext ictx = new InitialDirContext();
            //Attributes attrs3 = ictx.getAttributes("dns://89.188.108.78/" + domain, new String[] {"A"});
            Attributes attrs3 = ictx.getAttributes("dns://" + dnsServerIP + "/" + domain, new String[]{"A"});

            javax.naming.NamingEnumeration en = attrs3.getAll();

            while (en.hasMore()) {
                Attribute attr = (Attribute) en.next();
                return attr.get().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.warn("getMXIpForDomain(), domain: " + domain + " exception:", ex);
        }
        return null;
    }

    //** Get mail server IP
    public static String getMXIpForDomain(String domain) {
        try {
            // JNDI DNS example:
            DirContext ictx = new InitialDirContext();
            Attributes attrs3 = ictx.getAttributes("dns://89.188.108.78/" + domain, new String[]{"MX"});

            javax.naming.NamingEnumeration en = attrs3.getAll();

            while (en.hasMore()) {
                Attribute attr = (Attribute) en.next();
                return attr.get().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.warn("getMXIpForDomain(), domain: " + domain + " exception:", ex);
        }
        return null;
    }

    public static String getReverseDNS(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);

            return address.getCanonicalHostName();
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.warn("getReverseDNS(), ip: " + ip + " exception:", ex);
            return null;
        }
    }
}
