/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.framework;

import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.Module;
import java.util.Map;

/**
 *
 * @author nima
 */
public class SimulationUnit {
    
    private String mUnitName;
    private String mAlgorithmName;
    private String mSignature;
    private DistributionModel mDistModel;    
    
    private double mUnitCost = 0.0;   
    
    public SimulationUnit(String unitName, String algName, DistributionModel distModel){
        this(unitName, algName, distModel, null);
    }
    
    public SimulationUnit(String unitName, String algName, 
            DistributionModel distModel, String sig){
        mUnitName = unitName;
        mAlgorithmName = algName;
        mDistModel = distModel;    
        mSignature = sig;
    }
    
    public SimulationUnit(SimulationUnit templateUnit){
        mUnitName = templateUnit.getName();
        mAlgorithmName = templateUnit.getAlgorithmName();
        mDistModel = new DistributionModel(templateUnit.getDistModel().getModuleMap(),
                templateUnit.getDistModel().getHostModel());
    }
    
    public DistributionModel getDistModel() {
        return mDistModel;
    }

    public void setDistModel(DistributionModel mDistModel) {
        this.mDistModel = mDistModel;
    }

    public String getName() {
        return mUnitName;
    }

    public void setName(String mUnitName) {
        this.mUnitName = mUnitName;
    }
    
    public String getAlgorithmName() {
        return mAlgorithmName;
    }

    public void setAlgorithmName(String algName) {
        this.mAlgorithmName = algName;
    }
    
    public String getSignature(){
        return mSignature;
    }
    
    public void setSignature(String sig){
        this.mSignature = sig;
    }
    
    public String applySignature(SimulationUnit templateUnit){
        StringBuilder strBldr = new StringBuilder();
        Map<String, Module> moduleMap = mDistModel.getModuleMap();
        Map<String, Module> templateMap = templateUnit.getDistModel().getModuleMap();
        for (String key : templateMap.keySet()){
            Module m = moduleMap.get(key);
            strBldr.append(m.getPartitionId() - 1);
        }
        mSignature = strBldr.toString();
        return mSignature;
    }

    public double getUnitCost() {
        return mUnitCost;
    }

    public void setUnitCost(double unitCost) {
        this.mUnitCost = unitCost;
    }
    
    public String getKey(){
        return getName()+getAlgorithmName()+(
                (mSignature != null) ? "-"+mSignature.hashCode() : "");
    }
}
