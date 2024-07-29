package com.springboot.board.qna.controller;

import com.google.gson.Gson;
import com.springboot.board.qna.dto.QnaDto;
import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.entity.Qna;
import com.springboot.board.qna.mapper.QnaMapper;
import com.springboot.board.qna.service.QnaService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v11/board/qna")
@CrossOrigin(origins = "http://127.0.0.1:3000 , http://127.0.0.1:8080")
public class QnaController {
    private final QnaService qnaService;
    private  final QnaMapper mapper;
    private final MemberService memberService;

    public QnaController(QnaService qnaService, QnaMapper mapper, MemberService memberService) {
        this.qnaService = qnaService;
        this.mapper = mapper;
        this.memberService = memberService;
    }

    //등록
    @PostMapping
    public ResponseEntity createQna(@RequestBody QnaDto.Post qnaDto,
                                    @AuthenticationPrincipal Object email){
        Member member = memberService.findVerifiedMember(email.toString());
        qnaDto.setMemberId(member.getMemberId());
        QnaDto.Response response =
                mapper.qnaResponseToqna(qnaService.createQna(mapper.qnaToQnaPostDto(qnaDto)));

        URI location = UriCreator.createUri("localhost:8080/v11/board/qna/", 1);

        return ResponseEntity.created(location).build();
        //return new ResponseEntity(response, HttpStatus.CREATED);
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
                                   @RequestParam String sortType,
                                   @RequestParam String keyword){

        Page<Qna> qnas;

        if (keyword == null || keyword == "") {
            qnas = qnaService.findQnas(page - 1, size, sortType);
        }
        else {
            qnas = qnaService.findQnas(page - 1, size, sortType, keyword);
        }

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
