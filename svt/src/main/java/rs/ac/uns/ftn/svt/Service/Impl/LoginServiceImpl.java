package rs.ac.uns.ftn.svt.Service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Dto.LoginRequestDto;
import rs.ac.uns.ftn.svt.Dto.LoginResponseDto;
import rs.ac.uns.ftn.svt.Exception.UnauthorizedException;
import rs.ac.uns.ftn.svt.Service.LoginService;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            return LoginResponseDto.builder()
                    .accessToken(jwtService.generateToken(userDetails))
                    .refreshToken(jwtService.generateRefreshToken(userDetails))
                    .build();
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("Authentication failed. Please check your credentials.");
        }
    }

    @Override
    public LoginResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = authHeader.substring(7);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(refreshToken));

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            return LoginResponseDto.builder()
                    .accessToken(jwtService.generateToken(userDetails))
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UnauthorizedException("Refresh token is not valid");
        }
    }
}
