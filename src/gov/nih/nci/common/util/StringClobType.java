package gov.nih.nci.common.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.sql.CLOB;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.jboss.resource.adapter.jdbc.WrappedConnection;

/**
 * <!-- LICENSE_TEXT_START -->
* Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by the SAIC and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
*/
/**
 * @author cabio-team
 */
/**
 * StringClobType serializes an instances of a Clob to a String. 
 * 
 */
public class StringClobType implements UserType
{
	 /** Name of the oracle driver -- used to support Oracle clobs as a special case */
	 private static final String ORACLE_DRIVER_NAME = "Oracle JDBC driver";
	  
	  /** Version of the oracle driver being supported with clob. */
	  private static final int ORACLE_DRIVER_MAJOR_VERSION = 9;
	  private static final int ORACLE_DRIVER_MINOR_VERSION = 0;
	
	/**
	 * Restructs an object from the cachable representation
	 * @param cached 
	 * @owner
	 */
	public Object assemble(Serializable cached, Object owner){
		return cached;
		}
	/**
	 * Returns a deep copy of the persistent state
	 */
	 public Object deepCopy(Object value)
	    {
	        if (value == null) return null;
	        return new String((String) value);
	    }
	 /**
	  * Transforms an object into it's cacheable representation
	  * @param value
	  */
	public Serializable disassemble (Object value){
		return (Serializable)value;
		}   
	
	/**
	 * Compares two instances of the class mapped by this type of persistance entity
	 * @param x
	 * @param y
	 */
	public boolean equals(Object x, Object y)
    {
        return (x == y)
            || (x != null
                && y != null
                && (x.equals(y)));
    }
	
	/**
	 * Returns a hashcode for the instance
	 * @param x
	 */
	   public int hashCode(Object x){
    	int code = 0;
    	if(x!= null){
    		code =  x.hashCode();	    	
    	}
    	return code;
    	}
    
	/**
	 * Checks if object is mutable
	 */
    public boolean isMutable()
    {
        return false;
    }
	
    /**
     * Retrievs an instance of the mapped class from a JDBC resultset
     * @param rs - resultset
     * @param names
     * @param owner
     * @return Object
     */
	 public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
     throws HibernateException, SQLException
	 {
	     Clob clob = rs.getClob(names[0]);
	     if(clob == null)
	         return null;
	     else     
	         return clob.getSubString(1, (int) clob.length());
		 /*Reader clobReader = rs.getCharacterStream(names[0]);
		if (clobReader == null)
		{
		  return null;
		}
		
		String str = new String();
		BufferedReader bufferedClobReader = new BufferedReader(clobReader);
		try
		{
		  String line = null;
		  while( (line = bufferedClobReader.readLine()) != null )
		  {
			  str += line;
			  str += "\n";
		  }
		  bufferedClobReader.close();
		}
		catch (IOException e)
		{
		  throw new SQLException( e.toString() );
		}

	    return str;*/
	 }

	 
	 /**
	  * Writes an instance of the mapped class to a prepared statement
	  * @param st - PreparedStatement
	  * @param value
	  * @param index
	  */
	   public void nullSafeSet(PreparedStatement st, Object value, int index)
       throws HibernateException, SQLException
	   {		   
//		   st.setClob(index, Hibernate.createClob((String) value));
		   DatabaseMetaData dbMetaData = st.getConnection().getMetaData();
		    if (value==null)
		    {
		      st.setNull(index, sqlTypes()[0]);
		    }
		    else if (ORACLE_DRIVER_NAME.equals( dbMetaData.getDriverName() ))
		    {
		      if ((dbMetaData.getDriverMajorVersion() >= ORACLE_DRIVER_MAJOR_VERSION) &&
		      (dbMetaData.getDriverMinorVersion() >= ORACLE_DRIVER_MINOR_VERSION))
		      {
		    try
		    {
		      // Code compliments of Scott Miller
		      // support oracle clobs without requiring oracle libraries
		      // at compile time
		      // Note this assumes that if you are using the Oracle Driver.
		      // then you have access to the oracle.sql.CLOB class
		                        
		      // First get the oracle clob class
		      Class oracleClobClass = Class.forName("oracle.sql.CLOB");

		      // Get the oracle connection class for checking
		      Class oracleConnectionClass = Class.forName("oracle.jdbc.OracleConnection");
		                        
		      // now get the static factory method
		      Class partypes[] = new Class[3];
		      partypes[0] = Connection.class;
		      partypes[1] = Boolean.TYPE;
		      partypes[2] = Integer.TYPE;                
		      Method createTemporaryMethod = oracleClobClass.getDeclaredMethod( "createTemporary", partypes );                        
		      // now get ready to call the factory method
		      Field durationSessionField = oracleClobClass.getField( "DURATION_SESSION" );
		      Object arglist[] = new Object[3];
		      Connection conn = st.getConnection();

		      // Make sure connection object is right type
		      if (!oracleConnectionClass.isAssignableFrom(conn.getClass()))
		      {
		        if (conn.getClass().getName().startsWith("org.jboss"))
		        {
		          conn = ((WrappedConnection)conn).getUnderlyingConnection();
		        }
		                  
		        if (!oracleConnectionClass.isAssignableFrom(conn.getClass()))
		        {
		          throw new HibernateException("JDBC connection object must be a oracle.jdbc.OracleConnection. " +
		                                       "Connection class is " +
		          conn.getClass().getName());
		        }
		      }


		      arglist[0] = conn;
		      arglist[1] = Boolean.TRUE;
		      arglist[2] = durationSessionField.get(null); //null is valid because of static field
		                        
		      // Create our CLOB
		      Object tempClob = createTemporaryMethod.invoke( null, arglist ); //null is valid because of static method
		                        
		      // get the open method
		      partypes = new Class[1];
		      partypes[0] = Integer.TYPE;
		      Method openMethod = oracleClobClass.getDeclaredMethod( "open", partypes );
		                                                
		      // prepare to call the method
		      Field modeReadWriteField = oracleClobClass.getField( "MODE_READWRITE" );
		      arglist = new Object[1];
		      arglist[0] = modeReadWriteField.get(null); //null is valid because of static field
		                        
		      // call open(CLOB.MODE_READWRITE);
		      openMethod.invoke( tempClob, arglist );
		                        
		      // get the getCharacterOutputStream method
		      Method getCharacterOutputStreamMethod = oracleClobClass.getDeclaredMethod( "getCharacterOutputStream", null );
		                        
		      // call the getCharacterOutpitStream method
		      Writer tempClobWriter = (Writer) getCharacterOutputStreamMethod.invoke( tempClob, null );
		                        
		      // write the string to the clob
		      tempClobWriter.write((String)value); 
		      tempClobWriter.flush();
		      tempClobWriter.close(); 
		                        
		      // get the close method
		      Method closeMethod = oracleClobClass.getDeclaredMethod( "close", null );
		                        
		      // call the close method
		      closeMethod.invoke( tempClob, null );
		                        
		      // add the clob to the statement
		      st.setClob( index, (Clob)tempClob );
		    }
		    catch( ClassNotFoundException e )
		    {
		      // could not find the class with reflection
		      throw new HibernateException("Unable to find a required class.\n" + e.getMessage());
		    }
		    catch( NoSuchMethodException e )
		    {
		      // could not find the metho with reflection
		      throw new HibernateException("Unable to find a required method.\n" + e.getMessage());
		    }
		    catch( NoSuchFieldException e )
		    {
		      // could not find the field with reflection
		      throw new HibernateException("Unable to find a required field.\n" + e.getMessage());
		    }
		    catch( IllegalAccessException e )
		    {
		      throw new HibernateException("Unable to access a required method or field.\n" + e.getMessage());
		    }
		    catch( InvocationTargetException e )
		    {
		      throw new HibernateException(e.getMessage());
		    }
		    catch( IOException e )
		    {
		      throw new HibernateException(e.getMessage()); 
		    }
		      }
		      else
		      {
		    throw new HibernateException("No CLOBS support. Use driver version " + ORACLE_DRIVER_MAJOR_VERSION +
		                                 ", minor " + ORACLE_DRIVER_MINOR_VERSION);
		      }
		    }    
		    else
		    {
		      String str = (String)value;
		      StringReader r = new StringReader(str);
		      st.setCharacterStream(index, r, str.length());
		    }      

	   }
	   /**
	    * Replace the existing value with a new value
	    * @param original
	    * @param target
	    * @param owner
	    * @return
	    */
	   public Object replace(Object original, Object target, Object owner){
	   	target = original;
	   	return target;	   	
	   	}
	   
	   /**
	    * Class returned by nullSafeGet
	    */
	   public Class returnedClass()
	    {
	        return String.class;
	    }
	   
	   /**
	    * Returns the SQL tpe codes for the columns mapped by this type
	    */

	   public int[] sqlTypes()
	    {
	        return new int[] { Types.CLOB };
	    }

}
