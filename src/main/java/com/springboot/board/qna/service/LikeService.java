package com.springboot.board.qna.service;

import com.springboot.board.qna.entity.Like;
import com.springboot.board.qna.repository.LikeRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void deleteLike(long likeId){
        Optional<Like> findLike = likeRepository.findById(likeId);
        likeRepository.findById(likeId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIKED_NOT_FOUND));
        likeRepository.delete(findLike.get());
    }
}
