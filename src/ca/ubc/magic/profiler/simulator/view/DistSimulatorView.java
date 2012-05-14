/*
 * DistSimulatorView.java
 */

package ca.ubc.magic.profiler.simulator.view;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.simulator.control.ISimulator;
import ca.ubc.magic.profiler.simulator.control.TimeSimulator;
import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.transform.IModuleCoarsener;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.transform.ModuleCoarsenerFactory;
import ca.ubc.magic.profiler.dist.transform.ModuleCoarsenerFactory.ModuleCoarsenerType;
import ca.ubc.magic.profiler.parser.DistParser;
import ca.ubc.magic.profiler.parser.HostParser;
import ca.ubc.magic.profiler.parser.JipParser;
import ca.ubc.magic.profiler.parser.JipRun;
import ca.ubc.magic.profiler.simulator.control.ISimulatorListener;
import java.awt.Color;
import javax.swing.event.TableModelEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * The application's main frame.
 */
public class DistSimulatorView extends FrameView {

    public DistSimulatorView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        moduleTable.getModel().addTableModelListener(new ModuleTableListener());
        
        fileChooser.setVisible(false);
        statusMessageLabel.setForeground(Color.RED);
        
        profileXmlPath.setText("/home/nima/osgi/apps/rubis/deployment/Profile-20120104-153304.xml");
        distXmlPath.setText("/home/nima/workspace/trunk/dependency.graph.mvn/dependency.graph.analyze/src/test/resources/dist-model/dist2.xml");
        hostXmlPath.setText("/home/nima/workspace/trunk/dependency.graph.mvn/dependency.graph.analyze/src/test/resources/dist-model/host1.xml");
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DistSimulatorApp.getApplication().getMainFrame();
            aboutBox = new DistSimulatorAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DistSimulatorApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        profileXmlLabel = new javax.swing.JLabel();
        distXmlLabel = new javax.swing.JLabel();
        profileXmlPath = new javax.swing.JTextField();
        distXmlPath = new javax.swing.JTextField();
        profileXmlOpen = new javax.swing.JButton();
        distXmlOpen = new javax.swing.JButton();
        run = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        moduleTable = new javax.swing.JTable();
        extractBundles = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        loadDistribution = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        hostXmlPath = new javax.swing.JTextField();
        hostXmlOpen = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        fileChooser = new javax.swing.JFileChooser();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ca.ubc.magic.profiler.simulator.view.DistSimulatorApp.class).getContext().getResourceMap(DistSimulatorView.class);
        profileXmlLabel.setText(resourceMap.getString("profileXmlLabel.text")); // NOI18N
        profileXmlLabel.setName("profileXmlLabel"); // NOI18N

        distXmlLabel.setText(resourceMap.getString("distXmlLabel.text")); // NOI18N
        distXmlLabel.setName("distXmlLabel"); // NOI18N

        profileXmlPath.setText(resourceMap.getString("profileXmlPath.text")); // NOI18N
        profileXmlPath.setMaximumSize(new java.awt.Dimension(10, 28));
        profileXmlPath.setName("profileXmlPath"); // NOI18N

        distXmlPath.setText(resourceMap.getString("distXmlPath.text")); // NOI18N
        distXmlPath.setMaximumSize(new java.awt.Dimension(10, 28));
        distXmlPath.setName("distXmlPath"); // NOI18N

        profileXmlOpen.setText(resourceMap.getString("profileXmlOpen.text")); // NOI18N
        profileXmlOpen.setName("profileXmlOpen"); // NOI18N
        profileXmlOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileXmlOpenActionPerformed(evt);
            }
        });

        distXmlOpen.setText(resourceMap.getString("distXmlOpen.text")); // NOI18N
        distXmlOpen.setName("distXmlOpen"); // NOI18N
        distXmlOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distXmlOpenActionPerformed(evt);
            }
        });

        run.setText(resourceMap.getString("run.text")); // NOI18N
        run.setName("run"); // NOI18N
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        moduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Module", "Partition #"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        moduleTable.setName("moduleTable"); // NOI18N
        jScrollPane1.setViewportView(moduleTable);
        moduleTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("moduleTable.columnModel.title0")); // NOI18N
        moduleTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("moduleTable.columnModel.title1")); // NOI18N

        extractBundles.setText(resourceMap.getString("extractBundles.text")); // NOI18N
        extractBundles.setName("extractBundles"); // NOI18N
        extractBundles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractBundlesActionPerformed(evt);
            }
        });

        jSeparator1.setName("jSeparator1"); // NOI18N

        loadDistribution.setText(resourceMap.getString("loadDistribution.text")); // NOI18N
        loadDistribution.setName("loadDistribution"); // NOI18N
        loadDistribution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDistributionActionPerformed(evt);
            }
        });

        clear.setText(resourceMap.getString("clear.text")); // NOI18N
        clear.setName("clear"); // NOI18N
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        hostXmlPath.setMaximumSize(new java.awt.Dimension(10, 28));
        hostXmlPath.setName("hostXmlPath"); // NOI18N

        hostXmlOpen.setText(resourceMap.getString("hostXmlOpen.text")); // NOI18N
        hostXmlOpen.setName("hostXmlOpen"); // NOI18N
        hostXmlOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostXmlOpenActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(profileXmlLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(distXmlLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(7, 7, 7)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(profileXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(distXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profileXmlOpen, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(distXmlOpen, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(extractBundles, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(loadDistribution, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(run, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(hostXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hostXmlOpen, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(clear, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profileXmlLabel)
                    .addComponent(profileXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profileXmlOpen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(distXmlLabel)
                    .addComponent(distXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distXmlOpen))
                .addGap(9, 9, 9)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostXmlOpen)
                    .addComponent(hostXmlPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(extractBundles)
                    .addComponent(run)
                    .addComponent(loadDistribution)
                    .addComponent(clear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ca.ubc.magic.profiler.simulator.view.DistSimulatorApp.class).getContext().getActionMap(DistSimulatorView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(statusMessageLabel)
                        .addComponent(statusAnimationLabel)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3))
        );

        fileChooser.setCurrentDirectory(new java.io.File("/home/nima"));
        fileChooser.setName("fileChooser"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

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

private void distXmlOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distXmlOpenActionPerformed
    if (distXmlPath.getText() != null)
        fileChooser.setCurrentDirectory(new File(distXmlPath.getText()));
    fileChooser.setVisible(true);
    int returnVal = fileChooser.showOpenDialog(fileChooser);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
          // What to do with the file, e.g. display it in a TextArea
          distXmlPath.setText(file.getAbsolutePath());
        } catch (Exception ex) {
          statusMessageLabel.setText("Error: problem accessing file"+file.getAbsolutePath());
        }
    } else {
        System.out.println("File access cancelled by user.");
    }
}//GEN-LAST:event_distXmlOpenActionPerformed

private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
    try{
        if (jipRun == null || distModel == null)
            throw new RuntimeException("Either the execution profile or the "
                    + "distributin model are not set properly.");
         if (runBox == null) {
            JFrame mainFrame = DistSimulatorApp.getApplication().getMainFrame();
            runBox = new DistSimulatorRunBox(mainFrame);
            runBox.setLocationRelativeTo(mainFrame);
        }
        DistSimulatorApp.getApplication().show(runBox);
        final ISimulator sim = new TimeSimulator();
        distModel.setHostModel(hostModel);
        ((TimeSimulator) sim).init(jipRun, moduleCoarsener);
        sim.addListener((ISimulatorListener) runBox);
        final Thread t = new Thread((Runnable)sim);
        t.start();
        
        runBox.addWindowListener(new WindowListener(){
            public void windowOpened(WindowEvent we){};

            public void windowClosing(WindowEvent we){};

            public void windowClosed(WindowEvent we){
                sim.removeListener((ISimulatorListener) runBox);
                t.interrupt();
            };

            public void windowIconified(WindowEvent we){};

            public void windowDeiconified(WindowEvent we){};

            public void windowActivated(WindowEvent we){};

            public void windowDeactivated(WindowEvent we){};
            
        });
    }catch(Exception e){
        statusMessageLabel.setText("Error: " + e.getMessage());
    }
}//GEN-LAST:event_runActionPerformed

private void extractBundlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractBundlesActionPerformed
 try{       
       if (profileXmlPath.getText() == null || profileXmlPath.getText().equals(""))
           throw new Exception("No profiler dump data is provided.");
       if (profileFlag == true)
           throw new Exception("Clear the current bundle execution.");
       InputStream in = new BufferedInputStream(
                          new ProgressMonitorInputStream(
                                  fileChooser,
                                  "Reading " +  profileXmlPath.getText(),
                                  new FileInputStream(profileXmlPath.getText())));   
       
       jipRun = JipParser.parse(in);                    
       moduleModel = moduleCoarsener.getModuleModelFromParser(jipRun);       
       
       for (String module : moduleModel.getModuleMap().keySet()){
           ((DefaultTableModel)moduleTable.getModel()).addRow(new Object[]{module, ""});         
       }                  
       profileFlag = true;
   }catch(Exception e){
       statusMessageLabel.setText("Error: " + e.getMessage());
   }
}//GEN-LAST:event_extractBundlesActionPerformed

private void loadDistributionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDistributionActionPerformed
    try{
        if (distXmlPath.getText().equals(""))
            throw new Exception ("No distribution layout is provided.");
        if (hostXmlPath.getText().equals(""))
            throw new Exception ("No host layout is provided.");
        if (distFlag == true)
            throw new Exception("Clear the current bundle execution.");
        DistParser dParser = new DistParser();
        distModel = dParser.parse(distXmlPath.getText());
        HostParser hParser = new HostParser();
        hostModel = hParser.parse(hostXmlPath.getText());
        Enumeration itr = ((DefaultTableModel)moduleTable.getModel()).getDataVector().elements();
        while (itr.hasMoreElements()){
            Vector row = (Vector) itr.nextElement();
            row.setElementAt(distModel.getModuleMap().get((String) row.elementAt(0)).getPartitionId(), 1);
        }
        ((DefaultTableModel)moduleTable.getModel()).fireTableRowsUpdated(
                moduleTable.getSelectedRow(),moduleTable.getSelectedRow());  
        distFlag = true;
    }catch(Exception e){
        e.printStackTrace();
        statusMessageLabel.setText("Error: " + e.getMessage());
    }
}//GEN-LAST:event_loadDistributionActionPerformed

private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
    distFlag = profileFlag = false;
    profileXmlPath.setText("");
    distXmlPath.setText("");
    hostXmlPath.setText("");
    statusMessageLabel.setText("");
    int rowCount = ((DefaultTableModel)moduleTable.getModel()).getRowCount();
    for (int i=0; i < rowCount; i++)
        ((DefaultTableModel)moduleTable.getModel()).removeRow(i);
}//GEN-LAST:event_clearActionPerformed

    private void hostXmlOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostXmlOpenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hostXmlOpenActionPerformed

private class ModuleTableListener implements TableModelListener {
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        if (((DefaultTableModel)moduleTable.getModel()).getColumnName(col).equals("Partition #")){
            if (((DefaultTableModel)moduleTable.getModel()).getValueAt(row, col) != null){
                if (col != 1)
                    throw new RuntimeException("Incorrect column changed");
                String partitionName = (String) ((DefaultTableModel)
                        moduleTable.getModel()).getValueAt(row, col - 1);
                String newPartition = (String) ((DefaultTableModel)
                        moduleTable.getModel()).getValueAt(row, col);
                int newPartitionId = Integer.parseInt(newPartition);
                distModel.updateModulePartition(partitionName, newPartitionId);                                                        
            }
        }            
    }    
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clear;
    private javax.swing.JLabel distXmlLabel;
    private javax.swing.JButton distXmlOpen;
    private javax.swing.JTextField distXmlPath;
    private javax.swing.JButton extractBundles;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton hostXmlOpen;
    private javax.swing.JTextField hostXmlPath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loadDistribution;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTable moduleTable;
    private javax.swing.JLabel profileXmlLabel;
    private javax.swing.JButton profileXmlOpen;
    private javax.swing.JTextField profileXmlPath;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton run;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private boolean profileFlag = false;
    private boolean distFlag = false;
    
    private JipRun jipRun;
    private DistributionModel distModel;
    private HostModel hostModel;
    private ModuleModel moduleModel;
       
    IModuleCoarsener moduleCoarsener = 
            ModuleCoarsenerFactory.getModuleCoarsener(ModuleCoarsenerType.BUNDLE, null);
            
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;    
    private JDialog runBox;    
}
