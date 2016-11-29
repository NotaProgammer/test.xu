package com.test.xu.output;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.FilterDocument;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.teamcenter.rac.util.iTextField;
import com.teamcenter.rac.util.combobox.iComboBox;

public class OutputDialog extends AbstractAIFDialog implements
		InterfaceAIFOperationListener {

	private static final long serialVersionUID = 865337289065006478L;
	private JButton startBtn;
	protected TCSession session = RACUIUtil.getTCSession();
	private JButton cancelBtn;
	private JButton fileBtn;
	private iTextField file;
	private iComboBox icBox;
	private TCComponent tcComponent;

	private iTextField levelNumber;
	public OutputDialog(TCComponent tcComponent) {

		super(AIFDesktop.getActiveDesktop());
		icBox = new iComboBox();
		for(int i =1;i<=10;i++){
			icBox.addItem(i, i);
		}
		icBox.addItem("不限",11);
		this.tcComponent = tcComponent;

		this.setTitle("导出BOM");
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void run() {
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);
		JPanel basicInfoPanel = new JPanel(
				new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);
		// 输入保存路径
		JLabel nameLabel = new JLabel("保存路径：");
		basicInfoPanel.add("1.1.left.top", nameLabel);
		nameLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
		file = new iTextField(new FilterDocument(40), "", 20, 40, false, null);
		basicInfoPanel.add("1.2.left.top", file);
		fileBtn = new JButton("选择");
		fileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectPath();
				// TODO 自动生成的方法存根

			}
		});
		basicInfoPanel.add("1.3.left.top", fileBtn);
		// 输入需要输出的层数
		JLabel levelLabel = new JLabel("输出层数：");
		basicInfoPanel.add("2.1.left.top", levelLabel);
		nameLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
		basicInfoPanel.add("2.2.left.top", icBox);
		JPanel attrLayOut = new JPanel(new HorizontalLayout(10, 1, 1, 1, 0));
		attrLayOut.add("unbound.nobind.center.center", basicInfoPanel);
		attrLayOut.setBackground(Color.white);
		dlgPanel.add(BorderLayout.CENTER, attrLayOut);
//Button设置
		JPanel btnPanel = new JPanel(new ButtonLayout(ButtonLayout.HORIZONTAL,
				ButtonLayout.CENTER, 7));
		startBtn = new JButton("确定");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OutputOp op = new OutputOp(OutputDialog.this);
				op.addOperationListener(OutputDialog.this);
				session.queueOperation(op);

				OutputDialog.this.dispose();
			}
		});
		btnPanel.add(startBtn);
		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OutputDialog.this.dispose();
			}
		});
		btnPanel.add(cancelBtn);
		JPanel btnOutter = new JPanel(new BorderLayout());
		btnOutter.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnOutter.add(BorderLayout.NORTH, new Separator());
		btnOutter.add(BorderLayout.CENTER, btnPanel);
		dlgPanel.add(BorderLayout.SOUTH, btnOutter);

		setPreferredSize(new Dimension(400, 200));

		super.run();
	}

	@Override
	public void endOperation() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void startOperation(String arg0) {
		// TODO 自动生成的方法存根

	}

	protected void selectPath() {
		File file;
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			this.file.setText(file.toString() + ".xlsx");
		}
	}

	public iTextField getFile() {
		return file;
	}

	public void setFile(iTextField file) {
		this.file = file;
	}

	public iTextField getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(iTextField levelNumber) {
		this.levelNumber = levelNumber;
	}

	public TCComponent getTcComponent() {
		return tcComponent;
	}

	public void setTcComponent(TCComponent tcComponent) {
		this.tcComponent = tcComponent;
	}

	public iComboBox getIcBox() {
		return icBox;
	}

	public void setIcBox(iComboBox icBox) {
		this.icBox = icBox;
	}

}
