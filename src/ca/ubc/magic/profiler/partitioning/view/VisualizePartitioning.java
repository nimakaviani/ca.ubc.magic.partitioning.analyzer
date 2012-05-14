/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VisualizePartitioning.java
 *
 * Created on 6-Jan-2012, 4:10:00 PM
 */
package ca.ubc.magic.profiler.partitioning.view;

import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JRootPane;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author nima
 */
public class VisualizePartitioning extends javax.swing.JInternalFrame implements
        Transformer{

    public String transform(Object m) {
        if (m instanceof Module){
            return ((Module)m).getName() + ((showVertexWeight) ? 
                    " (" + ((Module)m).costToString()+")" : "");
        }else if (m instanceof InteractionData){
            return (showEdgeWeight ? 
                    (Long.toString(((InteractionData)m).getTotalData()) + " X " +
                     Long.toString(((InteractionData)m).getTotalCount())) : "");
        }
        return "";
    }

    /** Creates new form VisualizePartitioning */
    public VisualizePartitioning() {
        initComponents();
    }
    
    public VisualizePartitioning(String name, boolean arg1, boolean arg2,
            boolean arg3, boolean arg4){
        super(name, arg1, arg2, arg3, arg4);
        initComponents();
        initGraph();
    }
    
    public final void initGraph(){
        this.g = getNewGraphInstance();
        layout = new FRLayout2<Module, InteractionData>(g);
        vv = new VisualizationViewer<Module, InteractionData>(layout,
                this.getSize());
        JRootPane rp = this.getRootPane();
        rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
        vv.setPreferredSize(new java.awt.Dimension(687, 300));
        vv.getModel().getRelaxer().setSleepTime(500);
        vv.setGraphMouse(new DefaultModalGraphMouse<Number,Number>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderer().setVertexRenderer(new PartitionChangedRenderer());              
        vv.getRenderContext().setVertexLabelTransformer(this);
        vv.getRenderContext().setEdgeLabelTransformer(this);
        vv.setForeground(Color.BLUE);                
        
        graphMouse = new DefaultModalGraphMouse();        
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(graphMouse);        
        
        vv.addKeyListener(new java.awt.event.KeyListener(){
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyChar() == 's' || keyEvent.getKeyChar() == 'S'){
                    ModalGraphMouse.Mode m = ModalGraphMouse.Mode.valueOf(((String) modeComboBox.getSelectedItem()));
                    if(m == ModalGraphMouse.Mode.PICKING){
                    graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
                    modeComboBox.setSelectedIndex(0);
                    } else if (m == ModalGraphMouse.Mode.TRANSFORMING){
                    graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
                    modeComboBox.setSelectedIndex(1);
                    }
                }
            }
            public void keyReleased(KeyEvent keyEvent) {}
            public void keyTyped(KeyEvent keyEvent) {}
        });
                
        getContentPane().add(vv, BorderLayout.CENTER);
        
        validate();        
        vv.repaint();
    }

    private Graph <Module, InteractionData> getNewGraphInstance() {
        Graph <Module, InteractionData> ig = 
                Graphs.<Module, InteractionData>synchronizedUndirectedGraph(new
                        UndirectedSparseGraph<Module, InteractionData>());
        ObservableGraph<Module, InteractionData> og = new 
                ObservableGraph<Module, InteractionData>(ig);
        return og;
    }
    
    public void drawModules(Map<ModulePair, InteractionData> moduleMap){
        mModuleMap = moduleMap;
        Set<Module> moduleSet = new HashSet<Module>();
        for (Entry<ModulePair, InteractionData> entry : moduleMap.entrySet()){
            for (Module m : entry.getKey().getModules())
                if (!moduleSet.contains(m)){                    
                    g.addVertex(m);
                    moduleSet.add(m);
                }
            g.addEdge(entry.getValue(), 
                Arrays.asList(entry.getKey().getModules()), EdgeType.UNDIRECTED);
        }
        layout.initialize();
        layout.lock(false);
    }       
    
    public void redrawModules(Map<ModulePair, InteractionData> moduleMap){
        mModuleMap = moduleMap;        
        g = getNewGraphInstance();
        drawModules(mModuleMap);
//        PartitionChangedTransformer pct = new PartitionChangedTransformer();
        rerenderGraph();
    }    
    
    private void rerenderGraph(){
        vv.getRenderer().setVertexRenderer(new PartitionChangedRenderer());              
        vv.repaint();
    }
    
    private class PartitionChangedTransformer implements Transformer<Module, Paint> {

        public Paint transform(Module m) {
            Color p = null;
            if (m.getPartitionId() == 1 || m.getPartitionId() == -1){
                p = Color.red;
                
            }else {
                p = Color.green;
            }
            return new GradientPaint(0, 0, p, 20, 0, Color.blue, true);
        }           
    }
    
    private class PartitionChangedRenderer implements Renderer.Vertex<Module, InteractionData> {

        public void paintVertex(RenderContext<Module, InteractionData> rc, 
                Layout<Module, InteractionData> layout, Module m) {
            
            Color typeColor = Color.white;
            Color partitionColor = Color.red;
            
            if (m.getPartitionId() == 1 || m.getPartitionId() == -1){
                partitionColor = Color.red;
                
            }else {
                partitionColor = Color.green;
            }
             
             switch (m.getType()){
                case COMPONENT:
                    typeColor = Color.white;
                    break;
                case CLASS:
                    typeColor = Color.yellow;
                    break;
                case METHOD:
                    typeColor = Color.gray;
                    break;
                case DEFAULT:
                    typeColor = partitionColor;
            }
            
             GradientVertexRenderer vr = 
                     new GradientVertexRenderer<Module, InteractionData>(typeColor, partitionColor, false);
             vr.paintVertex(rc, layout, m);
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

        controlPanel = new javax.swing.JPanel();
        modeLabel = new javax.swing.JLabel();
        modeComboBox = new javax.swing.JComboBox();
        layoutComboBox = new javax.swing.JComboBox();
        layoutLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        algorithmLabel = new javax.swing.JLabel();
        solutionLabel = new javax.swing.JLabel();
        vertexWeightCheckbox = new javax.swing.JCheckBox();
        edgeWeightCheckbox = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(689, 600));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                modeHandler(evt);
            }
        });

        controlPanel.setName("controlPanel"); // NOI18N
        controlPanel.setPreferredSize(new java.awt.Dimension(687, 105));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ca.ubc.magic.profiler.simulator.view.DistSimulatorApp.class).getContext().getResourceMap(VisualizePartitioning.class);
        modeLabel.setText(resourceMap.getString("modeLabel.text")); // NOI18N
        modeLabel.setName("modeLabel"); // NOI18N

        modeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TRANSFORMING", "PICKING" }));
        modeComboBox.setName("modeComboBox"); // NOI18N
        modeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeComboBoxActionPerformed(evt);
            }
        });

        layoutComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "KKLayout", "FRLayout", "CircleLayout", "SpringLayout", "SpringLayout2", "ISOMLayout" }));
        layoutComboBox.setName("layoutComboBox"); // NOI18N
        layoutComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                layoutComboBoxActionPerformed(evt);
            }
        });

        layoutLabel.setText(resourceMap.getString("layoutLabel.text")); // NOI18N
        layoutLabel.setName("layoutLabel"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        algorithmLabel.setFont(resourceMap.getFont("algorithmLabel.font")); // NOI18N
        algorithmLabel.setText(resourceMap.getString("algorithmLabel.text")); // NOI18N
        algorithmLabel.setName("algorithmLabel"); // NOI18N

        solutionLabel.setFont(resourceMap.getFont("solutionLabel.font")); // NOI18N
        solutionLabel.setText(resourceMap.getString("solutionLabel.text")); // NOI18N
        solutionLabel.setName("solutionLabel"); // NOI18N

        vertexWeightCheckbox.setText(resourceMap.getString("vertexWeightCheckbox.text")); // NOI18N
        vertexWeightCheckbox.setName("vertexWeightCheckbox"); // NOI18N
        vertexWeightCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vertexWeightCheckboxActionPerformed(evt);
            }
        });

        edgeWeightCheckbox.setText(resourceMap.getString("edgeWeightCheckbox.text")); // NOI18N
        edgeWeightCheckbox.setName("edgeWeightCheckbox"); // NOI18N
        edgeWeightCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgeWeightCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                        .addComponent(algorithmLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(solutionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                    .addGroup(controlPanelLayout.createSequentialGroup()
                        .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlPanelLayout.createSequentialGroup()
                                .addComponent(modeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(modeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(vertexWeightCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlPanelLayout.createSequentialGroup()
                                .addComponent(layoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(layoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(edgeWeightCheckbox))))
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(layoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(layoutLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edgeWeightCheckbox)
                    .addComponent(vertexWeightCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(algorithmLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(solutionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(controlPanel, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeComboBoxActionPerformed
        JComboBox cb = (JComboBox)evt.getSource();
        String itemName = (String)cb.getSelectedItem();
        graphMouse.setMode(ModalGraphMouse.Mode.valueOf(itemName));
    }//GEN-LAST:event_modeComboBoxActionPerformed

    private void layoutComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_layoutComboBoxActionPerformed
                
        String className = (String) layoutComboBox.getSelectedItem();        
        Class layoutClass;
        if (KKLayout.class.getName().contains(className))
            layoutClass = KKLayout.class;
        else if (FRLayout.class.getName().contains(className))
            layoutClass = FRLayout.class;
        else if (CircleLayout.class.getName().contains(className))
            layoutClass = CircleLayout.class;
        else if (SpringLayout.class.getName().contains(className))
            layoutClass = SpringLayout.class;
        else if (SpringLayout2.class.getName().contains(className))
            layoutClass = SpringLayout2.class;
        else if (ISOMLayout.class.getName().contains(className))
            layoutClass = ISOMLayout.class;        
        else
            throw new RuntimeException("Layout class not found.");        
        Class<? extends Layout<Module, InteractionData>> layoutC = 
            (Class<? extends Layout<Module, InteractionData>>) layoutClass;
//            Class lay = layoutC;
        try
        {
            Constructor<? extends Layout<Module, InteractionData>> constructor = layoutC
                    .getConstructor(new Class[] {Graph.class});  
            Object o = constructor.newInstance(new Object[]{g});
            Layout<Module, InteractionData> l = (Layout<Module, InteractionData>) 
                    o;            
            l.setInitializer(vv.getGraphLayout());
            l.setSize(vv.getSize());

            LayoutTransition<Module, InteractionData> lt =
                    new LayoutTransition<Module, InteractionData>(vv, vv.getGraphLayout(), l);
            Animator animator = new Animator(lt);
            animator.start();
            layout = (AbstractLayout) l;
            vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
            vv.repaint();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }        
    }//GEN-LAST:event_layoutComboBoxActionPerformed

    private void vertexWeightCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vertexWeightCheckboxActionPerformed
        if (vertexWeightCheckbox.isSelected())
            showVertexWeight = Boolean.TRUE;
        else 
            showVertexWeight = Boolean.FALSE;
        this.rerenderGraph();
    }//GEN-LAST:event_vertexWeightCheckboxActionPerformed

    private void edgeWeightCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edgeWeightCheckboxActionPerformed
        if (edgeWeightCheckbox.isSelected())
            showEdgeWeight = Boolean.TRUE;
        else
            showEdgeWeight = Boolean.FALSE;
        this.rerenderGraph();
    }//GEN-LAST:event_edgeWeightCheckboxActionPerformed

    private void modeHandler(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_modeHandler
        System.out.println(evt.getKeyChar());
    }//GEN-LAST:event_modeHandler

    public void setAlgorithm(String text){
        algorithmLabel.setText(text);
    }
    
    public void setSolution(String text){
        solutionLabel.setText(text);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel algorithmLabel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JCheckBox edgeWeightCheckbox;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox layoutComboBox;
    private javax.swing.JLabel layoutLabel;
    private javax.swing.JComboBox modeComboBox;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JLabel solutionLabel;
    private javax.swing.JCheckBox vertexWeightCheckbox;
    // End of variables declaration//GEN-END:variables
    private Graph <Module, InteractionData> g = null;
    private VisualizationViewer<Module, InteractionData> vv = null;
    private AbstractLayout<Module, InteractionData> layout;
    private Map<ModulePair, InteractionData> mModuleMap = null;
        
    private DefaultModalGraphMouse graphMouse;
    
    private boolean showVertexWeight = Boolean.FALSE;
    private boolean showEdgeWeight = Boolean.FALSE;
        
}
