package silver.silvernote.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import silver.silvernote.domain.Address;
import silver.silvernote.domain.Center;
import silver.silvernote.domain.member.Manager;
import silver.silvernote.domain.member.Member;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CenterMemberRepositoryTest {

    @Autowired
    MemberService memberService;
    @Autowired CenterService centerService;

    @Test
    public void ���͵��() throws Exception {
        //given
        Address address = createAddress("�뱸", "������", "155");

        Center center = createCenter("���ۼ���", "02-0000-0000", "����", address);

        //when
        Long centerId = centerService.save(center);

        //then
        assertThat(centerId).isEqualTo(centerService.findOne(centerId).orElseThrow(NoSuchElementException::new).getId());

    }

    @Test
    public void ����() {
        Address address = createAddress("�뱸", "������", "155");

        Center center = createCenter("���ۼ���", "02-0000-0000", "����", address);

        centerService.save(center);

        Member member = createManager(center,"userC","��", "950512-1779015", "010-9921-4156", address);
        Long memberId = memberService.save(member);

        assertThat(member).isEqualTo(memberService.findOne(memberId).orElseGet(() -> fail("ȸ�����Կ� �����߽��ϴ�")));
    }

    @Test
    public void ��ȸ() {
        Address address = createAddress("�뱸", "������", "155");

        Center center = createCenter("���ۼ���", "02-0000-0000", "����", address);

        Member member = createManager(center,"userC", "��", "930911-1779015", "010-9921-4156", address);
        Long memberId = memberService.save(member);

        assertThat(member).isEqualTo(memberService.findOne(memberId).orElseGet(() -> fail("ȸ�����Կ� �����߽��ϴ�")));
    }

    @Test
    public void ���Խ���_�̸�����(){
        Address address = createAddress("�뱸", "������", "155");

        Center center = createCenter("���ۼ���", "02-0000-0000", "����", address);

        Member member = createManager(center,"","��", "930911-1779017", "010-9921-4156", address);

        assertThrows(ConstraintViolationException.class, () -> memberService.save(member));
    }

    @Test
    public void ���Խ���_�������̵��ߺ�(){
        Address address = createAddress("�뱸", "������", "155");
        Center center = createCenter("���ۼ���", "02-0000-0000", "����", address);

        Member member = createManager(center,"", "��", "930823-1779013", "010-9921-4156", address);

        assertThrows(ConstraintViolationException.class, () -> memberService.save(member));
    }


    private Address createAddress(String city, String street, String zipcode) {
        return Address.
                BuilderByParam()
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .build();
    }

    private Center createCenter(String name, String phone, String description, Address address) {
        return Center.
                BuilderByParam()
                .name(name)
                .phone(phone)
                .description(description)
                .address(address)
                .build();
    }

    private Manager createManager(Center center, String name, String sex, String rrn, String phone, Address address) {
        return Manager.BuilderByParam()
                .center(center)
                .name(name)
                .sex(sex)
                .rrn(rrn)
                .phone(phone)
                .address(address)
                .build();
    }
}