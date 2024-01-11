package com.group.libraryapp.controller.calculator;

import com.group.libraryapp.dto.calculator.request.CalculatorAddRequest;
import com.group.libraryapp.dto.calculator.request.CalculatorMultiplyRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController //클래스를 API의 진입지점으로 설정
public class CalculatorController {
    /*
    @GetMapping("/add") //GET / add
    public int addTwoNumbers(@RequestParam int number1, @RequestParam int number2){
        return number1 + number2;
    }
    */
    @GetMapping("/add")
    public int addTwoNumbers(CalculatorAddRequest request){
        return request.getNumber1() + request.getNumber2();
    }

    /*
    Post 방식
    데이터를 받을 떄 HTTP Body를 이용한다.
    JSON(JavaScript Object Notation)
    - 객체 표기법, 무언가를 표현하기 위한 형식이다
    - ex{"name": "안중건",
         "age":99
         "house":{
         "address":"대한민국 서울",
         "hasDoor": true
          //JSON안에 JSON 추가 가능}
         }
         속성은 쉼표로 구분
         Java의 Map<object,object>와 비슷

     */
    //곱셈 API
    /* 설계
    HTTP Method - POST
    HTTP Path - /multiply
    HTTP Body(JSON) -{
                      "number1":숫자,
                      "number2":숫자
                       }
    API의 반환 결과 - 곳셈 결과
     */
    @PostMapping("/multiply")//POST
    public int multiplyTwoNumbers(@RequestBody CalculatorMultiplyRequest request){
        //@ReuquestBody : HTTP Body로 들어오는 JSON을 CalculatorMultiplyRequest로 바꾼다
        return request.getNumber1() * request.getNumber2();
    }

}
