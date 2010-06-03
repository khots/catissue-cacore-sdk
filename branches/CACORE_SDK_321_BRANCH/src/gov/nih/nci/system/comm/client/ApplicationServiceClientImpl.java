
package gov.nih.nci.system.comm.client;

import gov.nih.nci.common.util.ClientInfo;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.comm.common.ApplicationServiceProxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationServiceClientImpl.
 */
public class ApplicationServiceClientImpl extends ApplicationService
{

	/** The application service proxy. */
	private static ApplicationServiceProxy applicationServiceProxy;

	/** The application service. */
	private static ApplicationService applicationService;

	/** The records count. */
	private static int recordsCount;

	/** The max records count. */
	private static int maxRecordsCount;

	/** The case sensitivity. */
	private static boolean caseSensitivity;

	/** The log. */
	private static Logger log = Logger.getLogger(ApplicationServiceClientImpl.class.getName());

	/**
	 * Default Constructor. Obtains the Remote instance of {@link
	 * ApplicationService} and caches it.
	 */
	public ApplicationServiceClientImpl()
	{
		try
		{
			Properties _properties = new Properties();
			_properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(
					"CORESystem.properties"));
			String rsPerQuery = _properties.getProperty("RECORDSPERQUERY");
			String maxRsPerQuery = _properties.getProperty("MAXRECORDSPERQUERY");

			if (rsPerQuery != null)
			{
				log.info("RECORDSPERQUERY property found : " + rsPerQuery);
				recordsCount = new Integer(rsPerQuery).intValue();
			}
			else
			{
				log.error("RECORDSPERQUERY property not found. Using default");
				recordsCount = Constant.RESULT_COUNT_PER_QUERY;
			}
			if (maxRsPerQuery != null)
			{
				log.info("MAXRECORDSPERQUERY property found : " + maxRsPerQuery);
				maxRecordsCount = new Integer(maxRsPerQuery).intValue();
			}
			else
			{
				log.error("MAXRECORDSPERQUERY property not found. Using default");
				maxRecordsCount = Constant.MAX_RESULT_COUNT_PER_QUERY;
			}
		}
		catch (IOException e)
		{
			log.error("IOException: ", e);
		}
		catch (Exception ex)
		{
			log.error("Exception: ", ex);
		}
	}

	//@Override
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance()
	 */
	protected ApplicationService getBeanInstance()
	{
		applicationServiceProxy = getRemoteServiceFromClassPath();
		applicationService = new ApplicationServiceClientImpl();
		return applicationService;
	}

	//@Override
	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance(java.lang.String)
	 */
	protected ApplicationService getBeanInstance(String URL)
	{
		applicationServiceProxy = getRemoteServiceFromPath(URL);
		ClientSession.getInstance(applicationServiceProxy);
		return new ApplicationServiceClientImpl();
	}

	/**
	 * Gets the remote service from path.
	 *
	 * @param URL the uRL
	 *
	 * @return the remote service from path
	 */
	private static ApplicationServiceProxy getRemoteServiceFromPath(String URL)
	{
		String xmlFileString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\"><beans><bean id=\"remoteService\" class=\"org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean\"><property name=\"serviceUrl\"><value>"
				+ URL
				+ "</value></property><property name=\"serviceInterface\"><value>gov.nih.nci.system.comm.common.ApplicationServiceProxy</value></property> <property name=\"httpInvokerRequestExecutor\"><bean class=\"org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor\"/></property></bean></beans>";
		GenericApplicationContext ctx = new GenericApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		InputStream inputStream = new ByteArrayInputStream(xmlFileString.getBytes());
		InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
		xmlReader.loadBeanDefinitions(inputStreamResource);
		ctx.refresh();
		ApplicationServiceProxy applicationServiceProxy = (ApplicationServiceProxy) ctx
				.getBean(Constant.REMOTE_APPLICATION_SERVICE);
		return applicationServiceProxy;
	}

	/**
	 * Gets the remote service from class path.
	 *
	 * @return the remote service from class path
	 */
	private static ApplicationServiceProxy getRemoteServiceFromClassPath()
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				Constant.REMOTE_SERVICE_FILE_NAME);
		ApplicationServiceProxy applicationServiceProxy = (ApplicationServiceProxy) ctx
				.getBean(Constant.REMOTE_APPLICATION_SERVICE);
		return applicationServiceProxy;
	}

	/**
	 * Gets the client info.
	 *
	 * @return the client info
	 */
	private ClientInfo getClientInfo()
	{
		ClientSession cs = ClientSession.getInstance();
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setUserName(cs.getSessionKey());
		clientInfo.setRecordsCount(ApplicationServiceClientImpl.recordsCount);
		clientInfo.setCaseSensitivity(ApplicationServiceClientImpl.caseSensitivity);
		return clientInfo;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#setRecordsCount(int)
	 */
	//@Override
	public void setRecordsCount(int recordsCount) throws ApplicationException
	{
		if (recordsCount > maxRecordsCount)
			throw new ApplicationException(
					"Illegal Value for RecordsCount: RECORDSPERQUERY cannot be greater than MAXRECORDSPERQUERY. RECORDSPERQUERY = "
							+ recordsCount + " MAXRECORDSPERQUERY = " + maxRecordsCount);
		else
			ApplicationServiceClientImpl.recordsCount = recordsCount;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#setSearchCaseSensitivity(boolean)
	 */
	public void setSearchCaseSensitivity(boolean caseSensitivity)
	{
		ApplicationServiceClientImpl.caseSensitivity = caseSensitivity;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String, java.util.List)
	 */
	public List search(String path, List objList) throws ApplicationException
	{
		return applicationServiceProxy.search(getClientInfo(), path, objList);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String, java.lang.Object)
	 */
	public List search(String path, Object obj) throws ApplicationException
	{
		return applicationServiceProxy.search(getClientInfo(), path, obj);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class, java.util.List)
	 */
	public List search(Class targetClass, List objList) throws ApplicationException
	{
		return applicationServiceProxy.search(getClientInfo(), targetClass, objList);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class, java.lang.Object)
	 */
	public List search(Class targetClass, Object obj) throws ApplicationException
	{
		return applicationServiceProxy.search(getClientInfo(), targetClass, obj);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object, int, int, java.lang.String)
	 */
	public List query(Object criteria, int firstRow, int resultsPerQuery, String targetClassName)
			throws ApplicationException
	{
		return applicationServiceProxy.query(getClientInfo(), criteria, firstRow, resultsPerQuery,
				targetClassName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(org.hibernate.criterion.DetachedCriteria, java.lang.String)
	 */
	public List query(DetachedCriteria detachedCriteria, String targetClassName)
			throws ApplicationException
	{
		return applicationServiceProxy.query(getClientInfo(), detachedCriteria, targetClassName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.common.util.HQLCriteria, java.lang.String)
	 */
	public List query(HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException
	{
		return applicationServiceProxy.query(getClientInfo(), hqlCriteria, targetClassName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.query.cql.CQLQuery, java.lang.String)
	 */
	//	public List query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException
	//	{
	//		return applicationServiceProxy.query(getClientInfo(), cqlQuery, targetClassName);
	//	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getQueryRowCount(java.lang.Object, java.lang.String)
	 */
	public int getQueryRowCount(Object criteria, String targetClassName)
			throws ApplicationException
	{
		return applicationServiceProxy.getQueryRowCount(getClientInfo(), criteria, targetClassName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getObjects(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public List getObjects(Object object) throws ApplicationException
	{
		return applicationServiceProxy.getObjects(getClientInfo(), object);
	}

	/*@WRITABLE_API_END@*/

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#createObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object createObject(Object object) throws ApplicationException
	{
		return applicationServiceProxy.createObject(getClientInfo(), object);
	}

	/*@WRITABLE_API_END@*/

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#updateObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object updateObject(Object object) throws ApplicationException
	{
		return applicationServiceProxy.updateObject(getClientInfo(), object);
	}

	/*@WRITABLE_API_END@*/

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#removeObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public void removeObject(Object object) throws ApplicationException
	{
		applicationServiceProxy.removeObject(getClientInfo(), object);
	}

	/*@WRITABLE_API_END@*/

	/**
	 * Participant lookup API.
	 *
	 * @param domainObject the domain object
	 *
	 * @return the participant matching obects
	 *
	 * @throws ApplicationException the application exception
	 */
	public List getParticipantMatchingObects(Object domainObject) throws ApplicationException
	{
		return applicationServiceProxy.getParticipantMatchingObects(getClientInfo(), domainObject);
	}

	/**
     * Participant lookup API.
     *
     * @param domainObject the domain object
     *
     * @return the participant matching obects
     *
     * @throws ApplicationException the application exception
     */
    public List getParticipantMatchingObects(Object domainObject,Long protocolId) throws ApplicationException
    {
        return applicationServiceProxy.getParticipantMatchingObects(getClientInfo(), domainObject,protocolId);
    }



	/**
	 * Get scg label.
	 *
	 * @param domainObject the domain object
	 *
	 * @return the specimen collection group label
	 *
	 * @throws ApplicationException the application exception
	 */
	public String getSpecimenCollectionGroupLabel(Object domainObject) throws ApplicationException
	{
		return applicationServiceProxy.getSpecimenCollectionGroupLabel(getClientInfo(),
				domainObject);
	}

	/**
	 * Get default value for key.
	 *
	 * @param key the key
	 *
	 * @return the default value
	 *
	 * @throws ApplicationException the application exception
	 */
	public String getDefaultValue(String key) throws ApplicationException
	{
		return applicationServiceProxy.getDefaultValue(getClientInfo(), key);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#registerParticipantToEMPI(java.lang.Object)
	 */
	public void registerParticipantToEMPI(Object object) throws ApplicationException
	{
		applicationServiceProxy.registerParticipantToEMPI(getClientInfo(), object);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#updateParticipantWithEMPIDetails(java.lang.String)
	 */
	public void updateParticipantWithEMPIDetails(String demographicXML) throws ApplicationException
	{
		applicationServiceProxy.updateParticipantWithEMPIDetails(getClientInfo(), demographicXML);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#registerParticipant(java.lang.Object, java.lang.Long, java.lang.String)
	 */
	public Object registerParticipant(Object object, Long cpid, String userName)
			throws ApplicationException
	{
		return applicationServiceProxy.registerParticipant(getClientInfo(), object, cpid, userName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getClinportalUrlIds(java.util.Map)
	 */
	public Object getClinportalUrlIds(Map map) throws ApplicationException
	{
		return applicationServiceProxy.getClinportalUrlIds(getClientInfo(), map);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getCaTissueLocalParticipantMatchingObects(java.lang.Object, java.lang.Long)
	 */
	public List getCaTissueLocalParticipantMatchingObects(Object domainObject,  Set<Long> cpIdSet)
			throws ApplicationException
	{
		// TODO Auto-generated method stub
		return applicationServiceProxy.getCaTissueLocalParticipantMatchingObects(getClientInfo(),
				domainObject, cpIdSet);
	}

	public Object getVisitRelatedEncounteredDate(Map<String, Long> map) throws ApplicationException
    {
        return applicationServiceProxy.getVisitRelatedEncounteredDate(getClientInfo(), map);
    }

}