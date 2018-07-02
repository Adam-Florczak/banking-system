package banking.system.exceptions;

public class ValidationError {

        private String desciption;
        private String field;

    public ValidationError() {
    }

    public ValidationError(String desciption, String field) {
        this.desciption = desciption;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String getDesciption() {
        return desciption;
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "desciption='" + desciption + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
