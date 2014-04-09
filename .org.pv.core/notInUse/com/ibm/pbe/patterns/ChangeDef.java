package com.ibm.pbe.patterns;

	public class ChangeDef {
		String target;
		String replacement;
		boolean regex=false;
		boolean caseSensitive=true;
		public ChangeDef(String from, String to) {
			this.target=from;
			this.replacement=to;
		}
		public ChangeDef(String from, String to, boolean regex) {
			this.target=from;
			this.replacement=to;
			this.regex=regex;
		}
		public boolean isRegex() {
			return regex;
		}
		public void setRegex(boolean regex) {
			this.regex = regex;
		}
		public String getReplacement() {
			return replacement;
		}
		public String getTo() {
			return replacement;
		}
		public void setReplacement(String replacement) {
			this.replacement = replacement;
		}
		public String getTarget() {
			return target;
		}
		public String getFrom() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		public boolean isCaseSensitive() {
			return caseSensitive;
		}
		public void setCaseSensitive(boolean caseSensitive) {
			this.caseSensitive = caseSensitive;
		}
	}