package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //User findByName(String name);
    Optional<User> findByName(String name);

    // By 뒤에 붙는 필드 이름으로 select 문이 작성된다.
    // SELECT * FROM user WHERE name =?

    long countByAge(Integer age);
}
