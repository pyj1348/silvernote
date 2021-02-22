package silver.silvernote.controller.admin;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silver.silvernote.domain.Center;
import silver.silvernote.domain.CenterLearning;
import silver.silvernote.domain.Learning;
import silver.silvernote.domain.dto.SimpleResponseDto;
import silver.silvernote.responsemessage.HttpHeaderCreator;
import silver.silvernote.responsemessage.HttpStatusEnum;
import silver.silvernote.responsemessage.Message;
import silver.silvernote.service.CenterLearningService;
import silver.silvernote.service.CenterService;
import silver.silvernote.service.LearningService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CenterLearningController {

    private final CenterLearningService centerLearningService;
    private final CenterService centerService;
    private final LearningService learningService;

    /**
     * 조회
     * */
    @GetMapping("/member-exercises")
    public ResponseEntity<Message> findCenterLearnings() {

        List<CenterLearningResponseDto> collect = centerLearningService.findCenterLearnings().stream().map(CenterLearningResponseDto::new).collect(Collectors.toList());
        return new ResponseEntity<>( // MESSAGE, HEADER, STATUS
                new Message(HttpStatusEnum.OK, "성공적으로 완료되었습니다", collect), // STATUS, MESSAGE, DATA
                HttpHeaderCreator.createHttpHeader(),
                HttpStatus.OK);
    }

    /**
     * 생성
     * */
    @PostMapping("/member-exercises/new")
    public ResponseEntity<Message> saveCenterLearning(@RequestBody @Valid CenterLearningRequestDto request) {
        Center center = centerService.findOne(request.getCenterId()).orElseThrow(NoSuchElementException::new);
        Learning learning = learningService.findOne(request.getLearningId()).orElseThrow(NoSuchElementException::new);

        CenterLearning centerLearning = CenterLearning.BuilderByParam()
                    .date(request.getDate())
                    .center(center)
                    .learning(learning)
                    .build();

        centerLearningService.save(centerLearning);

        return new ResponseEntity<>( // MESSAGE, HEADER, STATUS
                new Message(HttpStatusEnum.CREATED, "리소스가 생성되었습니다", new SimpleResponseDto(centerLearning.getId(), LocalDateTime.now())), // STATUS, MESSAGE, DATA
                HttpHeaderCreator.createHttpHeader(),
                HttpStatus.CREATED);
    }

    /**
     * 수정
     * */
    /**
     * 삭제
     * */
    @DeleteMapping("/member-exercises/{id}")
    public ResponseEntity<Message> deleteCenterLearning(@PathVariable("id") Long id) {

        centerLearningService.deleteCenterLearning(id);

        return new ResponseEntity<>( // MESSAGE, HEADER, STATUS
                new Message(HttpStatusEnum.OK, "성공적으로 완료되었습니다", new SimpleResponseDto(id, LocalDateTime.now())), // STATUS, MESSAGE, DATA
                HttpHeaderCreator.createHttpHeader(),
                HttpStatus.OK);
    }


    /**
     * Request DTO
     * */
    @Data
    static class CenterLearningRequestDto {
        @NotNull(message = "날짜를 확인하세요")
        private LocalDate date;

        @NotNull(message = "센터 ID를 확인하세요")
        private Long centerId;

        @NotNull(message = "학습 ID를 확인하세요")
        private Long learningId;

    }


    /**
     * Response DTO
     * */
    @Data // JSON 요청의 응답으로 보낼 데이터 클래스
    static class CenterLearningResponseDto {
        private Long id;
        private LocalDate date;
        private Long centerId;
        private Long learningId;

        public CenterLearningResponseDto(CenterLearning centerLearning) {
            this.id = centerLearning.getId();
            this.date = centerLearning.getDate();
            this.centerId = centerLearning.getCenter().getId();
            this.learningId = centerLearning.getLearning().getId();
        }
    }

}
