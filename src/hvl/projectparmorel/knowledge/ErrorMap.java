package hvl.projectparmorel.knowledge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

class ErrorMap<T extends Comparable<T> & Savable> {
	private final String XML_NODE_NAME = "error";
	private final String XML_CODE_NAME = "code";
	/**
	 * A map containing the context for the given error codes.
	 */
	private Map<Integer, ContextMap<T>> contexts;
	private Logger logger = Logger.getGlobal();

	protected ErrorMap() {
		contexts = new HashMap<>();
	}
	
	/**
	 * Clears all the values, setting them to the provided value.
	 * 
	 * @param value to set
	 */
	protected void setAllValuesTo(T value) {
		for (ContextMap<T> context : contexts.values()) {
			context.setAllValuesTo(value);
		}
	}

	/**
	 * Gets all the error codes
	 * 
	 * @return a Set containing all the error codes.
	 */
	protected Set<Integer> getAllErrorCodes() {
		return contexts.keySet();
	}

	/**
	 * Influence the weight of the scores by the once stored in prefereneScores if
	 * the preference is in preferences.
	 * 
	 * @param preferenceScores, the ErrorMap that should influence the QTable
	 * @param preferences, the preferences to be affected. Only preferences listed
	 *        hwere will be affected.
	 */
	protected void influenceWeightsByPreferedScores(ErrorMap<Action> preferenceScores, List<Integer> preferences) {
		for (Integer errorCode : getAllErrorCodes()) {
			ContextMap<T> context = contexts.get(errorCode);
			context.influenceWeightsByPreferedScores(preferenceScores.getContextMapForErrorCode(errorCode),
					preferences);
		}
	}

	/**
	 * Returns the ContextMap for the given errorCode
	 * 
	 * @param errorCode
	 * @return the corresponding context map.
	 */
	private ContextMap<T> getContextMapForErrorCode(Integer errorCode) {
		return contexts.get(errorCode);
	}

	/**
	 * Checks that the provided error code is stored in the ErrorMap.
	 * 
	 * @param errorCode to check
	 * @return true if the errorCode is in the ErrorMap, false otherwise.
	 */
	protected boolean containsErrorCode(Integer errorCode) {
		return contexts.containsKey(errorCode);
	}
	
	/**
	 * Checks that the provided action id is stored for the given error and context.
	 * 
	 * @param errorCode
	 * @param contextId
	 * @param actionId
	 * @return true if the action ID if found for the specified errorCode and contextId, false otherwise.
	 */
	protected boolean containsValueForErrorCodeAndContextId(int errorCode, int contextId, int actionId) {
		if(contexts.containsKey(errorCode)) {
			return contexts.get(errorCode).containsValueForContext(contextId, actionId);
		} else {
			return false;
		}
		
	}

	/**
	 * Gets the optimal context and action ID to handle the specified error.
	 * 
	 * @param errorCode
	 * @return the location of highest value in the context map. If two are equal, one of them is returned. If the set is empty, null is returned.
	 */
	protected T getOptimalActionForErrorCode(Integer errorCode) {
		ContextMap<T> contextForErrorCode = contexts.get(errorCode);
		return contextForErrorCode.getOptimalAction();
	}

	/**
	 * Gets the number of contexts that exists for a specified error.
	 * 
	 * @param errorCode
	 * @return the number of contexts for the error code
	 */
	protected int getNumberOfContextsForError(int errorCode) {
		return contexts.get(errorCode).getNumberOfContexts();
	}

	/**
	 * Gets a random context.
	 * 
	 * @param errorCode 
	 * @return a random context
	 */
	protected T getRandomActionInRandomContextForError(int errorCode) {
		ContextMap<T> contextsForError = contexts.get(errorCode);
		return contextsForError.getRandomValueInRandomContext();
	}
	
	/**
	 * Gets the value for the specified error code, context id and action id.
	 * 
	 * @param errorCode
	 * @param contextId
	 * @param actionId
	 * @return the corresponding value
	 */
	protected T getValue(Integer errorCode, Integer contextId, Integer actionId) {
		return contexts.get(errorCode).getValue(contextId, actionId);
	}
	
	/**
	 * Sets the value for the specified action in the specified context for the
	 * specified error. If the error, context or action is not in the hierarchy,
	 * they will be added.
	 * 
	 * @param errorCode
	 * @param contextId
	 * @param actionId
	 * @param value
	 */
	protected void setValue(Integer errorCode, Integer contextId, Integer actionId, T value) {
		if (contexts.containsKey(errorCode)) {
			contexts.get(errorCode).setValue(contextId, actionId, value);
		} else {
			contexts.put(errorCode, new ContextMap<T>(contextId, actionId, value));
		}
	}

	/**
	 * Saves content to the document under the root element
	 * 
	 * @param document 
	 * @param root
	 */
	protected void saveTo(Document document, Element root) {
		for(Integer key : contexts.keySet()) {
            Element error = document.createElement(XML_NODE_NAME);
            
            Attr errorCode = document.createAttribute(XML_CODE_NAME);
            errorCode.setValue("" + key);
            error.setAttributeNode(errorCode);
            
            contexts.get(key).saveTo(document, error);
            root.appendChild(error);
		}	
	}

	/**
	 * Loads the content from the specified document.
	 * 
	 * @param document
	 */
	protected void loadFrom(Document document) {
		NodeList errorList = document.getElementsByTagName(XML_NODE_NAME);
		for (int i = 0; i < errorList.getLength(); i++) {
			Node error = errorList.item(i);
			if(error.getNodeType() == Node.ELEMENT_NODE) {
				Element errorElement = (Element) error;
				Integer errorCode = Integer.parseInt(errorElement.getAttribute(XML_CODE_NAME));
				ContextMap<T> contextMap = new ContextMap<>(errorElement);
				contexts.put(errorCode, contextMap);
			} else {
				logger.warning("The node " + error.getNodeName() + " is not correctly formated.");
			}
		}

	}
}
