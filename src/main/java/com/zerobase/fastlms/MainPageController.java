package com.zerobase.fastlms;


//주소와 파일 매핑
// 메소드를 통한 주소 매핑

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller // 주소를 매핑하는 클래스를 특정
public class MainPageController {
@RequestMapping("/")//해당 주소요청에 대한 매핑
    public String index(){
       //thymeleaf를 통해 동일한 html명을 return하면 자동 매핑
        //spring: thymeleaf: suffix: .html 이 default값으로 설정되어 있음 임의 변경 가능
        return "index";
    }

    // 스프링 -> MVC(view -> 템플릿 엔진 화면에 내용을 출력(html))
    // 템플릿 엔진? 화면 출력시 한글 인코딩 등을 도와줌

    //request -> web -> server
    //response -> server -> web
    @RequestMapping("/hello")//해당 주소요청에 대한 매핑
    public void hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8"); // 한글 인코딩
        PrintWriter printWriter = response.getWriter();
        String msg = "Hello fastlms website : 안녕하세요!";
        printWriter.write(msg); // 문자열 출력
        printWriter.close();

    }
}
