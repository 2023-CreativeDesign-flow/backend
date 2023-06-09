package flow.cp.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import flow.cp.dto.ResponseDTO;
import flow.cp.dto.UserDTO;
import flow.cp.entity.UserEntity;
import flow.cp.service.TokenProvider;
import flow.cp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/auth")
public class UserController {
	
	@Autowired
	public UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenProvider tokenprovider;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			UserEntity user = UserEntity.builder()
										.email(userDTO.getEmail())
										.username(userDTO.getUsername())
										.password(passwordEncoder.encode(userDTO.getPassword()))
										.build();

			userService.create(user);
			
			//이것을 만드는 이유는 비밀번호 password를 사용자가 볼 수 없도록 하기 위9함이다.
			UserDTO responseUserDTO = userDTO.builder()
											.email(user.getEmail())
											.username(user.getUsername())
											.build();
			return ResponseEntity.ok().body(responseUserDTO);
		}catch(Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	// 로그인 시 회원 찾기
	@PostMapping("/signin")
	public ResponseEntity<?> loginAuthorization(@RequestBody UserDTO userDTO){
		UserEntity user = userService.FindUser(userDTO.getEmail());
		if(user != null) {
			if(passwordEncoder.matches(userDTO.getPassword(), user.getPassword())){
				final String token = tokenprovider.create(user);
				System.out.println(token);
				UserDTO responseDTO = userDTO.builder()
												.email(user.getEmail())
												.username(user.getUsername())
												.member_id(user.getId())
												.token(token)
												.build();
				return ResponseEntity.ok().body(responseDTO);
			}
		}
		
		ResponseDTO responseDTO = ResponseDTO.builder()
				.error("회원 정보 없음")
				.build();

		return ResponseEntity.badRequest().body(responseDTO);			
	}
	
//	// 누르면 토큰을 반환하는 함수
//	@PostMapping("/logout")
//	public ResponseEntity<?> Logout(HttpServletRequest request){
//		String token = tokenprovider.getTokenFromRequest(request);
//		 if (token != null) {
//	            // 토큰이 존재하는 경우, 만료시간을 현재 시간으로 설정하여 토큰 무효화
//			 	tokenprovider.setTokenExpiration(token, new Date());
//	            // 토큰 반환
//	            return ResponseEntity.ok("Logout successful");  
//	        }
//	        return ResponseEntity.badRequest().body("Token not found");
//	  }

// 카카오 로그인 코드 구현
//	public String getKakaoAccessToken(String code) {
//        String accessToken = "";
//        String refreshToken;
//        String reqUrl = "https://kauth.kakao.com/oauth/token";
//
//        try {
//            URL url = new URL(reqUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            String sb = "grant_type=authorization_code" +
//                    "&client_id=[키 보안]" +
//                    "&redirect_uri=[URI 보안]" +
//                    "&code=" + code;
//            bw.write(sb);
//            bw.flush();
//
//            int responseCode = conn.getResponseCode();
//            log.info(String.valueOf(responseCode));
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line="";
//            String result="";
//
//            while((line = br.readLine()) != null) {
//                result += line;
//            }
//            log.info(result);
//
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(result);
//
//            accessToken = element.getAsJsonObject().get("access_token").getAsString();
//            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
//
//            br.close();
//            bw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return accessToken;
//    }
}
