package frontend;

import backend.*;

public class BackendModelSetup {
    public ClientClass theClient;
    public BackendModelSetup() {
        this.theClient = new ClientClass("127.0.0.1", 12445);
    }
}