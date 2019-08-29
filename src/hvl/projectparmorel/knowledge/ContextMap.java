package hvl.projectparmorel.knowledge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class ContextMap<T extends Comparable<T>> {
	/**
	 * A map containing the actions for the given context.
	 */
	private Map<Integer, ActionMap<T>> actions;

	protected ContextMap() {
		actions = new HashMap<>();
	}

	protected ContextMap(Integer contextId, Integer actionId, T value) {
		actions = new HashMap<>();
		actions.put(contextId, new ActionMap<T>(actionId, value));
	}

	/**
	 * Clears all the values, setting them to the provided value.
	 * 
	 * @param value to set
	 */
	protected void setAllValuesTo(T value) {
		for (ActionMap<T> action : actions.values()) {
			action.setAllValuesTo(value);
		}
	}

	/**
	 * Influence the weight of the scores by the once stored in prefereneScores if
	 * the preference is in preferences.
	 * 
	 * @param contextMapForErrorCode
	 * @param preferences
	 */
	protected void influenceWeightsByPreferedScores(ContextMap<Action> contextMapForErrorCode,
			List<Integer> preferences) {
		for (Integer contextId : actions.keySet()) {
			ActionMap<T> actionMapForContext = actions.get(contextId);
			actionMapForContext.influenceWeightsByPreferedScores(
					contextMapForErrorCode.getActionMapForContext(contextId), preferences);
		}
	}

	/**
	 * Gets the action map for the specified context
	 * 
	 * @param contextId, the contextId to get the actionMap for.
	 * @return the corresponding actionMap
	 */
	private ActionMap<T> getActionMapForContext(Integer contextId) {
		return actions.get(contextId);
	}

	/**
	 * Checks that the provided context id is stored in the Context Map.
	 * 
	 * @param contextId
	 * @return true if the context ID exists in the map, false otherwise.
	 */
	protected boolean containsContextId(Integer contextId) {
		return actions.containsKey(contextId);
	}

	/**
	 * Checks that the provided action ID exists for the given context
	 * 
	 * @param contextId
	 * @param actionId
	 * @return true if the action ID is found for the given context, false otherwise.
	 */
	protected boolean containsValueForContext(int contextId, int actionId) {
		if (actions.containsKey(contextId)) {
			return actions.get(contextId).containsValue(actionId);
		} else {
			return false;
		}
	}

	/**
	 * Gets the optimal context and action ID to handle the specified error.
	 * 
	 * @return the location of highest value in the context map. If two are equal,
	 *         one of them is returned. If the set is empty, null is returned.
	 */
	protected ActionLocation getOptimalActionLocation() {
		Set<Integer> contextIdSet = actions.keySet();
		Integer[] contextIds = new Integer[contextIdSet.size()];
		contextIds = contextIdSet.toArray(contextIds);

		if (contextIds.length > 0) {
			Integer optimalContextId = contextIds[0];
			Integer optimalActionId = actions.get(optimalContextId).getHihgestValueKey();
			T optimalAction = actions.get(optimalContextId).getValue(optimalActionId);

			for (int i = 1; i < contextIds.length; i++) {
				Integer optimalActionIdForContext = actions.get(contextIds[i]).getHihgestValueKey();
				T action = actions.get(contextIds[i]).getValue(optimalActionIdForContext);
				if (action.compareTo(optimalAction) > 0) {
					optimalContextId = contextIds[i];
					optimalActionId = optimalActionIdForContext;
					optimalAction = action;
				}
			}

			return new ActionLocation(optimalContextId, optimalActionId);
		}
		return null;
	}

	/**
	 * Gets the number of contexts
	 * 
	 * @return the number of contexts
	 */
	protected int getNumberOfContexts() {
		return actions.keySet().size();
	}

	/**
	 * Returns the number of actions stored in the current context.
	 * 
	 * @param contextId
	 * @return the number of actions stored.
	 */
	protected int getNumberOfActionsInContext(Integer contextId) {
		return actions.keySet().size();
	}

	/**
	 * Gets a random action in a random context
	 * 
	 * @return a random value
	 */
	protected T getRandomValueInRandomContext() {
		Random randomGenerator = new Random();
		Integer[] contextIds = new Integer[actions.keySet().size()];
		contextIds = actions.keySet().toArray(contextIds);
		int randomContextIndex = randomGenerator.nextInt(contextIds.length);
		return actions.get(contextIds[randomContextIndex]).getRandomValue();
	}
	
	/**
	 * Gets the value for the specified error code, context id and action id.
	 * 
	 * @param contextId
	 * @param actionId
	 * @return the corresponding value
	 */
	protected T getValue(Integer contextId, Integer actionId) {
		return actions.get(contextId).getValue(actionId);
	}

	/**
	 * Sets the value for the specified action in the specified context. If the
	 * context or action is not in the hierarchy, they will be added.
	 * 
	 * @param contextId
	 * @param actionId
	 * @param value
	 */
	protected void setValue(Integer contextId, Integer actionId, T value) {
		if (actions.containsKey(contextId)) {
			actions.get(contextId).setValue(actionId, value);
		} else {
			actions.put(contextId, new ActionMap<T>(actionId, value));
		}
	}
}
