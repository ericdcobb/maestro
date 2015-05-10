package com.ericdcobb.maestroService.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 *   
 *
 * @author Eric Cobb on 5/9/15.
 */
@AutoValue
public abstract class Route {

	@JsonCreator
	public static Route create(@JsonProperty("path") String path, @JsonProperty("variables") Map<String, Object>
			variables, @JsonProperty("script") String script) {
		return new AutoValue_Route(path, variables, script);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String path;
		private String script;
		private Map<String, Object> variables = new HashMap<>();

		public Builder setPath(String path) {
			this.path = path;
			return this;
		}

		public Builder setVariables(Map<String, Object> variables){
			this.variables = variables;
			return this;
		}

		public Builder addVariables(String key, Object value){
			this.variables.put(key, value);
			return this;
		}

		public Builder setScript(String script) {
			this.script = script;
			return this;
		}

		public Route build() {
			return Route.create(path, variables, script);
		}

	}

	public abstract String path();
	public abstract Map<String, Object> variables();
	public abstract String script();
}
