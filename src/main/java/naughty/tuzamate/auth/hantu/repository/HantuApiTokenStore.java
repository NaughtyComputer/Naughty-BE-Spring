package naughty.tuzamate.auth.hantu.repository;

public interface HantuApiTokenStore {

    void saveAccessToken(String accessToken);

    String getAccessToken();
}
