/* Copyright (c) 2012-2017 VMware, Inc. All rights reserved. */
package com.ibm.spp.services;


/**
 * Interface used to test a java service call from the UI.
 *
 * It must be declared as osgi:service with the same name in
 * main/resources/META-INF/spring/bundle-context-osgi.xml
 */
public interface EchoService {
   /**
    * @return the same message it received
    */
   String echo(String message);
}
