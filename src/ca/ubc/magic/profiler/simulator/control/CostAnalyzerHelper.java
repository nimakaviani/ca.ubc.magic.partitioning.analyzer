/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.control;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleHost;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.TwoHostHelper;
import ca.ubc.magic.profiler.dist.model.cost.conversion.CostConversionSingleton;
import ca.ubc.magic.profiler.dist.model.execution.CloudExecutionMonetaryCostModel;
import ca.ubc.magic.profiler.dist.model.execution.ExecutionTimeCostModel;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import ca.ubc.magic.profiler.dist.model.report.ReportModel;
import java.util.Map.Entry;

/**
 *
 * @author nima
 */
public class CostAnalyzerHelper {
    
    /**
     * The analyzeCosts method is a analytic method helping to measure how much the deployment costs
     * and what are the factors affecting the cost of deployment. This gives a better look into the
     * internals of the deployment when the partitioning is performed.
     */
    public static ReportModel analyzeCosts(String name, final ModuleModel moduleModel, final HostModel hostModel){
        
        ReportModel report = new ReportModel(hostModel.getHostMap().values());
        
        long   total2PremiseData = 0,  total2CloudData = 0;
        double total2PremiseCost = 0, total2CloudCost = 0;
        
        // updating the partition ids for the module exchange map if it is not already updated
        for (Entry<ModulePair, InteractionData> e : moduleModel.getModuleExchangeMap().entrySet()){
            
            ModulePair mp = e.getKey();
            
            mp.getModules()[0].setPartitionId(moduleModel.getModuleMap().get(mp.getModules()[0].getName()).getPartitionId());
            mp.getModules()[1].setPartitionId(moduleModel.getModuleMap().get(mp.getModules()[1].getName()).getPartitionId());
            
            if (mp.getModules()[0].getPartitionId() == Constants.INVALID_PARTITION_ID ||
                    mp.getModules()[1].getPartitionId() == Constants.INVALID_PARTITION_ID)
                continue;
            
            // Calculating the amount of data going over the wire for the deployment.
            InteractionData iData = e.getValue();
            HostPair hph1h2 = new HostPair(TwoHostHelper.getSourceHost(hostModel), TwoHostHelper.getTargetHost(hostModel));
            HostPair hph2h1 = new HostPair(TwoHostHelper.getSourceHost(hostModel), TwoHostHelper.getTargetHost(hostModel));
            InteractionCost cost = null;
            
            if (!mp.getModules()[0].getPartitionId().equals(mp.getModules()[1].getPartitionId())){
                if (iData == null)
                    throw new RuntimeException("Error! There is no interaction data for the pair: " + mp.toString());
                if (mp.getModules()[0].getPartitionId() > mp.getModules()[1].getPartitionId()){
                    cost = hph1h2.getInteractionCost(hostModel, iData);
                    total2PremiseData += CostConversionSingleton.getInstance().interactionConvert(
                                iData.getFromParentData(), iData.getFromParentCount());
                    total2CloudData   += CostConversionSingleton.getInstance().interactionConvert(
                            iData.getToParentData(), iData.getToParentCount()); 
                } else{
                    cost = hph2h1.getInteractionCost(hostModel, iData);
                    total2CloudData   += CostConversionSingleton.getInstance().interactionConvert(
                            iData.getFromParentData(), iData.getFromParentCount());
                    total2PremiseData += CostConversionSingleton.getInstance().interactionConvert(
                            iData.getToParentData(), iData.getToParentCount());
                }
            }
            if (cost != null){
                total2CloudCost   += cost.getH1toH2Cost();
                total2PremiseCost += cost.getH2toH1Cost();
            }
        }
                
        
        
        // Execution time based on simulation results.
        double totalExecTime = 0.0;
        // Execution cost based on the cost model provided from the cloud
        double totalExecCost = 0.0;
        // getting the percentage of partitions
        double totalCount = 0.0;
        double p1Count = 0.0;
        double p2Count = 0.0;
        for(Module m : moduleModel.getModuleMap().values()){
            totalCount++;
            
            if (m.getPartitionId() == Constants.INVALID_PARTITION_ID)
                continue;
            else if (m.getPartitionId() == 1)
                p1Count++;
            else if (m.getPartitionId() == 2)
                p2Count++;
            else
                throw new RuntimeException("Invalid partition id for module " + m.getName());
            
            // calculating the overall time of execution
            hostModel.setExecutionCostModel(new  ExecutionTimeCostModel());
            ModuleHost mh = new ModuleHost(m, hostModel.getHostMap().get(m.getPartitionId()));
            totalExecTime += mh.setExecutionCost(hostModel, hostModel.getDefaultHost());
            
            hostModel.setExecutionCostModel(new CloudExecutionMonetaryCostModel());
            totalExecCost += mh.setExecutionCost(hostModel, hostModel.getDefaultHost());
        }
        System.out.println(name + "part1% = " + (p1Count / totalCount));
        System.out.println(name + "part2% = " + (p2Count / totalCount));
        System.out.println("\t Total data going to Cloud     : " + total2CloudData + "(" + total2CloudCost + " $)" );
        System.out.println("\t Total data going to Premise   : " + total2PremiseData +  "(" + total2PremiseCost + " $)");
        System.out.println("\t Total data going over the wire: " + (total2CloudData + total2PremiseData) +  "(" + 
                (total2CloudCost + total2PremiseCost) + " $)");
        System.out.println("\t Total Exec. Time              : " + totalExecTime);
        System.out.println("\t Total Exec. Cost$$            : " + totalExecCost);
        
        report.getCostModel().setCommunicationCost(total2CloudCost + total2PremiseCost);
        report.getCostModel().setExecutionCost(totalExecTime);
        
        return report;
    }
}
