package swssm.garden.sdk;

import java.io.IOException;


/** 
 * 
 * refresh token의 결과를 위한 리스너.
 * refresh token을 받았다는 response 후에 불려짐.
 * @author Garden
 * @version 1.0
 * 
 * */
public interface CredentialRefreshListener {

	
	/**
	 * HTTP Response가 성공적이었을 때
	 * @param credential
	 * @param tokenResponse
	 * @throws IOException
	 */
	void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException;
	
	/**
	 * HTTP Response가 오류가 있을 때
	 * @param credential
	 * @param tokenErrorResponse
	 * @throws IOException
	 */
	void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException;
	
}
