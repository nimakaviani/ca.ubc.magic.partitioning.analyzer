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

import java.io.CharArrayWriter;


import java.io.FileReader;
import java.io.InputStream;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


public class JipParser extends DefaultHandler {

    private IJipParseHandler mHandler;
    CharArrayWriter text = new CharArrayWriter ();

    private JipParser(IJipParseHandler handler) {
        mHandler = handler;
    }

    public static JipRun parse(String filename) throws Exception {
        JipRun run = new JipRun();
        JipParser jipParser = new JipParser(run);

        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(jipParser);
        xr.setErrorHandler(jipParser);        
        FileReader r = new FileReader(filename);
        xr.parse(new InputSource(r));        

        return run;
    }
    
    public static JipRun parse(InputStream in) throws Exception {     
        JipRun run = new JipRun();
        JipParser jipParser = new JipParser(run);

        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(jipParser);
        xr.setErrorHandler(jipParser);                
        xr.parse(new InputSource(in));   
        return run;
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

        //System.out.println("startElt " + name);
        if (name.equals("profile")) {
            String date = getAttrString(atts,"date");
            mHandler.setDate(date);
        } else if (name.equals("thread")) {
            mHandler.startThread(getAttrLong(atts, "id"));
        } else if (name.equals("interaction")) {
            mHandler.startInteraction(getAttrLong(atts, "id"));
        } else if (name.equals("frame")) {
            mHandler.startFrame(getAttrString(atts, "bn"),
            			getAttrString(atts, "cn"),
                                getAttrString(atts, "mn"),
                                getAttrLong(atts, "c"),
                                getAttrLong(atts, "t"),
                                getAttrLong(atts, "dfp"),
                                getAttrLong(atts, "dtp"),
                                getAttrLong(atts, "cfp"),
                                getAttrLong(atts, "ctp"));
        } else if (name.equals("entry")) {
            mHandler.addToClassMap(getAttrString(atts, "s"),
                                   getAttrString(atts, "f"));
        } else if (name.equals("allocation")) {
        } else if (name.equals("class")) {
        } else if (name.equals("class-map")) {
        } else if (name.equals("bundle-map")) {
        } else if (name.equals("bundle")) {
        	mHandler.startBundle(getAttrString(atts, "sn"));
        } else if (name.equals(JipBundle.BUNDLE_SYMBOLIC_NAME.toLowerCase())){
        } else if (name.equals(JipBundle.BUNDLE_NAME.toLowerCase())){
        } else if (name.equals(JipBundle.BUNDLE_VERSION.toLowerCase())){
        } else if (name.equals(JipBundle.IMPORT_PACKAGE.toLowerCase())){
        } else if (name.equals(JipBundle.EXPORT_PACKAGE.toLowerCase())){
        } else if (name.equals(JipBundle.BUNDLE_DESCRIPTION.toLowerCase())){
        } else if (name.equals(JipBundle.BUNDLE_LICENSE.toLowerCase())){
        } else if (name.equals(JipBundle.BUNDLE_VENDOR.toLowerCase())){
        } else if (name.equals(JipBundle.PRIVATE_PACKAGE.toLowerCase())){
        } else if (name.equals(JipBundle.BUNDLE_MANIFEST_VERSION.toLowerCase())){
        } else if (name.equals(JipBundle.INCLUDE_RESOURCE.toLowerCase())){
        } else if (name.equals(JipBundle.CONDITIONAL_PACKAGE.toLowerCase())){
        } else if (name.equals(JipBundle.SERVICE_COMPONENT.toLowerCase())){
        } else if (name.equals(JipBundle.SERVICE.toLowerCase())){
        	mHandler.updateBundle().setService( getAttrString(atts, "c"),
        										getAttrString(atts, "id"), 
        										getAttrString(atts, "in"),
        										getAttrString(atts, "out"),
        										getAttrString(atts, "ttl"));
        }else {
            throw new RuntimeException("unexpected tag <" + name + ">");
        }
    }

    public void endElement (String uri, String name, String qName) {
        if (name.equals("thread")) {
            mHandler.endThread();
        } else if (name.equals("interaction")) {
            mHandler.endInteraction();
        } else if (name.equals("frame")) {
            mHandler.endFrame();
        }else if (name.equals(JipBundle.BUNDLE_SYMBOLIC_NAME.toLowerCase())){
        	mHandler.updateBundle().setBundleSymbolicName(getText());
        }else if (name.equals(JipBundle.BUNDLE_NAME.toLowerCase())){
        	mHandler.updateBundle().setBundleName(getText());
        }else if (name.equals(JipBundle.BUNDLE_VERSION.toLowerCase())){
        	mHandler.updateBundle().setBundleVersion(getText());
        }else if (name.equals(JipBundle.IMPORT_PACKAGE.toLowerCase())){
        	mHandler.updateBundle().setImportPackage(getText());
        }else if (name.equals(JipBundle.EXPORT_PACKAGE.toLowerCase())){
        	mHandler.updateBundle().setExportPackage(getText());
        }else if (name.equals(JipBundle.BUNDLE_DESCRIPTION.toLowerCase())){
        	mHandler.updateBundle().setBundleDescription(getText());
        }else if (name.equals(JipBundle.BUNDLE_LICENSE.toLowerCase())){
        	mHandler.updateBundle().setBundleLicense(getText());
        }else if (name.equals(JipBundle.BUNDLE_VENDOR.toLowerCase())){
        	mHandler.updateBundle().setBundleVendor(getText());
        }else if (name.equals(JipBundle.PRIVATE_PACKAGE.toLowerCase())){
        	mHandler.updateBundle().setPrivatePackage(getText());
        }else if (name.equals(JipBundle.BUNDLE_MANIFEST_VERSION.toLowerCase())){
        	mHandler.updateBundle().setBundleManifestVersion(getText());
        }else if (name.equals(JipBundle.INCLUDE_RESOURCE.toLowerCase())){
        	mHandler.updateBundle().setIncludeResource(getText());
        }else if (name.equals(JipBundle.CONDITIONAL_PACKAGE.toLowerCase())){
        	mHandler.updateBundle().setConditionalPackage(getText());
        }else if (name.equals(JipBundle.SERVICE_COMPONENT.toLowerCase())){
        	mHandler.updateBundle().setServiceComponent(getText());
        }else if (name.equals(JipBundle.SERVICE.toLowerCase())){
        	mHandler.updateBundle().endService();
        }else if (name.equals("bundle")){
        	mHandler.endBundle();
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
