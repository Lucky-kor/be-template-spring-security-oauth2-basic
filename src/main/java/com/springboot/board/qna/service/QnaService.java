package com.springboot.board.qna.service;

import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.entity.Qna;
import com.springboot.board.qna.entity.Reply;
import com.springboot.board.qna.repository.QnaRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QnaService {
    private final QnaRepository qnaRepository;
    private final MemberService memberService;
    private final LikeService likeService;

    public QnaService(QnaRepository qnaRepository, MemberService memberService, LikeService likeService) {
        this.qnaRepository = qnaRepository;
        this.memberService = memberService;
        this.likeService = likeService;
    }

    //등록
    public Qna createQna(Qna qna){
        qna.setMember(memberService.findMember(qna.getMember().getMemberId()));
        Qna saveQna =  qnaRepository.save(qna);
        return saveQna;
    }

    //수정
    public Qna aditQna(Qna qna, String email){
        Qna findQna = findQna(qna.getQnaId());

        memberService.checkSameMember(email, findQna.getMember().getMemberId());

        //답이 있을 경우 예외
        if (findQna.getReply() != null)
            throw new BusinessLogicException(ExceptionCode.ANSWERED_QNA);

        findQna.setBody(qna.getBody());
        findQna.setTitle(qna.getTitle());
        findQna.setLock(qna.getLock());
        Qna saveQna =  qnaRepository.save(findQna);
        return saveQna;
    }

    //댓글등록
    public Qna createQnaReply(Reply reply){
        Qna findQna = verifyQna(reply.getQna().getQnaId());
        reply.setMember(memberService.findMember(reply.getMember().getMemberId()));
        findQna.setReply(reply);
        findQna.setQnaStatus(Qna.QnaStatus.QUESTION_ANSWERED);
        qnaRepository.save(findQna);
        return findQna;
    }

    //댓글삭제
    public void deleteQnaReply(long qnaId){
        Qna findQna = verifyQna(qnaId);
        findQna.setReply(null);
        qnaRepository.save(findQna);
    }

    //단일조회
    public Qna findQna(Long qnaId){
        Qna findQna = verifyQna(qnaId);
        //조회수 올려주기
        findQna.setView(findQna.getView() + 1);
        qnaRepository.save(findQna);
        return findQna;
    }

    //여러개 조회
    public Page<Qna> findQnas(int page, int size, String sortType){
        Pageable pageable;

        pageable = PageRequest.of(page, size, Qna.SortType(sortType));

        Page<Qna> qnas = qnaRepository.findAllByQnaStatusNot(pageable, Qna.QnaStatus.QUESTION_DELETED);
        return qnas;
    }

    public Page<Qna> findQnas(int page, int size, String sortType, String keyword){
        Pageable pageable;

        pageable = PageRequest.of(page, size, Qna.SortType(sortType));

        Page<Qna> qnas = qnaRepository.findByTitleContainingAndQnaStatusNot(pageable, keyword, Qna.QnaStatus.QUESTION_DELETED);
        return qnas;
    }

    //삭제
    public void deleteQna(Long qnaId, String email){
        Qna deleteQna = verifyQna(qnaId);

        memberService.checkSameMember(email, deleteQna.getMember().getMemberId());

        if(deleteQna.getQnaStatus() == Qna.QnaStatus.QUESTION_DELETED)
            throw new BusinessLogicException(ExceptionCode.QNA_DELETED);
        deleteQna.setQnaStatus(Qna.QnaStatus.QUESTION_DELETED);

        qnaRepository.save(deleteQna);
    }

    public Qna likedQna(Like like){
        Qna findQna = verifyQna(like.getQna().getQnaId());
        Member member = memberService.findMember(like.getMember().getMemberId());

        if(member.likedQna(like)){
            long deleteMemberId = member.removeLikeQna(like);
            findQna.removeLiek(like);

            likeService.deleteLike(deleteMemberId);

            //좋아요 빼주기
            findQna.setLikeCount(findQna.getLikeCount() - 1);
        }
        else
        {
            findQna.addLike(like);
            member.addLikeQna(like);
            //좋아요 올려주기
            findQna.setLikeCount(findQna.getLikeCount() + 1);
        }

        Qna likeQna = qnaRepository.save(findQna);
        return likeQna;
    }

    //검증
    public Qna verifyQna(Long qnaId){
        Optional<Qna> findQna = qnaRepository.findById(qnaId);
        Qna qna = findQna.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return qna;
    }

    //이미 답변이 달린 경우
    public boolean chakedCompleteQna(long qnaId){
        return verifyQna(qnaId).getQnaStatus() == Qna.QnaStatus.QUESTION_ANSWERED;
    }

}
