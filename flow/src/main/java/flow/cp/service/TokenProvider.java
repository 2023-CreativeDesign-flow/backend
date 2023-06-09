package flow.cp.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import flow.cp.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	private static final String SECRET_KEY = "NMA8JPctFuna59f5";
	
	public String create(UserEntity userEntity) {
		Date expireDate	= Date.from(
				Instant.now()
						.plus(1, ChronoUnit.DAYS));
				
				return Jwts.builder()
							.signWith(SignatureAlgorithm.HS512,SECRET_KEY)
							.setSubject(userEntity.getId())
							.setIssuer("todo app")
							.setIssuedAt(new Date())
							.setExpiration(expireDate)
							.compact();
	}
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(SECRET_KEY)
							.parseClaimsJws(token)
							.getBody();
		
		return claims.getSubject();
	}
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//
//    public String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    public void setTokenExpiration(String token, Date expirationDate) {
//        Claims claims = getAllClaimsFromToken(token);
//        claims.setExpiration(expirationDate);
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//    	return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//        // 토큰에서 클레임 추출 로직
//    }
}
