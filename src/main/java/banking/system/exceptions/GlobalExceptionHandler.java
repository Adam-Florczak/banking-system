package banking.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidVerificationTokenException.class)
    @ResponseBody
    public ResponseEntity<String> handleInvalidVerificationTokenException(InvalidVerificationTokenException e){
        return new ResponseEntity<String>("Given token is invalid",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VerificationTokenExpiredException.class)
    @ResponseBody
    public ResponseEntity<String> handleVerificationTokenExpiredException(VerificationTokenExpiredException e){
        return new ResponseEntity<String>("Your verification token expired",HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    @ResponseBody
    public ResponseEntity<String> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException e){
        return new ResponseEntity<String>("Incorrect mail",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SendToOwnAccountException.class)
    @ResponseBody
    public ResponseEntity<String> handleSendToOwnAccountException(SendToOwnAccountException e){
        return new ResponseEntity<String>("You can't send money to yourself",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoEnoughMoneyException.class)
    @ResponseBody
    public ResponseEntity<String> handleNoEnoughMoneyException(NoEnoughMoneyException e){
        return new ResponseEntity<String>("You don't have enough money",HttpStatus.I_AM_A_TEAPOT);
    }


    @ExceptionHandler(PastTransactionException.class)
    @ResponseBody
    public ResponseEntity<String> handlePastTransactionException(PastTransactionException e){
        return new ResponseEntity<String>("Choose the correct date",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEmptyTitleFieldException.class)
    @ResponseBody
    public ResponseEntity<String> handleNotEmptyTitleFieldException(NotEmptyTitleFieldException e){
        return new ResponseEntity<String>(" Enter the title of the transfer",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NegativeAmountException.class)
    @ResponseBody
    public ResponseEntity<String> handleNegativeAmountException(NegativeAmountException e){
        return new ResponseEntity<String>("You don't have enough money",HttpStatus.BAD_REQUEST);
    }
}
