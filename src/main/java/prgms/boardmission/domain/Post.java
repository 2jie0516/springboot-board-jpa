package prgms.boardmission.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import prgms.boardmission.post.exception.NotFoundMemberException;

@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Lob
    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    protected Post() {
    }

    public Post(String title, String content, Member member) {
        checkTitleLength(title);
        checkContentLength(content);

        boolean isMemberEmpty = member == null;

        if (isMemberEmpty) {
            throw new NotFoundMemberException();
        }

        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void updatePost(String editTitle, String editContent) {
        checkTitleLength(editTitle);
        checkContentLength(editContent);

        this.title = editTitle;
        this.content = editContent;
    }

    private static void checkTitleLength(String editTitle) {
        if (editTitle.length() > 30) {
            throw new IllegalArgumentException("타이틀은 30자를 넘을 수 없습니다.");
        }
    }

    private static void checkContentLength(String editContent) {
        if (editContent.length() > 100) {
            throw new IllegalArgumentException("본문은 100자를 넘을 수 없습니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Member getMember() {
        return member;
    }
}
