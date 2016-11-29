package com.test.xu.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.test.xu.handler.CustomFileChooser;
public class InputDialog extends AbstractAIFDialog implements
InterfaceAIFOperationListener {

	private static final long serialVersionUID = 1L;
	private TCComponentFolder tcFolder;
	private ImportInfoPanel excelFilePanel;
	protected TCSession session = RACUIUtil.getTCSession();
	private JButton startBtn;
	private JButton cancelBtn;
	
public InputDialog()  throws TCException, IOException{
	excelFilePanel = new ImportInfoPanel(null, JFileChooser.FILES_ONLY, new CustomFileChooser("xlsx"));
	setTitle("��������");
	// TODO �Զ����ɵĹ��캯�����
}
	@Override
	public void run() {

		// �������
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);
		JPanel basicInfoPanel = new JPanel(
				new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);

		JLabel pathJLabel = new JLabel("�����ļ�·����");
		basicInfoPanel.add("1.1.left.top", pathJLabel);
		pathJLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
		basicInfoPanel.add("1.2.left.top", excelFilePanel);
		// ���Button
		JPanel attrLayOut = new JPanel(new HorizontalLayout(10, 1, 1, 1, 0));
		attrLayOut.add("unbound.nobind.center.center", basicInfoPanel);
		attrLayOut.setBackground(Color.white);
		dlgPanel.add(BorderLayout.CENTER, attrLayOut);

		dlgPanel.add(BorderLayout.NORTH, basicInfoPanel);

		JPanel btnPanel = new JPanel(new ButtonLayout(ButtonLayout.HORIZONTAL,
				ButtonLayout.CENTER, 7));
		startBtn = new JButton("ȷ��");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InputOp op = new InputOp(InputDialog.this);
				op.addOperationListener(InputDialog.this);
				session.queueOperation(op);

				InputDialog.this.dispose();
			}
		});
		btnPanel.add(startBtn);
		cancelBtn = new JButton("ȡ��");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InputDialog.this.dispose();
			}
		});
		btnPanel.add(cancelBtn);
		JPanel btnOutter = new JPanel(new BorderLayout());
		btnOutter.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnOutter.add(BorderLayout.NORTH, new Separator());
		btnOutter.add(BorderLayout.CENTER, btnPanel);
		dlgPanel.add(BorderLayout.SOUTH, btnOutter);

		setPreferredSize(new Dimension(500, 150));

		super.run();
	}
	@Override
	public void endOperation() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void startOperation(String arg0) {
		// TODO �Զ����ɵķ������
		
	}
	public TCComponentFolder getTcFolder() {
		return tcFolder;
	}
	public void setTcFolder(TCComponentFolder tcFolder) {
		this.tcFolder = tcFolder;
	}
	public ImportInfoPanel getExcelFilePanel() {
		return excelFilePanel;
	}
	public void setExcelFilePanel(ImportInfoPanel excelFilePanel) {
		this.excelFilePanel = excelFilePanel;
	}

}
