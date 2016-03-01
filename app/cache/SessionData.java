package cache;

import dto.WebUser;

public class SessionData {

    private WebUser user;

    public void setUser(WebUser user) {
        this.user = user;
    }

    public WebUser getUser() {
        return this.user;
    }
}