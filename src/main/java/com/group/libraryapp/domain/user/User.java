package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // 스프링이 User 객체와 user 테이블을 같은 것으로 바라본다./ 저장되고 , 관리되어야 하는 데이터
public class User {

    @Id //이 필드를 primary key로 간주한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)//primary key는 자동 생성되는 값이다. mysql용
    private Long id = null; //bigint - long
    @Column(nullable = false, length = 20) // name varchar(20) DB에서 Column 명 같으면 생략 가능

    private String name;
    //null이어도 되고 Integer와 int는 동일하기에 @Column 생략 가능
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //1 대 n 관계를 만들어 주기 위해 리스트 생성
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();
    protected User(){};//매개변수가 없는 기본 생성자
    //JPA를 사용하기 위해서는 기본 생성자가 필요하다
    // JPA는 추가 설정이 필요하가 application.yml에 추가

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        this.name = name;
        this.age = age;
        }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void loanBook(String bookName){
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    public void returnBook(String bookName){
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName))//.filter -> 조건이 충족되는 것만 필터링
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        targetHistory.doReturn();
    }
}

