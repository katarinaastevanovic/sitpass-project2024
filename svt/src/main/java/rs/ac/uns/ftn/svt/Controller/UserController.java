package rs.ac.uns.ftn.svt.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.Dto.AccountRequestDto;
import rs.ac.uns.ftn.svt.Dto.ChangePasswordDto;
import rs.ac.uns.ftn.svt.Dto.FacilityDto;
import rs.ac.uns.ftn.svt.Dto.UserDto;
import rs.ac.uns.ftn.svt.Service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email) {
        System.out.println("Fetching user by email: " + email);
        return ResponseEntity.ok(userService.getOneByEmail(email));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountRequestDto accountRequestDto) {
        System.out.println("POST /api/v1/users/register - Start");
        System.out.println("AccountRequestDto: " + accountRequestDto);

        userService.createAccountRequest(accountRequestDto);

        System.out.println("POST /api/v1/users/register - Success");
        return ResponseEntity.ok("Registration request submitted successfully.");
    }


    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }
        boolean exists = userService.isEmailExists(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<AccountRequestDto> approveAccountRequest(@PathVariable Long id, @RequestBody AccountRequestDto accountRequestDto) {
        System.out.println("sss");
        System.out.println(id);
        return ResponseEntity.ok(userService.acceptAccountRequest(id, accountRequestDto));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<AccountRequestDto> rejectAccountRequest(@PathVariable Long id,
                                                       @RequestBody AccountRequestDto accountRequestDto) {
        return ResponseEntity.ok(userService.rejectAccountRequest(id, accountRequestDto));

    }

    @GetMapping("/requests")
    public ResponseEntity<List<AccountRequestDto>> getRequestByStatusPENDING(){
       return ResponseEntity.ok(userService.getRequestByStatusPENDING());
    }


    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        userService.updateUser(userId, userDto);
        return ResponseEntity.ok("User updated successfully.");
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        System.out.println("Fetching user by ID: " + userId);
        return ResponseEntity.ok(userService.getOneById(userId));
    }

    @PostMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(userId, changePasswordDto);
        return ResponseEntity.ok("Password changed successfully.");
    }


}
