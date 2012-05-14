/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nima
 */
public class ModuleModel {
    Map<String, Module> mModuleMap;
    Map<ModulePair, InteractionData> mModuleExchangeMap;
    
    private boolean mIsSimulation;
    private boolean mIsModelPartitioned;
    
    private String  mName;
    
    public ModuleModel(){
        mModuleMap = new HashMap<String, Module>();
        mModuleExchangeMap = new HashMap<ModulePair, InteractionData>();
        mIsSimulation =  Boolean.FALSE;
        mIsModelPartitioned = Boolean.FALSE;
    }
    
    public ModuleModel(ModuleModel moduleModel){
        mModuleMap = new HashMap<String, Module>(moduleModel.getModuleMap());
        mModuleExchangeMap = new HashMap<ModulePair, InteractionData>(
                moduleModel.getModuleExchangeMap());
        mIsSimulation = moduleModel.isSimulation();
        mIsModelPartitioned = moduleModel.isPartitioned();
        mName = moduleModel.getName();
    }
    
    public Map<String, Module> getModuleMap() {
        return mModuleMap;
    }

    public Map<ModulePair, InteractionData> getModuleExchangeMap() {
        return mModuleExchangeMap;
    }

    public void setModuleMap(Map<String, Module> mModuleMap) {
        this.mModuleMap = mModuleMap;
    }

    public void setModuleExchangeMap(Map<ModulePair, InteractionData> moduleExchangeMap) {
        this.mModuleExchangeMap = moduleExchangeMap;
    }
    
    public void setSimulation(boolean isSimulation){
        mIsSimulation = isSimulation;
    }
    
    public boolean isSimulation(){
            return mIsSimulation;
    }
    
    public boolean isPartitioned(){
        return mIsModelPartitioned;
    }
    
    public void setParitioned(boolean partitioned){
        mIsModelPartitioned = partitioned;
    }
    
    public void setName(String name){
        mName = name;
    }
    
    public String getName(){
        return mName;
    }
    
}
