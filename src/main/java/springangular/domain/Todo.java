package springangular.domain;

import java.io.Serializable;

public class Todo implements Serializable {

    private Long id;
    private String title;
    private String description;

    public Todo(){}
    
    private Todo(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;

        public Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Todo build() {
            return new Todo(this);
        }
    }
}

