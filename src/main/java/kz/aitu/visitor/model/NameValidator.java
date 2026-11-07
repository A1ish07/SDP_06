package kz.aitu.visitor.model;

final class NameValidator {

    private NameValidator() {
    }

    static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (name.contains("/")) {
            throw new IllegalArgumentException("Name must not contain '/': " + name);
        }
    }
}