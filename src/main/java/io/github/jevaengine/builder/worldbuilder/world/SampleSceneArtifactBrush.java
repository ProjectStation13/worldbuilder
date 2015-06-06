/* 
 * Copyright (C) 2015 Jeremy Wildsmith.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package io.github.jevaengine.builder.worldbuilder.world;

import io.github.jevaengine.math.Vector3F;
import io.github.jevaengine.world.scene.model.ISceneModel;
import io.github.jevaengine.world.scene.model.NullSceneModel;

public final class SampleSceneArtifactBrush implements IBrushBehaviour
{
	private final ISceneArtifactSampleHandler m_sampleHandler;
	
	public SampleSceneArtifactBrush(ISceneArtifactSampleHandler sampleHandler)
	{
		m_sampleHandler = sampleHandler;
	}
	
	@Override
	public boolean isSizable()
	{
		return false;
	}

	@Override
	public ISceneModel getModel()
	{
		return new NullSceneModel();
	}
	
	@Override
	public void apply(EditorWorld world, Vector3F location)
	{
		EditorSceneArtifact tile = world.getTile(location);
		
		if(tile == null)
			return;
		
		m_sampleHandler.sample(tile);
	}
	
	public interface ISceneArtifactSampleHandler
	{
		void sample(EditorSceneArtifact sample);
	}
}