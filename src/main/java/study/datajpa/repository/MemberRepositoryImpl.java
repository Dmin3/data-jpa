package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    // MemberRepository + Impl <- 규칙!!

    private final EntityManager em;


    @Override
    public List<Member> findMemberCuostom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
