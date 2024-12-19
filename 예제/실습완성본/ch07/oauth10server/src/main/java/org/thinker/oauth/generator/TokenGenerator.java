package org.thinker.oauth.generator;

import java.util.UUID;

import org.springframework.util.DigestUtils;

import com.multi.oauth10server.model.AccessTokenVO;
import com.multi.oauth10server.model.ConsumerVO;
import com.multi.oauth10server.model.RequestTokenVO;
import com.multi.oauth10server.model.UsersVO;

public class TokenGenerator {
	
	public static void generateConsumerKey(ConsumerVO consumerVO) throws Exception {
		consumerVO.setConsumerKey(UUID.randomUUID().toString());
		String consumerSecret = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
		consumerVO.setConsumerSecret(consumerSecret);
	}

	public static void generateRequestToken(RequestTokenVO requestTokenVO) throws Exception {
		if (requestTokenVO.getConsumerKey() == null) {
			throw new Exception("because ConsumerKey is null, request token can't be created!!");
		}
		
		String requestToken = UUID.randomUUID().toString();
		requestTokenVO.setRequestToken(requestToken);
		
        String requestTokenSecret = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
        requestTokenVO.setRequestTokenSecret(requestTokenSecret);
        
        requestTokenVO.setVerifier(DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()));
	}
	
	
	public static AccessTokenVO generateAccessToken(UsersVO usersVO, ConsumerVO consumerVO) {
		String tokenBase = usersVO.getUserno() + ":" + consumerVO.getConsumerKey();
		String accessToken = usersVO.getUserno() + "-" + DigestUtils.md5DigestAsHex(tokenBase.getBytes());
		String accessTokenSecret = getAccessTokenSecret(
				usersVO.getPassword(), consumerVO.getConsumerSecret());
		return new AccessTokenVO(accessToken, accessTokenSecret, usersVO.getUserid(), usersVO.getUserno()); 
	}
	
	public static String getAccessTokenSecret(String password, String consumerSecret) {
		String secretBase = password + ":" + consumerSecret;
		String accessTokenSecret = DigestUtils.md5DigestAsHex(secretBase.getBytes());
		return accessTokenSecret;
	}
	
	
}
