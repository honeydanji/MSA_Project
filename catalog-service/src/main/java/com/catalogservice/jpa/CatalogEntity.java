package com.catalogservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "catalog")
public class CatalogEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 고유식별자
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP") // 기본값을 현재시간
    private Date createdAt;
}


/*  직렬화 이유
    : 자바에 있는 객체 데이터를 파일형식으로 저장하고 외부에서 사용하기 위함.
    외부로 데이터를 보내면 받은 쪽에서 역직렬화를 통해서 데이터 사용이 가능하다.
    (자바에서 유리함)
    하지만 json 같은 경우는 데이터를 외부로 보낼 때 별도의 파싱 작업이 필요하다.
    그렇다고 해서 단점만 있는 건 아니고 장점으로는 java, javascript, python 처럼
    서로 다른 언어에 공통적으로 사용되는 데이터를 쉽게 전송할 수 있다.
 */

/* Serializable
   > Serializable 인터페이스는 아무런 내용도 없는 마커 인터페이스Visit Website 로서,
   > 직렬화를 고려하여 작성한 클래스인지를 판단하는 기준으로 사용된다.
 */



