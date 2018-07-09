package banking.system.exceptions;

public class ClientNotActivatedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Client is not activated via email verification";
    }
}
