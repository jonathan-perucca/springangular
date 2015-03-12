package com.jperucca.springangular.web.dto;

public class TodoDTO {

	private Long id;
	private String description;
	private boolean checked;

	private TodoDTO() {
	}

	private TodoDTO(Builder builder) {
		setId(builder.id);
		setDescription(builder.description);
		setChecked(builder.checked);
	}

	public static Builder newTodoDTO() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(final boolean checked) {
		this.checked = checked;
	}

	public static final class Builder {
		private Long id;
		private String description;
		private boolean checked;

		private Builder() {
		}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withChecked(boolean checked) {
			this.checked = checked;
			return this;
		}

		public TodoDTO build() {
			return new TodoDTO(this);
		}
	}
}
