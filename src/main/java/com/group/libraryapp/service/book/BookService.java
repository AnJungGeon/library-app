package com.group.libraryapp.service.book;


import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;//의존성 주입
    private final UserRepository userRepository;
    public BookService(BookRepository bookRepository, UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
    }
    //스프링 컨테이너를 사용하면
    /*
    컨테이너가 BookService를 대신 인스턴스화하고, 그 때 알아서 BookRepository를 결정해준다.
    ->이런 방식을 제어의 역전이라고 한다.(IoC,Inversion of Control)
    ->컨테이너가 선택해 BookService에 넣어주는 과정을 의존성 주입(DI, Dependency Injection)라고 한다.
    @Primary: 우선권을 결정하는 어노테이션
    - 빈을 등록하는 방법
    @Configuration
    - 클래스에 붙이는 어노테이션
    -@Bean을 사용할 때 함께 사용해 주어야 한다.

    @Bean
    - 메소드에 붙이는 어노테이션
    - 메소드에서 반환되는 객체를 스프링 빈에 등록한다.
    @Service, @Repository는 개발자가 직접 만들 클래스를 스프링 빈으로 등록할 때 많이 씀
    @Configuration + @Bean은 외부 라이브러리, 프레임웨크에서 만든 클래스를 등록할 때 사용

    @Component
    - 1)컨트롤러, 서비스, 레포지토리가 모두 아니고
    - 2)개발자가 직접 작성한 클래스를 스프링 빈으로 등록할 떄 사용되기도 한다.


    // 스프링 빈을 주입 받는 방법
    - 생성자을 이요해 주입받는 방식(권장)-@Autowired 생략 가능
    - setter와 @Autowired 활용 -  누군가 setter를 사용하면 오작동 할 수 있다.
    - 필드에 바로 @Autowierd 사용 - 테스트를 어렵게 만드는 요인

    //@Qualifier
    스프링 빈을 사용하는 쪽, 스프링 빈을 등록하는 쪽 모두 @Qualifier를 사용할 수 있다.
    스프링 빈을 사용하는 쪽에서만 쓰면, 빈의 이름을 적어주어야 한다.
    양쪽 모두 사용하면, @Qualifier 끼리 연결된다.

    @Primary vs @Qualifier
    사용하는 쪽에서 직접 적어준 @Qualifier가 우선된다.
    */

    @Transactional
    public void saveBook(BookCreateRequest request ){
        bookRepository.save(new Book(request.getName()));
    }

    //도서 대출
    @Transactional
    public void loanBook(BookLoanRequest request){
        //1. 책 정보를 가져온다.
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        //2. 대출 기록 정보를 확인해서 대출중인지 확인다.
        //3. 만약 확인했는데 대출 중이라면 예외를 발생시킨다.
        if(userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)){
            throw new IllegalArgumentException("이미 대출되어 있는 책입니다.");
        }

        //4. 유저 정보를 가져온다.
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        user.loanBook(book.getName());

    }

    //도서반납
    @Transactional
    public void returnBook(BookReturnRequest request) {
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

       user.returnBook(request.getBookName());
    }

}
