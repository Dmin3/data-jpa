package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //@Query 어노테이션 통해 원하는 dto값 가져오기
    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // Optional

    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // JPA에서 executeUpdate() 메소드 실행해주는 역할, em.clear를 자동으로 처리해줌
    @Query("update Member m set m.age=m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team") // Member 조회할때 Team도 같이 가져옴 -> fetch join
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

//    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) // 내부적으로 성능 최적화
//    Member findReadOnlyByUserName(String username);


}
