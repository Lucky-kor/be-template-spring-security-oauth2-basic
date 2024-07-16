package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    COFFEE_NOT_FOUND(404, "Coffee not found"),
    COFFEE_CODE_EXISTS(409, "Coffee Code exists"),
    ORDER_NOT_FOUND(404, "Order not found"),
    CANNOT_CHANGE_ORDER(403, "Order can not change"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_MEMBER_STATUS(400, "Invalid member status"),
    QNA_NOT_FOUND(404, "Qna not found"),
    REPLY_NOT_FOUND(404, "Reply not found"),
    ANSWERED_QNA(403, "Answed Question"),
    LIKED_QNA(403, "Liked Question"),
    LIKED_NOT_FOUND(403, "Liked Not Found"),
    QNA_DELETED(403, "Answed Deleted"),
    NOT_WRITER(403, "Not Writer"),
    ;


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
