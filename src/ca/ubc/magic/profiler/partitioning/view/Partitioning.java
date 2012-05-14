/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Partitioning.java
 *
 * Created on 6-Jan-2012, 3:52:38 PM
 */
package ca.ubc.magic.profiler.partitioning.view;

import ca.ubc.magic.profiler.partitioning.view.filter.FilterListDialog;
import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;
import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.transform.IModuleCoarsener;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.execution.ExecutionFactory;
import ca.ubc.magic.profiler.dist.model.execution.ExecutionFactory.ExecutionCostType;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionFactory;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionFactory.InteractionCostType;
import ca.ubc.magic.profiler.dist.transform.IFilter;
import ca.ubc.magic.profiler.dist.transform.ModuleCoarsenerFactory;
import ca.ubc.magic.profiler.dist.transform.ModuleCoarsenerFactory.ModuleCoarsenerType;
import ca.ubc.magic.profiler.parser.EntityConstraintParser;
import ca.ubc.magic.profiler.parser.HostParser;
import ca.ubc.magic.profiler.parser.JipParser;
import ca.ubc.magic.profiler.parser.JipRun;
import ca.ubc.magic.profiler.parser.ModuleModelHandler;
import ca.ubc.magic.profiler.parser.ModuleModelParser;
import ca.ubc.magic.profiler.partitioning.control.alg.IPartitioner;
import ca.ubc.magic.profiler.partitioning.control.alg.PartitionerFactory;
import ca.ubc.magic.profiler.partitioning.control.alg.PartitionerFactory.PartitionerType;
import ca.ubc.magic.profiler.partitioning.control.filters.FilterHelper;
import ca.ubc.magic.profiler.simulator.framework.IFrameworkListener;
import ca.ubc.magic.profiler.simulator.framework.SimulationFramework;
import ca.ubc.magic.profiler.simulator.framework.SimulationUnit;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.ProgressMonitorInputStream;

/**
 *
 * @author nima
 */
public class Partitioning extends javax.swing.JFrame {

    /** Creates new form Partitioning */
    public Partitioning() {
        initComponents();        
        desktopPane.setVisible(Boolean.FALSE);
        this.setSize(new Dimension(this.getSize().width,
            commandPanel.getSize().height + 65));
        
        initDynamicComponents();
        
        mSimFramework = new SimulationFramework(Boolean.FALSE);
        
        profileXmlPath.setText(Constants.DEFAULT_PROFILE_XML_PATH);
        hostXmlPath.setText(Constants.DEFAULT_HOST_XML_PATH);
        constraintXMLPath.setText(Constants.DEFAULT_CONSTRAINT_XML_PATH);
    }

    private void initDynamicComponents() {
        // Initializing the InteractionCostType menu from the menu bar
        for (InteractionCostType type : InteractionFactory.InteractionCostType.values()){
            JCheckBoxMenuItem interactionTypeItem = new JCheckBoxMenuItem(type.getText());
            interactionTypeItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (mHostModel != null){
                        mHostModel.setInteractionCostModel(
                                InteractionFactory.getInteractionCostModel(
                                InteractionCostType.fromString(((JCheckBoxMenuItem) evt.getSource()).getText())));
                    }
                }
            });
            interactionModelMenu.add(interactionTypeItem);
        }
        
        // Initializing the ExecutionCostType menu from the menu bar
        for (ExecutionCostType type : ExecutionFactory.ExecutionCostType.values()){
            JCheckBoxMenuItem execTypeItem = new JCheckBoxMenuItem(type.getText());
            execTypeItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (mHostModel != null){
                        mHostModel.setExecutionCostModel(
                                ExecutionFactory.getInteractionCostModel(
                                ExecutionCostType.fromString(((JCheckBoxMenuItem) evt.getSource()).getText())));
                    }
                }
            });
            executionModelMenu.add(execTypeItem);
        }
        
        // Initializing the list of partitioner algorithms 
        for (PartitionerType ptype : PartitionerFactory.PartitionerType.values()){
            // initializing the partitioning combo box
            partitioningAlgCombo.addItem(ptype.getText());
            
            // initilizing the algorithm list menu item from the menu bar
            JMenuItem algorithmItem = new JMenuItem(ptype.getText());
            algorithmItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                   runPartitioningAlgorithm(((JMenuItem) evt.getSource()).getText());
                }
            });
            algorithmsMenu.add(algorithmItem);
        }
        
        for (final ModuleCoarsenerType mcType : ModuleCoarsenerFactory.ModuleCoarsenerType.values()){
            JCheckBoxMenuItem mcItem = new JCheckBoxMenuItem(mcType.getText());
            mcItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    mModuleType = mcType;
                }
            });
            moduleCoarsenerMenu.add(mcItem);
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
        desktopPane = new javax.swing.JDesktopPane();
        commandPanel = new javax.swing.JPanel();
        profileXmlLabel = new javax.swing.JLabel();
        hostXmlLabel = new javax.swing.JLabel();
        profileXmlPath = new javax.swing.JTextField();
        hostXmlPath = new javax.swing.JTextField();
        profileXmlOpen = new javax.swing.JButton();
        statusMessageLabel = new javax.swing.JLabel();
        partitioningAlgCombo = new javax.swing.JComboBox();
        visualizeBtn = new javax.swing.JButton();
        hostXmlOpen = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        moduleModelCheckBox = new javax.swing.JCheckBox();
        loadTestFrameworkBtn = new javax.swing.JButton();
        testBtn = new javax.swing.JButton();
        constraintXMLLabel = new javax.swing.JLabel();
        constraintXMLPath = new javax.swing.JTextField();
        constraintXMLOpen = new javax.swing.JButton();
        commandMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitItem = new javax.swing.JMenuItem();
        partitioningMenu = new javax.swing.JMenu();
        moduleCoarsenerMenu = new javax.swing.JMenu();
        algorithmsMenu = new javax.swing.JMenu();
        activateExposingMenu = new javax.swing.JCheckBoxMenuItem();
        syntheticNodeMenu = new javax.swing.JCheckBoxMenuItem();
        bruteforceMenuItem = new javax.swing.JMenuItem();
        simulationMenu = new javax.swing.JMenu();
        loadTestFramework = new javax.swing.JMenuItem();
        addForTest = new javax.swing.JMenuItem();
        filterMenu = new javax.swing.JMenu();
        moduleFilterMenu = new javax.swing.JMenu();
        hostPlacementFilterItem = new javax.swing.JCheckBoxMenuItem();
        hostPlacementFilterItemThreaded = new javax.swing.JCheckBoxMenuItem();
        interactionFilterMenu = new javax.swing.JMenu();
        moduleDependencyFilterItem = new javax.swing.JCheckBoxMenuItem();
        moduleDependencyFilterItemThreaded = new javax.swing.JCheckBoxMenuItem();
        editFilter = new javax.swing.JMenuItem();
        costModelMenu = new javax.swing.JMenu();
        executionModelMenu = new javax.swing.JMenu();
        interactionModelMenu = new javax.swing.JMenu();

        fileChooser.setName("fileChooser"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Graph Partitioning"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ca.ubc.magic.profiler.simulator.view.DistSimulatorApp.class).getContext().getResourceMap(Partitioning.class);
        desktopPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(resourceMap.getColor("desktopPane.border.highlightColor"), resourceMap.getColor("desktopPane.border.shadowColor"))); // NOI18N
        desktopPane.setName("desktopPane"); // NOI18N
        desktopPane.setPreferredSize(new java.awt.Dimension(300, 300));
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        commandPanel.setName("commandPanel"); // NOI18N
        commandPanel.setPreferredSize(new java.awt.Dimension(850, 210));

        profileXmlLabel.setText(resourceMap.getString("profileXmlLabel.text")); // NOI18N
        profileXmlLabel.setName("profileXmlLabel"); // NOI18N

        hostXmlLabel.setText(resourceMap.getString("hostXmlLabel.text")); // NOI18N
        hostXmlLabel.setName("hostXmlLabel"); // NOI18N

        profileXmlPath.setMaximumSize(new java.awt.Dimension(10, 28));
        profileXmlPath.setName("profileXmlPath"); // NOI18N

        hostXmlPath.setMaximumSize(new java.awt.Dimension(10, 28));
        hostXmlPath.setName("hostXmlPath"); // NOI18N

        profileXmlOpen.setText(resourceMap.getString("profileXmlOpen.text")); // NOI18N
        profileXmlOpen.setName("profileXmlOpen"); // NOI18N
        profileXmlOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileXmlOpenActionPerformed(evt);
            }
        });

        statusMessageLabel.setForeground(resourceMap.getColor("statusMessageLabel.foreground")); // NOI18N
        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        partitioningAlgCombo.setName("partitioningAlgCombo"); // NOI18N
        partitioningAlgCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partitioningAlgComboActionPerformed(evt);
            }
        });

        visualizeBtn.setText(resourceMap.getString("visualizeBtn.text")); // NOI18N
        visualizeBtn.setName("visualizeBtn"); // NOI18N
        visualizeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visualizeBtnActionPerformed(evt);
            }
        });

        hostXmlOpen.setText(resourceMap.getString("hostXmlOpen.text")); // NOI18N
        hostXmlOpen.setName("hostXmlOpen"); // NOI18N
        hostXmlOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostXmlOpenActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        moduleModelCheckBox.setText(resourceMap.getString("moduleModelCheckBox.text")); // NOI18N
        moduleModelCheckBox.setName("moduleModelCheckBox"); // NOI18N

        loadTestFrameworkBtn.setText(resourceMap.getString("loadTestFrameworkBtn.text")); // NOI18N
        loadTestFrameworkBtn.setName("loadTestFrameworkBtn"); // NOI18N
        loadTestFrameworkBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadTestFrameworkBtnActionPerformed(evt);
            }
        });

        testBtn.setText(resourceMap.getString("testBtn.text")); // NOI18N
        testBtn.setName("testBtn"); // NOI18N
        testBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testBtnActionPerformed(evt);
            }
        });

        constraintXMLLabel.setText(resourceMap.getString("constraintXMLLabel.text")); // NOI18N
        constraintXMLLabel.setName("constraintXMLLabel"); // NOI18N

        constraintXMLPath.setMaximumSize(new java.awt.Dimension(10, 28));
        constraintXMLPath.setName("constraintXMLPath"); // NOI18N

        constraintXMLOpen.setText(resourceMap.getString("constraintXMLOpen.text")); // NOI18N
        constraintXMLOpen.setName("constraintXMLOpen"); // NOI18N
        constraintXMLOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                constraintXMLOpenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout commandPanelLayout = new javax.swing.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(commandPanelLayout.createSequentialGroup()
                                .addComponent(visualizeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moduleModelCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1))
                            .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(commandPanelLayout.createSequentialGroup()
                                .addComponent(testBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loadTestFrameworkBtn))
                            .addComponent(partitioningAlgCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(commandPanelLayout.createSequentialGroup()
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profileXmlLabel)
                            .addComponent(hostXmlLabel)
                            .addComponent(constraintXMLLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(commandPanelLayout.createSequentialGroup()
                                .addComponent(hostXmlPath, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hostXmlOpen))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(constraintXMLPath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                                    .addComponent(profileXmlPath, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(constraintXMLOpen)
                                    .addComponent(profileXmlOpen))))))
                .addGap(55, 55, 55))
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileXmlLabel)
                    .addComponent(profileXmlOpen)
                    .addComponent(profileXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(constraintXMLLabel)
                    .addComponent(constraintXMLPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(constraintXMLOpen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostXmlLabel)
                    .addComponent(hostXmlOpen)
                    .addComponent(hostXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(visualizeBtn)
                    .addComponent(partitioningAlgCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(moduleModelCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loadTestFrameworkBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(testBtn))
                .addGap(23, 23, 23))
        );

        getContentPane().add(commandPanel, java.awt.BorderLayout.PAGE_START);

        commandMenuBar.setName("commandMenuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setFont(resourceMap.getFont("fileMenu.font")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitItem.setFont(resourceMap.getFont("exitItem.font")); // NOI18N
        exitItem.setText(resourceMap.getString("exitItem.text")); // NOI18N
        exitItem.setName("exitItem"); // NOI18N
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        commandMenuBar.add(fileMenu);

        partitioningMenu.setText(resourceMap.getString("partitioningMenu.text")); // NOI18N
        partitioningMenu.setFont(resourceMap.getFont("partitioningMenu.font")); // NOI18N
        partitioningMenu.setName("partitioningMenu"); // NOI18N

        moduleCoarsenerMenu.setText(resourceMap.getString("moduleCoarsenerMenu.text")); // NOI18N
        moduleCoarsenerMenu.setFont(resourceMap.getFont("moduleCoarsenerMenu.font")); // NOI18N
        moduleCoarsenerMenu.setName("moduleCoarsenerMenu"); // NOI18N
        partitioningMenu.add(moduleCoarsenerMenu);

        algorithmsMenu.setText(resourceMap.getString("algorithmsMenu.text")); // NOI18N
        algorithmsMenu.setFont(resourceMap.getFont("algorithmsMenu.font")); // NOI18N
        algorithmsMenu.setName("algorithmsMenu"); // NOI18N
        partitioningMenu.add(algorithmsMenu);

        activateExposingMenu.setFont(resourceMap.getFont("activateExposingMenu.font")); // NOI18N
        activateExposingMenu.setText(resourceMap.getString("activateExposingMenu.text")); // NOI18N
        activateExposingMenu.setActionCommand(resourceMap.getString("activateExposingMenu.actionCommand")); // NOI18N
        activateExposingMenu.setName("activateExposingMenu"); // NOI18N
        partitioningMenu.add(activateExposingMenu);

        syntheticNodeMenu.setText(resourceMap.getString("syntheticNodeMenu.text")); // NOI18N
        syntheticNodeMenu.setName("syntheticNodeMenu"); // NOI18N
        partitioningMenu.add(syntheticNodeMenu);

        bruteforceMenuItem.setFont(resourceMap.getFont("bruteforceMenuItem.font")); // NOI18N
        bruteforceMenuItem.setText(resourceMap.getString("bruteforceMenuItem.text")); // NOI18N
        bruteforceMenuItem.setName("bruteforceMenuItem"); // NOI18N
        bruteforceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bruteforceMenuItemActionPerformed(evt);
            }
        });
        partitioningMenu.add(bruteforceMenuItem);

        commandMenuBar.add(partitioningMenu);

        simulationMenu.setText(resourceMap.getString("simulationMenu.text")); // NOI18N
        simulationMenu.setFont(resourceMap.getFont("simulationMenu.font")); // NOI18N
        simulationMenu.setName("simulationMenu"); // NOI18N

        loadTestFramework.setFont(resourceMap.getFont("loadTestFramework.font")); // NOI18N
        loadTestFramework.setText(resourceMap.getString("loadTestFramework.text")); // NOI18N
        loadTestFramework.setName("loadTestFramework"); // NOI18N
        loadTestFramework.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadTestFrameworkActionPerformed(evt);
            }
        });
        simulationMenu.add(loadTestFramework);

        addForTest.setFont(resourceMap.getFont("addForTest.font")); // NOI18N
        addForTest.setText(resourceMap.getString("addForTest.text")); // NOI18N
        addForTest.setName("addForTest"); // NOI18N
        addForTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addForTestActionPerformed(evt);
            }
        });
        simulationMenu.add(addForTest);

        commandMenuBar.add(simulationMenu);

        filterMenu.setText(resourceMap.getString("filterMenu.text")); // NOI18N
        filterMenu.setFont(resourceMap.getFont("filterMenu.font")); // NOI18N
        filterMenu.setName("filterMenu"); // NOI18N

        moduleFilterMenu.setText(resourceMap.getString("moduleFilterMenu.text")); // NOI18N
        moduleFilterMenu.setFont(resourceMap.getFont("moduleFilterMenu.font")); // NOI18N
        moduleFilterMenu.setName("moduleFilterMenu"); // NOI18N

        hostPlacementFilterItem.setFont(resourceMap.getFont("hostPlacementFilterItem.font")); // NOI18N
        hostPlacementFilterItem.setText(resourceMap.getString("hostPlacementFilterItem.text")); // NOI18N
        hostPlacementFilterItem.setName("hostPlacementFilterItem"); // NOI18N
        hostPlacementFilterItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostPlacementFilterItemActionPerformed(evt);
            }
        });
        moduleFilterMenu.add(hostPlacementFilterItem);

        hostPlacementFilterItemThreaded.setFont(resourceMap.getFont("hostPlacementFilterItemThreaded.font")); // NOI18N
        hostPlacementFilterItemThreaded.setText(resourceMap.getString("hostPlacementFilterItemThreaded.text")); // NOI18N
        hostPlacementFilterItemThreaded.setName("hostPlacementFilterItemThreaded"); // NOI18N
        hostPlacementFilterItemThreaded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostPlacementFilterItemThreadedActionPerformed(evt);
            }
        });
        moduleFilterMenu.add(hostPlacementFilterItemThreaded);

        filterMenu.add(moduleFilterMenu);

        interactionFilterMenu.setText(resourceMap.getString("interactionFilterMenu.text")); // NOI18N
        interactionFilterMenu.setFont(resourceMap.getFont("interactionFilterMenu.font")); // NOI18N
        interactionFilterMenu.setName("interactionFilterMenu"); // NOI18N

        moduleDependencyFilterItem.setFont(resourceMap.getFont("moduleDependencyFilterItem.font")); // NOI18N
        moduleDependencyFilterItem.setText(resourceMap.getString("moduleDependencyFilterItem.text")); // NOI18N
        moduleDependencyFilterItem.setName("moduleDependencyFilterItem"); // NOI18N
        moduleDependencyFilterItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduleDependencyFilterItemActionPerformed(evt);
            }
        });
        interactionFilterMenu.add(moduleDependencyFilterItem);

        moduleDependencyFilterItemThreaded.setFont(resourceMap.getFont("moduleDependencyFilterItemThreaded.font")); // NOI18N
        moduleDependencyFilterItemThreaded.setText(resourceMap.getString("moduleDependencyFilterItemThreaded.text")); // NOI18N
        moduleDependencyFilterItemThreaded.setName("moduleDependencyFilterItemThreaded"); // NOI18N
        moduleDependencyFilterItemThreaded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduleDependencyFilterItemThreadedActionPerformed(evt);
            }
        });
        interactionFilterMenu.add(moduleDependencyFilterItemThreaded);

        filterMenu.add(interactionFilterMenu);

        editFilter.setFont(resourceMap.getFont("editFilter.font")); // NOI18N
        editFilter.setText(resourceMap.getString("editFilter.text")); // NOI18N
        editFilter.setName("editFilter"); // NOI18N
        editFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFilterActionPerformed(evt);
            }
        });
        filterMenu.add(editFilter);

        commandMenuBar.add(filterMenu);

        costModelMenu.setText(resourceMap.getString("costModelMenu.text")); // NOI18N
        costModelMenu.setFont(resourceMap.getFont("costModelMenu.font")); // NOI18N
        costModelMenu.setName("costModelMenu"); // NOI18N

        executionModelMenu.setText(resourceMap.getString("executionModelMenu.text")); // NOI18N
        executionModelMenu.setFont(resourceMap.getFont("executionModelMenu.font")); // NOI18N
        executionModelMenu.setName("executionModelMenu"); // NOI18N
        costModelMenu.add(executionModelMenu);

        interactionModelMenu.setText(resourceMap.getString("interactionModelMenu.text")); // NOI18N
        interactionModelMenu.setFont(resourceMap.getFont("interactionModelMenu.font")); // NOI18N
        interactionModelMenu.setName("interactionModelMenu"); // NOI18N
        costModelMenu.add(interactionModelMenu);

        commandMenuBar.add(costModelMenu);

        setJMenuBar(commandMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reseting the filters for the modules.
     */
    private void initFilters() { 
        mFilterMap = new HashMap<String, IFilter>();
        hostPlacementFilterItem.setSelected(Boolean.FALSE);
        hostPlacementFilterItemThreaded.setSelected(Boolean.FALSE);
        moduleDependencyFilterItem.setSelected(Boolean.FALSE);
    }
    
    /**
     * Reseting the cost model
     */
    private void initCostModels(){
        for (int i = 0; i < interactionModelMenu.getItemCount(); i++)
            interactionModelMenu.getItem(i).setSelected(Boolean.FALSE);
        for (int i = 0; i < executionModelMenu.getItemCount(); i++)
            executionModelMenu.getItem(i).setSelected(Boolean.FALSE);
    }
    
    private void initModuleCoarseners(){
        for (int i = 0; i < moduleCoarsenerMenu.getItemCount(); i++)
            moduleCoarsenerMenu.getItem(i).setSelected(Boolean.FALSE);
    }

    private void loadTestFramework() {
        mSimFrameworkDialog = new SimulationFrameworkDialog(mSimFramework, mFilterMap,
                mModuleModel, mHostModel, 
                ModuleCoarsenerFactory.getModuleCoarsener(mModuleType, mConstraintModel),
                this, Boolean.FALSE);
        mSimFramework.addFrameworkListener((IFrameworkListener) mSimFrameworkDialog);
        
        mSimFrameworkDialog.setLocationRelativeTo(this);
        mSimFrameworkDialog.setVisible(true);
    }

    private void profileXmlOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileXmlOpenActionPerformed
        if (profileXmlPath.getText() != null)
        fileChooser.setCurrentDirectory(new File(profileXmlPath.getText()));
        fileChooser.setVisible(true);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
              // What to do with the file, e.g. display it in a TextArea
              profileXmlPath.setText(file.getAbsolutePath());
            } catch (Exception ex) {
              statusMessageLabel.setText("Error: problem accessing file"+file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_profileXmlOpenActionPerformed

    private void visualizeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeBtnActionPerformed
        
//        InputStream in;
        
        try{       
           if (profileXmlPath.getText() == null || profileXmlPath.getText().equals(""))
               throw new Exception("No profiler dump data is provided.");   
           if (hostXmlPath.getText().equals(""))
               throw new Exception ("No host layout is provided.");
           
           mHostModel = null;
           mModuleModel = null;
           mConstraintModel = null;
           
           // reset the filters for thesystem during each parsing of a new
           // profiling input and also resetting the cost models after each
           // iteration of profiling
           initFilters();
           initCostModels();
           initModuleCoarseners();

           // parsing the configuration for hosts involved in the system
           HostParser hostParser = new HostParser();
           mHostModel = hostParser.parse(hostXmlPath.getText());
           
           // reading the input stream for the profling XML document provided
           // to the tool.
           SwingWorker worker = new SwingWorker(){
              
               public Object construct(){
                  try{
                        
                        final InputStream in =  new BufferedInputStream(
                            new ProgressMonitorInputStream(
                                null,
                                "Reading " +  profileXmlPath.getText(),
                                new FileInputStream(profileXmlPath.getText())));
                        
                        // If the Profile XML file belongs to a real trace of the application
                        // (i.e., the "Preset Module Placement" checkbox is checked),
                        // create the ModuleModel from the collected traces.
                        if (!moduleModelCheckBox.isSelected()){     
                            if (activateExposingMenu.isSelected()){
                                // parsing the entity constraints to be exposed in the dependency graph
                                EntityConstraintParser ccParser = new EntityConstraintParser();
                                mConstraintModel = ccParser.parse(constraintXMLPath.getText());
                            }else
                                mConstraintModel = null;
                            
                            // here we set the list of extra switch constraints 
                            // that would affect the parsing of the model
                            if (mConstraintModel != null){
                                mConstraintModel.getConstraintSwitches().setSyntheticNodeActivated(
                                        syntheticNodeMenu.isSelected());
                            }
                            
                            JipRun jipRun = JipParser.parse(in);             
                            IModuleCoarsener moduleCoarsener = 
                                    ModuleCoarsenerFactory.getModuleCoarsener(mModuleType, mConstraintModel);                                 
                            mModuleModel = moduleCoarsener.getModuleModelFromParser(jipRun);
                        }
                        
                        // If the Profile XML file carries only the hypotetical information for
                        // potential module placements use the ModuleModelParser together with
                        // host information in order to derive the ModuleModel, the ModuleHost
                        // placment and the ModulePairHostPair interactions.
                        else {
                            ModuleModelParser mmParser = new ModuleModelParser();
                            mmHandler = mmParser.parse(in);
                            mModuleModel = mmHandler.getModuleModel();
                        }
                        
                        in.close();
                        
                  }catch(Exception e){
                      statusMessageLabel.setText("Error: " + e.getMessage());                      
                  }
                  return "Done!";
                }
               
               @Override
               public void finished(){
                    if (mModuleModel == null){
                        throw new RuntimeException("No module model can be retrieved.");                                              
                    }

                    // After parsing the input of a profiling trace, a template is added
                    // to the simulation framework to be later on used to create multiple
                    // instances of the test units for testing agains the distribution.
                    mSimFramework.addTemplate(new SimulationUnit(
                        mModuleModel.getName(), (String) partitioningAlgCombo.getSelectedItem(), 
                        new DistributionModel(mModuleModel, mHostModel)));
                    
                    // visualize the model for the parsed module model
                    visualizeModuleModel();  
               }

               private void visualizeModuleModel() {
                    currentVP = new VisualizePartitioning(
                            "Visualization for graph: " + mModuleModel.getName(), true, true, true, true);
                    vpList.add(currentVP);

                    desktopPane.add(currentVP);     
                    desktopPane.setVisible(true);
                    
                    currentVP.drawModules(mModuleModel.getModuleExchangeMap());  
                    currentVP.setLocation(30 * vpList.indexOf(currentVP), 30 * vpList.indexOf(currentVP));
                    currentVP.setVisible(true);
                    
                    // resizing the frame after initalizing the child graph.
                    currentFrame.setPreferredSize(
                        new Dimension(currentFrame.getSize().width,
                           (currentFrame.getSize().height + desktopPane.getSize().height)));
                    currentFrame.pack();
               }
           };
           worker.start();

        }catch(Exception e){      
            statusMessageLabel.setText("Error: " + e.getMessage());
        }
    }//GEN-LAST:event_visualizeBtnActionPerformed

    private void hostXmlOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostXmlOpenActionPerformed
        if (hostXmlPath.getText() != null)
            fileChooser.setCurrentDirectory(new File(hostXmlPath.getText()));
        fileChooser.setVisible(true);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
              // What to do with the file, e.g. display it in a TextArea
              hostXmlPath.setText(file.getAbsolutePath());
            } catch (Exception ex) {
              statusMessageLabel.setText("Error: problem accessing file"+file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_hostXmlOpenActionPerformed

    private void partitioningAlgComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partitioningAlgComboActionPerformed
        if (mModuleModel != null && mHostModel != null){
            
            // the following line ensures that the synthetic node placement filter
            // gets activated when we have the module placed in the proper position.
            if (syntheticNodeMenu.isSelected())
                addSyntheticNodeFilter();
            
            runPartitioningAlgorithm((String) partitioningAlgCombo.getSelectedItem());
        }
    }//GEN-LAST:event_partitioningAlgComboActionPerformed

    private void runPartitioningAlgorithm(final String partitioningAlgorithm){
        try{
            if (mHostModel.getInteractionCostModel() == null)
                throw new RuntimeException("No Interaction Cost Model is set");   
             if (mHostModel.getExecutionCostModel() == null)
                throw new RuntimeException("No Execution Cost Model is set"); 
            
            Thread t = new Thread(new Runnable() {
                public void run(){
                    try{                        
                        IPartitioner partitioner = PartitionerFactory.getPartitioner(PartitionerType.fromString(
                                partitioningAlgorithm));        

                        if (mModuleModel.isSimulation()){
                            partitioner.init(mmHandler.getModuleModel(), mHostModel, mmHandler.getModuleHostPlacementList());
                        }else if (!mModuleModel.isSimulation()){                    
                            partitioner.init(mModuleModel, mHostModel);                    
                        }
                        for (IFilter f : mFilterMap.values())
                            partitioner.addFilter(f);    
                        partitioner.partition();         
                        currentVP.redrawModules(mModuleModel.getModuleExchangeMap());
                        currentVP.setAlgorithm(partitioningAlgorithm);
                        currentVP.setSolution(partitioner.getSolution());
                    }catch(Exception e){
                        e.printStackTrace();
                        statusMessageLabel.setText(e.getMessage());
                    }
                }
            });
            t.start();
        }catch(Exception e){
            statusMessageLabel.setText("Error: " + e.getMessage());
        }
    }
    
    private void loadTestFrameworkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadTestFrameworkBtnActionPerformed
        loadTestFramework();
    }//GEN-LAST:event_loadTestFrameworkBtnActionPerformed

    private void testBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testBtnActionPerformed
        addSimulationUnit();
    }//GEN-LAST:event_testBtnActionPerformed

    private void addForTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addForTestActionPerformed
        addSimulationUnit();
    }//GEN-LAST:event_addForTestActionPerformed

    private void loadTestFrameworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadTestFrameworkActionPerformed
        loadTestFramework();
    }//GEN-LAST:event_loadTestFrameworkActionPerformed

    private void hostPlacementFilterItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostPlacementFilterItemActionPerformed
        try{
            if (hostPlacementFilterItem.isSelected()){
                mFilterMap.put(FilterHelper.INFEASIBLE_HOST, FilterHelper.setModuleFilter(mModuleModel, mHostModel));
            }else{
                mFilterMap.remove(FilterHelper.INFEASIBLE_HOST);
            }
        }catch(Exception e){
            statusMessageLabel.setText(e.getMessage());
        }
    }//GEN-LAST:event_hostPlacementFilterItemActionPerformed

    private void moduleDependencyFilterItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleDependencyFilterItemActionPerformed
        try {
            if (moduleDependencyFilterItem.isSelected())
                mFilterMap.put(FilterHelper.INFEASIBLE_SPLIT, FilterHelper.setInteractionFilter(mModuleModel));
            else
                mFilterMap.remove(FilterHelper.INFEASIBLE_SPLIT);
        }catch(Exception e){
            statusMessageLabel.setText(e.getMessage());
        }
    }//GEN-LAST:event_moduleDependencyFilterItemActionPerformed

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitItemActionPerformed

    private void editFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFilterActionPerformed
        mFilterListDialog = new FilterListDialog(this, Boolean.FALSE, mFilterMap, mModuleModel, mHostModel);
        mFilterListDialog.setLocationRelativeTo(this);
        mFilterListDialog.setVisible(Boolean.TRUE);
    }//GEN-LAST:event_editFilterActionPerformed

    private void bruteforceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bruteforceMenuItemActionPerformed
        mbruteForcePartitioningDialog = new BruteForcePartitioningDialog(this, 
                Boolean.FALSE, mModuleModel, mHostModel);
        mbruteForcePartitioningDialog.setVisible(Boolean.TRUE);
        mbruteForcePartitioningDialog.setLocationRelativeTo(this);
    }//GEN-LAST:event_bruteforceMenuItemActionPerformed

    private void hostPlacementFilterItemThreadedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostPlacementFilterItemThreadedActionPerformed
        try{
            if (hostPlacementFilterItemThreaded.isSelected()){
                mFilterMap.put(FilterHelper.INFEASIBLE_HOST_THREAD, 
                        FilterHelper.setModuleFilterThread(mModuleModel, mHostModel));
            }else{
                mFilterMap.remove(FilterHelper.INFEASIBLE_HOST_THREAD);
            }
        }catch(Exception e){
            statusMessageLabel.setText(e.getMessage());
        }
    }//GEN-LAST:event_hostPlacementFilterItemThreadedActionPerformed

    private void constraintXMLOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_constraintXMLOpenActionPerformed
         if (constraintXMLPath.getText() != null)
            fileChooser.setCurrentDirectory(new File(constraintXMLPath.getText()));
        fileChooser.setVisible(true);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
              // What to do with the file, e.g. display it in a TextArea
              constraintXMLPath.setText(file.getAbsolutePath());
            } catch (Exception ex) {
              statusMessageLabel.setText("Error: problem accessing file"+file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_constraintXMLOpenActionPerformed

    private void moduleDependencyFilterItemThreadedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduleDependencyFilterItemThreadedActionPerformed
        
    }//GEN-LAST:event_moduleDependencyFilterItemThreadedActionPerformed

    private void addSyntheticNodeFilter(){
        try {
            if (syntheticNodeMenu.isSelected())
                mFilterMap.put(FilterHelper.INFEASIBLE_SYNTHETIC, FilterHelper.setSyntheticNodeModuleFitler(mModuleModel, mHostModel));
            else
                mFilterMap.remove(FilterHelper.INFEASIBLE_SYNTHETIC);
        }catch(Exception e){
            statusMessageLabel.setText(e.getMessage());
        }
    }
    
    private void addSimulationUnit(){
         try{
            if (mModuleModel == null)
                throw new Exception("moduleModel is empty, no partitioning performed");
            if (!mModuleModel.isPartitioned())
                throw new Exception("moduleModel is not partitioned yet.");
            mSimFramework.addUnit(new SimulationUnit(
                    mModuleModel.getName(), (String) partitioningAlgCombo.getSelectedItem(), 
                    new DistributionModel(mModuleModel, mHostModel)), Boolean.FALSE);
        }catch (Exception e){
            statusMessageLabel.setText(e.getMessage());
        }
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Partitioning.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Partitioning.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Partitioning.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Partitioning.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension screenSize = tk.getScreenSize();
                final int WIDTH = screenSize.width;
                final int HEIGHT = screenSize.height;

                Partitioning p = new Partitioning();
                p.setVisible(true);
                p.setLocation(WIDTH / 4, HEIGHT / 5);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem activateExposingMenu;
    private javax.swing.JMenuItem addForTest;
    private javax.swing.JMenu algorithmsMenu;
    private javax.swing.JMenuItem bruteforceMenuItem;
    private javax.swing.JMenuBar commandMenuBar;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JLabel constraintXMLLabel;
    private javax.swing.JButton constraintXMLOpen;
    private javax.swing.JTextField constraintXMLPath;
    private javax.swing.JMenu costModelMenu;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem editFilter;
    private javax.swing.JMenu executionModelMenu;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu filterMenu;
    private javax.swing.JCheckBoxMenuItem hostPlacementFilterItem;
    private javax.swing.JCheckBoxMenuItem hostPlacementFilterItemThreaded;
    private javax.swing.JLabel hostXmlLabel;
    private javax.swing.JButton hostXmlOpen;
    private javax.swing.JTextField hostXmlPath;
    private javax.swing.JMenu interactionFilterMenu;
    private javax.swing.JMenu interactionModelMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem loadTestFramework;
    private javax.swing.JButton loadTestFrameworkBtn;
    private javax.swing.JMenu moduleCoarsenerMenu;
    private javax.swing.JCheckBoxMenuItem moduleDependencyFilterItem;
    private javax.swing.JCheckBoxMenuItem moduleDependencyFilterItemThreaded;
    private javax.swing.JMenu moduleFilterMenu;
    private javax.swing.JCheckBox moduleModelCheckBox;
    private javax.swing.JComboBox partitioningAlgCombo;
    private javax.swing.JMenu partitioningMenu;
    private javax.swing.JLabel profileXmlLabel;
    private javax.swing.JButton profileXmlOpen;
    private javax.swing.JTextField profileXmlPath;
    private javax.swing.JMenu simulationMenu;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JCheckBoxMenuItem syntheticNodeMenu;
    private javax.swing.JButton testBtn;
    private javax.swing.JButton visualizeBtn;
    // End of variables declaration//GEN-END:variables
    
    private javax.swing.JDialog  mSimFrameworkDialog;
    private javax.swing.JDialog  mFilterListDialog;
    private javax.swing.JDialog  mbruteForcePartitioningDialog;
    
    private ModuleCoarsenerType mModuleType = ModuleCoarsenerType.BUNDLE;
    
    List<VisualizePartitioning> vpList = new ArrayList<VisualizePartitioning>();
    VisualizePartitioning currentVP;
    
    private ModuleModel mModuleModel;
    private HostModel   mHostModel;    
    private EntityConstraintModel mConstraintModel;
    
    ModuleModelHandler mmHandler;
    SimulationFramework mSimFramework;
    
    javax.swing.JFrame currentFrame = this;
    
    Map<String, IFilter> mFilterMap = new HashMap<String, IFilter>();;
}
