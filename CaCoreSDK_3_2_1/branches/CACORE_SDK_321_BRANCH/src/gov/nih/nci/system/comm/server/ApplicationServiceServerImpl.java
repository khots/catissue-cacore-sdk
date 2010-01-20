
package gov.nih.nci.system.comm.server;

import gov.nih.nci.common.util.ClientInfo;
import gov.nih.nci.common.util.ClientInfoThreadVariable;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.common.util.SecurityConfiguration;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.comm.common.ApplicationServiceProxy;
import gov.nih.nci.system.server.mgmt.SecurityEnabler;
import gov.nih.nci.system.server.mgmt.SessionManager;
import gov.nih.nci.system.server.mgmt.UserSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationServiceServerImpl.
 */
public class ApplicationServiceServerImpl implements ApplicationServiceProxy
{

	/** The application service. */
	private ApplicationService applicationService;

	/** The security enabler. */
	private SecurityEnabler securityEnabler;

	/**
	 * Default Constructor it takes in.
	 */
	public ApplicationServiceServerImpl()
	{
		securityEnabler = new SecurityEnabler(SecurityConfiguration.getApplicationName());
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				Constant.APPLICATION_SERVICE_FILE_NAME);
		applicationService = (ApplicationService) ctx.getBean(Constant.APPLICATION_SERVICE);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#authenticate(java.lang.String, java.lang.String)
	 */
	public String authenticate(String userId, String password) throws ApplicationException
	{
		return securityEnabler.authenticate(userId, password);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#logOut(java.lang.String)
	 */
	public void logOut(String sessionKey)
	{
		securityEnabler.logOut(sessionKey);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#setSearchCaseSensitivity(java.lang.String, boolean)
	 */
	public void setSearchCaseSensitivity(ClientInfo clientInfo)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#setSearchCaseSensitivity(java.lang.String, boolean)
	 */
	public void setRecordsCount(ClientInfo clientInfo)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.String, java.util.List)
	 */
	public List search(ClientInfo clientInfo, String path, List objList)
			throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		/*return applicationService.search(path, objList);*/
		List list = applicationService.search(path, objList);
		return callSearchFilter(list, clientInfo);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.String, java.lang.Object)
	 */
	public List search(ClientInfo clientInfo, String path, Object obj) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		//return applicationService.search(path, obj);
		List list = applicationService.search(path, obj);
		return callSearchFilter(list, clientInfo);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.Class, java.util.List)
	 */
	public List search(ClientInfo clientInfo, Class targetClass, List objList)
			throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		List list = applicationService.search(targetClass, objList);
		return callSearchFilter(list, clientInfo);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#search(gov.nih.nci.common.util.ClientInfo, java.lang.Class, java.lang.Object)
	 */
	public List search(ClientInfo clientInfo, Class targetClass, Object obj)
			throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		//return applicationService.search(targetClass, obj);
		List list = applicationService.search(targetClass, obj);
		return callSearchFilter(list, clientInfo);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#query(gov.nih.nci.common.util.ClientInfo, java.lang.Object, int, int, java.lang.String)
	 */
	public List query(ClientInfo clientInfo, Object criteria, int firstRow, int resultsPerQuery,
			String targetClassName) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);

		List list = applicationService.query(criteria, firstRow, resultsPerQuery, targetClassName);

		/*	if (securityEnabler.getSecurityLevel() > 0)
			{
				if (list.size() != 0)
					targetClassName.concat("," + list.get(0).getClass().getName());
				StringTokenizer tokenPath = new StringTokenizer(targetClassName, ",");
				while (tokenPath.hasMoreTokens())
				{
					String domainObjectName =  tokenPath.nextToken().trim();
					if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
						throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
				}
			} */

		return callSearchFilter(list, clientInfo);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#query(gov.nih.nci.common.util.ClientInfo, org.hibernate.criterion.DetachedCriteria, java.lang.String)
	 */
	public List query(ClientInfo clientInfo, DetachedCriteria detachedCriteria,
			String targetClassName) throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);

		List list = applicationService.query(detachedCriteria, targetClassName);

		/*	if (securityEnabler.getSecurityLevel() > 0)
			{
				if (list.size() != 0)
					targetClassName.concat("," + list.get(0).getClass().getName());
				StringTokenizer tokenPath = new StringTokenizer(targetClassName, ",");
				while (tokenPath.hasMoreTokens())
				{
					String domainObjectName =  tokenPath.nextToken().trim();
					if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
						throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
				}
			}*/

		return callSearchFilter(list, clientInfo);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#query(gov.nih.nci.common.util.ClientInfo, gov.nih.nci.common.util.HQLCriteria, java.lang.String)
	 */
	public List query(ClientInfo clientInfo, HQLCriteria hqlCriteria, String targetClassName)
			throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);

		List list = applicationService.query(hqlCriteria, targetClassName);

		/*	if (securityEnabler.getSecurityLevel() > 0)
			{
				if (list.size() != 0)
					targetClassName.concat("," + list.get(0).getClass().getName());
				StringTokenizer tokenPath = new StringTokenizer(targetClassName, ",");
				while (tokenPath.hasMoreTokens())
				{
					String domainObjectName =  tokenPath.nextToken().trim();
					if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(),domainObjectName, "READ"))
						throw new ApplicationException("User does not have privilege to perform a READ on " + domainObjectName+ " object");
				}
			}*/

		return callSearchFilter(list, clientInfo);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#getQueryRowCount(gov.nih.nci.common.util.ClientInfo, java.lang.Object, java.lang.String)
	 */
	public int getQueryRowCount(ClientInfo clientInfo, Object criteria, String targetClassName)
			throws ApplicationException
	{
		ClientInfoThreadVariable.setClientInfo(clientInfo);
		return applicationService.getQueryRowCount(criteria, targetClassName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#createObject(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */
	public Object createObject(ClientInfo clientInfo, Object domainobject)
			throws ApplicationException
	{

		// method name for add
		String methodName = "delegateAdd";
		// calls the caTissue delegator
		return callDelegator(methodName, clientInfo, domainobject);
		/*
		 * Since we are checking authorization & priviledges in caTissue core application,it is not required.
		 */
		/*
		if (securityEnabler.getSecurityLevel() > 0)
		{

			String domainObjectName = domainobject.getClass().getName();
			try
			{
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "CREATE"))
				{
					throw new ApplicationException("User does not have privilege to CREATE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		return applicationService.createObject(domainobject);
		*/

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#updateObject(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */
	// NOTE: Use only "//" for comments in the following method
	public Object updateObject(ClientInfo clientInfo, Object domainobject)
			throws ApplicationException
	{

		//method name for edit

		String methodName = "delegateEdit";
		// calls the caTissue delegator
		return callDelegator(methodName, clientInfo, domainobject);
		/*
		 * Since we are checking authorization & priviledges in caTissue core application,it is not required.
		 */
		/*
		if (securityEnabler.getSecurityLevel() > 0)
		{
			try
			{
				String domainObjectName = domainobject.getClass().getName();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "UPDATE"))
				{
					throw new ApplicationException("User does not have privilege to CREATE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		return applicationService.updateObject(domainobject);
		*/

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#removeObject(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */

	// NOTE: Use only "//" for comments in the following method
	public void removeObject(ClientInfo clientInfo, Object domainobject)
			throws ApplicationException
	{

		// method name for delete
		String methodName = "delegateDelete";
		// calls the caTissue delegator
		callDelegator(methodName, clientInfo, domainobject);
		/*
		 * Since we are checking authorization & priviledges in caTissue core application,it is not required.
		 */
		/*
		if (securityEnabler.getSecurityLevel() > 0)
		{
			try
			{
				String domainObjectName = domainobject.getClass().getName();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "DELETE"))
				{
					throw new ApplicationException("User does not have privilege to DELETE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		applicationService.removeObject(domainobject);
		*/

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#getObjects(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */

	// NOTE: Use only "//" for comments in the following method
	public List getObjects(ClientInfo clientInfo, Object domainobject) throws ApplicationException
	{

		/*
		if (securityEnabler.getSecurityLevel() > 0)
		{
			try
			{
				String domainObjectName = domainobject.getClass().getName();
				if (!securityEnabler.hasAuthorization(clientInfo.getSessionKey(), domainObjectName, "READ"))
				{
					throw new ApplicationException("User does not have privilege to CREATE " + domainObjectName + " object");
				}
			}
			catch (ApplicationException e)
			{
				throw new ApplicationException(e.getMessage());
			}
		}

		return applicationService.getObjects(domainobject);
		*/

		// method name for delete

		String methodName = "delegateGetObjects";
		// calls the caTissue delegator
		List list = (List) callDelegator(methodName, clientInfo, domainobject);
		return list;
	}

	/**
	 * Participant lookup API.
	 *
	 * @param clientInfo the client info
	 * @param domainobject the domainobject
	 *
	 * @return the participant matching obects
	 *
	 * @throws ApplicationException the application exception
	 */
	public List getParticipantMatchingObects(ClientInfo clientInfo, Object domainobject)
			throws ApplicationException
	{
		/*
		 * method name for to retrieve matching participant
		 */
		String methodName = "delegateGetParticipantMatchingObects";
		// calls the caTissue delegator
		List list = (List) callDelegator(methodName, clientInfo, domainobject);
		return list;
	}

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
	public String getSpecimenCollectionGroupLabel(ClientInfo clientInfo, Object domainobject)
			throws ApplicationException
	{
		/*
		 * method name for get next SCG id
		 */
		String methodName = "delegateGetSpecimenCollectionGroupLabel";
		// calls the caTissue delegator
		String label = (String) callDelegator(methodName, clientInfo, domainobject);
		return label;
	}

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
	public String getDefaultValue(ClientInfo clientInfo, String key) throws ApplicationException
	{
		/*
		 * method name for get next SCG id
		 */
		String methodName = "delegateGetDefaultValue";
		// calls the caTissue delegator
		String value = (String) callDelegator(methodName, clientInfo, key);
		return value;
	}

	/**
	 * Calls the specified method of caTissue Delegator class which is used to delegate call to actual biz logic.
	 *
	 * @param methodName method name of catissue delegator class
	 * @param clientInfo client info object
	 * @param domainObject domain object
	 *
	 * @return domain object
	 *
	 * @throws ApplicationException - throw application exception with message
	 *
	 * @see ApplicationException
	 */
	private Object callDelegator(String methodName, ClientInfo clientInfo, Object domainObject)
			throws ApplicationException
	{
		// specify the className of caTissue core delgator class
		final String DELEGATOR_CLASS = "edu.wustl.catissuecore.client.CaCoreAppServicesDelegator";
		String userId = getUserId(clientInfo);
		try
		{
			Class delegator = Class.forName(DELEGATOR_CLASS);
			Object obj = delegator.newInstance();
			Method method = getMethod(delegator, methodName);
			Object[] args = {userId, domainObject};
			domainObject = method.invoke(obj, args);
		}
		catch (ClassNotFoundException e)
		{
			throw handleException(e);
		}
		catch (IllegalAccessException e)
		{
			throw handleException(e);
		}

		catch (InvocationTargetException e)
		{
			throw handleException(e);
		}
		catch (InstantiationException e)
		{
			throw handleException(e);
		}

		return domainObject;
	}

	/**
	 * Gets the user id from session manager.Here User Id will be like user name e.g. admin@admin.com
	 *
	 * @param clientInfo Client Info
	 *
	 * @return user id
	 *
	 * @throws ApplicationException - throws application exception
	 */
	private String getUserId(ClientInfo clientInfo) throws ApplicationException
	{
		SessionManager sessionManager = SessionManager.getInstance();
		UserSession userSession = (UserSession) sessionManager.getSession(clientInfo.getUserName());
		if (userSession == null)
		{
			throw new ApplicationException("User is not in session !");
		}
		return userSession.getUserId();
	}

	/**
	 * Handles exception & returns wrapped application exception from specified exception.
	 *
	 * @param throwable throwable
	 *
	 * @return application exception object wrapped with message.
	 *
	 * @see ApplicationException
	 */
	private ApplicationException handleException(Throwable throwable)
	{
		throwable.printStackTrace();
		String message = throwable.getMessage();

		if (message == null)
		{
			message = throwable.getCause().getMessage();
		}
		return new ApplicationException(message);
	}

	/**
	 * Gets the specified method from a Class's methods.
	 *
	 * @param objClass object class
	 * @param methodName method name
	 *
	 * @return method
	 */
	private Method getMethod(Class objClass, String methodName)
	{
		Method method[] = objClass.getMethods();
		for (int i = 0; i < method.length; i++)
		{
			if (method[i].getName().equals(methodName))
				return method[i];
		}
		return null;
	}

	/**
	 * Call search filter on caTissuecore delegator.Call <code>delegateSearchFilter</code> of caTissueCore delegator.
	 *
	 * @param list the list
	 * @param clientInfo the client info
	 *
	 * @return list of resulted domain objects
	 *
	 * @throws ApplicationException the application exception
	 */
	private List callSearchFilter(List list, ClientInfo clientInfo) throws ApplicationException
	{
		String methodName = "delegateSearchFilter";
		list = (List) callDelegator(methodName, clientInfo, list);
		return list;
	}

	/**
	 * TThis will register particiant with empi.
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
	public Object registerParticipant(ClientInfo clientInfo, Object object, Long cpid,
			String userName) throws ApplicationException
	{
		/*
		 * method name for to retrieve matching participant
		 */
		String methodName = "delegateRegisterParticipant";
		// calls the caTissue delegator
		final String DELEGATOR_CLASS = "edu.wustl.catissuecore.client.CaCoreAppServicesDelegator";
		String userId = getUserId(clientInfo);
		System.out.println("userId*******::" + userId);
		try
		{
			Class delegator = Class.forName(DELEGATOR_CLASS);
			Object obj = delegator.newInstance();
			Method method = getMethod(delegator, methodName);
			Object[] args = {object, cpid, userId};
			object = method.invoke(obj, args);
		}
		catch (ClassNotFoundException e)
		{
			throw handleException(e);
		}
		catch (IllegalAccessException e)
		{
			throw handleException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw handleException(e);
		}
		catch (InstantiationException e)
		{
			throw handleException(e);
		}
		catch (InvocationTargetException e)
		{
			throw handleException(e);
		}
		return object;
	}

	/* Regestring clinportal patient to EMPI */
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#registerParticipantToEMPI(gov.nih.nci.common.util.ClientInfo, java.lang.Object)
	 */
	public void registerParticipantToEMPI(ClientInfo clientInfo, Object domainObject)
			throws ApplicationException
	{
		try
		{
			String methodName = "delegateRegisterParticipantToEMPI";
			callDelegator(methodName, clientInfo, domainObject);
		}
		catch (IllegalArgumentException e)
		{
			throw handleException(e);
		}
	}

	/* Updating the clinportal participant with eMPI Id */
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.comm.common.ApplicationServiceProxy#updateParticipantWithEMPIDetails(gov.nih.nci.common.util.ClientInfo, java.lang.String)
	 */
	public void updateParticipantWithEMPIDetails(ClientInfo clientInfo, String demographicXML)
			throws ApplicationException
	{
		try
		{
			String methodName = "delegateUpdateParticipantWithEMPIDetails";
			callDelegator(methodName, clientInfo, demographicXML);
		}
		catch (IllegalArgumentException e)
		{
			throw handleException(e);
		}
	}

	/**
	 * This method will return the required ids of clinportal on the basis caTissue objects id.
	 *
	 * @param clientInfo the client info
	 * @param map the map
	 *
	 * @return the clinportal url ids
	 *
	 * @throws ApplicationException the application exception
	 */
	public Object getClinportalUrlIds(ClientInfo clientInfo, Map<String, Long> map)
			throws ApplicationException
	{
		String methodName = "getClinportalUrlIds";
		return callDelegator(methodName, clientInfo, map);
	}

	/**
	 * Call delegator forca tissue local participant match.
	 *
	 * @param methodName the method name
	 * @param clientInfo the client info
	 * @param domainObject the domain object
	 * @param cpId the cp id
	 *
	 * @return the object
	 *
	 * @throws ApplicationException the application exception
	 */
	private Object callDelegatorForcaTissueLocalParticipantMatch(String methodName,
			ClientInfo clientInfo, Object domainObject, Long cpId) throws ApplicationException
	{
		// specify the className of caTissue core delgator class
		final String DELEGATOR_CLASS = "edu.wustl.catissuecore.client.CaCoreAppServicesDelegator";
		String userId = getUserId(clientInfo);
		try
		{
			Class delegator = Class.forName(DELEGATOR_CLASS);
			Object obj = delegator.newInstance();
			Method method = getMethod(delegator, methodName);
			Object[] args = {userId, domainObject, cpId};
			domainObject = method.invoke(obj, args);
		}
		catch (ClassNotFoundException e)
		{
			throw handleException(e);
		}
		catch (IllegalAccessException e)
		{
			throw handleException(e);
		}

		catch (InvocationTargetException e)
		{
			throw handleException(e);
		}
		catch (InstantiationException e)
		{
			throw handleException(e);
		}

		return domainObject;
	}

	/**
	 *  fetch the caTissue local matched partiicpants.
	 */
	public List getCaTissueLocalParticipantMatchingObects(ClientInfo clientInfo,
			Object domainobject) throws ApplicationException
	{
		/*
		 * method name for to retrieve matching participant
		 */
		String methodName = "delegateGetCaTissueLocalParticipantMatchingObects";
		// calls the caTissue delegator
		List list = (List) callDelegator(methodName, clientInfo,
				domainobject);
		return list;
	}

    public Object getVisitRelatedEncounteredDate(ClientInfo clientInfo,Map<String, Long> map) throws ApplicationException
    {
        String methodName = "getVisitRelatedEncounteredDate";
        return callDelegator(methodName, clientInfo, map);
    }

}