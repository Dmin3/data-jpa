package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("MemberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();


        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("m1", 10);

        assertThat(result.get(0)).isEqualTo(m1);


    }

    @Test
    public void findMemberDto(){
        Team team = new Team("TeamA");
        teamRepository.save(team);
        Member member = new Member("AAA", 20);
        member.setTeam(team);
        memberRepository.save(member);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto =" + dto);
        }
    }
    
    @Test
    public void findByNames(){
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("m1", "m2"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        Member findMember = memberRepository.findMemberByUsername("m1");
        System.out.println("findMember = " + findMember);


    }

    @Test
    public void testPaging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        int age = 10;
        //when
        //반환타입이 Page면 totalCount 쿼리까지 같이 날림
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//        System.out.println("totalElements = " + totalElements);
//

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
       //em.flush();
        // em.clear();

        assertThat(resultCount).isEqualTo(3);


    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> TeamA
        //member2 -> TeamB

        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear(); // 영속성 컨텍스트 클리어 후 DB 완전 반영

        //when
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getClass = " + member.getTeam().getClass());
            System.out.println("member.Team = " + member.getTeam().getName());
        }


    }

//    @Test
//    public void queryHint() {
//        //given
//        Member member1 = new Member("member1", 10);
//        memberRepository.save(member1);
//        em.flush();
//        em.clear();
//
//        //when
//        Member findMember = memberRepository.findReadOnlyByUserName("member1");
//        findMember.setUsername("member2");
//        em.flush();
//
//    }


    @Test
    public void testEntity(){

    }

}