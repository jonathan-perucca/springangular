package com.jperucca.springangular.web.exception;

public class ErrorInfo {
    private String url;
    private int errorCode;
    private String reasonCause;
    
    public ErrorInfo(){}
    
    public ErrorInfo(Builder builder) {
        this.url = builder.url;
        this.errorCode = builder.errorCode.getCode();
        this.reasonCause = builder.errorCode.getDescription();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getReasonCause() {
        return reasonCause;
    }

    public void setReasonCause(String reasonCause) {
        this.reasonCause = reasonCause;
    }

    public static class Builder {
        private final String url;
        private final ErrorCode errorCode;
        
        public Builder(String url, ErrorCode errorCode) {
            this.url = url;
            this.errorCode = errorCode;
        }
        
        public ErrorInfo build() {
            return new ErrorInfo(this);
        }
    }
}
