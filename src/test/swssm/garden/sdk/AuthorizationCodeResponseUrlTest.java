package swssm.garden.sdk;

import junit.framework.TestCase;

/** Response받은  Parameter : code, state */
public class AuthorizationCodeResponseUrlTest extends TestCase{
	
	public void testConstructor(){
		
		AuthorizationCodeResponseUrl sample_response = new AuthorizationCodeResponseUrl(
				"http://client.gardenSample.com/rd"
				+ "?code=access_success"
				+ "&state=asdf"
				);
		assertEquals("access_success", sample_response.getCode());
		assertEquals("asdf", sample_response.getState());
		
		
		AuthorizationCodeResponseUrl sample_response_2 = new AuthorizationCodeResponseUrl(
				"http://client.gardenSample.com/re?error=access_denied&state=asdf");
		assertEquals("access_denied",sample_response_2.getError());
		assertEquals("asdf",sample_response_2.getState());
	}
}
