package BriCommonAPIUtils.utils;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import org.apache.axis2.context.MessageContext;
import com.softwareag.apigateway.api.model.application.Application;
import com.softwareag.apigateway.runtime.provider.content.type.ByteContent;
import com.softwareag.pg.PGConstants;
import com.softwareag.pg.rest.RestMessageContext;
import com.softwareag.util.IDataMap;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
// --- <<IS-END-IMPORTS>> ---

public final class java

{
	// ---( internal utility methods )---

	final static java _instance = new java();

	static java _newInstance() { return new java(); }

	static java _cast(Object o) { return (java)o; }

	// ---( server methods )---




	public static final void encodeBase64 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(encodeBase64)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputString
		// [o] field:0:required encodedString
		String encodedString ="";  
			// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	inputString = IDataUtil.getString( pipelineCursor, "inputString" );
		if(inputString!=null)
			//encodedString=Base64.encodeToString(inputString.getBytes());
		IDataUtil.put( pipelineCursor, "encodedString", encodedString );
		pipelineCursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void getDataFromContext (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getDataFromContext)>> ---
		// @sigtype java 3.5
		// [i] object:0:required payloadObject
		// [i] object:0:required MessageContext
		// [i] field:0:required appContextPropName
		// [o] field:0:required reqPayloadString
		// [o] field:0:required consumerAppId
		// initialize the pipeline map
		IDataMap pipelineMap = new IDataMap(pipeline);
				
		Object context = pipelineMap.get(PGConstants.MESSAGE_CONTEXT);
				// get the prop name for getting the application object
		String appContextPropName = pipelineMap.getAsString("appContextPropName");
				if (appContextPropName != null && !"".equals(appContextPropName)) {
					// object for respective soap and rest context
			MessageContext msgContext = null;
			RestMessageContext restMsgContext = null;
						// holds the output variables
		
			String appId = "null";
			String reqPayloadString = "";
			String apiType = "";
		
			if (context instanceof RestMessageContext) {
				apiType = "REST";
				// extraction logic for rest apis
				restMsgContext = (RestMessageContext) context;
				Application application = (Application) restMsgContext.getProperty("gateway.identifiedApplication");
				
				if (application != null ) {
				
					appId=application.getApplicationID();
		
					
				}
				
				ByteContent content = (ByteContent) restMsgContext.getRequestContent();
				if (content != null) {
					reqPayloadString = content.getAsString();
				} else {
					throw new ServiceException("Exception in extracting request payload. ByteContent is null");
				}
				
			} else if (context instanceof MessageContext) {
			
				apiType = "SOAP";
				
		
				// payload logic not implemented for SOAP
			}
			
			// update to pipeline
			
			pipelineMap.put("reqPayloadString", reqPayloadString);
			
			pipelineMap.put("consumerAppId", appId);
			
		} else {
			throw new ServiceException(" Missing Context prop to retrieve Consuming application name");
		}
		
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void hmacsha256 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(hmacsha256)>> ---
		// @sigtype java 3.5
		// [i] field:0:required secretKey
		// [i] field:0:required data
		// [o] field:0:required encodedData
		// pipeline
				IDataCursor pipelineCursor = pipeline.getCursor();
					String	secretKey = IDataUtil.getString( pipelineCursor, "secretKey" );
					String	data = IDataUtil.getString( pipelineCursor, "data" ); 
				
				
					
					try {
						SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), SHA256ALGORITHM);
						Mac mac = Mac.getInstance(SHA256ALGORITHM);
						mac.init(signingKey);
						Base64.Encoder encoder = Base64.getEncoder();  
						String encodedData = encoder.encodeToString( mac.doFinal(data.getBytes()) );
						
						
						IDataUtil.put( pipelineCursor, "encodedData", toHexString( mac.doFinal(data.getBytes()) ) );
						pipelineCursor.destroy();
				
					} catch (InvalidKeyException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
		// --- <<IS-END>> ---

                
	}



	public static final void hmacsha512 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(hmacsha512)>> ---
		// @sigtype java 3.5
		// [i] field:0:required secretKey
		// [i] field:0:required data
		// [o] field:0:required encodedData
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	secretKey = IDataUtil.getString( pipelineCursor, "secretKey" );
			String	data = IDataUtil.getString( pipelineCursor, "data" );
						
			try {
				SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), SHA512ALGORITHM);
				Mac mac = Mac.getInstance(SHA512ALGORITHM);
				mac.init(signingKey);
				Base64.Encoder encoder = Base64.getEncoder();  
				String encodedData = encoder.encodeToString( mac.doFinal(data.getBytes()) );
				
				
				IDataUtil.put( pipelineCursor, "encodedData", toHexString( mac.doFinal(data.getBytes()) ) );
				pipelineCursor.destroy();
		
			} catch (InvalidKeyException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				((Throwable) e).printStackTrace();
			}
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void queryJson (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(queryJson)>> ---
		// @sigtype java 3.5
		// [i] record:0:required document
		// [o] record:0:required finalDocument
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		
			IData	document = IDataUtil.getIData( pipelineCursor, "document" );
			if ( document != null)
			{
				IDataCursor documentCursor = document.getCursor();
			    boolean hasMore = documentCursor.first();
				if (hasMore) {
					if(documentCursor.getKey()!=null)
					{
					    IData temp = (IData) documentCursor.getValue();
						IDataUtil.put( pipelineCursor, "finalDocument", temp ); 
					}			
				}
					
				}
		
		
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void sha256 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sha256)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inString
		// [o] field:0:required sha256String
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	inString = IDataUtil.getString( pipelineCursor, "inString" );
		
		  try { 
		        // getInstance() method is called with algorithm SHA-512 
		        MessageDigest md = MessageDigest.getInstance("SHA-512"); 
		    
		        
		        // digest() method is called 
		        // to calculate message digest of the input string 
		        // returned as array of byte 
		      //  byte[] messageDigest = md.digest(input.getBytes()); 
		  
		        // Convert byte array into signum representation 
		       // BigInteger no = new BigInteger(1, messageDigest); 
		  
		        // Convert message digest into hex value 
		       // String hashtext = no.toString(16); 
		  
		        // Add preceding 0s to make it 32 bit 
		    //    while (hashtext.length() < 32) { 
		    //        hashtext = "0" + hashtext; 
		    //    } 
		  
		        // return the HashText 
		     
		    } 
		  
		    // For specifying wrong message digest algorithms 
		    catch (NoSuchAlgorithmException e) { 
		        throw new RuntimeException(e); 
		    } 
		
		IDataUtil.put( pipelineCursor, "sha256String", "sha256String" );
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void sha512 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sha512)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inString
		// [o] field:0:required sha512HashString
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	inString = IDataUtil.getString( pipelineCursor, "inString" );
		try{
		
		// getInstance() method is called with algorithm SHA-512 
		MessageDigest md = MessageDigest.getInstance("SHA-512"); 
		
		// digest() method is called 
		// to calculate message digest of the input string 
		// returned as array of byte 
		byte[] messageDigest = md.digest(inString.getBytes()); 
		
		// Convert byte array into signum representation 
		BigInteger no = new BigInteger(1, messageDigest); 
		
		// Convert message digest into hex value 
		String hashtext = no.toString(16); 
		
		// Add preceding 0s to make it 32 bit 
		while (hashtext.length() < 32) { 
		    hashtext = "0" + hashtext; 
		} 
		
		 // return the HashText 
		IDataUtil.put( pipelineCursor, "sha512HashString", hashtext );
		pipelineCursor.destroy();
		} 
		
		// For specifying wrong message digest algorithms 
		catch (NoSuchAlgorithmException e) { 
		throw new RuntimeException(e); 
		} 
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
  private static final String SHA512ALGORITHM = "HmacSHA512";
  private static final String SHA256ALGORITHM = "HmacSHA256";
	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		return formatter.toString();
	}
	// --- <<IS-END-SHARED>> ---
}

