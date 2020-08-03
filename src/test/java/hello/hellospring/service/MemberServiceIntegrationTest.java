package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//통합테스트. spring container 없이 테스트 할수록 해야한다.
//단위테스트가 더 좋은 테스트다.
@SpringBootTest
//testCase에 추가하면 테스트 한 후에 롤백을 한다. 다음 테스트에 영향을 안준다.
@Transactional
class MemberServiceIntegrationTest {

    //기존 코드는 생성자 testCase는 필드기반 Autowired 편함
    @Autowired MemberService memberService;
    @Autowired MemberRepository memoryMemberRepository;

//    Transactional 때문에 필요가 없어진다.
//    @AfterEach
//    public void afterEach(){
//        memoryMemberRepository.clearStore();
//    }


    @Test
    void 회원가입() {
        //테스트는 상황이 주어진다.
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 중복회원가입() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
//        try {
//            memberService.join(member2);
//            fail();
//        } catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }
        //message 반환한다.
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void findOne() {
    }
}