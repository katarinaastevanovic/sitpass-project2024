package rs.ac.uns.ftn.svt.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Dto.AccountRequestDto;
import rs.ac.uns.ftn.svt.Dto.ChangePasswordDto;
import rs.ac.uns.ftn.svt.Dto.UserDto;
import rs.ac.uns.ftn.svt.Model.User;
import rs.ac.uns.ftn.svt.Repository.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
public interface UserService {


    UserDto getOneByEmail(String email);

    void createAccountRequest(AccountRequestDto accountRequestDto);

    boolean isEmailExists(String email);

    AccountRequestDto acceptAccountRequest(Long accountRequestId, AccountRequestDto accountRequestDto);

    List<AccountRequestDto> getRequestByStatusPENDING();

    AccountRequestDto rejectAccountRequest(Long accountRequestId, AccountRequestDto accountRequestDto);

    void updateUser(Long userId, UserDto userDto);

    UserDto getOneById(Long userId);

    void changePassword(Long userId, ChangePasswordDto changePasswordDto);

    List<UserDto> getAllUsers();



/*

    private UserRepository userRepository;


    public User registerUser(User user)  {
        if (userRepository.existsByEmail(user.getEmail())) {
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            // Označavanje korisnika kao prijavljenog može biti dodatno rukovanje sesijom ili tokenom
            return user.get();
        } else {
            return null;
        }
    }
    public void logoutUser() {
        // Očisti bilo kakve sesije ili tokene na frontendu
    }

    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getPassword().equals(currentPassword)) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

*/
}






