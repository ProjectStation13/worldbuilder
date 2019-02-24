/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.jevaengine.builder.worldbuilder.ui;

import io.github.jevaengine.builder.worldbuilder.world.EditorWorld;
import io.github.jevaengine.math.Vector2F;
import io.github.jevaengine.math.Vector3F;
import io.github.jevaengine.util.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 *
 * @author Jeremy Wildsmith
 */
public class SelectLayerQuery extends javax.swing.JFrame {

	private final String NAME_DISABLED = "Disabled";

	private final Logger m_logger = LoggerFactory.getLogger(SelectLayerQuery.class);

	@Nullable
	private File m_lastSelectedModel;

	private final Object m_sync = new Object();

	private final SortedListModel<JCheckBoxList.Datum<Layer>> m_layersModel;

	private final EditorWorld m_world;

	private int lastSelectedLayer = 0;

	public SelectLayerQuery(Map<String, Float> layers, EditorWorld world) {
		initComponents();

		m_world = world;
		m_layersModel = new SortedListModel<>();

		for(Map.Entry<String, Float> e : layers.entrySet()) {
			m_layersModel.add(new JCheckBoxList.Datum<>(new Layer(e.getKey(), e.getValue()), true));
		}


		browseLayers.setModel(m_layersModel);

		if(!layers.isEmpty()) {
			browseLayers.setSelectedIndex(0);
			lastSelectedLayer = 0;
		}
	}

	public void poll()
	{
		synchronized(m_sync)
		{
			int index = browseLayers.getSelectedIndex();

			if(index < 0)
				return;

			if(index != lastSelectedLayer) {
				lastSelectedLayer = index;
				Layer l = m_layersModel.getElementAt(index).info;
				if (l.name.compareTo(NAME_DISABLED) == 0)
					return;

				Vector3F location = m_world.getEditCursor().getLocation();

				if (Math.abs(location.z - l.depth) > Vector2F.TOLERANCE) {
					location.z = l.depth;
					m_world.getEditCursor().setLocation(location);
				}
			} else {
				float curIndex = m_world.getEditCursor().getLocation().z;

				Layer closest_layer = null;
				float closest_distance = 0;
				int closest_index = 0;
				for(int i = 0; i < browseLayers.getModel().getSize(); i++) {
					JCheckBoxList.Datum<Layer> d = browseLayers.getModel().getElementAt(i);
					float distance = Math.abs(curIndex - d.info.depth);

					if(closest_layer == null || closest_distance > distance) {
						closest_distance = distance;
						closest_layer = d.info;
						closest_index= i;
					}
				}

				if(closest_layer != null) {
					Vector3F location = m_world.getEditCursor().getLocation();
					location.z = closest_layer.depth;
					m_world.getEditCursor().setLocation(location);
					lastSelectedLayer = closest_index;
					browseLayers.setSelectedIndex(closest_index);
				}
			}

			m_world.clearHiddenLayers();
			for(int i = 0; i < browseLayers.getModel().getSize(); i++) {
				JCheckBoxList.Datum<Layer> d = browseLayers.getModel().getElementAt(i);
				if(!d.isChecked() && i != browseLayers.getSelectedIndex()) {
					m_world.addHiddenLayer(d.info.depth);
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

        jScrollPane1 = new javax.swing.JScrollPane();
		browseLayers = new JCheckBoxList<Layer>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		browseLayers.setName("browseBrush"); // NOI18N
        jScrollPane1.setViewportView(browseLayers);
		browseLayers.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGap(43, 43, 43)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed

    }//GEN-LAST:event_btnRefreshActionPerformed

    private void lstDirectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lstDirectionActionPerformed
		
    }//GEN-LAST:event_lstDirectionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBoxList<Layer> browseLayers;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

	private final class Layer implements Comparable<Layer> {
		private String name;
		private float depth;

		public Layer(String name, float depth) {
			this.name = name;
			this.depth = depth;
		}

		public float getDepth() {
			return depth;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public int compareTo(Layer o) {
			return depth < o.depth ? -1 : 1;
		}
	}

	class SortedListModel<T> extends AbstractListModel<T> {
		SortedSet<T> model;

		public SortedListModel() {
			model = new TreeSet<T>();
		}

		public int getSize() {
			return model.size();
		}

		public T getElementAt(int index) {
			return (T)model.toArray()[index];
		}

		public void add(T element) {
			if (model.add(element)) {
				fireContentsChanged(this, 0, getSize());
			}
		}
		public void addAll(T elements[]) {
			Collection<T> c = Arrays.asList(elements);
			model.addAll(c);
			fireContentsChanged(this, 0, getSize());
		}

		public void clear() {
			model.clear();
			fireContentsChanged(this, 0, getSize());
		}

		public boolean contains(Object element) {
			return model.contains(element);
		}

		public Object firstElement() {
			return model.first();
		}

		public Iterator iterator() {
			return model.iterator();
		}

		public Object lastElement() {
			return model.last();
		}

		public boolean removeElement(Object element) {
			boolean removed = model.remove(element);
			if (removed) {
				fireContentsChanged(this, 0, getSize());
			}
			return removed;
		}
	}

}
