package com.server.smallgroup.Controller;

import com.server.smallgroup.Entity.Users;
import com.server.smallgroup.ResponseEntityBuilder;
import com.server.smallgroup.Service.UserService;
import com.server.smallgroup.DTO.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/user/id/{id}")
    public ResponseEntity<Map<String, Object>> checkId(@PathVariable String id){
        if(!this.userService.checkID(id)){
            return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
        }else{
            return ResponseEntityBuilder.buildErrorResponse("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("user/phonenum/{num}")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@PathVariable String num, HttpServletRequest request){
        Matcher matcher = Pattern.compile("[^0-9]").matcher(num);
        if(num.length()<10 || num.length()>11 || matcher.find()){
            return ResponseEntityBuilder.buildErrorResponse("전화번호 형식이 맞지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if(this.userService.checkPhoneNum(num)){
            return ResponseEntityBuilder.buildErrorResponse("이미 존재하는 전화번호입니다.", HttpStatus.BAD_REQUEST);
        }

        String result = this.userService.sendVerificationCode(num);
        HttpSession session = request.getSession();
        session.setAttribute("Code", result);
        session.setMaxInactiveInterval(360);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @GetMapping("/user/checknum/{num}")
    public ResponseEntity<Map<String, Object>> checkId(@PathVariable String num, HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("Code")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        if(session.getAttribute("Code").equals(num)){
            session.invalidate();
            return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
        }else{
            return ResponseEntityBuilder.buildErrorResponse("유효하지 않은 인증번호입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/findID")
    public ResponseEntity<Map<String, Object>> findId(@Valid @RequestBody FindIdDTO findIdDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntityBuilder.buildBindingErrorResponse(bindingResult);
        }

        Optional<Users> ou = this.userService.findId(findIdDTO.getName(), findIdDTO.getPhonenum());
        if (ou.isPresent()) {
            return ResponseEntityBuilder.buildSuccessResponseWithInfo(HttpStatus.OK, "id", ou.get().getUserId());
        } else {
            return ResponseEntityBuilder.buildErrorResponse("해당 회원이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/user/findPW")
    public ResponseEntity<Map<String, Object>> findPw(@Valid @RequestBody FindPwDTO findPwDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntityBuilder.buildBindingErrorResponse(bindingResult);
        }

        if(this.userService.findPw(findPwDTO.getName(), findPwDTO.getId(), findPwDTO.getPhonenum())){
            return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
        }else {
            return ResponseEntityBuilder.buildErrorResponse("해당 회원이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> checksession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }else{
            return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
        }
    }
    @PostMapping("/session")
    public ResponseEntity<Map<String, Object>> signIn(HttpServletRequest request, @Valid @RequestBody LoginDTO info, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntityBuilder.buildBindingErrorResponse(bindingResult);
        }

        Optional<Users> ou = this.userService.signIn(info.getId(), info.getPw());
        if(ou.isPresent()){
            HttpSession session = request.getSession();
            session.setAttribute("id", String.valueOf(ou.get().getId()));
            session.setMaxInactiveInterval(60*60);
            return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
        } else {
            return ResponseEntityBuilder.buildErrorResponse("아이디 혹은 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/session")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("id") == null) {
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        session.invalidate();
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Long id = Long.valueOf(session.getAttribute("id").toString());

        UserInfoDTO info = this.userService.getUserInfo(id);
        return ResponseEntityBuilder.buildSuccessResponseWithInfo(HttpStatus.OK, "user", info);
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody SignupDTO signupReq, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntityBuilder.buildBindingErrorResponse(bindingResult);
        }

        if(this.userService.checkID(signupReq.getID())){
            return ResponseEntityBuilder.buildErrorResponse("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }else if(this.userService.checkPhoneNum(signupReq.getPhonenum())){
            return ResponseEntityBuilder.buildErrorResponse("이미 존재하는 전화번호입니다.", HttpStatus.BAD_REQUEST);
        }

        this.userService.signUp(signupReq.getID(), signupReq.getPW(), signupReq.getPhonenum(),
                                signupReq.getName(), signupReq.getGender(), signupReq.getAge(), signupReq.getAddress());
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.CREATED);
    }

    @PatchMapping("/user")
    public ResponseEntity<Map<String, Object>> changeUserInfo(HttpServletRequest request, @Valid @RequestBody ChangeUserInfoDTO info, BindingResult bindingResult){
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Long id = Long.valueOf(session.getAttribute("id").toString());

        if(info.getOldpw()==null) {
            if(info.getHobby()==null)
                return ResponseEntityBuilder.buildErrorResponse("취미를 입력하세요.", HttpStatus.BAD_REQUEST);
            this.userService.changeUserInfo(id, info.getAddress(), info.getHobby(), info.getMessage());
            return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
        }else{
            if(info.getNewpw()==null)
                return ResponseEntityBuilder.buildErrorResponse("새 비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
            if(this.userService.changePw(id, info.getOldpw(),info.getNewpw())) {
                return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
            }
            return ResponseEntityBuilder.buildErrorResponse("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<Map<String, Object>> deleteUser(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.valueOf(session.getAttribute("id").toString());
        this.userService.deleteUser(id);
        session.invalidate();
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @GetMapping("profiles")
    public List<UrlResource> getUserImages(HttpServletRequest request){
        List<UrlResource> list = new ArrayList<>();
        return list;
    }

    @GetMapping({"profile/{n}", "profile"})
    public UrlResource getUserImage(@PathVariable(required = false) Integer n, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Long id = null;
        if(n!=null){
            id = Long.valueOf(n);
        }else{
            if(session.getAttribute("id")==null){
                throw new Exception("세션이 없습니다.");
            }
            id = Long.valueOf(session.getAttribute("id").toString());
        }

        String path = this.userService.getUserImage(id);
        if(path == null)
            return null;
        else
            return new UrlResource("file:"+path);
    }

    @PostMapping("profile")
    public ResponseEntity<Map<String, Object>> changeUserImage(@RequestParam MultipartFile file,
                                                               HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.valueOf(session.getAttribute("id").toString());
        this.userService.changeUserImage(id, file);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @DeleteMapping("profile")
    public ResponseEntity<Map<String, Object>> deleteUserImage(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.valueOf(session.getAttribute("id").toString());
        this.userService.deleteUserImage(id);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }
}
