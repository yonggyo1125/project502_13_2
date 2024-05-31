# 실무 프로젝트(P-2)

## [공통 구성](https://github.com/yonggyo1125/project502_13_2)

### 마이바티스 설정 

> 마이바티스는 설정 및 사용 방법을 공식 사이트에서 참고 할 수 있습니다.
> [공식사이트](https://mybatis.org/mybatis-3/ko/index.html)

#### 데이터 베이스 연결 설정
> resources/org/choongang/global/configs/mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties>
        <property name="driver" value="oracle.jdbc.driver.OracleDriver" />
        <property name="url" value="jdbc:oracle:thin:@localhost:1521:XE" />
        <property name="username" value="PROJECT2_1" />
        <property name="password" value="oracle" />
    </properties>
    <environments default="dev">
        <environment id="dev">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    
    ....
    
</configuration>
```

#### 회원 테이블

> 테이블 및 시퀀스 생성

```
CREATE SEQUENCE SEQ_MEMBER;

CREATE TABLE MEMBER (
    USER_NO NUMBER(10) PRIMARY KEY, 
    USER_ID VARCHAR2(30) UNIQUE NOT NULL,
    USER_PW VARCHAR2(65) NOT NULL,
    USER_NM VARCHAR2(40) NOT NULL,
    REG_DT DATE DEFAULT SYSDATE,
    MOD_DT DATE
);
```

#### 데이터 조회 결과를 담을 수 있는 클래스 추가

> 데이터 베이스에서 조회한 레코드 한개 한개를 entity라 하므로 조회 결과를 담는 VO(Value Object)  클래스를 entity 클래스라고 하고 정의

> org/choongang/member/entities/Member.java

```java
package org.choongang.member.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Member {
    private long userNo;
    private String userId;
    private String userPw;
    private String userNm;
    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
```

#### 매퍼 xml 설정
> resources/org/choongang/member/mapper/MemberMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.choongang.member.mapper.MemberMapper">
    <resultMap id="memberMap" type="org.choongang.member.entities.Member">
        <result column="USER_NO" property="userNo" />
        <result column="USER_ID" property="userId" />
        <result column="USER_PW" property="userPw" />
        <result column="USER_NM" property="userNm" />
        <result column="REG_DT" property="regDt" />
        <result column="MOD_DT" property="modDt" />
    </resultMap>
</mapper>
```
> resultMap은 테이블의 컬럼과 엔티티 클래스의 멤버변수명과 일치하지 않는 경우 맞춰주기 위해 정의한다.


#### 마이바티스 설정에 매퍼 추가

> resources/org/choongang/global/configs/mybatis-config.xml
> mappers 태그 안쪽에 \<mapper resource='경로' ../\> 형태로 입력
> 경로는 src/main/resources 하위 경로로 다음과 같이 입력 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    ...

    <mappers>
        <mapper resource="org/choongang/member/mapper/MemberMapper.xml" />
    </mappers>
</configuration>
```

> MemberMapper 인터페이스 추가 
> src/java/org/choongang/member/mapper/MemberMapper.java
> MemberMapper.xml과 동일한 패키지 경로, 동일 파일명으로 인터페이스를 만들면 MemberMapper에 정의한 select, insert, update, delete 태그의 id명으로 추상 메서드만 만들어도 서로 연결이 됩니다.

```java
package org.choongang.member.mapper;

public interface MemberMapper {
    
}
```


#### 로거 설정(slf4j)

> 의존성 추가
> build.gradle dependencies 항목에 다음과 같이 추가 

```groovy
...
dependencies {
    ...
    
    implementation 'org.slf4j:slf4j-api:2.0.13'
    implementation 'ch.qos.logback:logback-classic:1.5.6'
    
    ...
}
...
```

> 설정 추가
> resources/logback.xml
> logger태그는 매퍼가 추가되면 해당 경로를 더 추가하여 로깅이 될수 있도록 합니다.

```xml
<?xml version="1.0" encoding="UTF-8" ?>

<configuration>
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d %5p %c{2} - %m%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="stdout" />
    </root>

    <logger name="org.choongang.member.mapper" level="DEBUG" />
</configuration>
```

### 마이바티스 설정 반영 및 SqlSession 객체 생성 소스 구현 

> org/choongang/global/configs/DBConn.java

```java
package org.choongang.global.configs;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class DBConn {
    private static SqlSessionFactory factory;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("org/choongang/global/configs/mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }

    public static SqlSession getSession() {
        return getSession(true);
    }
}
```


### 회원 가입

> MemberMapper.xml에 회원 관련 SQL 추가
> resources/org/choongang/member/mapper/MemberMapper.xml
> 
```xml
...

<mapper namespace="org.choongang.member.mapper.MemberMapper">
    ...
    <!-- 회원 목록 조회 -->
    <select id="getList" resultMap="memberMap">
        <bind name="pUserId" value="'%' + _parameter.getUserId() + '%'" />
        <bind name="pUserNm" value="'%' + _parameter.getUserNm() + '%'" />
        <bind name="pKeyword" value="'%' + _parameter.getKeyword() + '%'" />
        SELECT * FROM (SELECT ROWNUM NUM, t.* FROM MEMBER t) m
        <where>
            <![CDATA[m.NUM >= {sRow} AND m.NUM <= {eRow}]]>
            <if test="userId != null">
                AND USER_ID LIKE #{pUserId}
            </if>
            <if test="userNm != null">
                AND USER_NM LIKE #{pUserNm}
            </if>
            <if test="keyword != null">
                AND CONCAT(USER_ID, USER_NM) LIKE #{pKeyword}
            </if>
        </where>
        ORDER BY m.regDt DESC
    </select>

    <!-- 회원 개별 조회 -->
    <select id="get" resultMap="memberMap">
        SELECT * FROM MEMBER
        <where>
            <if test="userId != null">
                USER_ID=#{userId}
            </if>
        </where>
    </select>

    <!-- 회원 목록 갯수, 페이징 구현시 활용 가능 -->
    <select id="getTotal" resultType="long">
        <bind name="pUserId" value="'%' + _parameter.getUserId() + '%'" />
        <bind name="pUserNm" value="'%' + _parameter.getUserNm() + '%'" />
        <bind name="pKeyword" value="'%' + _parameter.getKeyword() + '%'" />
        SELECT COUNT(*) FROM MEMBER
        <where>
            <if test="userId != null">
                AND USER_ID LIKE #{pUserId}
            </if>
            <if test="userNm != null">
                AND USER_NM LIKE #{pUserNm}
            </if>
            <if test="keyword != null">
                AND CONCAT(USER_ID, USER_NM) LIKE #{pKeyword}
            </if>
        </where>
    </select>

    <!-- 회원 등록 여부 파악 - 갯수가 나오면 이미 등록으로 판단 -->
    <select id="exist" resultType="int">
        SELECT COUNT(*) FROM MEMBER WHERE USER_ID=#{userId}
    </select>

    <!-- 회원 등록 -->
    <insert id="register">
        <selectKey keyProperty="userNo" order="BEFORE" resultType="long">
            SELECT SEQ_MEMBER.NEXTVAL FROM DUAL
        </selectKey>

        INSERT INTO MEMBER (USER_NO, USER_ID, USER_PW, USER_NM)
        VALUES (#{userNo}, #{userId}, #{userPw}, #{userNm})
    </insert>

    <!-- 회원 정보 수정 -->
    <update id="modify">
        UPDATE MEMBER
        <set>
            <if test="userPw != null">
                USER_PW=#{userPw},
            </if>
            <if test="userNm != null">
                USER_NM=#{userNm},
            </if>
            MOD_DT = SYSDATE
        </set>
        WHERE USER_ID=#{userId}
    </update>

    <!-- 회원 삭제 -->
    <delete id="delete">
        DELETE FROM MEMBER WHERE USER_ID=#{userId}
    </delete>
</mapper>
```

> 검색을 위한 데이터 전달 클래스 정의 
> org/choongang/member/controllers/SearchMember.java

```java
package org.choongang.member.controllers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMember {
    private String userId;
    private String userNm;
    private String keyword;
    private int sRow; // 페이징 시작 번호
    private int eRow; // 페이징 종료 번호
}
```

> 인터페이스에 SQL 매핑 추상 메서드 추가
> org/choongang/member/mapper/MemberMapper.java

```java
package org.choongang.member.mapper;

import org.choongang.member.controllers.SearchMember;
import org.choongang.member.entities.Member;

import java.util.List;

public interface MemberMapper {
    List<Member> getList(SearchMember search);
    Member get(String userId);
    long getTotal(SearchMember search);
    int exist(String userId);
    int register(Member member);
    int modify(Member member);
    int delete(String userId);
}
```

> 회원 가입 서비스 
> 회원 가입시 처리할 부분 
> 1. 전달 받은 데이터 유효성 검사 : 유효성 검사 실패시 ValidationException 발생시킴
>   - 필수 데이터 체크(아이디, 비밀번호, 비밀번호 확인, 회원명)
>   - 아이디 자리수 체크(6자리 이상)
>   - 비밀번호 자리수 체크(8자리 이상)
>   - 아이디 중복 여부 체크
>   - 비밀번호와 비밀번호 확인 일치 여부
> 
> 2. 비밀번호 해시화
>    - 비밀번호를 그대로 데이터베이스에 저장하는 것은 보안상 바람직하지 않음 
>    - 비밀번호는 해시화하여 저장 
>    - 암호화는 2가지 
>      - 복호화(원 데이터로 복구 가능) 가능 암호화(양방향)
>      - 해시화(원 데이터로 복구 불가 - 단방향)
>       - 해시화 역시 같은 값에 대한 같은 값으로 해시가 만들어지는 고정해시(SHA1,SHA256, SHA512, MD5 등등)
>       - 해시를 만들때마다 바뀌는 유동해시가 있음(예 - BCrypt), 보안은 예측 불가해야 강해짐, 따라서 비밀번호와 같은 민감한 데이터는 맞는지 여부만 체크할 수 있는 유동해시를 사용해야 함
> 
> 3. 데이터베이스에 영구 저장 처리


> 비밀번호 해시화를 위한 JBCrypt 의존성 추가
> build.gradle

```groovy
...
dependencies {
    ...
    
    implementation 'org.mindrot:jbcrypt:0.4'
    
    ...
}
...
```

> 공통 예외 정의
> org/choongang/global/exceptions/CommonException.java

```java
package org.choongang.global.exceptions;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private int code; // 에러 코드

    public CommonException(String message, int code) {
        super(message);
        this.code = code;
    }
}
```

> 공통 예외 정의 - 유효성 검사 실패시
> org/choongang/global/exceptions/ValidationExcetpion.java

```java
package org.choongang.global.exceptions;

public class ValidationException extends CommonException{

    // 유효성 검사 실패 오류 코드는 400으로 정함
    public ValidationException(String message) {
        super(message, 400);
    }
}

``` 

> 공통 Validator 인터페이스 정의
> org/choongang/global/validators/Validator.java

```java
package org.choongang.global.validators;

public interface Validator<T> {
    void check(T form);
}
```

> 공통 필수항목 체크 인터페이스 정의
> org/choongang/global/validators/RequiredValidator.java

```java
package org.choongang.global.validators;

/**
 * 필수 항목 체크
 * 
 */
public interface RequiredValidator {
    default void checkRequired(String str, RuntimeException e) {
        if (str == null || str.isBlank()) {
            throw e;
        }
    }
}
```


> 회원가입 Validator 정의
> org/choongang/member/validators/JoinValidator.java

```java
package org.choongang.member.validators;

import org.choongang.global.exceptions.ValidationException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.mapper.MemberMapper;

public class JoinValidator implements Validator<RequestJoin>, RequiredValidator {

    private final MemberMapper mapper;

    public JoinValidator(MemberMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void check(RequestJoin form) {
        // 필수 항목 체크 S
        String userId = form.getUserId();
        String userPw = form.getUserPw();
        String confirmPw = form.getConfirmPw();
        String userNm = form.getUserNm();

        checkRequired(userId, new ValidationException("아이디를 입력하세요."));
        checkRequired(userPw, new ValidationException("비밀번호를 입력하세요."));
        checkRequired(confirmPw, new ValidationException("비밀번호 확인을 입력하세요."));
        checkRequired(userNm, new ValidationException("회원명을 입력하세요."));
        // 필수 항목 체크 E

        // 아이디 자리수 체크(6자리 이상)
        if (userId == null || userId.length() < 6) {
            throw new ValidationException("아이디는 6자리 이상 입력하세요.");
        }

        // 아이디 중복 여부 체크
        if (mapper.exist(userId) > 0) {
            throw new ValidationException("이미 등록된 아이디 입니다.");
        }

        // 비밀번호 자리수 체크(8자리 이상)
        if (userPw == null || userPw.length() < 8) {
            throw new ValidationException("비밀번호는 8자리 이상 입력하세요.");
        }

        // 비밀번호, 비밀번호 확인 일치 여부 체크
        if (userPw != null && confirmPw != null && !userPw.equals(confirmPw)) {
            throw new ValidationException("비밀번호가 일치하지 않습니다.");
        }
    }
}
```

> org/choongang/member/services/JoinService.java
> 회원가입 기능을 구현하기 위한 의존성 (MemberMapper, JoinValidator)

```java
package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.Service;
import org.choongang.global.exceptions.ValidationException;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.validators.JoinValidator;
import org.mindrot.jbcrypt.BCrypt;

@RequiredArgsConstructor
public class JoinService implements Service<RequestJoin> {

    private final MemberMapper mapper;
    private final JoinValidator validator;

    @Override
    public void process(RequestJoin form) {
        // 회원 가입 유효성 검사
        validator.check(form);

        // 비밀번호 해시화
        String userPw = BCrypt.hashpw(form.getUserPw(), BCrypt.gensalt(10));


        // 데이터베이스에 영구 저장
        Member member = Member.builder()
                        .userId(form.getUserId())
                        .userPw(userPw)
                        .userNm(form.getUserNm())
                        .build();
        int affectedRows = mapper.register(member);

        if (affectedRows < 1) { // 회원 가입 처리 실패시
            throw new ValidationException("회원가입 실패하였습니다.");
        }

    }
}
```


> org/choongang/member/services/MemberServiceLocator.java
> joinValidator(), memberMapper() : 의존 객체 생성 메서드 추가
> new JoinService(memberMapper(), joinValidator()); 와 같이 의존 객체 생성자에 주입

```java

...

public class MemberServiceLocator extends AbstractServiceLocator {

    ...

    // 회원가입 유효성 검사 Validator
    public JoinValidator joinValidator() {
        return new JoinValidator(memberMapper());
    }

    // MemberMapper 인터페이스 구현체
    public MemberMapper memberMapper() {
        return DBConn.getSession().getMapper(MemberMapper.class);
    }

    @Override
    public Service find(Menu mainMenu) {
        
        ...

        switch (mainMenu) {
            case JOIN: service = new JoinService(memberMapper(), joinValidator()); break;
            case LOGIN: service = new LoginService(); break;
        }

        ...
    }
}

```

#### 회원가입 서비스 기능 테스트 


- 회원 가입 성공시 예외 발생 없음
> src/test/java/.../member/services/JoinServiceTest.java

```java
package org.choongang.global.validators;

/**
 * 필수 항목 체크
 *
 */
public interface RequiredValidator {
    default void checkRequired(String str, RuntimeException e) {
        if (str == null || str.isBlank()) {
            throw e;
        }
    }
}
```

- 아이디 자리수(6자리) 또는 비밀번호 자리수(8자리) 맞지 않는 경우 예외 발생 체크
- 비밀번호, 비밀번호 확인 불일치 예외 발생 체크
- 아이디 중복시 예외 발생 체크 

### 로그인 

#### 로그인 구현 절차

- 아이디, 비밀번호 필수 입력 유효성 체크 
- 아이디로 조회된 회원이 있는지 체크
- 조회된 회원 데이터의 비밀번호와 사용자가 입력한 비밀번호 일치 여부 체크(BCrypt 해시 검증)
- 일치하면 애플리케이션 전역에 회원정보 유지


> 로그인 유효성 검사
> org/choongang/member/validators/LoginValidator.java

```java
package org.choongang.member.validators;

import org.choongang.global.exceptions.ValidationException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.controllers.RequestLogin;
import org.choongang.member.entities.Member;
import org.choongang.member.mapper.MemberMapper;
import org.mindrot.jbcrypt.BCrypt;

public class LoginValidator implements Validator<RequestLogin>, RequiredValidator {

    private final MemberMapper mapper;

    public LoginValidator(MemberMapper mapper) {
        this.mapper = mapper;
    }
    /**
     * 1. 아이디, 비밀번호 필수 입력 유효성 체크
     * 2. 아이디로 조회된 회원이 있는지 체크
     * 3. 조회된 회원 데이터의 비밀번호와 사용자가 입력한 비밀번호 일치 여부 체크(BCrypt 해시 검증)
     * @param form
     */
    @Override
    public void check(RequestLogin form) {
        String userId = form.getUserId();
        String userPw = form.getUserPw();

        // 아이디, 비밀번호 필수 입력 유효성 체크
        checkRequired(userId, new ValidationException("아이디를 입력하세요."));
        checkRequired(userPw, new ValidationException("비밀번호를 입력하세요."));

        // 아이디로 조회된 회원이 있는지, 있다면 비밀번호가 일치하는지 체크
        String message = "아이디 또는 비밀번호가 일치하지 않습니다.";
        Member member = mapper.get(userId);
        if (member == null || !BCrypt.checkpw(userPw, member.getUserPw())) {
            throw new ValidationException(message);
        }
    }
}
```
> 로그인 상태 유지
> org/choongang/member/MemberSession.java


```java
package org.choongang.member;

import org.choongang.member.entities.Member;

public class MemberSession {
    private static Member member;

    /**
     * 로그인은 회원 정보를 애플리케이션 전역에 유지하면 되므로
     * DB에서 조회된 member를 정적 변수 member에 할당
     * @param member
     */
    public static void login(Member member) {
        MemberSession.member = member;
    }

    /**
     * 로그인 여부 체크
     * 정적 변수인 member에 회원 정보가 유지되고 있으면 로그인 상태이다.
     * @return
     */
    public static boolean isLogin() {
        return member != null;
    }

    /**
     * 로그아웃은 정적 변수인 member의 값을 비워 주면 된다.
     *
     */
    public static void logout() {
        member = null;
    }

    public static Member getMember() {
        return member;
    }
}
```

> 로그인 처리
> org/choongang/member/services/LoginService.java

```java
package org.choongang.member.services;

import org.choongang.global.Service;
import org.choongang.member.MemberSession;
import org.choongang.member.controllers.RequestLogin;
import org.choongang.member.entities.Member;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.validators.LoginValidator;

public class LoginService implements Service<RequestLogin> {

    private final LoginValidator validator;
    private final MemberMapper mapper;


    public LoginService(MemberMapper mapper, LoginValidator validator) {
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public void process(RequestLogin form) {
        // 로그인 유효성 검사 - 예외 발생하지 않으면 이상 없음
        validator.check(form);

        // 회원정보 조회
        Member member = mapper.get(form.getUserId());

        // 로그인 처리
        MemberSession.login(member);
    }
}
```

> MemberServiceLocator에 LoginValidator와 LoginService 객체 생성시 의존 객체 주입
> org/choongang/member/services/MemberServiceLocator.java

```java

...

public class MemberServiceLocator extends AbstractServiceLocator {

    ...

    // 로그인 유효성 검사 Validator
    public LoginValidator loginValidator() {
        return new LoginValidator(memberMapper());
    }

    ...
    
    @Override
    public Service find(Menu mainMenu) {
        
        ...

        switch (mainMenu) {
            case JOIN: service = new JoinService(memberMapper(), joinValidator()); break;
            case LOGIN: service = new LoginService(memberMapper(), loginValidator()); break;
        }

        ...
    }
}
```
> 로그인 기능 테스트
> src/test/java/.../member/services/LoginServiceTest
```java
package org.choongang.member.services;

import org.choongang.global.Service;
import org.choongang.global.constants.MainMenu;
import org.choongang.member.MemberSession;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.controllers.RequestLogin;
import org.choongang.member.entities.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Service loginService;
    private Service joinService;
    private RequestLogin loginForm;
    private RequestJoin joinForm;

    @BeforeEach
    void init() {
        /**
         * 로그인 테스트는 회원 가입을 한 후 가입한 회원 정보로
         * 로그인이 되는지 테스트 한다. 즉, 가입먼저 시키고 테스트 진행
         */
        joinService = MemberServiceLocator.getInstance().find(Menu.JOIN);
        loginService = MemberServiceLocator.getInstance().find(Menu.LOGIN);

        joinForm = RequestJoin.builder()
                .userId("u" + System.currentTimeMillis())
                .userPw("12345678")
                .confirmPw("12345678")
                .userNm("사용자")
                .build();

        joinService.process(joinForm);

        loginForm = RequestLogin.builder()
                .userId(joinForm.getUserId())
                .userPw(joinForm.getUserPw())
                .build();
    }

    @Test
    @DisplayName("로그인 성공시 예외 발생 하지 않고, 로그인 상태 true, 회원정보 유지됨")
    void loginSuccessTest() {
        // 성공한다면 예외가 없어야 한다.
        assertDoesNotThrow(() -> loginService.process(loginForm));

        // 로그인 상태가 true
        assertTrue(MemberSession.isLogin());

        // 회원 정보가 있는지 체크
        Member member = MemberSession.getMember();
        assertEquals(loginForm.getUserId(), member.getUserId());
    }
}
```

> 메인 템플릿 - 로그인시 회원 전용 메뉴 변경 처리
> org/choongang/template/main/MainTpl.java

```java
package org.choongang.template.main;

import org.choongang.member.MemberSession;
import org.choongang.member.entities.Member;
import org.choongang.template.Template;

public class MainTpl implements Template {
    @Override
    public String getTpl() {
        StringBuffer sb = new StringBuffer(2000);

        sb.append("메뉴를 선택하세요.\n");
        if (MemberSession.isLogin()) {
            Member member = MemberSession.getMember();
            sb.append(String.format("%s(%s)님 로그인\n", member.getUserNm(), member.getUserId()));
            sb.append("1. 학생관리\n");
            sb.append("2. 출결관리\n");
            sb.append("3. 성적관리\n");
            sb.append("4. 로그아웃\n");
        } else {
            sb.append("1. 회원가입\n");
            sb.append("2. 로그인\n");
        }
        sb.append("--------------------------------\n");

        return sb.toString();
    }
}
```

## [묵찌빠 게임](https://github.com/yonggyo1125/project502_13_2/tree/game)

## [학생관리프로그램](https://github.com/yonggyo1125/project502_13_2/tree/lms)

### 데이터베이스 스키마 생성

- 도커 컨테이너 접속

```
docker exec -it oracle-xe /bin/bash
```

- sqlplus system 계정 접속

```
sqlplus system/oracle
```

- PROJECT2_2 사용자 및 스키마 생성 
- CONNECT, RESOURCE 롤 권한 부여
```
CREATE USER PROJECT2_2 IDENTIFIED BY oracle QUOTA LIMITED ON USERS;
GRANT CONNECT, RESOURCE TO PROJECT2_2;
```

### 서브 메뉴 분리 작업

> 주 메뉴, 서브 메뉴 기본 인터페이스
> org/choongang/global/Menu.java

```java
package org.choongang.global;

public interface Menu {
}
```

> 기 Menu

