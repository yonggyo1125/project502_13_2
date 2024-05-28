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
            <if test="userNo != null">
                USER_NO=#{userNo}
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
        VALUES {#{userNo}, #{userId}, #{userPw}, #{userNm})
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

```



### 로그인 



## [묵찌빠 게임]()
## [학생관리프로그램]()

