#KCS 프로젝트

## 구현기술
    Spring, Spring Data JPA, QueryDsl, H2 Database

근거 
1. 현재 웹사이트 구현시 가장 대중적으로 사용하는 프레임워크로 가장많은 Document를 제공하여 원하는 정보를 얻기에 편리
2. 비지니스 요건자체가 복잡하지 않으므로 개발생산성을 위해 ORM 기술을 사용 JPA는 그중 가장 Docuemnt가 많아서 사용
3. QuertDsl은 코드가 직관적이고 컴파일시점, 런타임시점에 에러를 발생시켜 서비스가 올라가기전에 장애를 방지할수있어서 사용
4. 서비스기동시 바로 DB에 접근이 가능하게 하기위해 인메모리 DB인 H2 사용

##1. DB 모델링 참고사항  
    PK를 Long타입으로 구성한 이유

1. PK가 변경되었을때 보다 유연하게 대처가 가능
2. JPA 에서는 PK 값에 변경을 지원하지 않음 ( 실사례로 복합키구조에 데이터기반 테이블에서 PK컬럼중 하나인 상태코드를 변경할수 없어서 NativeQuery 로 구현한 사례가 있음 )
3. 데이터기반에 테이블구조로 PK 컬럼을 구성했을때 복합키 엔티티 매핑이 어려우며 유지보수가 어렵다고 느껴짐
4. 인터넷참고사항 : https://jwkim96.tistory.com/166

결론 : PK컬럼은 BIGINT 타입의 대리키로 대체하고 유니크제약조건 및 인덱스를 추가하여 처리

##2. 사용자 테이블 모델링이 심플한 이유
    카카오, 휴대폰인증 데이터중 서비스에 사용할 데이터에 유의미한 데이터가 별로없는것같은데...

1. 사용자테이블을 메인으로 인증정보 테이블을 상속구조로 구현할까했지만 서비스에서 사용할 데이터 기준 카카오, 휴대폰인증에서 차이가 나는 데이터는 사용자식별키정보밖에 없어서 굳이 상속구조로 만들필요가 있을까라는 생각 
2. 사용자테이블을보면 IDENTIFICATION_INFO (사용자식별키) 정보가 존재하는데 카카오 공식 API 사이트에 기재된 내용을보면 본사는 본인인증기관이 아니므로 CI를 제공하지않고 사용자를 식별할수있는 유일키를 지원한다고 기재되어있어 IDENTIFICATION_INFO 컬럼은 카카오기준 카카오에서 제공하는 사용자식별키를 저장하며 휴대폰인증은 CI를 저장한다.

결론 : 현재는 나만의 생각으로 구현하였기떄문에 팀과 의견을 공유하여 사용자 인증정보를 상속구조로 만들수도있으며 그외에 엔티티 및 API 스펙도 요건이나 기획에따라 얼마든지 변경될수있기에 현재 프로젝트는 샘플용 프로젝트로 생각하고 구현



