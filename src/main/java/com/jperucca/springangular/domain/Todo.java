package com.jperucca.springangular.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Todo implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    private boolean checked;

    public static Builder newTodo() {
        return new Builder();
    }

    public Todo(){}
    
    private Todo(Builder builder) {
        this.description = builder.description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Builder {
        private String description;

        public Builder() {
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

