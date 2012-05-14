/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SimulationFrameworkDialog.java
 *
 * Created on 22-Jan-2012, 4:23:54 PM
 */
package ca.ubc.magic.profiler.partitioning.view;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;
import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.report.ReportModel;
import ca.ubc.magic.profiler.dist.transform.IFilter;
import ca.ubc.magic.profiler.dist.transform.IModuleCoarsener;
import ca.ubc.magic.profiler.parser.JipParser;
import ca.ubc.magic.profiler.parser.JipRun;
import ca.ubc.magic.profiler.partitioning.view.filter.FilterListDialog;
import ca.ubc.magic.profiler.simulator.control.ISimulator;
import ca.ubc.magic.profiler.simulator.control.SimulatorFactory;
import ca.ubc.magic.profiler.simulator.control.SimulatorFactory.SimulatorType;
import ca.ubc.magic.profiler.simulator.control.StaticTimeSimulator;
import ca.ubc.magic.profiler.simulator.control.TimeSimulator;
import ca.ubc.magic.profiler.simulator.framework.IFrameworkListener;
import ca.ubc.magic.profiler.simulator.framework.SimulationFramework;
import ca.ubc.magic.profiler.simulator.framework.SimulationFrameworkHelper;
import ca.ubc.magic.profiler.simulator.framework.SimulationUnit;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.collections15.keyvalue.DefaultKeyValue;

/**
 *
 * @author nima
 */
public class SimulationFrameworkDialog extends javax.swing.JDialog implements IFrameworkListener {    
  
    private int id = 0;
    
    private Map<String, DefaultKeyValue> unitMap;
    private final ModuleModel mModuleModel;
    private final HostModel   mHostModel;
    
    private SimulationFramework mSimFramework;
    /** Creates new form SimulationFrameworkDialog */
    public SimulationFrameworkDialog(SimulationFramework f, Map<String, IFilter> filterMap,
            final ModuleModel moduleModel, final HostModel hostModel, final IModuleCoarsener coarsener,
            java.awt.Frame parent, boolean modal) {        
        super(parent, modal);
        initComponents();
        mSimFramework = f;
        mFilterMap = filterMap;
        mModuleModel = moduleModel;
        mHostModel = hostModel;
        mModuleCoarsener = coarsener;
        unitMap = new HashMap<String, DefaultKeyValue>();
        profileXmlPath.setText(Constants.DEFAULT_PROFILE_XML_PATH);
        initDynamicComponents();
    }
    
    private void initDynamicComponents(){
        Set<String> nameSet = new HashSet<String>();
        for (SimulatorType type : SimulatorFactory.SimulatorType.values()){
            nameSet.add(type.getText());
        }
        simulationCombo.setModel(new javax.swing.DefaultComboBoxModel(nameSet.toArray()));
    }
    
    public void simulationAdded(SimulationUnit unit) {
        ((DefaultTableModel)simTable.getModel()).addRow(new Object[]{id, 
            unit.getName(), unit.getAlgorithmName(), "", "", "", "..."});
        unitMap.put(unit.getKey(), new DefaultKeyValue(id, unit.getSignature()));
        totalRun.setText(Integer.toString(id));
        id++;
    }

    public void simulationRemoved(SimulationUnit unit) {
        unitMap.remove(unit.getKey());
    }
    
    public void updateSimulationReport(SimulationUnit unit, ReportModel report) {        
        int id = (Integer) unitMap.get(unit.getKey()).getKey();
        TableColumn tcol = simTable.getColumnModel().getColumn(5);
        tcol.setCellRenderer(new CustomTableCellRenderer(id, Color.RED));
         ((DefaultTableModel)simTable.getModel()).setValueAt(
                report.getCostModel().getExecutionCost(), id, 3);
          ((DefaultTableModel)simTable.getModel()).setValueAt(
                report.getCostModel().getCommunicationCost(), id, 4);
        ((DefaultTableModel)simTable.getModel()).setValueAt(
                report.getCostModel().getTotalCost(), id, 5);
    }
    
    public void updateBestSimReport(SimulationUnit unit){
        if (unit != mBestSimUnit){
            mBestSimUnit = unit;
            bestRunName.setText(((Integer) (unitMap.get(unit.getKey()).getKey())) + ": " + unit.getName());
            bestRunAlg.setText(unit.getAlgorithmName());
            bestRunCost.setText(Double.toString(unit.getUnitCost()));            
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        panel1 = new java.awt.Panel();
        testToolLabel = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        runButton = new javax.swing.JButton();
        generateRandomTestsButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        profileXmlOpen = new javax.swing.JButton();
        profileXmlLabel = new javax.swing.JLabel();
        profileXmlPath = new javax.swing.JTextField();
        editFiltersButton = new javax.swing.JButton();
        customSimulationButton = new javax.swing.JButton();
        simulationCombo = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        simTable = new javax.swing.JTable();
        statusMessageLabel = new javax.swing.JLabel();
        bestRunNameLabel = new javax.swing.JLabel();
        bestRunName = new javax.swing.JLabel();
        bestRunCostLabel = new javax.swing.JLabel();
        bestRunCost = new javax.swing.JLabel();
        bestRunAlgLabel = new javax.swing.JLabel();
        bestRunAlg = new javax.swing.JLabel();
        totalRunLabel = new javax.swing.JLabel();
        totalRun = new javax.swing.JLabel();

        fileChooser.setName("fileChooser"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                simulationWindowClosed(evt);
            }
        });

        panel1.setName("panel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ca.ubc.magic.profiler.simulator.view.DistSimulatorApp.class).getContext().getResourceMap(SimulationFrameworkDialog.class);
        testToolLabel.setText(resourceMap.getString("testToolLabel.text")); // NOI18N
        testToolLabel.setName("testToolLabel"); // NOI18N

        resetButton.setText(resourceMap.getString("resetButton.text")); // NOI18N
        resetButton.setName("resetButton"); // NOI18N
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        runButton.setText(resourceMap.getString("runButton.text")); // NOI18N
        runButton.setName("runButton"); // NOI18N
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        generateRandomTestsButton.setText(resourceMap.getString("generateRandomTestsButton.text")); // NOI18N
        generateRandomTestsButton.setName("generateRandomTestsButton"); // NOI18N
        generateRandomTestsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateRandomTestsButtonActionPerformed(evt);
            }
        });

        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        profileXmlOpen.setText(resourceMap.getString("profileXmlOpen.text")); // NOI18N
        profileXmlOpen.setName("profileXmlOpen"); // NOI18N
        profileXmlOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileXmlOpenActionPerformed(evt);
            }
        });

        profileXmlLabel.setText(resourceMap.getString("profileXmlLabel.text")); // NOI18N
        profileXmlLabel.setName("profileXmlLabel"); // NOI18N

        profileXmlPath.setMaximumSize(new java.awt.Dimension(10, 28));
        profileXmlPath.setName("profileXmlPath"); // NOI18N

        editFiltersButton.setText(resourceMap.getString("editFiltersButton.text")); // NOI18N
        editFiltersButton.setName("editFiltersButton"); // NOI18N
        editFiltersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFiltersButtonActionPerformed(evt);
            }
        });

        customSimulationButton.setText(resourceMap.getString("customSimulationButton.text")); // NOI18N
        customSimulationButton.setName("customSimulationButton"); // NOI18N
        customSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customSimulationButtonActionPerformed(evt);
            }
        });

        simulationCombo.setName("simulationCombo"); // NOI18N
        simulationCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                        .addGap(276, 276, 276))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(testToolLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profileXmlLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(profileXmlPath, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(profileXmlOpen))
                            .addComponent(simulationCombo, 0, 775, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(generateRandomTestsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                        .addComponent(editFiltersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileXmlLabel)
                    .addComponent(profileXmlOpen)
                    .addComponent(profileXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(testToolLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(simulationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(runButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editFiltersButton))
                    .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(generateRandomTestsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(customSimulationButton))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1))))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        simTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Algorithm", "Exec Cost", "Comm. Cost", "Total Cost", "Edit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        simTable.setName("simTable"); // NOI18N
        simTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                simTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(simTable);
        simTable.getColumnModel().getColumn(0).setMinWidth(50);
        simTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        simTable.getColumnModel().getColumn(0).setMaxWidth(50);
        simTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("simTable.columnModel.title0")); // NOI18N
        simTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("simTable.columnModel.title1")); // NOI18N
        simTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("simTable.columnModel.title3")); // NOI18N
        simTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("simTable.columnModel.title2")); // NOI18N
        simTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("simTable.columnModel.title4")); // NOI18N
        simTable.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("simTable.columnModel.title5")); // NOI18N
        simTable.getColumnModel().getColumn(6).setMinWidth(50);
        simTable.getColumnModel().getColumn(6).setPreferredWidth(50);
        simTable.getColumnModel().getColumn(6).setMaxWidth(50);
        simTable.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("simTable.columnModel.title6")); // NOI18N

        statusMessageLabel.setForeground(resourceMap.getColor("statusMessageLabel.foreground")); // NOI18N
        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        bestRunNameLabel.setFont(resourceMap.getFont("bestRunNameLabel.font")); // NOI18N
        bestRunNameLabel.setText(resourceMap.getString("bestRunNameLabel.text")); // NOI18N
        bestRunNameLabel.setName("bestRunNameLabel"); // NOI18N

        bestRunName.setText(resourceMap.getString("bestRunName.text")); // NOI18N
        bestRunName.setName("bestRunName"); // NOI18N

        bestRunCostLabel.setFont(resourceMap.getFont("bestRunCostLabel.font")); // NOI18N
        bestRunCostLabel.setText(resourceMap.getString("bestRunCostLabel.text")); // NOI18N
        bestRunCostLabel.setName("bestRunCostLabel"); // NOI18N

        bestRunCost.setText(resourceMap.getString("bestRunCost.text")); // NOI18N
        bestRunCost.setName("bestRunCost"); // NOI18N

        bestRunAlgLabel.setFont(resourceMap.getFont("bestRunAlgLabel.font")); // NOI18N
        bestRunAlgLabel.setText(resourceMap.getString("bestRunAlgLabel.text")); // NOI18N
        bestRunAlgLabel.setName("bestRunAlgLabel"); // NOI18N

        bestRunAlg.setText(resourceMap.getString("bestRunAlg.text")); // NOI18N
        bestRunAlg.setName("bestRunAlg"); // NOI18N

        totalRunLabel.setFont(resourceMap.getFont("totalRunLabel.font")); // NOI18N
        totalRunLabel.setText(resourceMap.getString("totalRunLabel.text")); // NOI18N
        totalRunLabel.setName("totalRunLabel"); // NOI18N

        totalRun.setText(resourceMap.getString("totalRun.text")); // NOI18N
        totalRun.setName("totalRun"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bestRunAlgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bestRunNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bestRunName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bestRunAlg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bestRunCostLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bestRunCost, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(totalRunLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(totalRun, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 933, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bestRunNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(bestRunCostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                                .addComponent(bestRunCost, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(bestRunName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bestRunAlg, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bestRunAlgLabel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalRunLabel)
                        .addComponent(totalRun, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void profileXmlOpenActionPerformed(java.awt.event.ActionEvent evt) 
    {//GEN-FIRST:event_profileXmlOpenActionPerformed

        if (profileXmlPath.getText() != null) {             
            fileChooser.setCurrentDirectory(new File(profileXmlPath.getText()));         
        }         
        fileChooser.setVisible(true);         
        int returnVal = fileChooser.showOpenDialog(fileChooser);         
        if (returnVal == JFileChooser.APPROVE_OPTION) {
             File file = fileChooser.getSelectedFile();
             try {
                 // What to do with the file, e.g. display it in a TextArea
                 profileXmlPath.setText(file.getAbsolutePath());
             } catch (Exception ex) {
                 
             }
         } else {
             System.out.println("File access cancelled by user.");
         }
     }//GEN-LAST:event_profileXmlOpenActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
       try{
            if (simulationCombo.getSelectedItem() == null ||
                    simulationCombo.getSelectedItem().equals(SimulatorType.NONE.getText()))
                throw new RuntimeException("No selected simulator.");
            
            SwingWorker worker = new SwingWorker() {
               public Object construct(){
                   mSimFramework.run(mSim);
                   return "Done!";
               }
           };
           worker.start();
       }catch(Exception e){
           statusMessageLabel.setText(e.getMessage());
       }
    }//GEN-LAST:event_runButtonActionPerformed

    private void generateRandomTestsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateRandomTestsButtonActionPerformed
        SwingWorker worker = new SwingWorker() {
           public Object construct(){
                mSimFramework.generateRandomSimulationUnits(mSim, jipRun, mModuleCoarsener, mFilterMap);
                return "Done!";
           }
        };
        worker.start();
    }//GEN-LAST:event_generateRandomTestsButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        mSimFramework.stopSimThread();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void editFiltersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFiltersButtonActionPerformed
        mFilterListDialog = new FilterListDialog((java.awt.Frame)this.getOwner(), Boolean.FALSE, 
                mFilterMap, mModuleModel, mHostModel);
        mFilterListDialog.setLocationRelativeTo(this);
        mFilterListDialog.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_editFiltersButtonActionPerformed

    private void customSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customSimulationButtonActionPerformed
        mSimUnitCustomization = new SimulationUnitCustomization((java.awt.Frame)this.getOwner(), Boolean.FALSE, 
                mSimFramework, new SimulationUnit(
                        mModuleModel.getName(), (mSimFramework.getTemplate().getAlgorithmName() != null ? 
                        mSimFramework.getTemplate().getAlgorithmName() : "Template"), 
                        new DistributionModel(mModuleModel, mHostModel)));
        mSimUnitCustomization.setLocationRelativeTo(this);
        mSimUnitCustomization.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_customSimulationButtonActionPerformed

    private void simulationComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationComboActionPerformed
        SimulatorType type = SimulatorType.fromString((String) simulationCombo.getSelectedItem());
       mSim = SimulatorFactory.getSimulator(type);
        switch (type){
            case TIME_SIMULATOR:
                try{
                    if (profileXmlPath.getText() == null || profileXmlPath.getText().equals(""))
                        throw new RuntimeException("No profiler dump data is provided.");
                    InputStream in = new BufferedInputStream(
                            new ProgressMonitorInputStream(
                                      fileChooser,
                                      "Reading " +  profileXmlPath.getText(),
                                      new FileInputStream(profileXmlPath.getText())));   

                    jipRun = JipParser.parse(in);       
                    ((TimeSimulator) mSim).init(jipRun, mModuleCoarsener);
                }catch(Exception e){
                     statusMessageLabel.setText(e.getMessage());
                }
                break;
            case STATIC_TIME_SIMULATOR:
                ((StaticTimeSimulator) mSim).init(mModuleModel);
                break;
            default:
                throw new RuntimeException("A simulator needs to be selected.");
        }
    }//GEN-LAST:event_simulationComboActionPerformed

    private void simTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_simTableMouseClicked
        int row = simTable.getSelectedRow();
        int col = simTable.getSelectedColumn();
        if (col != 6)
            return;
        
        int unitId = (Integer) ((DefaultTableModel)simTable.getModel()).getValueAt(row, 0);
        for (DefaultKeyValue keyVal : unitMap.values())
            if (((Integer) keyVal.getKey()) == unitId){
                SimulationUnit unit = SimulationFrameworkHelper.getUnitFromSig(
                       (String) keyVal.getValue(), mSimFramework.getTemplate(), Boolean.TRUE);
                mSimUnitCustomization = new SimulationUnitCustomization((java.awt.Frame)this.getOwner(), 
                        Boolean.FALSE, mSimFramework, unit);
                mSimUnitCustomization.setLocationRelativeTo(this);
                mSimUnitCustomization.setVisible(Boolean.TRUE);
                return;
            }
    }//GEN-LAST:event_simTableMouseClicked

    private void simulationWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_simulationWindowClosed
        mSimFramework.removeFrameworkListener(this);
    }//GEN-LAST:event_simulationWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bestRunAlg;
    private javax.swing.JLabel bestRunAlgLabel;
    private javax.swing.JLabel bestRunCost;
    private javax.swing.JLabel bestRunCostLabel;
    private javax.swing.JLabel bestRunName;
    private javax.swing.JLabel bestRunNameLabel;
    private javax.swing.JButton customSimulationButton;
    private javax.swing.JButton editFiltersButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton generateRandomTestsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    private javax.swing.JLabel profileXmlLabel;
    private javax.swing.JButton profileXmlOpen;
    private javax.swing.JTextField profileXmlPath;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton runButton;
    private javax.swing.JTable simTable;
    private javax.swing.JComboBox simulationCombo;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JLabel testToolLabel;
    private javax.swing.JLabel totalRun;
    private javax.swing.JLabel totalRunLabel;
    // End of variables declaration//GEN-END:variables
    
    private JipRun jipRun = null;
    private IModuleCoarsener mModuleCoarsener = null;
    private ISimulator mSim = null;
    
    private SimulationUnit mBestSimUnit;
    
    private Map<String, IFilter> mFilterMap;
    
    private javax.swing.JDialog  mFilterListDialog;
    private javax.swing.JDialog  mSimUnitCustomization;
}
