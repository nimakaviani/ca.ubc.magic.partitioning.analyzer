/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.report;

/**
 *
 * @author nima
 */
public class CostModel {
    double mExecution = 0;
    double mCommunication = 0;    
    
    public void setExecutionCost(double exec){
        mExecution = exec;
    }
    
    public void addExecutionCost(double extraExec){
        mExecution += extraExec;
    }
    
    public void setCommunicationCost(double comm){
        mCommunication = comm;
    }
    
    public void addCommunicationCost(double extraComm){
        mCommunication += extraComm;
    }
    
    public double getExecutionCost(){
        return mExecution;
    }
    
    public double getCommunicationCost(){
        return mCommunication;
    }
    
    public double getTotalCost(){
        return mExecution +  mCommunication;
    }
}
