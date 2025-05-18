package rs.ac.uns.ftn.svt.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rs.ac.uns.ftn.svt.Dto.LoginRequestDto;
import rs.ac.uns.ftn.svt.Dto.LoginResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto request);

    LoginResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response);
}
