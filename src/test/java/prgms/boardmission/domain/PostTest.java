package prgms.boardmission.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import prgms.boardmission.post.exception.NotFoundMemberException;

class PostTest {

    @Test
    void 타이틀이_30자를_넘을_경우() {
        //Given
        String title = "titlevtitletitletitletitletitletitletitletitletitletitletitletitletitle";
        String content = "content";
        Member member = new Member("name", 20, "hobby");

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Post(title, content, member));
    }

    @Test
    void 본문이_30자를_넘을_경우() {
        //Given
        String title = "title";
        String content = "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent";
        Member member = new Member("name", 20, "hobby");

        //Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Post(title, content, member));
    }

    @Test
    void 작성자가_없을_경우() {
        //Given
        String title = "title";
        String content = "content";
        Member member = null;

        //Then
        Assertions.assertThrows(NotFoundMemberException.class, () -> new Post(title, content, member));
    }

    @Nested
    class update {
        private Post post;

        @BeforeEach
        void setUp() {
            String title = "title";
            String content = "content";

            Member member = new Member("name", 20, "hobby");

            post = new Post(title, content, member);
        }

        @Test
        void 변경할_타이틀이_30자를_넘을_경우() {
            //Given
            String editTitle = "titlevtitletitletitletitletitletitletitletitletitletitletitletitletitle";
            String editContent = "content";

            //Then
            Assertions.assertThrows(IllegalArgumentException.class, () -> post.updatePost(editTitle, editContent));
        }

        @Test
        void 변경할_본문이_100자를_넘을_경우() {
            //Given
            String editTitle = "title";
            String editContent = "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent";

            //Then
            Assertions.assertThrows(IllegalArgumentException.class, () -> post.updatePost(editTitle, editContent));
        }
    }
}