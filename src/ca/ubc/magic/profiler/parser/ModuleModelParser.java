/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import java.io.CharArrayWriter;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author nima
 */
public class ModuleModelParser extends DefaultHandler {
    
    private ModuleModelHandler mHandler;
    CharArrayWriter text = new CharArrayWriter ();

    public ModuleModelParser() {
        mHandler = new ModuleModelHandler();
    }

    public ModuleModelHandler parse(InputStream in) throws Exception {
                
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);                
        xr.parse(new InputSource(in));        
        
        return mHandler;
    }   

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void startElement (String uri, String name,
			      String qName, Attributes atts) {
    	text.reset();

        try{
            //System.out.println("startElt " + name);
            if (name.equals("module-model")){
                String modelName = getAttrString(atts, "name");
                mHandler.setModelName(modelName);
            } else if (name.equals("module-map") ||
                    name.equals("partition") ||
                    name.equals("exec-cost") || name.equals("exec-count") ||
                    name.equals("ignore-rates") || name.equals("data-from-parent") ||
                    name.equals("data-to-parent") || name.equals("data-count")){
                
            } else if (name.equals("exchange-map")){
            
            }else if (name.equals("module")) {
                String moduleName = getAttrString(atts,"name");
                mHandler.startModule(moduleName);
            } else if (name.equals("interaction")) {
                mHandler.startInteraction(getAttrString(atts, "m1"),                        
                        getAttrString(atts, "m2"));
            }else {
                throw new RuntimeException("unexpected tag <" + name + ">");
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void endElement (String uri, String name, String qName) {
        if (name.equals("partition")) {
            mHandler.setModulePartition(getText());       
        } else if (name.equals("exec-cost")) {
            mHandler.setModuleExecCost(getText());
        } else if (name.equals("exec-count")) {
            mHandler.setModuleExecCount(getText());
        } else if (name.equals("ignore-rates")) {
            mHandler.setIgnoreRate(getText());
        } else if (name.equals("data-from-parent")) {
            mHandler.setInteractionDFP(getText());
        } else if (name.equals("data-to-parent")) {
           mHandler.setInteractionDTP(getText());
        } else if (name.equals("data-count")) {
           mHandler.setInteractionDataCount(getText());
        } else if (name.equals("module")) {
           mHandler.endModule();
        } else if (name.equals("interaction")) {
           mHandler.endInteraction();
        }
    }

    private String getText()
    {
        return text.toString().trim();
    }

    @Override
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
