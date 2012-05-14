/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.model.HostModel;
import java.io.CharArrayWriter;
import java.io.FileReader;
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
public class HostParser extends DefaultHandler {
    
    private HostHandler mHandler;
    CharArrayWriter text = new CharArrayWriter ();

    public HostParser() {
        mHandler = new HostHandler();
    }

    public HostModel parse(String filename) throws Exception {
                
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);        
        FileReader r = new FileReader(filename);
        xr.parse(new InputSource(r));        

        return mHandler.mHostModel;
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
    public void endElement(String uri, String name, String qName) throws SAXException {
        if (name.equals("host")){
            mHandler.endHost();
        } else if (name.equals("cpu")) {
            mHandler.endCPU();
        } else if (name.equals("memory")) {
            mHandler.endMemory();
        } else if (name.equals("exchange-rate")) {
            mHandler.endExchangeRate();
        } else if (name.equals("capability")) {
            mHandler.endCapability(getText());
        }else if (name.equals("cost")) {
            mHandler.endCost(getText());
        }else if (name.equals("latency")) {
            mHandler.endLatency(getText());
        }
    }
    
    @Override
    public void startElement (String uri, String name, String qName, 
            Attributes atts) throws SAXException {    
        text.reset();
        try{
            //System.out.println("startElt " + name);
            if (name.equals("capabilities")) {            
                
            } else if (name.equals("hosts")) {            
                mHandler.startHosts(getAttrString(atts, "host-num"));
            } else if (name.equals("host")) {
                try{
                    mHandler.startHost(getAttrString(atts, "id"), getAttrString(atts, "default"));
                }catch(Exception e){
                    if (e.getMessage().contains("default"))
                        mHandler.startHost(getAttrString(atts, "id"), null);
                    else
                        throw new Exception(e);
                }
            } else if (name.equals("cpu")) {
                mHandler.startCPU();
            } else if (name.equals("memory")) {
                mHandler.startMemory();
            } else if (name.equals("exchange-rates")){
                
            } else if (name.equals("exchange-rate")) {
                mHandler.startExchangeRate(getAttrString(atts, "from-host"), getAttrString(atts, "to-host"));
            } else if (name.equals("capability")){
                mHandler.startCapability(getAttrString(atts, "scale"));
            } else if (name.equals("cost")){
                mHandler.startCost(getAttrString(atts, "unit"), getAttrString(atts, "scale"));
            } else if (name.equals("latency")){
                mHandler.startLatency(getAttrString(atts, "scale"));
            } else {
                throw new SAXException("unexpected tag <" + name + ">");
            }
        }catch (Exception e){
            throw new SAXException(e.getMessage());
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
