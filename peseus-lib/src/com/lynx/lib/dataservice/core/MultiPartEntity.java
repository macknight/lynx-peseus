package com.lynx.lib.dataservice.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

/**
 * 
 * @author chris.liu
 * @name MultiPartEntity.java
 * @update 2013-4-17 下午10:52:25
 *
 */
public class MultiPartEntity implements HttpEntity {
	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	private String boundary = null;

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	boolean isSetLast = false;
	boolean isSetFirst = false;

	public MultiPartEntity() {
		final StringBuffer buf = new StringBuffer();
		final Random rand = new Random();
		for (int i = 0; i < 30; i++) {
			buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		this.boundary = buf.toString();
	}

	public void writeFirstBoundaryIfNeeds() {
		if (!isSetFirst) {
			try {
				out.write(("--" + boundary + "\r\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		isSetFirst = true;
	}

	public void writeLastBoundaryIfNeeds() {
		if (isSetLast) {
			return;
		}

		try {
			out.write(("\r\n" + boundary + "--\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		isSetLast = true;
	}

	public void addPart(final String key, final String value) {
		writeFirstBoundaryIfNeeds();
		try {
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n")
					.getBytes());
			out.write(value.getBytes());
			out.write(("\r\n--" + boundary + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPart(final String key, final String filename,
			final InputStream instream, final boolean isLst) {
		addPart(key, filename, instream, "application/octet-stream", isLst);
	}

	public void addPart(final String key, final String filename,
			final InputStream instream, String type, final boolean isLst) {
		writeFirstBoundaryIfNeeds();
		try {
			type = "Content-Type: " + type + "\r\n";
			out.write(("Content-Disposition: form-data; name=\"" + key
					+ "\"; filename=\"" + filename + "\"\r\n").getBytes());
			out.write(type.getBytes());
			out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

			final byte[] tmp = new byte[4096];
			int l = 0;
			while ((l = instream.read(tmp)) != -1) {
				out.write(tmp, 0, l);
			}
			if (!isLst) {
				out.write(("\r\n--" + boundary + "\r\n").getBytes());
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addPart(final String key, final File value, final boolean isLast) {
		try {
			addPart(key, value.getName(), new FileInputStream(value), isLast);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void consumeContent() throws IOException,
			UnsupportedOperationException {
		if (isStreaming()) {
			throw new UnsupportedOperationException(
					"Streaming entity does not implement #consumeContent()");
		}
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException,
			UnsupportedOperationException {
		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	public Header getContentEncoding() {
		return null;
	}

	@Override
	public long getContentLength() {
		writeLastBoundaryIfNeeds();
		return out.toByteArray().length;
	}

	@Override
	public Header getContentType() {
		return new BasicHeader("Content-Type", "multipart/form-data; boundary="
				+ boundary);
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		outstream.write(out.toByteArray());
	}

}
