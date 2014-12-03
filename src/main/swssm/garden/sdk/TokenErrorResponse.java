package swssm.garden.sdk;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

/**
 *
 * 
 * access_token요청에 대한 error response의 JSON Model 
 * 
 *  @author SW
 *  @version 1.0
 *  
 */

public class TokenErrorResponse extends GenericJson{
	
	@Override
	public GenericJson clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public GenericJson set(String fieldName, Object value) {
		// TODO Auto-generated method stub
		return super.set(fieldName, value);
	}

	/**
	 * 에러 코드 
	 */
	@Key
	private String error;
	/**
	 * 에러 코드에 대한 설명 
	 */
	@Key("error_description")
	private String errorDescription;
	/**
	 * 에러 URI  
	 */
	@Key("error_uri")
	private String errorUri;
	
	/**
	 * 에러 코드를 리턴. 
	 * (invalid_request /invalid_client / invalid_grant / unauthorized_client / unsupported_grant_type / invalid_scope
	 * 
	 */
	public final String getError(){
		return error;
	}
	/**
	 * 에러 코드를 셋팅 
	 * @param error
	 * @return
	 */
	public TokenErrorResponse setError(String error){
		this.error = Preconditions.checkNotNull(error);
		return this;
	}
	
	/**
	 * 에러 URI를 리턴 
	 * @return errorUri
	 */
	public final String getErrorUri(){
		return errorUri;
	}
	/**
	 * 에러 URI를 셋팅 
	 * @param errorUri
	 * @return
	 */
	public TokenErrorResponse setErrorUri(String errorUri){
		this.errorUri = errorUri;
		return this;
	}
	
	
}
