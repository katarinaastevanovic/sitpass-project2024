package rs.ac.uns.ftn.svt.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.Dto.LoginRequestDto;
import rs.ac.uns.ftn.svt.Dto.LoginResponseDto;
import rs.ac.uns.ftn.svt.Service.Impl.LoginServiceImpl;
import rs.ac.uns.ftn.svt.Service.JwtService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginServiceImpl loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        return ResponseEntity.ok(loginService.login(loginRequestDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(loginService.refreshToken(request, response));
    }



}
