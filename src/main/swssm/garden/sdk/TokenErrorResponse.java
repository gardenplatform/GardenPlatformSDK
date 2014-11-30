package swssm.garden.sdk;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

/**
 *
 * 
 * access_token의 response를 파싱하는 클래스
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

	@Key
	private String error;
	
	@Key("error_description")
	private String errorDescription;
	
	@Key("error_uri")
	private String errorUri;
	
	/**
	 * error code를 리턴. 
	 * (invalid_request /invalid_client / invalid_grant / 
	 * unauthorized_client / unsupported_grant_type / invalid_scope
	 * 
	 */
	public final String getError(){
		return error;
	}
	
	public TokenErrorResponse setError(String error){
		this.error = Preconditions.checkNotNull(error);
		return this;
	}
	
	public final String getErrorUri(){
		return errorUri;
	}
	
	public TokenErrorResponse setErrorUri(String errorUri){
		this.errorUri = errorUri;
		return this;
	}
	
	
}
