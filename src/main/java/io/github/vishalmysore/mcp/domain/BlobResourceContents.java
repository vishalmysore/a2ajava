package io.github.vishalmysore.mcp.domain;

public class BlobResourceContents {
    /**
     * A base64-encoded string representing the binary data of the item.
     */
    private String blob;
    /**
     * The MIME type of this resource, if known.
     */
    private String mimeType;
    /**
     * The URI of this resource.
     */
    private String uri;

    public String getBlob() {
        return blob;
    }

    public void setBlob(String blob) {
        this.blob = blob;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
