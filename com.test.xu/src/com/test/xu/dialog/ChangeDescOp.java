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
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void executeOperation() throws Exception {
		text = substring(text, 120);
		text.replace("?", "_");
		itemRevision.setProperty("k3_jointDesc",text);
		MessageBox.post(PlatformHelper.getCurrentShell(),
				"修改完成！", "提示", MessageBox.INFORMATION);
		// TODO 自动生成的方法存根
		
	}

	public String substring(String orignal, int count)
			throws UnsupportedEncodingException {
		// 原始字符不为null，也不是空字符串
		if (orignal != null && !"".equals(orignal)) {
			// 将原始字符串转换为GBK编码格式
			orignal = new String(orignal.getBytes(), "GBK");
			// 要截取的字节数大于0，且小于原始字符串的字节数
			if (count > 0 && count < orignal.getBytes("GBK").length) {
				StringBuffer buff = new StringBuffer();
				char c;
				for (int i = 0; i < count; i++) {
					c = orignal.charAt(i);
					buff.append(c);
					if (isChineseChar(c)) {
						// 遇到中文汉字，截取字节总数减1
						--count;
					}
				}
				return buff.toString();
			}
		}
		return orignal;
	}

	public boolean isChineseChar(char c) throws UnsupportedEncodingException {
		// 如果字节数大于1，是汉字
		return String.valueOf(c).getBytes("GBK").length > 1;
	}

}
