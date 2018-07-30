package net.dgg.framework.https;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.AbstractContentBody;
/**
 * <p>@Title 文件系统</p>
 * <p>@Description </p>
 * <p>@Version 1.0.0</p>
 * <p>@author rebin</p>
 * <p>@date 2017年7月20日</p>
 * <p>xiefangjian@163.com</p>
 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
 */
public class HFileBody extends AbstractContentBody {
	private ByteArrayOutputStream bout;
	private String filename;
	private String charset;
	public HFileBody(InputStream in, String filename, ContentType mimeType,String charset) {
		super(mimeType);
		bout = null;
		if (in == null) {
			throw new IllegalArgumentException("File may not be null");
		}
		try {
			bout = new ByteArrayOutputStream();
			byte bytes[] = new byte[1024];
			int Len;
			while ((Len = in.read(bytes)) != -1) {
				bout.write(bytes, 0, Len);
			}
			bout.close();
			in.close();
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (filename != null) {
			this.filename = filename;
		}
		this.charset = charset;
	}

	public HFileBody(InputStream in, String filename) {
		this(in, filename, ContentType.APPLICATION_OCTET_STREAM, null);
	}

	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(bout.toByteArray());
	}

	public void writeTo(OutputStream out, int mode) throws IOException {
		writeTo(out);
	}

	public void writeTo(OutputStream out) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		try {
			InputStream in = new ByteArrayInputStream(bout.toByteArray());
			byte tmp[] = new byte[4096];
			int l;
			while ((l = in.read(tmp)) != -1) {
				out.write(tmp, 0, l);
			}
			out.flush();
			in.close();
			in.close();
		} catch (Exception se) {
			se.printStackTrace();
		}
	}
	public String getTransferEncoding()
    {
        return "binary";
    }

    public String getCharset()
    {
        return charset;
    }

    public long getContentLength()
    {
        return (long)bout.size();
    }

    public String getFilename()
    {
        return filename;
    }

}
