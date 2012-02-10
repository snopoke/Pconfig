package com.nomsic.pconfig.model;
public enum FileType {
		PDF(".pdf"),
		XML(".xml"),
		HTML(".html");

		private final String extension;
		private FileType(String extension){
			this.extension = extension;
		}
		
		public String getExtension() {
			return extension;
		}

	}