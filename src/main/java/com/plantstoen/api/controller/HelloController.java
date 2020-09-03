package com.plantstoen.api.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class HelloController {

    private static final String HELLO = "helloworld-nice to meet you";

    @Setter
    @Getter
    public static class Hello {
        private String message;
    }

    @GetMapping(value = "/helloworld/string")
    @ResponseBody
    public String helloworldString() {
        return "hello, World";
    }

    @GetMapping(value = "/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloworld";
        return hello;
    }

    // 이렇게 @ResponseBody 를 지정하지 않으면 "helloworld" 이름으로 된 파일을 프로젝트 경로에서 찾아 화면에 출력
    @GetMapping(value = "/helloworld/page")
    public String helloworld() {
        return "helloworld";
    }

    @GetMapping("/helloworld/long-process")
    @ResponseBody
    public String pause() throws InterruptedException {
        Thread.sleep(10000);
        return "Process finished";
    }
}
