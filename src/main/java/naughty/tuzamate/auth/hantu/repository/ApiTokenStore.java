package naughty.tuzamate.auth.hantu.repository;

public interface ApiTokenStore {

    void saveAccessToken(String accessToken);

    String getAccessToken();
}
