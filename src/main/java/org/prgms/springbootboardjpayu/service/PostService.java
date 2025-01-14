package org.prgms.springbootboardjpayu.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.prgms.springbootboardjpayu.domain.Post;
import org.prgms.springbootboardjpayu.domain.User;
import org.prgms.springbootboardjpayu.dto.request.CreatePostRequest;
import org.prgms.springbootboardjpayu.dto.request.UpdatePostRequest;
import org.prgms.springbootboardjpayu.dto.response.ListResponse;
import org.prgms.springbootboardjpayu.dto.response.PostResponse;
import org.prgms.springbootboardjpayu.repository.PostRepository;
import org.prgms.springbootboardjpayu.repository.UserRepository;
import org.prgms.springbootboardjpayu.service.converter.PostConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(@Valid CreatePostRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 유저입니다.")
        );

        Post post = PostConverter.toPost(request, user);
        return PostConverter.toPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse updatePost(Long id, @Valid UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        post.editPostTitleContent(request.title(), request.content());
        return PostConverter.toPostResponse(post);
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        return PostConverter.toPostResponse(post);
    }

    public ListResponse getPosts(Pageable pageable) {
        Page<PostResponse> page = postRepository
                .findAll(pageable)
                .map(PostConverter::toPostResponse);
        return PostConverter.toPostListResponse(page);
    }
}
