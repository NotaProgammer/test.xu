package com.test.xu.dialog;

import java.io.UnsupportedEncodingException;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class ChangeDescOp extends AbstractAIFOperation {

	TCComponentItemRevision itemRevision;
	String text;

	public ChangeDescOp(ChangeDescDialog changeDialog) {
		itemRevision = changeDialog.getItemRevision();
		text = changeDialog.getItextArea().getText();
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void executeOperation() throws Exception {
		text = substring(text, 120);
		text.replace("?", "_");
		itemRevision.setProperty("k3_jointDesc",text);
		MessageBox.post(PlatformHelper.getCurrentShell(),
				"�޸���ɣ�", "��ʾ", MessageBox.INFORMATION);
		// TODO �Զ����ɵķ������
		
	}

	public String substring(String orignal, int count)
			throws UnsupportedEncodingException {
		// ԭʼ�ַ���Ϊnull��Ҳ���ǿ��ַ���
		if (orignal != null && !"".equals(orignal)) {
			// ��ԭʼ�ַ���ת��ΪGBK�����ʽ
			orignal = new String(orignal.getBytes(), "GBK");
			// Ҫ��ȡ���ֽ�������0����С��ԭʼ�ַ������ֽ���
			if (count > 0 && count < orignal.getBytes("GBK").length) {
				StringBuffer buff = new StringBuffer();
				char c;
				for (int i = 0; i < count; i++) {
					c = orignal.charAt(i);
					buff.append(c);
					if (isChineseChar(c)) {
						// �������ĺ��֣���ȡ�ֽ�������1
						--count;
					}
				}
				return buff.toString();
			}
		}
		return orignal;
	}

	public boolean isChineseChar(char c) throws UnsupportedEncodingException {
		// ����ֽ�������1���Ǻ���
		return String.valueOf(c).getBytes("GBK").length > 1;
	}

}
