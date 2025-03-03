package dev.ctrlspace.bootcamp2410.tasos.bootcamp2410tasos.exceptions;

public class ProductExceptions {

    public static class ProductValidationException extends Exception {
        public ProductValidationException(String message) {
            super(message);
        }
    }
}
