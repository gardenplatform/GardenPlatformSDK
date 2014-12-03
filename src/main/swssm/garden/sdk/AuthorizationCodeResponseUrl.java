package swssm.garden.sdk;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;

/**
 * 
 *  Authorization Server로부터 Response받은 URL을 파싱해주는 클래스
 * 
 * 
 * @author SW
 * 	@version 1.0
 *
 */

public class AuthorizationCodeResponseUrl extends GenericUrl{
	
	@Key
	private String code;
	@Key
	private String error;
	@Key
	private String state;
	@Key("error_description")
	private  String errorDescription;
	/**
	 * 
	 * @param encodedResponseUrl
	 */
	public AuthorizationCodeResponseUrl(String encodedResponseUrl){
		super(encodedResponseUrl);
		Preconditions.checkArgument((code==null) != (error == null));
	}
	/**
	 * code를 리턴 
	 * @return code
	 */
	public final String getCode(){
		return code;
	}
	/**
	 * code를 셋팅 
	 * @param code
	 * @return code
	 */
	public AuthorizationCodeResponseUrl setCode(String code){
		this.code = code;
		return this;
	}
	/**
	 * state를 리턴 
	 * @return state
	 */
	public final String getState(){
		return state;
	}
	/**
	 * state를 셋팅 
	 * @param state
	 * @return state
	 */
	public AuthorizationCodeResponseUrl setState(String state){
		this.state = state;
		return this;
	}
	/**
	 * error를 리턴 
	 * @return error
	 */
	public final String getError(){
		return error;
	}
	/**
	 * error를 셋팅 
	 * @param error
	 * @return error
	 */
	public AuthorizationCodeResponseUrl setError(String error){
		this.error = error;
		return this;
	}
	/**
	 * error description을 리턴 
	 * @return errorDescription
	 */
	public final String getErrorDescription(){
		return errorDescription;
	}
	/**
	 * error description을 셋팅 
	 * @param errorDescription
	 * @return errorDescription
	 */
	public AuthorizationCodeResponseUrl setErrorDescription(String errorDescription){
		this.errorDescription = errorDescription;
		return this;
	}
	
	@Override
	public AuthorizationCodeResponseUrl set(String fieldName, Object value){
		return (AuthorizationCodeResponseUrl) super.set(fieldName,value);
	}
	
	@Override
	public AuthorizationCodeResponseUrl clone(){
		return (AuthorizationCodeResponseUrl) super.clone();
	}
}
