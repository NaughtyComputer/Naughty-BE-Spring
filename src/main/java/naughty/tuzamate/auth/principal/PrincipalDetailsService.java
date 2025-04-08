package naughty.tuzamate.auth.principal;

import lombok.RequiredArgsConstructor;
import naughty.tuzamate.domain.user.domain.User;
import naughty.tuzamate.domain.user.error.UserErrorCode;
import naughty.tuzamate.domain.user.error.exception.UserCustomException;
import naughty.tuzamate.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    // email을 이용해 사용자를 가져오기 위해 선언
    private final UserRepository userRepository;

    @Override
    // email로 PrincipalDetails(UserDetails) 객체를 가져오는 메소드, username은 email로 생각
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UserCustomException(UserErrorCode.USER_NOT_FOUND));
        return new PrincipalDetails(user);
    }
}
