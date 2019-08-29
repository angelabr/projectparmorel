package hvl.projectparmorel.knowledge;

import java.util.List;
import java.util.Set;

public class HashErrorContextActionDirectory<T extends Comparable<T>> extends ErrorContextActionDirectory<T> {
	private ErrorMap<T> errors;

	public HashErrorContextActionDirectory() {
		super();
		errors = new ErrorMap<>();
	}
	
	@Override
	public void setValue(Integer errorCode, Integer contextId, Integer actionId, T value) {
		errors.setValue(errorCode, contextId, actionId, value);
	}

	@Override
	public void setAllValuesTo(T value) {
		errors.setAllValuesTo(value);
	}

	@Override
	public Set<Integer> getAllErrorCodes() {
		return errors.getAllErrorCodes();
	}

	@Override
	public void influenceWeightsByPreferedScores(ErrorContextActionDirectory<Action> preferenceScores,
			List<Integer> preferences) {
		errors.influenceWeightsByPreferedScores(preferenceScores.getErrorMap(), preferences);
	}
	
	@Override
	protected ErrorMap<T> getErrorMap() {
		return errors;
	}

	@Override
	protected ActionLocation getOptimalActionIndexForErrorCode(Integer errorCode) {
		return errors.getOptimalActionIndexForErrorCode(errorCode);
	}

	@Override
	protected T getValue(Integer errorCode, Integer contextId, Integer actionId) {
		return errors.getValue(errorCode, contextId, actionId);
	}

	@Override
	protected T getRandomValueForError(int errorCode) {
		return errors.getRandomActionInRandomContextForError(errorCode);
	}

	@Override
	protected boolean containsValueForErrorAndContext(int errorCode, int contextId, int actionId) {
		return errors.containsValueForErrorCodeAndContextId(errorCode, contextId, actionId);
	}

}