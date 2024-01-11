package com.group.libraryapp.repository.user;

import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //스프링 빈으로 등록
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserNotExist(long id) {
        String readSql = "SELECT * FROM user WHERE id = ?";// id별 유저가 존재하는지 조회
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, id).isEmpty();
        // DB에 데이터가 있는지 확인, 있으면 0, 없으면 비어있는 리스트, 쿼리는  결과를 리스트로 감싸준다.

    }
    public void updateUserName(String name, long id){
        String sql = "UPDATE user SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public boolean isUserNotExist(String name){
        String readSql = "SELECT * FROM user WHERE name = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 0, name).isEmpty();
    }

    public void deleteUser(String name){
        String sql = "DELETE FROM user WHERE name = ?";
        jdbcTemplate.update(sql, name);
    }

    public void saveUser(String name, Integer age){
        String sql = "INSERT INTO user (name, age) VALUES(?, ?)";
        jdbcTemplate.update(sql, name, age);
    }

    public List<UserResponse> getUsers(){
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            return new UserResponse(id, name, age);
        });
    }
}
/*
직접 SQL을 작성할때 아쉬운 점
1. 문자열을 작성하기 때문에 실수할 수 있고, 실수를 인지하는 시점이 느리다
2. 특정 데이터베이스에 종속적이게 된다.
3. 반복 작업이 많아진다. 테이블 하나 만들 떄 마다다 CRUD쿼리가 항상 필요하다.
4. 데이터베이스의 테이블과 객체는 패러다임이 다르다

JPA(Java Persistence API),자바 진영의 ORM(Object-Relational Mapping)
-> 객채와 관계형 DB의 테이블을 짝지어 데이터를 영구적으로 저장할 수 있도록 정해진 Java 진영의 규칙
->을 구현해서 코드를 구현한 것은 Hibernate
Hibernate는 내부적으로 JDBC를 사용한다.


 */