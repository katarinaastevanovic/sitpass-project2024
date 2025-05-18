package rs.ac.uns.ftn.svt.Service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Dto.AccountRequestDto;
import rs.ac.uns.ftn.svt.Dto.ChangePasswordDto;
import rs.ac.uns.ftn.svt.Dto.UserDto;
import rs.ac.uns.ftn.svt.Enum.RequestStatus;
import rs.ac.uns.ftn.svt.Exception.NotFoundException;
import rs.ac.uns.ftn.svt.Model.AccountRequest;
import rs.ac.uns.ftn.svt.Model.User;
import rs.ac.uns.ftn.svt.Repository.AccountRequestRepository;
import rs.ac.uns.ftn.svt.Repository.UserRepository;
import rs.ac.uns.ftn.svt.Service.UserService;

import java.time.LocalDate;
import java.util.List;

import static rs.ac.uns.ftn.svt.Dto.UserDto.convertToDto;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRequestRepository accountRequestRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDto getOneByEmail(String email) {
        return convertToDto(userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("email not found")));
    }

    @Override
    public void createAccountRequest(AccountRequestDto accountRequestDto) {
        if (isEmailExists(accountRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists in the system.");
        }

        AccountRequest accountRequest = AccountRequest.builder()
                .email(accountRequestDto.getEmail())
                .address(accountRequestDto.getAddress())
                .password(accountRequestDto.getPassword())
                .createdAt(LocalDate.now())
                .status(RequestStatus.PENDING)
                .build();

        accountRequestRepository.save(accountRequest);
    }

    @Override
    public boolean isEmailExists(String email) {
        boolean userExists = userRepository.findByEmail(email).isPresent();
        boolean accountRequestExists = accountRequestRepository.findByEmailAndStatusNot(email, RequestStatus.REJECTED).isPresent();
        return userExists || accountRequestExists;
    }

    @Override
    public AccountRequestDto acceptAccountRequest(Long accountRequestId, AccountRequestDto accountRequestDto) {
        AccountRequest accountRequest = accountRequestRepository.findById(accountRequestId)
                .orElseThrow(() -> new NotFoundException("Account request not found"));


        // Ažuriramo status i razlog odbijanja
        accountRequest.setStatus(RequestStatus.ACCEPTED);
        accountRequest.setRejectionReason(accountRequestDto.getRejectionReason());

        User newUser = User.builder()
                .email(accountRequest.getEmail())
                .password(passwordEncoder.encode(accountRequest.getPassword()))  // Lozinka je već uneta u zahtevu, možeš je dodatno hešovati
                .address(accountRequest.getAddress())
                .createdAt(LocalDate.now())  // Postavljamo datum kreiranja na trenutni datum
                .build();

        userRepository.save(newUser);  // Čuvamo novog korisnika u bazi

        // Ažuriramo i čuvamo promenjeni AccountRequest
        accountRequestRepository.save(accountRequest);

        return AccountRequestDto.convertToDto(accountRequest);
    }



@Override
    public List<AccountRequestDto> getRequestByStatusPENDING() {
        // Pretpostavljamo da je status "PENDING" u bazi podataka sa velikim slovima
        return accountRequestRepository.findByStatus(RequestStatus.valueOf("PENDING"))
                .stream()
                .map(AccountRequestDto::convertToDto)
                .toList();
    }

    @Override
    public AccountRequestDto rejectAccountRequest(Long accountRequestId, AccountRequestDto accountRequestDto) {
        AccountRequest accountRequest = accountRequestRepository.findById(accountRequestId)
                .orElseThrow(() -> new NotFoundException("Account request not found"));

        // Ažuriramo status i razlog odbijanja
        accountRequest.setStatus(RequestStatus.REJECTED);
        accountRequest.setRejectionReason(accountRequestDto.getRejectionReason());

        accountRequestRepository.save(accountRequest);
        return AccountRequestDto.convertToDto(accountRequest);
    }

    @Override
    public void updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getSurname() != null) {
            user.setSurname(userDto.getSurname());
        }
        if (userDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getBirthday() != null) {
            user.setBirthday(userDto.getBirthday());
        }
        if (userDto.getZipCode() != null) {
            user.setZipCode(userDto.getZipCode());
        }
        if (userDto.getCity() != null) {
            user.setCity(userDto.getCity());
        }

        userRepository.save(user);
    }

    @Override
    public UserDto getOneById(Long userId) {
        return convertToDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::convertToDto)
                .toList();
    }

}

