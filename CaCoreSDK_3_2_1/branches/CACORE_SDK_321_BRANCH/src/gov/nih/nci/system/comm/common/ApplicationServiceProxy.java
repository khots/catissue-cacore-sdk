package gov.nih.nci.system.comm.common;

import gov.nih.nci.common.util.ClientInfo;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

// TODO: Auto-generated Javadoc
/**
 * The Interface ApplicationServiceProxy.
 */
public interface ApplicationServiceProxy
{

	/**
	 * Authenticate.
	 *
	 * @param userId the user id
	 * @param password the password
	 *
	 * @return the string
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract String authenticate(String userId, String password) throws ApplicationException;

	/**
	 * Log out.
	 *
	 * @param sessionKey the session key
	 */
	public abstract void logOut(String sessionKey);

	/**
	 * Sets the search case sensitivity.
	 *
	 * @param clientInfo the new search case sensitivity
	 */
	public abstract void setSearchCaseSensitivity(ClientInfo clientInfo);

	/**
	 * Gets the query row count.
	 *
	 * @param clientInfo the client info
	 * @param criteria the criteria
	 * @param targetClassName the target class name
	 *
	 * @return the query row count
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract int getQueryRowCount(ClientInfo clientInfo, Object criteria, String targetClassName) throws ApplicationException;

	/**
	 * Sets the records count.
	 *
	 * @param clientInfo the new records count
	 */
	public abstract void setRecordsCount (ClientInfo clientInfo);

	/**
	 * Search.
	 *
	 * @param clientInfo the client info
	 * @param path the path
	 * @param objList the obj list
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List search(ClientInfo clientInfo, String path, List objList) throws ApplicationException;

	/**
	 * Search.
	 *
	 * @param clientInfo the client info
	 * @param path the path
	 * @param obj the obj
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List search(ClientInfo clientInfo, String path, Object obj) throws ApplicationException;

	/**
	 * Search.
	 *
	 * @param clientInfo the client info
	 * @param targetClass the target class
	 * @param objList the obj list
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List search(ClientInfo clientInfo, Class targetClass, List objList) throws ApplicationException;

	/**
	 * Search.
	 *
	 * @param clientInfo the client info
	 * @param targetClass the target class
	 * @param obj the obj
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List search(ClientInfo clientInfo, Class targetClass, Object obj) throws ApplicationException;

	/**
	 * Query.
	 *
	 * @param clientInfo the client info
	 * @param criteria the criteria
	 * @param firstRow the first row
	 * @param resultsPerQuery the results per query
	 * @param targetClassName the target class name
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List query(ClientInfo clientInfo, Object criteria, int firstRow, int resultsPerQuery, String targetClassName) throws ApplicationException;

	/**
	 * Query.
	 *
	 * @param clientInfo the client info
	 * @param detachedCriteria the detached criteria
	 * @param targetClassName the target class name
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List query(ClientInfo clientInfo, DetachedCriteria detachedCriteria, String targetClassName) throws ApplicationException;

	/**
	 * Query.
	 *
	 * @param clientInfo the client info
	 * @param hqlCriteria the hql criteria
	 * @param targetClassName the target class name
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List query(ClientInfo clientInfo, HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException;

	/*@WRITABLE_API_START@*/
	/**
	 * Creates the object.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @return the object
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract Object createObject(ClientInfo clientInfo, Object domainobject) throws ApplicationException;
	/*@WRITABLE_API_END@*/

	/*@WRITABLE_API_START@*/
	/**
	 * Update object.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @return the object
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract Object updateObject(ClientInfo clientInfo, Object domainobject) throws ApplicationException;
	/*@WRITABLE_API_END@*/

	/*@WRITABLE_API_START@*/
	/**
	 * Removes the object.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract void removeObject(ClientInfo clientInfo, Object domainobject) throws ApplicationException;
	/*@WRITABLE_API_END@*/

	/*@WRITABLE_API_START@*/
	/**
	 * Gets the objects.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @return the objects
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List getObjects(ClientInfo clientInfo, Object domainobject) throws ApplicationException;
	/*@WRITABLE_API_END@*/
	/**
	 * PArticipant lookup API.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @return the participant matching obects
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List getParticipantMatchingObects(ClientInfo clientInfo, Object domainobject) throws ApplicationException;

	/**
	 * Get scg label.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @return the specimen collection group label
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract String getSpecimenCollectionGroupLabel(ClientInfo clientInfo, Object domainobject) throws ApplicationException;

	/**
	 * Get default value for key.
	 *
	 * @param clientInfo the client info
	 * @param key the key
	 *
	 * @return the default value
	 *
	 * @throws ApplicationException the application exception
	 */
	public String getDefaultValue(ClientInfo clientInfo, String key) throws ApplicationException;

	/**
	 * Register participant to empi.
	 *
	 * @param clientInfo the client info
	 * @param object the object
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract void registerParticipantToEMPI(ClientInfo clientInfo,Object object) throws ApplicationException;

	/**
	 * Update participant with empi details.
	 *
	 * @param clientInfo the client info
	 * @param demographicXML the demographic xml
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract void updateParticipantWithEMPIDetails(ClientInfo clientInfo,String demographicXML) throws ApplicationException;

    /**
     * Register participant.
     *
     * @param clientInfo the client info
     * @param object the object
     * @param cpid the cpid
     * @param userName the user name
     *
     * @return the object
     *
     * @throws ApplicationException the application exception
     */
    public abstract Object registerParticipant(ClientInfo clientInfo,Object object, Long cpid,String userName) throws ApplicationException;

    /**
     * Gets the clinportal url ids.
     *
     * @param clientInfo the client info
     * @param map the map
     *
     * @return the clinportal url ids
     *
     * @throws ApplicationException the application exception
     */
    public abstract Object getClinportalUrlIds(ClientInfo clientInfo,Map<String,Long> map)  throws ApplicationException;

	/**
	 * Gets the ca tissue local participant matching obects.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 * @param cpId the cp id
	 *
	 * @return the ca tissue local participant matching obects
	 *
	 * @throws ApplicationException the application exception
	 */
	public abstract List getCaTissueLocalParticipantMatchingObects(ClientInfo clientInfo, Object domainobject) throws ApplicationException;

	public abstract Object getVisitRelatedEncounteredDate(ClientInfo clientInfo,Map<String,Long> map)  throws ApplicationException;

}