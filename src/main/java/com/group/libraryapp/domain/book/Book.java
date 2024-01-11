package com.group.libraryapp.domain.book;

import javax.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false)
    private String name;

    protected Book(){//JPA는 기본생성자 필요

    }

    public Book(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException(String.format("잘못된 이름이 들어왔습니다."));
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
