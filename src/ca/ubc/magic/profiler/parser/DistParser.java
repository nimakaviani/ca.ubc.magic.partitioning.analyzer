/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*/////////////////////////////////////////////////////////////////////

Copyright (C) 2006 TiVo Inc.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

+ Redistributions of source code must retain the above copyright notice,
  this list of conditions and the following disclaimer.
+ Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.
+ Neither the name of TiVo Inc nor the names of its contributors may be
  used to endorse or promote products derived from this software without
  specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
  POSSIBILITY OF SUCH DAMAGE.

/////////////////////////////////////////////////////////////////////*/

package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.model.DistributionModel;
import java.io.CharArrayWriter;


import java.io.FileReader;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


public class DistParser extends DefaultHandler {

    private DistHandler mHandler;
    CharArrayWriter text = new CharArrayWriter ();

    public DistParser() {
        mHandler = new DistHandler();
    }

    public DistributionModel parse(String filename) throws Exception {
                
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);        
        FileReader r = new FileReader(filename);
        xr.parse(new InputSource(r));        

        return mHandler.mDistributionModel;
    }   

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void startElement (String uri, String name,
			      String qName, Attributes atts) {
    	text.reset();

        try{
            //System.out.println("startElt " + name);
            if (name.equals("distribution")){
                
            } else if (name.equals("modules")) {
                String partitionNums = getAttrString(atts,"partition-num");
                mHandler.startModules(partitionNums);
            } else if (name.equals("module")) {
                mHandler.startModule(getAttrString(atts, "name"),            					
                                    getAttrString(atts, "partition"));             
            }else {
                throw new RuntimeException("unexpected tag <" + name + ">");
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void endElement (String uri, String name, String qName) {
        if (name.equals("modules")) {
            mHandler.endModules();       
        } else if (name.equals("module")) {
            mHandler.endModule();
        } 
    }

    private String getText()
    {
        return text.toString().trim();
    }

    public void characters(char[] ch, int start, int length)
    {
        text.write (ch,start,length);
    }

    private String getAttrString(Attributes atts, String name) {
        String value = atts.getValue(name);
        if (value == null) {
            throw new RuntimeException("no value for '" + name + "'");
        }
        return value;
    }

    private long getAttrLong(Attributes atts, String name) {
        String value = getAttrString(atts, name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(name + " (" + value + ") isn't a long");
        }
    }
}

