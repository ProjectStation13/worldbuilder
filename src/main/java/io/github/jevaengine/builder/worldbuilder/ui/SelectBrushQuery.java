/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.jevaengine.builder.worldbuilder.ui;

import io.github.jevaengine.builder.worldbuilder.ui.SelectBrushQuery.FileTreeModel.SelectBrushChild;
import io.github.jevaengine.builder.worldbuilder.world.brush.Brush;
import io.github.jevaengine.builder.worldbuilder.world.brush.PlaceSceneArtifactBrushBehaviour;
import io.github.jevaengine.util.Nullable;
import io.github.jevaengine.world.Direction;
import io.github.jevaengine.world.scene.model.ISceneModel;
import io.github.jevaengine.world.scene.model.ISceneModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.io.File;
import java.net.URI;
import java.util.*;

/**
 *
 * @author Jeremy Wildsmith
 */
public class SelectBrushQuery extends javax.swing.JFrame {
	
	private final Logger m_logger = LoggerFactory.getLogger(SelectBrushQuery.class);
	
	@Nullable
	private File m_lastSelectedModel;
	
	private final File m_baseDirectory;
	private final ISceneModelFactory m_modelFactory;
	private final Brush m_brush;
	private final Object m_sync = new Object();
	
	public SelectBrushQuery(File baseDirectory, Brush brush, ISceneModelFactory sceneModelFactory) {
		initComponents();
		setTitle("Select Brush");

		lstDirection.setModel(new DefaultComboBoxModel<>(Direction.values()));
		
		m_brush = brush;
		browseBrushTree.setModel(new FileTreeModel(baseDirectory));
		m_baseDirectory = baseDirectory;
		m_modelFactory = sceneModelFactory;
		
		browseBrushTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object node = browseBrushTree.getLastSelectedPathComponent();
				
				if((node instanceof SelectBrushChild))
				{
					SelectBrushChild c = (SelectBrushChild)node;
					if(!c.getFile().isDirectory())
						m_lastSelectedModel = c.getFile();
				}
			}
		});
	}

	public void poll()
	{
		synchronized(m_sync)
		{
			if(m_lastSelectedModel != null)
			{
				URI relativeUri = URI.create("/").resolve(m_baseDirectory.toURI().relativize(m_lastSelectedModel.toURI()));
				
				try
				{
					Direction direction = lstDirection.getModel().getSelectedItem() == null ? Direction.XYPlus : (Direction)lstDirection.getModel().getSelectedItem();
					
					ISceneModel model = m_modelFactory.create(relativeUri);
					model.setDirection(direction);
					
					m_brush.setBehaviour(new PlaceSceneArtifactBrushBehaviour(model, relativeUri, direction, btnTraversable.isSelected(), btnStatic.isSelected()));
				} catch (ISceneModelFactory.SceneModelConstructionException e)
				{
					m_logger.error("Unable to load scene model", e);
				}
			}
				
		}
		
		m_lastSelectedModel = null;
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTraversable = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        browseBrushTree = new javax.swing.JTree();
        btnRefresh = new javax.swing.JButton();
        lstDirection = new javax.swing.JComboBox<Direction>();
        btnStatic = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnTraversable.setText("Traversable");
        btnTraversable.setName("btnIsTraversable"); // NOI18N

        browseBrushTree.setName("browseBrush"); // NOI18N
        jScrollPane1.setViewportView(browseBrushTree);
        browseBrushTree.getAccessibleContext().setAccessibleName("");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        lstDirection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lstDirection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lstDirectionActionPerformed(evt);
            }
        });

        btnStatic.setSelected(true);
        btnStatic.setText("Static");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lstDirection, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTraversable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStatic)
                        .addGap(43, 43, 43)
                        .addComponent(btnRefresh)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTraversable)
                    .addComponent(btnRefresh)
                    .addComponent(lstDirection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStatic))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
		browseBrushTree.setModel(new FileTreeModel(m_baseDirectory));
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void lstDirectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lstDirectionActionPerformed
		
    }//GEN-LAST:event_lstDirectionActionPerformed

	public static class FileTreeModel implements javax.swing.tree.TreeModel
	{
		
		private final SelectBrushChild m_root;

		public FileTreeModel(File root)
		{
			m_root = new SelectBrushChild(root);
		}
		
		@Override
		public Object getChild(Object parent, int index)
		{
			SelectBrushChild p = (SelectBrushChild) parent;
			return p.getFiles()[index];
		}

		@Override
		public int getChildCount(Object parent) {
			SelectBrushChild p = (SelectBrushChild) parent;
			if (!p.getFile().isDirectory()) {
				return 0;
			} else {
				return p.getFiles().length;
			}
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			SelectBrushChild par = (SelectBrushChild) parent;
			SelectBrushChild ch = (SelectBrushChild) child;
			
			return Arrays.asList(par.getFiles()).indexOf(ch);
		}

		@Override
		public Object getRoot() {
			return m_root;
		}

		@Override
		public boolean isLeaf(Object node) {
			SelectBrushChild n = (SelectBrushChild) node;
			return !n.getFile().isDirectory();
		}

		@Override
		public void removeTreeModelListener(javax.swing.event.TreeModelListener l) { }

		@Override
		public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) { }
		
		@Override
		public void addTreeModelListener(javax.swing.event.TreeModelListener l) { }
		
		public static final class SelectBrushChild
		{
			private final File m_file;
			private SelectBrushChild[] m_sortedFiles = null;
			
			public SelectBrushChild(File file)
			{
				m_file = file;
			}

			private final void initChildren()
			{
				if(m_sortedFiles != null)
					return;
				
				List<File> childrenOfParent = new ArrayList<>(Arrays.asList(m_file.listFiles()));

				childrenOfParent.sort(new Comparator<File>() {
					@Override
					public int compare(File a, File b) {
						if(a.isDirectory() != b.isDirectory())
							return a.isDirectory() ? -1 : 1;

						return a.getName().compareTo(b.getName());
					}
				});

				Iterator<File> it = childrenOfParent.iterator();
				List<SelectBrushChild> children = new ArrayList<>();
				for(File f : childrenOfParent)
				{
					if(f.isDirectory() || f.getName().toLowerCase().endsWith(".jmf") || f.getName().toLowerCase().endsWith(".jpar"))
						children.add(new SelectBrushChild(f));
				}

				m_sortedFiles = children.toArray(new SelectBrushChild[children.size()]);
			}

			public File getFile()
			{
				return m_file;
			}
		
			public SelectBrushChild[] getFiles()
			{
				initChildren();
				return m_sortedFiles;
			}
			
			@Override
			public String toString()
			{
				return m_file.getName();
			}

			@Override
			public int hashCode() {
				int hash = 7;
				hash = 89 * hash + Objects.hashCode(this.m_file);
				return hash;
			}

			@Override
			public boolean equals(Object obj) {
				if (obj == null) {
					return false;
				}
				if (getClass() != obj.getClass()) {
					return false;
				}
				final SelectBrushChild other = (SelectBrushChild) obj;
				return true;
			}
			
			
		}
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree browseBrushTree;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JToggleButton btnStatic;
    private javax.swing.JToggleButton btnTraversable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<Direction> lstDirection;
    // End of variables declaration//GEN-END:variables
}
