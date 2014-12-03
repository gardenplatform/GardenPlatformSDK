package swssm.garden.sdk;

import java.io.IOException;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;

/**
*
* 
*  Token Server로 부터의 에러 Response를 받았을 때 예외 처리 클래스.
* 
*  @author SW
*  @version 1.0
*  
*/
public class TokenResponseException extends HttpResponseException{

	private static final long serialVersionUID = 1L;
	/**
	 * Token error response의 디테일 
	 */
	private final transient TokenErrorResponse details;
	
	/**
	 * 
	 * @param builder
	 * @param details
	 */
	protected TokenResponseException(Builder builder, TokenErrorResponse details) {
		super(builder);
		this.details = details;
	}

	/**
	 * Token error response의 detail을 return
	 * @return details
	 */
	public final TokenErrorResponse getDetails(){
		return details;
	}

	
	/**
	 * JSON error response. TokenErrorResponse에 의해 파싱 되어 getDetails()를 이용하면 볼 수 있음.
	 * @param jsonFactory
	 * @param response
	 * @return 
	 */
	public static TokenResponseException from (JsonFactory jsonFactory, HttpResponse response){
		HttpResponseException.Builder builder = new HttpResponseException.Builder(
				response.getStatusCode(), response.getStatusMessage(), response.getHeaders());
		
		Preconditions.checkNotNull(jsonFactory);
		TokenErrorResponse details = null;
		String detailString = null;
		String contentType = response.getContentType();
		
		try{
			if(!response.isSuccessStatusCode() && contentType != null
					&& HttpMediaType.equalsIgnoreParameters(Json.MEDIA_TYPE, contentType)){
				details = new JsonObjectParser(jsonFactory).parseAndClose(
						response.getContent(), response.getContentCharset(), TokenErrorResponse.class);
				detailString = details.toPrettyString();
			} else
				detailString = response.parseAsString();
		} catch (IOException e){
			e.printStackTrace();
		}
	
		StringBuilder message = HttpResponseException.computeMessageBuffer(response);
		if(!com.google.api.client.util.Strings.isNullOrEmpty(detailString)){
			message.append(StringUtils.LINE_SEPARATOR).append(detailString);
			builder.setContent(detailString);
		}
		builder.setMessage(message.toString());
		return new TokenResponseException(builder, details);
				
		
	}


}
