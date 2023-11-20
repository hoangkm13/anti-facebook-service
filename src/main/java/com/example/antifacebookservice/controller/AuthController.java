package com.example.antifacebookservice.controller;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.controller.request.in.user.*;
import com.example.antifacebookservice.controller.request.out.user.UserInfoOut;
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
@CrossOrigin(origins = "http://localhost:8029", allowCredentials = "true")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserService userService, ModelMapper modelMapper) {
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

    @PostMapping(value = "/get-user-info", produces = "application/json")
    public ApiResponse<?> getUserInfo(String token, String userId) throws CustomException, IOException {
//        User authentication = (User) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        UserInfoOut userInfo = userService.getUserInfo(token, userId);
        return ApiResponse.successWithResult(userInfo);
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ApiResponse<Object> register(@Valid @RequestBody SignUpDTO signUpDTO) throws CustomException, IOException, InterruptedException {
        userService.createUser(signUpDTO);
        return ApiResponse.successWithResult(null, ResponseCode.OK.getMessage());
    }

    @PostMapping(value = "/changeInfoAfterSignUp", consumes = {"multipart/form-data"})
    public ApiResponse<UserInfoOut> changeInfoAfterSignUp(@RequestPart(required = false) String token,
                                                          @Valid @RequestPart(required = false) UserDTO userDTO,
                                                          @RequestPart(required = false) MultipartFile avatarFile,
                                                          @RequestPart(required = false) MultipartFile coverImageFile) throws CustomException, IOException {
        var User = userService.changeInfoAfterSignUp(token, userDTO, avatarFile, coverImageFile);
        return ApiResponse.successWithResult(modelMapper.map(User, UserInfoOut.class));
    }

    @PostMapping(value = "/changePassword/{userId}", produces = "application/json")
    public ApiResponse<UserDTO> resetPassword(@PathVariable String userId, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var User = userService.resetPassword(resetPasswordDTO, String.valueOf(authentication.getPrincipal().toString()), userId);
        return ApiResponse.successWithResult(modelMapper.map(User, UserDTO.class));
    }


}
