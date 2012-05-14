/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.framework;

import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.report.ReportModel;
import ca.ubc.magic.profiler.dist.transform.IFilter;
import ca.ubc.magic.profiler.dist.transform.IModuleCoarsener;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;
import ca.ubc.magic.profiler.parser.JipRun;
import ca.ubc.magic.profiler.simulator.control.ISimulator;
import ca.ubc.magic.profiler.simulator.control.ISimulatorListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nima
 */
public class SimulationFramework implements ISimulatorListener {
        
    private Map<String, SimulationUnit> mSimulationMap;
    private List<IFrameworkListener> listenerList;
    
    private SimulationUnit mTemplate;
    private boolean mCheckWithTemplate = Boolean.FALSE;
    
    private SimulationUnit mCurrentUnit = null;
    private Thread         mCurrentSimThread = null;
    
    private SimulationUnit mBestSimUnit = null;
    
    private List<Integer>   mRandomSimulationUnitList;
    
    public SimulationFramework(boolean checkWithTemplate){
        mSimulationMap = new HashMap<String, SimulationUnit>();
        listenerList = new ArrayList<IFrameworkListener>();
        mCheckWithTemplate = checkWithTemplate;
    }
    
    public void addTemplate(SimulationUnit template){
        mTemplate = template;
    }
    
    public final SimulationUnit getTemplate(){
        return mTemplate;
    }
    
    public void addUnit(SimulationUnit unit, boolean isRandom){
        // checks to see wether the template needs to be checked
        // against the submitted unit.
        if (mCheckWithTemplate && mTemplate == null)
            throw new RuntimeException("No template is set to check the unit against.");
        
        // if the template is not null, generate the signature corresponding
        // to the template for the submitted unit.
        if (mTemplate != null)
            unit.applySignature(mTemplate);
        
        // when checking the availability of a unit in the list, check for the
        // key of the unit(its name plys its algorithm) as well as the signature
        // of the two. Maybe the same algorithm for the same profiler is used on
        // a different module placement signature.
        if (mSimulationMap.get(unit.getKey()) != null)
            throw new RuntimeException("The unit is currently in the map");
        
        checkAgainstTemplate(unit);
        if (!isRandom)
            mSimulationMap.put(unit.getKey(), unit);
        for (IFrameworkListener l : listenerList){
            l.simulationAdded(unit);
        }
    }
    
    public void removeUnit(SimulationUnit unit){
        mSimulationMap.remove(unit.getKey());
//        for (IFrameworkListener l : listenerList){
//            l.simulationRemoved(unit);
//        }
    }
    
    public Map<String, SimulationUnit> getSimulationMap(){
        return mSimulationMap;
    }
    
    public void generateRandomSimulationUnits(final ISimulator simulator, 
            final JipRun jipRun, final IModuleCoarsener moduleCoarsener, 
            final Map<String, IFilter> filterMap){
        
        try{
                                   
            Thread t = new Thread(new Runnable() {
                public void run(){
                    
                    mRandomSimulationUnitList = new ArrayList<Integer>();
                    
                    HostModel hm = mTemplate.getDistModel().getHostModel(); 

                    String templateSig = SimulationFrameworkHelper.getTemplateSignature(mTemplate);
                    String generatedSig = templateSig;

                    do {

                        String filteredSig = generatedSig;
                        // applying filters to the generated string
                        for (IFilter filter : filterMap.values()){
                            if (! (filter instanceof IModuleFilter))
                                continue;
                            filteredSig = SimulationFrameworkHelper.getFilteredSignature(filteredSig, 
                                    mTemplate, (IModuleFilter) filter);
                        }
                        
                        // If the generated simulation is not already in the list of simulations
                        // add it to the list and perform simulation steps for the new signature
                        // otherwise get another generated simulation and repeat from there
                        
                        if (!mRandomSimulationUnitList.contains(filteredSig.hashCode())){
                            
                            mRandomSimulationUnitList.add(filteredSig.hashCode());
                            
                            SimulationUnit randomUnit = SimulationFrameworkHelper.getUnitFromSig(filteredSig, mTemplate, Boolean.TRUE);
                            addUnit(randomUnit, Boolean.TRUE);
                            runSimulationForUnit(simulator, randomUnit);
                            if (mBestSimUnit == null || mBestSimUnit.getUnitCost() > mCurrentUnit.getUnitCost())
                                mBestSimUnit = mCurrentUnit;
                            removeUnit(randomUnit);
                        
                        } else
                            System.out.println("Repeated test: " + filteredSig);
                        
                        generatedSig = SimulationFrameworkHelper.getNextSignature(generatedSig, hm.getNumberOfHosts());        
                        
                    } while (!generatedSig.equals(templateSig));
                }
            });
            mCurrentSimThread = t;
            t.start();
        }catch(Exception e){
            
        }finally{
            mRandomSimulationUnitList = null;
        }
    }
    
    public void addFrameworkListener(IFrameworkListener listener){
        listenerList.add(listener);
    }
    
    public void removeFrameworkListener(IFrameworkListener listener){
        listenerList.remove(listener);
    }
    
    public void run(ISimulator simulator){        
        for (SimulationUnit unit : mSimulationMap.values()){
            runSimulationForUnit(simulator, unit);
        }
    }
    
    private synchronized void runSimulationForUnit(ISimulator simulator, SimulationUnit unit){
        try{       
            mCurrentUnit = unit;            
            simulator.addListener(this);  
            simulator.simulate(unit);
            Thread t = new Thread((Runnable) simulator);
            mCurrentSimThread= t;
            t.start();        
            t.join();            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void valueChanged(ReportModel report) {
        for (IFrameworkListener l : listenerList)                       
            l.updateSimulationReport(mCurrentUnit, report);                    
    }
    
    public void unitSimulationOver(ReportModel report){
        mCurrentUnit.setUnitCost(report.getCostModel().getTotalCost());
        if (mBestSimUnit == null || mCurrentUnit.getUnitCost() < mBestSimUnit.getUnitCost() )
            mBestSimUnit = mCurrentUnit;
        for (IFrameworkListener l : listenerList)
            l.updateBestSimReport(mBestSimUnit);
    }
    
    private void checkAgainstTemplate(SimulationUnit unit){
        if (!mCheckWithTemplate)
            return;
        if (!mTemplate.getDistModel().getHostModel().equals(unit.getDistModel().getHostModel()))
            throw new RuntimeException("Template host model does not match unit host model");
        Set<String> moduleNameSet = mTemplate.getDistModel().getModuleMap().keySet();
        for (String key : unit.getDistModel().getModuleMap().keySet())
            if (!moduleNameSet.contains(key))
                throw new RuntimeException("Module does not exist in template: " + key);
    }
    
    public void stopSimThread(){
        mCurrentSimThread.interrupt();
    }
}
