package study.datajpa.entity;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
public class Member extends JpaBaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null){
        changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    //setter 대신 사용가능
    public void changeMember(String username){
        this.username = username;
    }

    //==연관관계 메소드==//
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
