/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.model.granularity.CodeUnitType;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
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
public class EntityConstraintParser extends DefaultHandler {
    
    private EntityConstraintHandler mHandler;
    CharArrayWriter text = new CharArrayWriter ();

    public EntityConstraintParser() {
        mHandler = new EntityConstraintHandler();
    }
    
    public EntityConstraintModel parse(String filename) throws Exception {
                
       XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);        
        FileReader r = new FileReader(filename);
        xr.parse(new InputSource(r));          
        
        return mHandler.getConstraintModel();
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
            if (name.equals("constraints")){
            }else if (name.equals("root")){
                mHandler.setConstraintType(name);
            }else if (name.equals("expose")){
                mHandler.setConstraintType(name);
            }else if (name.equals("ignore")){
                mHandler.setConstraintType(name);
            }else if (name.equals("replicable")){
                mHandler.setConstraintType(name);
            }else if (name.equals("non-replicable")){
                mHandler.setConstraintType(name);
            }else if (name.equals("entity")){
                mHandler.startEntity();
            }else if (name.equals("component")){
                mHandler.startUnit();
            }else if (name.equals("class")){
                mHandler.startUnit();
            }else if (name.equals("method")){
                mHandler.startUnit();
            }
            else if (name.equals("target")){
                mHandler.startTarget();
            }
            
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void endElement (String uri, String name, String qName) {
        if (name.equals("constraints")){
            }else if (name.equals("entity")){
                mHandler.endEntity();
            }else if (name.equals("component")){
                mHandler.endUnit(getText(), CodeUnitType.COMPONENT);
            }else if (name.equals("class")){
                mHandler.endUnit(getText(), CodeUnitType.CLASS);
            }else if (name.equals("method")){
                mHandler.endUnit(getText(), CodeUnitType.METHOD);
            }else if (name.equals("target")){
                mHandler.endTarget(getText());
            }else {
                mHandler.removeConstraintType();
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
