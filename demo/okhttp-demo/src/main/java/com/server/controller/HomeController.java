package com.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@Data
@Getter
@Setter
@AllArgsConstructor
class Person{
    String name;
    String age;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@RequestHeader(value = "Env", required = false, defaultValue = "prod") String env){
        return "Home Page: " + env;
    }

    @GetMapping("/search")
    public String search(@RequestParam("kw") String kw){
        return "Get Keyword: " + kw;
    }

    @PostMapping("/insert")
    public String insertInfo(@RequestBody Person person){
        return "Get info: " + person;
    }


}
