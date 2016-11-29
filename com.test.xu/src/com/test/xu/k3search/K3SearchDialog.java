package com.test.xu.k3search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.FilterDocument;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.teamcenter.rac.util.iTextArea;
import com.teamcenter.rac.util.iTextField;

public class K3SearchDialog  extends AbstractAIFDialog implements
InterfaceAIFOperationListener{

	private static final long serialVersionUID = -4364078402718350660L;
	

	private JButton saveBtn;
	protected TCSession session = RACUIUtil.getTCSession();
	private JButton cancelBtn;
	private iTextArea idITextArea;
	private iTextField pathTextField;
	public K3SearchDialog() {
		super(AIFDesktop.getActiveDesktop());
		setTitle("长虹零件查询");
		// TODO 自动生成的构造函数存根
	}
	@Override
	public void run() {
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);
		JPanel basicInfoPanel = new JPanel(new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);

		JLabel nameLabel = new JLabel("输入物料编号（每行一个）：");
		basicInfoPanel.add("1.1.left.top", nameLabel);
		nameLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
		String encoding = TCSession.getServerEncodingName(session);
		idITextArea = 	new iTextArea(new FilterDocument(9999, encoding), "",
				8, 20, 9999, false, null);
		idITextArea.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
		basicInfoPanel.add("2.1.left.top",idITextArea);
		
		JLabel pathJLabel = new JLabel("文件夹名：Home\\");
		basicInfoPanel.add("3.1.left.top", pathJLabel);
		pathJLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
		pathTextField = new  iTextField(new FilterDocument(40), "", 20, 40, false, null);
		basicInfoPanel.add("4.1.left.top",pathTextField);
		
		JPanel attrLayOut = new JPanel(new HorizontalLayout(10, 1, 1, 1, 0));
		attrLayOut.add("unbound.nobind.center.center", basicInfoPanel);
		attrLayOut.setBackground(Color.white);
		dlgPanel.add(BorderLayout.CENTER, attrLayOut);

		JPanel btnPanel = new JPanel(new ButtonLayout(ButtonLayout.HORIZONTAL, ButtonLayout.CENTER, 7));
		saveBtn = new JButton("存到文件夹");
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				K3SearchOp op = new K3SearchOp(K3SearchDialog.this);
				op.addOperationListener(K3SearchDialog.this);
				session.queueOperation(op);

				K3SearchDialog.this.dispose();
			}
		});
		btnPanel.add(saveBtn);
		//pathTextField = new  iTextField(new FilterDocument(40), "", 20, 40, false, null);
		//btnPanel.add(pathTextField);
		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				K3SearchDialog.this.dispose();
			}
		});
		btnPanel.add(cancelBtn);
		JPanel btnOutter = new JPanel(new BorderLayout());
		btnOutter.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnOutter.add(BorderLayout.NORTH, new Separator());
		btnOutter.add(BorderLayout.CENTER, btnPanel);
		dlgPanel.add(BorderLayout.SOUTH, btnOutter);

		setPreferredSize(new Dimension(300, 300 ));

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
	public JButton getSaveBtn() {
		return saveBtn;
	}
	public void setSaveBtn(JButton saveBtn) {
		this.saveBtn = saveBtn;
	}
	public TCSession getSession() {
		return session;
	}
	public void setSession(TCSession session) {
		this.session = session;
	}
	public JButton getCancelBtn() {
		return cancelBtn;
	}
	public void setCancelBtn(JButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	public iTextArea getIdITextArea() {
		return idITextArea;
	}
	public void setIdITextArea(iTextArea idITextArea) {
		this.idITextArea = idITextArea;
	}
	public iTextField getPathTextField() {
		return pathTextField;
	}
	public void setPathTextField(iTextField pathTextField) {
		this.pathTextField = pathTextField;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
