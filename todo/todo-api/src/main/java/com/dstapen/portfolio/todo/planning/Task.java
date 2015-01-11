// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.planning;

/**
 * @author Daria Stepanova
 */
public class Task {
    public String title;
    public boolean completed;

    public Task() {
    }

    public Task(Builder builder) {
        title = builder.title;
        completed = builder.completed;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private boolean completed;

        private Builder() {
        }


        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
