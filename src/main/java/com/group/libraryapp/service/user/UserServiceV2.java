package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.UserUpdateRequest;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;


    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional //트랜잭션 적용
    public void saveUser(UserCreateRequest request){
        User u = userRepository.save(new User(request.getName(), request.getAge()));

    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(){
       return userRepository.findAll().stream()//findAll().- 모든 데이터를 가져온다. select * from user를 자동으로
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public void updateUser(UserUpdateRequest request){
        // select * from user where id = ? 이 자동으로
        // Optional<User> -> orElseThrow
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);

        user.updateName(request.getName());
    }
    //findById - id를 기준으로 특정한한개의 id를 가져옴
    //Optianal의 orElseThrow를 사용해 User가 없다면 예외를 던진다.
    //User가 있다면 객체를 업데이트 해주고, save 메소드를 호출한다
    //자동으로 UPDATE SQL이 날라간다.
    /*
    save = 주어지는 객체를 저장하거나 업데이트 시켜주다
    Spring Date JPA
    - 복잡한 JPA 코드를 스프링과 함께 쉽게 사용할 수 있도록 도와주는 라이브러리

    */
    @Transactional
    public void deleteUser(String name){
        //SELECT * FROM user WHERE name = ? -> 인터페이스에 함수 생성
        //User user = userRepository.findByName(name); -> User findByName(String name)일떄
        User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
        //Optional 사용 시

        if(user == null){
            throw new IllegalArgumentException();
        }

        userRepository.delete(user);

    }

}

/*
다양한 Spring JPA 쿼리
By앞에 들어갈 수 있는 구절 정리
find : 1건을 가져온다. 반환 타입은 객체가 될 수도 이쏙, Optional<타입>이 될 수 도 있다.
findAll : 쿼리의 결과물이 N개인 경우 사용 List<타입> 반환
exists : 쿼리 결과가 존재하는지 확인. 반환 타입은 boolean
count : SQL의 결과 개수를 센다. 반환 타입은 long이다.

By 뒤에 들어올 수 있는 구절
And 나 Or로 조합할 수 도 있다.
ex) List<User> findAllByNameAge(String name, int age);
 -> select * from user where name = ? and age =?
 GreatherThan : 초과
 GreatherThanEqual : 이상
 LessThan : 미만
 LessThanEqual: 이하
 Between : 사잉[
 StartsWith : ~로 시작하는
 EendsWith: ~로 끝나는

 모든 SQL을 성공시키거나, 모두 실패시키자 - 쪼갤 수 없는 업무의 단위
 -> 트랜잭션
 트랜잭션 시작 - start transaction
 정상종료 - commit
 실패처리 rollback

 @Transactinal 어노테이션 - 아래 있는 함수가 시작 될 때 start transaction 처리
 - 에러 없으면 commit, 있으면  rollback
 SELECT 쿼리만 사용한다면, readOnly 옵션을 쓸 수 있다. -> 성능적 이점
 IOException같은 checked Exception은롤백 안됨

 영속성 컨텍스트 - 테이블과 매핑된 Entitiy 객체를 관리/보관 하는 역할
 스프링에는 트랜잭션을 사용하면 영속성 컨텍스트가 생겨나고
 트랜잭션이 종료되면 영속성 컨텍스트가 종료된다.

 - 영속성 컨텍스트의 특수능력 4가지
 - 1. 변경 감지(Dirty Check) -> 영속성 컨텍스트 안에서 불러와진 Entitiy는 명시적으로 save 하지 않더라고, 변경을 감지해 자동으로 저장된다.
 - 2. 쓰기 지연 - DB의 INSERT/UPDATE/DELETE SQL을 바로 날리는 것이 아니라, 트랜잭션이 commit 될 때 모아서 한 번만 날린다.
 - 3. 1차 캐싱 - ID를 기준으로 Entity를 기억한다.- 이렇게 캐싱된 객체는 완전 동일하다 / 같은 ID의 정보를 넘겨준다.
 - 4. 꼭 필요한 순간에 데이터를 로딩한다 - 지연로딩 @OneToMany의 fetch 옵션
*/