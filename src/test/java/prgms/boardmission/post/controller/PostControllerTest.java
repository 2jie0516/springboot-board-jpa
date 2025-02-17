package prgms.boardmission.post.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import prgms.boardmission.domain.Post;
import prgms.boardmission.member.dto.MemberDto;
import prgms.boardmission.post.dto.PostDto;
import prgms.boardmission.post.dto.PostUpdateDto;
import prgms.boardmission.post.service.PostService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    private MemberDto memberDto;

    @Test
    void save() throws Exception {
        //Given
        String name = "sehan";
        int age = 20;
        String hobby = "hobby";

        memberDto = new MemberDto(name, age, hobby);

        String title = "title";
        String content = "content";

        PostDto.Request postDto = new PostDto.Request(title, content, memberDto);

        doReturn(1L).when(postService)
                .save(postDto);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-save",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("memberDto").type(JsonFieldType.OBJECT).description("memberDto"),
                                fieldWithPath("memberDto.name").type(JsonFieldType.STRING).description("memberDto.name"),
                                fieldWithPath("memberDto.age").type(JsonFieldType.NUMBER).description("memberDto.age"),
                                fieldWithPath("memberDto.hobby").type(JsonFieldType.STRING).description("memberDto.hobby")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.STRING).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("데이터")
                        )
                ));


    }

    @Test
    void findAll() throws Exception {
        //Given
        PageRequest pageable = PageRequest.of(0, 10);
        PostDto.Response returnPost = new PostDto.Response(1L, "title", "name", LocalDateTime.now());

        List<PostDto.Response> postList = new ArrayList<>();
        postList.add(returnPost);

        Page<PostDto.Response> returnPage = new PageImpl<>(postList);

        doReturn(returnPage).when(postService)
                .findAll(pageable);

        //Then
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-findAll",
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.STRING).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("데이터"),
                                fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("data.content[].postId").type(JsonFieldType.NUMBER).description("postId"),
                                fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("title"),

                                fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("pageable"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("sort"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("empty"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("sorted"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsorted"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("offset"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("pageNumber"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("pageSize"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("paged"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("unpaged"),

                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("totalPages"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("last"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("size"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("number"),
                                fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("sort"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("empty"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("unsorted"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("unsorted"),

                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("numberOfElements"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("first"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("empty")
                        )
                ));
    }

    @Test
    void findBYId() throws Exception {
        //Given
        PostDto.Response returnPost = new PostDto.Response(1L, "title", "name", LocalDateTime.now());

        doReturn(returnPost).when(postService)
                .findById(1L);

        //Then
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-findById",
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.STRING).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("data.postId").type(JsonFieldType.NUMBER).description("postId"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("title")
                        )
                ));
    }

    @Test
    void update() throws Exception {
        //Given
        PostUpdateDto.Response updatedPost = new PostUpdateDto.Response(1L, LocalDateTime.now());

        String editTitle = "edit title";
        String editContent = "edit content";

        PostUpdateDto.Request postUpdateDto = new PostUpdateDto.Request(editTitle, editContent);

        doReturn(updatedPost).when(postService)
                .updatePost(1L, postUpdateDto);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.patch(("/posts/{postId}"), 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-update",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.STRING).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                fieldWithPath("data.postId").type(JsonFieldType.NUMBER).description("postId"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("updatedAt")
                        )
                ));
    }
}
