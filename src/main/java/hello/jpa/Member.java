package hello.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) default 'EMPTY'") // false: not null, DB 컬럼 정보를 직접 부여함
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) // default인 ORDINAL (순서) 절대 사용 X 변경에 유연하지 않음
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob // VARCHAR를 넘어서는 큰 컨텐츠, 내용
    private String description;

    @Transient  // DB와 연관성 x(메모리에서만 사용)
    private int temp;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getAge() {
        return age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }
}
