/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.report;

/**
 *
 * @author nima
 */
public class UnitModel {
    String mModuleFrom;
    String mModuleTo;
    
    public UnitModel(){
        mModuleFrom = mModuleTo = null;
    }
    
    public UnitModel(String from, String to){
        mModuleFrom = from;
        mModuleTo = to;
    }
    
    public void setFrom(String from){
        mModuleFrom = from;
    }
    
    public String getFrom(){
        return mModuleFrom;
    }
    
    public void setTo(String to){
        mModuleTo = to;
    }
    
    public String getTo(){
        return mModuleTo;
    }
}
