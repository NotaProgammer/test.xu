package com.test.xu.handler;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class CustomFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public CustomFileChooser(String suffix) {
		this(suffix, false);
	}

	// 限定所选文件后缀
	public CustomFileChooser(String suffix, boolean multiEnable) {
		super();
		final String s = suffix.trim().toUpperCase();
		super.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return !f.isFile()
						|| f.getName().toUpperCase()
								.endsWith("." + s.toUpperCase());
			}

			@Override
			public String getDescription() {
				return "可选的文件格式 (*." + s + ")";
			}
		});
		super.setFileSelectionMode(JFileChooser.FILES_ONLY);
		super.setMultiSelectionEnabled(multiEnable);
		super.setAcceptAllFileFilterUsed(false);
	}
}
