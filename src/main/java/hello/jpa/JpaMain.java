package hello.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        
        // persistence.xml 파일에 설정한 unit name  
        // 해당 객체 생성하는 순간 DB연동 등 작업이 가능함
        // EntityManagerFactory 는 하나만 생성해서 애플리케이션 전체에서 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // new 자동 생성 - ctrl + alt + v
        // EntityManager 쓰레드간에 공유 절대 (사용하고 버려야 한다.)
        // ★★ JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 설정, JPA는 트랜잭션 안에서 작동해야함
        // Ctrl + H => 계층 구조 확인
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /*
        JPA에서 가장 중요한 2가지
        객체와 관계형 데이터베이스 매핑하기(Object Relational Mapping - ORM)
        영속성 컨텍스트 - "엔티티를 영구 저장하는 환경", EntityManager.persist(entity);
        엔티티 매니저를 통해 DB가 아닌 영속성 컨텍스트에 저장하는 것
        엔티티 생명주기 .. 비영속, 영속, 준영속, 삭제
         */

        try {

            // 비영속
            Member member = new Member();
//            member.setId(101L);
//            member.setName("Hello JPA!");

            // 영속(DB에 쿼리가 날라가는게 아님) , COMMIT 시점
            System.out.println("== BEFORE ==");
            em.persist(member);
            //em.flush();
            System.out.println("== AFTER ==");

            // 조회를 할 때 SELECT 쿼리를 안날림 => 1차 캐시 위에서 persist() 에서 DB가 아닌 영속 컨텍스트에 저장
            Member findMember = em.find(Member.class, 101L);

            System.out.println("id: " + findMember.getId());
            //System.out.println("name: " + findMember.getName());


            // INSERT
            /*Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");

            em.persist(member);*/

            // UPDATE
            // JPA가 관리, 트랜잭션 변경 시 업데이트 쿼리문 날림
            /*Member findMember = em.find(Member.class, 1L);
            findMember.setName("Update JPA");*/

            // JQPL 멤버 객체를 대상으로 쿼리, 대상이 테이블이 아닌 멤버 객체
            // JPA를 사용하면 Entity 객체를 중심으로 개발
            // 문제는 검색 쿼리, 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
            // 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
            // 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요함
            // JPA는 SQL을 추상화한 JPQL이라는 검색 쿼리 언어 제공
            List<Member> result = em.createQuery("SELECT m FROM Member as m", Member.class)
                    .setFirstResult(5)  // 페이징 5번부터
                    .setMaxResults(10)  // 페이징 10번까지 limit , offset
                    .getResultList();
            


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
