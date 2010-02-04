
package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.common.util.SecurityConfiguration;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.dao.WritableDAO;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.server.mgmt.SecurityEnabler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationServiceImpl.
 *
 * @author Kunal Modi (Ekagra Software Technologies Ltd.)
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ApplicationServiceImpl extends ApplicationService
{

	/** The log. */
	private static Logger log = Logger.getLogger(ApplicationServiceImpl.class.getName());

	/** The application service business impl. */
	private ApplicationServiceBusinessImpl applicationServiceBusinessImpl = null;

	/** The writable dao. */
	private WritableDAO writableDAO = null;

	/** The security enabler. */
	private SecurityEnabler securityEnabler = null;

	/**
	 * Default Constructor. It obtains appropriate implementation of the
	 * {@link ApplicationService}interface and caches it. It also instantiates
	 * the instance of writableDAO and caches it.
	 */
	public ApplicationServiceImpl()
	{
		this.applicationServiceBusinessImpl = ApplicationServiceBusinessImpl.getLocalInstance();
		this.writableDAO = new WritableDAO();
		this.securityEnabler = new SecurityEnabler(SecurityConfiguration.getApplicationName());
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance()
	 */
	//@Override
	protected ApplicationService getBeanInstance()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance(java.lang.String)
	 */
	//@Override
	protected ApplicationService getBeanInstance(String URL)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#setRecordsCount(int)
	 */
	//@Override
	public void setRecordsCount(int recordsCount) throws ApplicationException
	{
		try
		{
			this.applicationServiceBusinessImpl.setRecordsCount(recordsCount);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#setSearchCaseSensitivity(boolean)
	 */
	public void setSearchCaseSensitivity(boolean caseSensitivity)
	{
		this.applicationServiceBusinessImpl.setSearchCaseSensitivity(caseSensitivity);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getQueryRowCount(java.lang.Object,
	 *      java.lang.String)
	 */
	public int getQueryRowCount(Object criteria, String targetClassName)
			throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.getQueryRowCount(criteria, targetClassName);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object,
	 *      java.lang.String)
	 */
	public List query(DetachedCriteria detachedcriteria, String targetClassName)
			throws ApplicationException
	{
		List list = null;
		try
		{
			list = this.applicationServiceBusinessImpl.query(detachedcriteria, targetClassName);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object,
	 *      java.lang.String)
	 */
	public List query(HQLCriteria hqlcriteria, String targetClassName) throws ApplicationException
	{
		List list = null;
		try
		{
			list = this.applicationServiceBusinessImpl.query(hqlcriteria, targetClassName);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.query.cql.CQLQuery, java.lang.String)
	 */
	/**
	 * Query.
	 *
	 * @param cqlQuery the cql query
	 * @param targetClassName the target class name
	 *
	 * @return the list
	 *
	 * @throws ApplicationException the application exception
	 */
	public List query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException
	{
		List list = null;
		try
		{
			list = applicationServiceBusinessImpl.query(cqlQuery, targetClassName);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object,
	 *      int, int, java.lang.String)
	 */
	public List query(Object criteria, int firstRow, int resultsPerQuery, String targetClassName)
			throws ApplicationException
	{
		List list = null;
		try
		{
			list = this.applicationServiceBusinessImpl.query(criteria, firstRow, resultsPerQuery,
					targetClassName);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class,
	 *      java.lang.Object)
	 */
	public List search(Class targetClass, Object obj) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(targetClass, obj);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class,
	 *      java.util.List)
	 */
	public List search(Class targetClass, List objList) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(targetClass, objList);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String,
	 *      java.lang.Object)
	 */
	public List search(String path, Object obj) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(path, obj);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String,
	 *      java.util.List)
	 */
	public List search(String path, List objList) throws ApplicationException
	{
		try
		{
			return this.applicationServiceBusinessImpl.search(path, objList);
		}
		catch (Exception e)
		{
			log.error("Exception: ", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#createObject(java.lang.Object)
	 */
	// NOTE: Use only "//" for comments in the following method
	public Object createObject(Object domainobject) throws ApplicationException
	{
		return writableDAO.createObject(domainobject);
	}

	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#updateObject(java.lang.Object)
	 */
	// NOTE: Use only "//" for comments in the following method
	public Object updateObject(Object domainobject) throws ApplicationException
	{
		return writableDAO.updateObject(domainobject);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#removeObject(java.lang.Object)
	 */
	// NOTE: Use only "//" for comments in the following method
	public void removeObject(Object domainobject) throws ApplicationException
	{
		writableDAO.removeObject(domainobject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getObjects(java.lang.Object)
	 */
	// NOTE: Use only "//" for comments in the following method
	public List getObjects(Object domainobject) throws ApplicationException
	{
		return writableDAO.getObjects(domainobject);
	}

	/*@WRITABLE_API_END@*/
	/**
	 * Participant look up API.
	 *
	 * @param object the object
	 *
	 * @return the participant matching obects
	 *
	 * @throws ApplicationException the application exception
	 */
	public List getParticipantMatchingObects(Object object) throws ApplicationException
	{

		return null;
	}

	/**
	 * Get scg label.
	 *
	 * @param object the object
	 *
	 * @return the specimen collection group label
	 *
	 * @throws ApplicationException the application exception
	 */
	public String getSpecimenCollectionGroupLabel(Object object) throws ApplicationException
	{
		return null;
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
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#registerParticipantToEMPI(java.lang.Object)
	 */
	public void registerParticipantToEMPI(Object object) throws ApplicationException
	{

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#updateParticipantWithEMPIDetails(java.lang.String)
	 */
	public void updateParticipantWithEMPIDetails(String demographicXML) throws ApplicationException
	{

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#registerParticipant(java.lang.Object, java.lang.Long, java.lang.String)
	 */
	public Object registerParticipant(Object object, Long cpid, String userName)
			throws ApplicationException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getClinportalUrlIds(java.util.Map)
	 */
	public Object getClinportalUrlIds(Map map) throws ApplicationException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getCaTissueLocalParticipantMatchingObects(java.lang.Object, java.lang.Long)
	 */
	public List getCaTissueLocalParticipantMatchingObects(Object object, Long cpId)
			throws ApplicationException
	{
		// TODO Auto-generated method stub
		return null;
	}

    public  Object getVisitRelatedEncounteredDate(Map<String,Long> map)  throws ApplicationException
    {
        return null;
    }

}
