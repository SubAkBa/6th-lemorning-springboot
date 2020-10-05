package com.example.demo.user;

import com.example.demo.config.JwtTokenUtil;
import com.example.demo.domain.Response;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtUserDetailsService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup")
    public Response signup(@RequestBody UserDto infoDto) { // 회원 추가
        Response response = new Response();
        try {
            userService.save(infoDto);
            response.setResponse("success");
            response.setMessage("회원가입을 성공적으로 완료했습니다.");
        } catch (Exception e) {
            response.setResponse("failed");
            response.setMessage("회원가입을 하는 도중 오류가 발생했습니다.");
            response.setData(e.toString());
        }
        return response;
    }

    @GetMapping("/user/mypage/charge/{point}")
    public @ResponseBody void chargePoint(@PathVariable("point")int point, Principal principal){
        System.out.println("principal.getName() = " + principal.getName());
        userService.addPoint(principal.getName(),point);
    }
}