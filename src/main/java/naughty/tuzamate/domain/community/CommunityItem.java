package naughty.tuzamate.domain.community;

import naughty.tuzamate.domain.post.entity.Post;

public interface CommunityItem {

    Long getCursorId(); // cursor
    Post getPost(); // 게시글 정보 - 제목, 내용 요약
}
