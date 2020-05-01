package hvl.projectparmorel.ecore;

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import hvl.projectparmorel.qlearning.ActionExtractor;
import hvl.projectparmorel.qlearning.ErrorExtractor;
import hvl.projectparmorel.qlearning.Model;
import hvl.projectparmorel.qlearning.ModelProcessor;
import hvl.projectparmorel.qlearning.QModelFixer;
import hvl.projectparmorel.qlearning.QSolution;
import hvl.projectparmorel.reward.PreferenceOption;

public class EcoreQModelFixer extends QModelFixer {
	private URI uri;
	private ResourceSet resourceSet;
	
	public EcoreQModelFixer() {
		super();
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		EcorePackage.eINSTANCE.eClass();
	}
	
	public EcoreQModelFixer(List<PreferenceOption> preferences) {
		super(preferences);
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
	}

	@Override
	protected Model initializeModelFromFile() {
		this.uri = URI.createFileURI(originalModel.getAbsolutePath());
		Resource modelResource = getModel(uri);
		
		return new EcoreModel(resourceSet, modelResource, uri);
	}

	private Resource getModel(URI uri) {
		return resourceSet.getResource(uri, true);
	}

	@Override
	protected Model getModel(File model) {
		URI episodeModelUri = URI.createFileURI(model.getAbsolutePath());
		Resource episodeModelResource = getModel(episodeModelUri);
		
		return new EcoreModel(resourceSet, episodeModelResource, uri);
	}

	@Override
	protected void updateRewardCalculator() {
		modelProcessor = new EcoreModelProcessor(knowledge);
	}

	@Override
	protected QSolution initializeSolution() {
		return new EcoreSolution();
	}

	@Override
	protected ActionExtractor initializeActionExtractor() {
		return new EcoreActionExtractor();
	}

	@Override
	protected ErrorExtractor initializeErrorExtractor() {
		return new EcoreErrorExtractor();
	}

	@Override
	protected ModelProcessor initializeModelProcessor() {
		return new EcoreModelProcessor(knowledge);
	}
}
