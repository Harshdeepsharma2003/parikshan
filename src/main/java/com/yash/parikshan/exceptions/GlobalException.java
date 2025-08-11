package com.yash.parikshan.exceptions;

/**
 * Global application exception that handles all types of errors including
 * network, database, validation, and business logic errors.
 */
public class GlobalException extends Exception {
    private final String errorCode;
    private final ErrorType errorType;

    // Enum for different error categories
    public enum ErrorType {
        GENERAL,
        NETWORK,
        DATABASE,
        VALIDATION,
        SECURITY,
        RESOURCE_NOT_FOUND
    }

    // Basic constructors
    public GlobalException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
        this.errorType = ErrorType.GENERAL;
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
        this.errorType = ErrorType.GENERAL;
    }

    public GlobalException(String message, String errorCode, ErrorType errorType) {
        super(message);
        this.errorCode = errorCode;
        this.errorType = errorType;
    }

    public GlobalException(String message, String errorCode, ErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorType = errorType;
    }

    // Static factory methods for Network Errors
    public static GlobalException networkError(String message) {
        return new GlobalException(message, "NETWORK_ERROR", ErrorType.NETWORK);
    }

    public static GlobalException networkError(String message, Throwable cause) {
        return new GlobalException(message, "NETWORK_ERROR", ErrorType.NETWORK, cause);
    }

    public static GlobalException connectionTimeout(String message) {
        return new GlobalException(message, "CONNECTION_TIMEOUT", ErrorType.NETWORK);
    }

    public static GlobalException connectionTimeout(String message, Throwable cause) {
        return new GlobalException(message, "CONNECTION_TIMEOUT", ErrorType.NETWORK, cause);
    }

    public static GlobalException externalServiceError(String message) {
        return new GlobalException(message, "EXTERNAL_SERVICE_ERROR", ErrorType.NETWORK);
    }

    public static GlobalException externalServiceError(String message, Throwable cause) {
        return new GlobalException(message, "EXTERNAL_SERVICE_ERROR", ErrorType.NETWORK, cause);
    }

    // Static factory methods for Database Errors
    public static GlobalException databaseError(String message) {
        return new GlobalException(message, "DATABASE_ERROR", ErrorType.DATABASE);
    }

    public static GlobalException databaseError(String message, Throwable cause) {
        return new GlobalException(message, "DATABASE_ERROR", ErrorType.DATABASE, cause);
    }

    public static GlobalException databaseConnectionError(String message) {
        return new GlobalException(message, "DB_CONNECTION_ERROR", ErrorType.DATABASE);
    }

    public static GlobalException databaseConnectionError(String message, Throwable cause) {
        return new GlobalException(message, "DB_CONNECTION_ERROR", ErrorType.DATABASE, cause);
    }

    public static GlobalException dataIntegrityError(String message) {
        return new GlobalException(message, "DATA_INTEGRITY_VIOLATION", ErrorType.DATABASE);
    }

    public static GlobalException dataIntegrityError(String message, Throwable cause) {
        return new GlobalException(message, "DATA_INTEGRITY_VIOLATION", ErrorType.DATABASE, cause);
    }

    public static GlobalException sqlExecutionError(String message) {
        return new GlobalException(message, "SQL_EXECUTION_ERROR", ErrorType.DATABASE);
    }

    public static GlobalException sqlExecutionError(String message, Throwable cause) {
        return new GlobalException(message, "SQL_EXECUTION_ERROR", ErrorType.DATABASE, cause);
    }

    // Static factory methods for other common errors
    public static GlobalException resourceNotFound(String message) {
        return new GlobalException(message, "RESOURCE_NOT_FOUND", ErrorType.RESOURCE_NOT_FOUND);
    }

    public static GlobalException validationError(String message) {
        return new GlobalException(message, "VALIDATION_ERROR", ErrorType.VALIDATION);
    }

    public static GlobalException securityError(String message) {
        return new GlobalException(message, "SECURITY_ERROR", ErrorType.SECURITY);
    }

    // Getters
    public String getErrorCode() {
        return errorCode;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    // Helper methods
    public boolean isNetworkError() {
        return errorType == ErrorType.NETWORK;
    }

    public boolean isDatabaseError() {
        return errorType == ErrorType.DATABASE;
    }

    public boolean isValidationError() {
        return errorType == ErrorType.VALIDATION;
    }

    public boolean isSecurityError() {
        return errorType == ErrorType.SECURITY;
    }

    public boolean isResourceNotFound() {
        return errorType == ErrorType.RESOURCE_NOT_FOUND;
    }
}

