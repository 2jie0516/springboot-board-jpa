package prgms.boardmission.post.exception;

public class NotFoundMemberException extends RuntimeException {
    private final static String NOT_FOUND_MEMBER_MESSAGE = "게시글의 작성자를 찾을 수 없습니다.";

    public NotFoundMemberException() {
        super(NOT_FOUND_MEMBER_MESSAGE);
    }
}
