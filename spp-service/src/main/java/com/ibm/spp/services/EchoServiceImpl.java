/* Copyright (c) 2012-2017 VMware, Inc. All rights reserved. */
package com.ibm.spp.services;

/**
 * Implementation of the EchoService interface
 */
public class EchoServiceImpl implements EchoService {

   /* (non-Javadoc)
    * @see com.ibm.spp.EchoService#echo(java.lang.String)
    */
   public String echo(String message) {
      return message;
   }
}
