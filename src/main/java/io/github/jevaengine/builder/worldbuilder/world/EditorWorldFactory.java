package io.github.jevaengine.builder.worldbuilder.world;

import io.github.jevaengine.IEngineThreadPool;
import io.github.jevaengine.audio.IAudioClipFactory;
import io.github.jevaengine.config.IConfigurationFactory;
import io.github.jevaengine.config.ValueSerializationException;
import io.github.jevaengine.config.json.JsonVariable;
import io.github.jevaengine.graphics.IFontFactory;
import io.github.jevaengine.graphics.ISpriteFactory;
import io.github.jevaengine.script.IScriptBuilderFactory;
import io.github.jevaengine.world.DefaultWorldFactory;
import io.github.jevaengine.world.DefaultWorldFactory.WorldConfiguration.EntityImportDeclaration;
import io.github.jevaengine.world.DefaultWorldFactory.WorldConfiguration.SceneArtifactDeclaration;
import io.github.jevaengine.world.entity.IEntity;
import io.github.jevaengine.world.entity.IEntityFactory;
import io.github.jevaengine.world.entity.IEntityFactory.EntityConstructionException;
import io.github.jevaengine.world.physics.IPhysicsWorldFactory;
import io.github.jevaengine.world.scene.model.IAnimationSceneModelFactory;
import io.github.jevaengine.world.scene.model.ISceneModel;
import io.github.jevaengine.world.scene.model.ISceneModelFactory.SceneModelConstructionException;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

public class EditorWorldFactory extends DefaultWorldFactory
{
	private final IFontFactory m_fontFactory;
	
	@Inject
	public EditorWorldFactory(IEngineThreadPool threadPool,
			IEntityFactory entityFactory, IScriptBuilderFactory scriptFactory,
			IConfigurationFactory configurationFactory,
			ISpriteFactory spriteFactory, IAudioClipFactory audioClipFactory,
			IPhysicsWorldFactory physicsWorldFactory,
			IFontFactory fontFactory,
			IAnimationSceneModelFactory animationSceneModelFactory) {
	
		super(threadPool, entityFactory, scriptFactory, configurationFactory,
				spriteFactory, audioClipFactory, physicsWorldFactory, animationSceneModelFactory);
	
		m_fontFactory = fontFactory;
	}

	@Override
	protected IEntity createSceneArtifact(SceneArtifactDeclaration artifactDecl) throws EntityConstructionException
	{
		try
		{
			URI modelUri = new URI(artifactDecl.model);
			
			ISceneModel model = m_animationSceneModelFactory.create(modelUri);
			model.setDirection(artifactDecl.direction);
			return new EditorSceneArtifact(model, modelUri, artifactDecl.direction, artifactDecl.isTraversable).getEntity();
		} catch (SceneModelConstructionException | URISyntaxException e)
		{
			throw new EntityConstructionException("Unnamed Tile", e);
		}
	}
	
	@Override
	protected IEntity createEntity(EntityImportDeclaration entityConfig) throws EntityConstructionException
	{
		try
		{
			EditorEntity e = new EditorEntity(m_fontFactory, m_animationSceneModelFactory, entityConfig.name, entityConfig.type, entityConfig.config == null ? null : new URI(entityConfig.config));
			
			if(entityConfig.auxConfig != null)
			{
				JsonVariable auxConfig = new JsonVariable();
				entityConfig.auxConfig.serialize(auxConfig);
				e.setAuxiliaryConfig(auxConfig);
			}
			
			return e.getEntity();
		} catch (ValueSerializationException | URISyntaxException e)
		{
			throw new EntityConstructionException(entityConfig.name, e);
		}
	}
}