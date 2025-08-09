package naughty.tuzamate.domain.post.service.command;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.auth.principal.PrincipalDetails;
import naughty.tuzamate.domain.post.converter.PostConverter;
import naughty.tuzamate.domain.post.dto.PostReqDTO;
import naughty.tuzamate.domain.post.dto.PostResDTO;
import naughty.tuzamate.domain.post.entity.Post;
import naughty.tuzamate.domain.post.enums.BoardType;
import naughty.tuzamate.domain.post.repository.PostRepository;
import naughty.tuzamate.domain.postLike.entity.PostLike;
import naughty.tuzamate.domain.postLike.repository.PostLikeRepository;
import naughty.tuzamate.domain.postScrap.entity.PostScrap;
import naughty.tuzamate.domain.postScrap.repository.PostScrapRepository;
import naughty.tuzamate.domain.user.entity.User;
import naughty.tuzamate.domain.user.repository.UserRepository;
import naughty.tuzamate.global.error.GeneralErrorCode;
import naughty.tuzamate.global.error.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;

    @Override
    public PostResDTO.CreatePostResponseDTO createPost(BoardType boardType, PostReqDTO.CreatePostRequestDTO reqDTO, PrincipalDetails principalDetails) {
        // reqDTO -> Post Entity 로 변환, Post Entity -> resDTO 로 return
        User user = userRepository.getReferenceById(principalDetails.getId());
        Post post = PostConverter.toPost(boardType, reqDTO, user);

        postRepository.save(post);

        return PostConverter.toCreatePostResponseDTO(post);
    }

    @Override
    public PostResDTO.UpdatePostResponseDTO updatePost(
            PostReqDTO.UpdatePostRequestDTO reqDTO, Long postId, PrincipalDetails principalDetails
    ) {
        // reqDTO -> Post Entity, Post Entity -> resDTO
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        if (!post.getUser().getId().equals(principalDetails.getUser().getId())) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403); // 권한 없음
        }

        if (reqDTO.title() != null) {
            post.updateTitle(reqDTO.title());
        }
        if (reqDTO.content() != null) {
            post.updateContent(reqDTO.content());
        }

        return PostConverter.toUpdatePostResponseDTO(post);
    }

    @Override
    public PostResDTO.DeletePostResponseDTO deletePost(Long postId, PrincipalDetails principalDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        if (!post.getUser().getId().equals(principalDetails.getUser().getId())) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403); // 권한 없음
        }

        postRepository.delete(post);

        return PostConverter.toDeletePostResponseDTO(postId);
    }

    @Override
    public String postLike(Long postId, PrincipalDetails principalDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        User user = userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new CustomException(GeneralErrorCode.ALREADY_LIKED);
        }

        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        postLikeRepository.save(like);

        post.increaseLike();

        return "좋아요 완료";
    }

    @Override
    public String deleteLike(Long postId, PrincipalDetails principalDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        User user = userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        PostLike like = postLikeRepository.findByPostAndUser(post, user);

        postLikeRepository.delete(like);
        post.decreaseLike();

        return "좋아요 취소";
    }

    @Override
    public String postScrap(Long postId, PrincipalDetails principalDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        User user = userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        if (postScrapRepository.existsByPostAndUser(post, user)) {
            throw new CustomException(GeneralErrorCode.ALREADY_LIKED);
        }

        PostScrap scrap = PostScrap.builder().post(post).user(user).build();
        postScrapRepository.save(scrap);

        return "스크랩 완료";
    }

    @Override
    public String deleteScrap(Long postId, PrincipalDetails principalDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
        User user = userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        PostScrap scrap = postScrapRepository.findByPostAndUser(post, user);

        postScrapRepository.delete(scrap);

        return "스크랩 취소";
    }
}
