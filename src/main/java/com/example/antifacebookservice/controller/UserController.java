package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.auth.*;
import com.example.antifacebookservice.controller.response.CheckVerifyCodeResponse;
import com.example.antifacebookservice.controller.response.GetCodeVerifyResponse;
import com.example.antifacebookservice.entity.User;
import com.example.antifacebookservice.exception.CustomException;
import com.example.antifacebookservice.model.ApiResponse;
import com.example.antifacebookservice.service.UserService;
import com.example.antifacebookservice.util.TokenUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("v1/auth")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public UserController(AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserService userService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @PostMapping(value = "/login", produces = "application/json")
    public ApiResponse<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws CustomException, IOException {
        var user = userService.findByUsername(loginRequestDTO.getEmail());
        if (!user.getActive()) {
            throw new CustomException(ResponseCode.CODE_VERIFY_IS_INCORRECT);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword())
        );

        String token = tokenUtils.generateToken(authentication);
        LoginResponseDTO userToken = new LoginResponseDTO(token, user);

        return ApiResponse.successWithResult(userToken);
    }

    @PostMapping(value = "/getCodeVerify", produces = "application/json")
    public ApiResponse<GetCodeVerifyResponse> getCodeVerify(@Valid @RequestBody GetCodeVerifyRequest getCodeVerify) throws CustomException, IOException, InterruptedException {
        var getCodeVerifyResponse = userService.getCodeVerify(getCodeVerify.getEmail());

        return ApiResponse.successWithResult(getCodeVerifyResponse);
    }

    @PostMapping(value = "/checkVerifyCode", produces = "application/json")
    public ApiResponse<CheckVerifyCodeResponse> checkVerifyCode(@Valid @RequestBody CheckCodeVerifyRequest checkCodeVerifyRequest) throws CustomException, IOException, InterruptedException {
        var checkVerifyCodeResponse = userService.checkVerifyCode(checkCodeVerifyRequest);

        return ApiResponse.successWithResult(checkVerifyCodeResponse);
    }

    @GetMapping(produces = "application/json")
    public ApiResponse<UserDTO> getCurrentUser() throws CustomException, IOException {
        User authentication = (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User User = userService.findByUsername(authentication.getUsername());
        return ApiResponse.successWithResult(modelMapper.map(User, UserDTO.class));
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ApiResponse<Object> register(@Valid @RequestBody SignUpDTO signUpDTO) throws CustomException, IOException, InterruptedException {
        userService.createUser(signUpDTO);
        return ApiResponse.successWithResult(null, ResponseCode.OK.getMessage());
    }

    @PutMapping(value = "/changeInfoAfterSignUp/{userId}", produces = "application/json")
    public ApiResponse<UserDTO> changeInfoAfterSignUp(@PathVariable String userId, @Valid @RequestPart(required = false) String username, @RequestPart(required = false) MultipartFile avatarFile) throws CustomException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var User = userService.changeInfoAfterSignUp(username, authentication.getPrincipal().toString(), userId, avatarFile);
        return ApiResponse.successWithResult(modelMapper.map(User, UserDTO.class));
    }

    @PostMapping(value = "changePassword/{userId}", produces = "application/json")
    public ApiResponse<UserDTO> resetPassword(@PathVariable String userId, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var User = userService.resetPassword(resetPasswordDTO, String.valueOf(authentication.getPrincipal().toString()), userId);
        return ApiResponse.successWithResult(modelMapper.map(User, UserDTO.class));
    }
}
