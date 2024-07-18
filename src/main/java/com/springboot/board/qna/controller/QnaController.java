package com.springboot.board.qna.controller;

import com.springboot.board.qna.dto.QnaDto;
import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.entity.Qna;
import com.springboot.board.qna.mapper.QnaMapper;
import com.springboot.board.qna.service.QnaService;
import com.springboot.dto.MultiResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v11/board/qna")
@CrossOrigin(origins = "http://localhost:8080")
public class QnaController {
    private final QnaService qnaService;
    private  final QnaMapper mapper;

    public QnaController(QnaService qnaService, QnaMapper mapper) {
        this.qnaService = qnaService;
        this.mapper = mapper;
    }

    //등록
    @PostMapping
    public ResponseEntity createQna(@RequestBody QnaDto.Post qnaDto){
        QnaDto.Response response =
                mapper.qnaResponseToqna(qnaService.createQna(mapper.qnaToQnaPostDto(qnaDto)));

        return new ResponseEntity(response, HttpStatus.CREATED);
    }
    //수정
    @PatchMapping("/{qna-id}")
    public ResponseEntity editQna(@PathVariable("qna-id") long qnaId,
                                  @RequestBody QnaDto.Patch qnaDto,
                                  @AuthenticationPrincipal Object email){
        qnaDto.setQnaId(qnaId);
        QnaDto.Response response =
                mapper.qnaResponseToqna(qnaService. aditQna(mapper.qnaToQnaPatchDto(qnaDto), email.toString()));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    //삭제
    @DeleteMapping("/{qna-id}")
    public ResponseEntity deleteQna(@PathVariable("qna-id") long qnaId,
                                    @AuthenticationPrincipal Object email){
        qnaService.deleteQna(qnaId, email.toString());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    //단일조회
    @GetMapping("/{qna-id}")
    public ResponseEntity findQna(@PathVariable("qna-id") long qnaId){
        QnaDto.Response response =
                mapper.qnaResponseToqna(qnaService.findQna(qnaId));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    //전부조회
    @GetMapping()
    public ResponseEntity findQnas(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size,
                                   @RequestParam String sortType){

        Page<Qna> qnas = qnaService.findQnas(page - 1, size, sortType);

        List<QnaDto.Response> response
                 = qnas.getContent().stream()
                .map(qna -> mapper.qnaResponseToqna(qna))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MultiResponseDto<>(response,
                        qnas),
                HttpStatus.OK);
    }

    //검색
    @GetMapping("/search")
    public ResponseEntity findKeywordQnas(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size,
                                   @RequestParam String sortType,
                                          @RequestParam String keyword){

        Page<Qna> qnas = qnaService.findQnas(page - 1, size, sortType, keyword);

        List<QnaDto.Response> response
                = qnas.getContent().stream()
                .map(qna -> mapper.qnaResponseToqna(qna))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new MultiResponseDto<>(response,
                        qnas),
                HttpStatus.OK);
    }
}
