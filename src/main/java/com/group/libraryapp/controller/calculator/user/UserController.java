package com.group.libraryapp.controller.calculator.user;

import com.group.libraryapp.dto.user.UserUpdateRequest;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import com.group.libraryapp.service.user.UserServiceV1;
import com.group.libraryapp.service.user.UserServiceV2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    // 유저 생성 API
    // 도서관리 애플리케이션의 요구사항
    /*
    사용자
    - 도서관의 사용자를 등록할 수 있다.(이름 필수, 나이 선택)
    (HTTP Method : POST(등록하기 때문)
     HTTP Paht : /user
     Http Body(JSON)
     결과 반환 X(HTTP 상태 200 OK면 충분)

    - 도서관 사용자의 목록을 볼 수 있다.
    - 도서관 사용자 이름을 업데이트 할 수 있다.
    - 도서관 사용자를 삭제할 수 있다.

    책
    - 도서관에 책을 등록 및 삭제 할 수 있다.
    - 사용자가 책을 빌릴 수 있다. ( 다른 사람이 그 책을 진작 빌렸다면 빌릴 수 없다.)
    - 사죵자가 책을 반납 할 수 있다.
    */


    private final UserServiceV2 userService;

    /*
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    */

    public UserController(UserServiceV2 userService){
        this.userService = userService;

    }


    @PostMapping("/user")
    public void saveUser(@RequestBody UserCreateRequest request){
        userService.saveUser(request);
    }
    @GetMapping("/user")
      /*
    public List<UserResponse> getUsers(){
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new RowMapper<UserResponse>() {
            @Override
            public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                return new UserResponse(id, name, age);
            }
        });
    }
    */
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

    //RowMapper 는 쿼리의 결과를 받아, 객체를 반환한다.
    //-> lamda로 변환 가능 alt-enter


    /*
    API 문제점
    유저 정보가 메모리(RAM)에서만 유지되고 있음.- 단기기억
    서버가 종료되면 사라짐 재시작시 초기화
    DISK에 저장 해야 함 - DB 사용(데이터를 구조화)
    RDB(Relational Database) - 표처럼 구조화 (MySQL)
    SQL - 구조화된 데이터를 조회하는 언어

    */
    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request){
       userService.updateUser(request);
    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestParam String name){
       userService.deleteUser(name);
    }
    //존재하지 않는 유저를 삭제, 수정해도 성공함 -> 예외처리 필요


    /*
    클린코드를 위한 삼단 분리 - 컨트롤러 하나가 너무 많은 역할을 함
    1. API 진입 지점으로써 HTTP Body를 객체로 변환하고 있다. - 컨트롤러의 역할
    2. 현재 유저가 있는지, 없는지 등을 확인하고 예외 처리를 해준다. -  Service의 역할
    3. SQL을 사용해 실제 DB와의 통신을 담당한다. - Repository의 역할

    UserController와 스프링 컨테이너

    1.static이 아닌 코드를 사용하려면 인스턴스화(생성자 호출) 해야한다.
    2.UserController는 JdbcTemplate에 의존하고 있다.
    @RestController는 UserController클래스를 스프링 빈으로 등록시킨다.
    스프링 빈이란 - 서버가 시작되면, 스프링 서버 내부에 거대한 컨테이너를 만들게 된다.
    컨테이너 안에는 클래스가 들어가게 된다. -이때 다양한 정보도 함께 들어있고, 인스턴스화도 이루어진다.
    -> 컨테이너 안에 들어간 클래스를 스프링 빈이라 한다.
    JdbcTemplate도 스프링 빈드로 등록되어 있다. -> 우리가 가져온 Dependency가 등록해 주고 있다.

    */

}
