package silver.silvernote.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silver.silvernote.domain.member.*;
import silver.silvernote.repository.member.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional // (readOnly = true) // 조회만 하는 서비스에선 readOnly = true 사용 권장, 성능이 좋다
// 생성자주입 때 lombok을 쓸 거면 @AllArgsConstructor | @RequiredArgsConstructor(final 요소만)를 사용
public class MemberService {
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;
    private final FamilyRepository familyRepository;

    /**
     * 회원가입
     */
    // 쓰기를 해야하는 서비스는 readOnly = false, default이면서 우선권도 false가 높다
    @Transactional
    public Long save(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 정보 수정
     */
    @Transactional
    public Long updateData(Long id, String email, String phone, String address, String zipcode) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        member.updateData(email, phone, address, zipcode);
        memberRepository.save(member);

        return member.getId();
    }


    @Transactional
    public Long changeManager(Long patientId, Long managerId){
        Patient patient = patientRepository.findById(patientId).orElseThrow(NoSuchElementException::new);
        Member manager = memberRepository.findById(managerId).orElseThrow(NoSuchElementException::new);

        patient.changeManager(manager);
        patientRepository.save(patient);

        return patient.getId();
    }

    @Transactional
    public Long updateGrade(Long id, int grade){
        Patient patient = patientRepository.findById(id).orElseThrow(NoSuchElementException::new);
        patient.updateGrade(grade);
        patientRepository.save(patient);

        return patient.getId();
    }
    /**
     * 가입 상태 수정
     */
    @Transactional
    public Long updateLoginData(Long id, String loginId, String password){
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        member.updateLoginData(loginId, password);
        member.updateStatus(JoinStatus.JOINED);
        memberRepository.save(member);

        return member.getId();
    }


    /**
     * 가입 상태 수정
     */
    @Transactional
    public Long updateStatus(Long id, JoinStatus status) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        member.updateStatus(status);
        memberRepository.save(member);

        return member.getId();
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public Long deleteMember(Long id){
        memberRepository.deleteById(id);
        return id;
    }

    /**
     * 센터별 전체 회원 조회
     */
    public List<Manager> findManagersByCenterId(Long centerId) { return managerRepository.findAllByCenterId(centerId);}
    public List<Employee> findEmployeesByCenterId(Long centerId) { return employeeRepository.findAllByCenterId(centerId);}
    public List<Patient> findPatientsByCenterId(Long centerId) { return patientRepository.findAllByCenterId(centerId);}
    public List<Family> findFamilyByCenterId(Long centerId) { return familyRepository.findAllByCenterId(centerId);}

    public List<Member> findMembersByIds(List<Long> ids) { return memberRepository.findAllById(ids);}

    /**
     * 역할별 전체 회원 조회
     */
    public List<Manager> findManagers() {
        return managerRepository.findAll();
    }
    public List<Manager> findWaitingManagers() { return managerRepository.findManagersByStatus(JoinStatus.WAITING); }

    public List<Employee> findEmployees() {
        return employeeRepository.findAll();
    }
    public List<Patient> findPatients() {
        return patientRepository.findAll();
    }
    public List<Family> findFamilies() { return familyRepository.findAll(); }

    /**
     * 개별 회원 조회
     */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
    public Optional<Member> findOneByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
    public Optional<Member> findOneByPassword(String password) {
        return memberRepository.findByPassword(password);
    }

    public Optional<Member> findOne(String name, String rrn) { return memberRepository.findByNameAndRrn(name, rrn); }
    public Optional<Member> findOneByLoginIdAndPassword(String loginId, String password) {
        return memberRepository.findByLoginIdAndPassword(loginId, password);
    }


    /**
     * 역할별 개별 회원 조회
     */
    public Optional<Manager> findOneManager(Long memberId) { return managerRepository.findById(memberId); }
    public Optional<Manager> findOneManager(String name, String rrn) { return managerRepository.findByNameAndRrn(name, rrn); }

    public Optional<Employee> findOneEmployee(Long memberId) { return employeeRepository.findById(memberId); }
    public Optional<Employee> findOneEmployee(String name, String rrn) { return employeeRepository.findByNameAndRrn(name, rrn); }

    public Optional<Patient> findOnePatient(Long memberId) { return patientRepository.findById(memberId); }
    public Optional<Patient> findOnePatient(String name, String rrn) { return patientRepository.findByNameAndRrn(name, rrn); }

    public Optional<Family> findOneFamily(Long memberId) { return familyRepository.findById(memberId); }
    public Optional<Family> findOneFamily(String name, String rrn) { return familyRepository.findByNameAndRrn(name, rrn); }


}
