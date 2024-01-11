package com.group.libraryapp.domain.user.loanhistory;

import com.group.libraryapp.domain.user.User;

import javax.persistence.*;

@Entity
public class UserLoanHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;


    //user 테이블과 user_loan_history 테이블의 연관관게 주인 설정
    // 연관관계 주인 : 테이블을 보았을 때 누가 관계의 주도권을 가지고 있는가
    // 아닌 쪽에 mappedBy를 달아주어야 한다.
    // 연관관계의 주인의 값이 설정되어야만 진정한 데이터가 저장된다
    // 연관관계 주인 효과 : 객체가 연결되는 기준이 된다.
    //1. 상대 테이블을 참조하고 있으면 연관관계의 주인
    //2. 연관관계의 주인이 아니면 mappedBy를 사용
    //3. 연관관계의 주인의 setter가 사용되어야만 테이블 연결
    //연관관계 주의할 점 : 트랜잭션이 끝나지 않았을 때 한 쪽만 연결해두면 반대 쪽은 알 수 없다.- >  setter를 한번에 둘을 이어주자
    //N:1관계는 무조건 주인은 N쪽
    //JoinColum 연관관계의 주인이 활용할 수 있는 어노테이션
    //필드의 이름이나 null여부, 유일성 여부, 업데이트 여부 등을 지정
    //@ManyToMany는 권장X
    //cascade옵션 한 객체가 저장되거나 삭제될 떄, 연결되어 있는 객체도 함께 저장되어가 삭제되는 기능
    //orphanRemoval 옵션 - 객체간에 관계가 끊어진 옵션을 자동으로 제거하는 옵션
    // 연관관계를 사용하면 - 각자의 역할에 집중하게 된다.
    // 새로운 개발자가 코드를 읽응ㄹ 떄 이해하기 쉬워진다.
    // 테스트 코드 작성이 쉬워진다.
    // 지나친 사용은 성능상의 문제, 도메인간 복잡한 연결로 인해 시스템을 파악하기 어려워질 수 있다.
    // 너무 얽혀있으면 A 수정시 B,C,D 등에 영향 - 비지니스 요구사항, 기술적인 요구사항, 도메인 아키텍처 등 여러 부분을 고민해서 연관관계 사용을 사용해야 한다.
    @ManyToOne // n:1관계 ex) 학생 : 교실
    //단반향으로 사용 가능
    private User user;

    private String bookName;

    private boolean isReturn;
    protected UserLoanHistory(){

    }
    public UserLoanHistory( User user, String bookName) {

        this.user = user;
        this.bookName = bookName;
        this.isReturn = false;
    }

    public void doReturn(){
        this.isReturn = true;
    }

    public String getBookName(){
        return this.bookName;
    }
}
