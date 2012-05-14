/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.granularity.CodeUnitType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nima
 */
public class ModuleModelHandler {
    
    ModuleModel mModuleModel = null;    
    
        
    private Module mCMFake = null;
    
    private InteractionData mCID = null;    
    private Long mCIDFP = null;
    private Long mCIDTP = null;
    private Long mCIDCount = null;
    
    private ModulePair mCMP = null;
    
    private List<Module> mList = null;
        
    ModuleModelHandler(){
        
        mModuleModel = new ModuleModel();
        mModuleModel.setSimulation(true);
        
        mList = new ArrayList<Module>();        
    }
    
    public void setModelName(String name){
        mModuleModel.setName(name);
    }
    
    public void startModule(String name){        
        mCMFake = new Module(name, CodeUnitType.COMPONENT);
    }
    
    public void startInteraction(String m1, String m2){
        if (mCMP != null)
            throw new RuntimeException("Module pair is already initialized");               
        
        mCMP = new ModulePair(
                mModuleModel.getModuleMap().get(m1), 
                mModuleModel.getModuleMap().get(m2));
        mCID = new InteractionData();
    }
    
    public void setModulePartition(String partitionId){
        if (mCMFake == null)
            throw new RuntimeException("No module to assign the partition"
                    + "for Id: " + partitionId);
        mCMFake.setPartitionId(Integer.parseInt(partitionId));
    }
    
    public void setModuleExecCost(String execCost){
        if (mCMFake == null)
            throw new RuntimeException("No module for exec cost: "
                    + execCost);
        mCMFake.setExecutionCost(Double.parseDouble(execCost));
    }
    
    public void setModuleExecCount(String execCount){
        if (mCMFake == null)
            throw new RuntimeException("No module for exec count: "
                    + execCount);
        mCMFake.setExecutionCount(Long.parseLong(execCount));
    }
    
    public void setIgnoreRate(String ignoreRate){
        if (!ignoreRate.equalsIgnoreCase("true") &&
                !ignoreRate.equalsIgnoreCase("false"))
            throw new RuntimeException("Incorrect value for ignore-rate");
        if (mCMFake != null && mCID == null){
            mCMFake.setIgnoreRate(Boolean.parseBoolean(ignoreRate));
        } else if (mCMFake == null && mCID != null){
            mCID.setIgnoreRate(Boolean.parseBoolean(ignoreRate));
        } else
            throw new RuntimeException("No element initialized for ignore rate : "
                    + ignoreRate);
    }
    
    public void setInteractionDFP(String rate){
        if (mCID == null)
            throw new RuntimeException("An interaction clause is ending without"
                    + "initlization");
        mCIDFP = Long.parseLong(rate);
        
    }
    
    public void setInteractionDTP(String rate){
        if (mCID == null)
            throw new RuntimeException("An interaction clause is ending without"
                    + "initlization");
        mCIDTP = Long.parseLong(rate);
    }
    
    public void setInteractionDataCount(String cout){
        if (mCID == null)
            throw new RuntimeException("An interaction clause is ending without"
                    + "initlization");
        mCIDCount = Long.parseLong(cout);
    }
    
    public void endModule(){
        if (mCMFake == null)
            throw new RuntimeException("A module clause is ending without"
                    + "initialization.");
        Module mReal;
        if (mModuleModel.getModuleMap().get(mCMFake.getName()) != null){
           mReal = mModuleModel.getModuleMap().get(mCMFake.getName());           
        }else 
            mReal = new Module(mCMFake);
        
        // partitionIds are equal to 1 or 2 in the current module model. We decrease the Id by one, in order
        // to properly assign the values for the partition Ids to the array of values in the modules.
        mReal.setExecutionCost(mCMFake.getExecutionCost(), mCMFake.getPartitionId() - 1);
        mReal.setExecutionCount(mCMFake.getExecutionCount(), mCMFake.getPartitionId() - 1);
        mReal.setPartitionId(-1);
        
        mModuleModel.getModuleMap().put(mReal.getName(), mReal);
        
        mList.add(mCMFake);
        
        mCMFake = null;        
    }
    
    public void endInteraction(){
        if (mCID == null || mCMP == null)
            throw new RuntimeException("An interaction clause is ending without"
                    + "initlization");
        mCID.addInteraction(mCIDFP, mCIDTP, mCIDCount, mCIDCount);
        mModuleModel.getModuleExchangeMap().put(mCMP, mCID);
        mCID = null;
        mCMP = null;
    }    
           
    public ModuleModel getModuleModel(){
        return mModuleModel;
    }
    
    public List getModuleHostPlacementList(){
        return mList;
    }
}
