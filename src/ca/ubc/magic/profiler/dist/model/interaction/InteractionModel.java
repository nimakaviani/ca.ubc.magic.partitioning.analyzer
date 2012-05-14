/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.UnitModel;
import ca.ubc.magic.profiler.dist.model.platform.Capability;
import ca.ubc.magic.profiler.dist.model.platform.CostUnit;
import ca.ubc.magic.profiler.dist.model.platform.DataExchangeCapability;
import ca.ubc.magic.profiler.dist.model.platform.DataExchangeCostUnit;
import ca.ubc.magic.profiler.dist.model.platform.LatencyUnit;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nima
 */
public class InteractionModel {  
    
    private Map<HostPair, InteractionUnit> map = new HashMap<HostPair, InteractionUnit>();  
    
    private double mHandShakeCost;
    private double mLiftingLowering;
    
    public InteractionModel(){
        mHandShakeCost = Constants.DEFAULT_HANDSHAKE_COST;
        mLiftingLowering = Constants.DEFAULT_LIFTING_LOWERING_COST;
    }
    
    public InteractionModel(int handshakeCost, double liftinglowering){
        mHandShakeCost = handshakeCost;
        mLiftingLowering = liftinglowering;
    }
    
    public double getHandShakeCost(){
        return mHandShakeCost;
    }
    
    public double getLiftingLoweringCost(){
        return mLiftingLowering;
    }
   
    public void addInteractionRate(Host from_host ,Host to_host, 
                Capability capability, CostUnit cost, LatencyUnit latency){       
        HostPair hp = new HostPair(from_host, to_host);
        if (map.get(hp) != null)
            throw new RuntimeException("The exchange rate for this HostPair is"
                    + "already defined: " + hp.toString());
        map.put(hp, new InteractionUnit(from_host, to_host, 
                capability, cost, latency));
    }
    
    public Capability getInteractionRateForHosts(Host from_host, Host to_host) throws RuntimeException {        
        InteractionUnit e = map.get(new HostPair(from_host, to_host));
        return e.getCapability();
    }           
    
    public CostUnit getInteractionCostForHosts(Host from_host, Host to_host){
        InteractionUnit e = map.get(new HostPair(from_host, to_host));
        return e.getCost();
    }
    
    public LatencyUnit getInteractionLatencyForHosts(Host from_host, Host to_host){
        InteractionUnit e = map.get(new HostPair(from_host, to_host));
        return e.getLatency();
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (InteractionUnit unit : map.values())
            builder.append(unit.toString()).append("\n");
        return builder.toString();
    }
    
    private class InteractionUnit extends UnitModel {
     
        Host mFromHost;
        Host mToHost;
        
        LatencyUnit    mLatency;
        
        InteractionUnit(Host from ,Host to, 
                double capabilityValue, String capabilityScale, 
                double latencyValue, String latencyScale, 
                double costValue, double costUnit, String costScale){
            
            this(from, to, new DataExchangeCapability(capabilityValue, capabilityScale),
                    new DataExchangeCostUnit(costValue, costUnit, costScale),
                    new LatencyUnit(latencyValue, latencyScale));
        }
        
        InteractionUnit(Host from, Host to,
                Capability capability, CostUnit cost, LatencyUnit latency){
            
            mFromHost = from;
            mToHost = to;
            
            mCapability = capability;
            mCost = cost;
            mLatency = latency;
        }                
        
        private LatencyUnit getLatency(){
            return mLatency;
        }
        
        @Override
        public String toString(){
            return "[" + mFromHost.toString() +":\n"+ 
                   " " + mToHost.toString() + "\n" + 
                     "\t(" + mCapability.toString() + "\n " + 
                     "\t " + mCost.toString() + "\n " +
                     "\t " + mLatency.toString() +")\n" + "]";
        }
    }            
}
