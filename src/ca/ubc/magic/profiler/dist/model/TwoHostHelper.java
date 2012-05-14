/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

/**
 *
 * @author nima
 */
public class TwoHostHelper {
    
    public static Host getTargetHost(final HostModel hostModel){
        Host host = null;
        if (hostModel.getHostMap().values().size() > 2)
            throw new RuntimeException("Simplex partitioner can be applied to 2 hosts only");
        for (Host h : hostModel.getHostMap().values())
            if (!h.getDefault()){
                host = h;
                break;
            }
        return host;
    }
    
    public static Host getSourceHost(final HostModel hostModel){
        return hostModel.getDefaultHost();
    }    
    
    public static Host getHostById(final HostModel hostModel, int id){
        return (hostModel.getDefaultHost().getId() == id) ? 
                getSourceHost(hostModel) : getTargetHost(hostModel);
    }
}
