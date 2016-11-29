package com.test.xu.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.FilterDocument;
import com.teamcenter.rac.util.iTextField;

public class ImportInfoPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final iTextField file;
	private final TCSession session = RACUIUtil.getTCSession();
	private int type = JFileChooser.DIRECTORIES_ONLY;
	private JFileChooser fileChooser = null;

	public ImportInfoPanel(File importFile) throws IOException {
		super(new BorderLayout());
		setBackground(Color.white);
		String encoding = TCSession.getServerEncodingName(session);

		// 文本框长度在不同环境下显示效果不同
		// 做成可配置的

		file = new iTextField(new FilterDocument(40, encoding), "", 30,
				255, false, null);
		if (importFile != null)
			file.setText(importFile.getCanonicalPath());
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(BorderLayout.CENTER, file);
		JButton brwBtn = createTextButton("...", null, this);
		topPanel.add(BorderLayout.EAST, brwBtn);
		this.add(BorderLayout.NORTH, topPanel);
	}
//构造函数
	public ImportInfoPanel(File importFile, int type) throws IOException {
		this(importFile);
		this.type = type;
	}

	public ImportInfoPanel(File importFile, int type, JFileChooser jfc)
			throws IOException {
		this(importFile);
		this.type = type;
		fileChooser = jfc;
	}

	// 继承了ActionListener
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (fileChooser == null)
			fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(type);
		int returnVal = fileChooser.showOpenDialog(fileChooser);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				file.setText(fileChooser.getSelectedFile().getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public File getFile() {
		if (file.getText().trim().length() < 1)
			return null;
		return new File(file.getText());
	}
//创建button 将本类（ActionListener添加到button上）
	public static JButton createTextButton(String text, String tooltip,
			ActionListener listener) {
		JButton b = new JButton(text);
		if (tooltip != null)
			b.setToolTipText(tooltip);
		b.setMargin(new java.awt.Insets(0, 0, 0, 0));
		b.setOpaque(false);
		b.setFocusable(false);
		if (listener != null)
			b.addActionListener(listener);
		return b;
	}

}
