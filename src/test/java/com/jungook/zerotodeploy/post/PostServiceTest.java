package com.jungook.zerotodeploy.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepo postRepo;

    @InjectMocks
    private PostService postService;

    private final String keyword = "test";

    @BeforeEach
    void setup() {
        Mockito.reset(postRepo);
    }

    @Test
    void searchPosts_filterTitle_callsSearchByTitle() {
        List<PostEntity> expected = Collections.emptyList();
        when(postRepo.searchByTitle(keyword)).thenReturn(expected);

        List<PostEntity> result = postService.searchPosts(keyword, "title");

        assertSame(expected, result);
        verify(postRepo).searchByTitle(keyword);
        verify(postRepo, never()).searchByContent(anyString());
    }

    @Test
    void searchPosts_filterContent_callsSearchByContent() {
        List<PostEntity> expected = Collections.emptyList();
        when(postRepo.searchByContent(keyword)).thenReturn(expected);

        List<PostEntity> result = postService.searchPosts(keyword, "content");

        assertSame(expected, result);
        verify(postRepo).searchByContent(keyword);
        verify(postRepo, never()).searchByTitle(anyString());
    }

    @Test
    void searchPosts_default_callsSearchByTitle() {
        List<PostEntity> expected = Collections.emptyList();
        when(postRepo.searchByTitle(keyword)).thenReturn(expected);

        List<PostEntity> result = postService.searchPosts(keyword, "other");

        assertSame(expected, result);
        verify(postRepo).searchByTitle(keyword);
        verify(postRepo, never()).searchByContent(anyString());
    }
}
