package swssm.garden.sdk;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.testing.http.MockHttpTransport;

import junit.framework.TestCase;

public class TokenRequestTest extends TestCase {

	  static final MockHttpTransport TRANSPORT = new MockHttpTransport();
	  static final JacksonFactory JSON_FACTORY = new JacksonFactory();

	  public void testTokenRequest() {
//	    check(new TokenRequest(TRANSPORT, JSON_FACTORY, "sample_grant_type"), "sample_grant_type");
	  }

	  static void check(TokenRequest request, String grantType) {
	    assertEquals(grantType, request.getGrantType());
	    assertNull(request.getScopes());
	    assertEquals(TRANSPORT, request.getTransport());
	    assertEquals(JSON_FACTORY, request.getJsonFactory());
	  }
}
