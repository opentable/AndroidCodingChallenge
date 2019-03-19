
package com.opentable.moviereviews.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author ronan.oneill
 *
 * Generate POJO via http://www.jsonschema2pojo.org/
 */

public class Link {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("suggested_link_text")
    @Expose
    private String suggestedLinkText;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSuggestedLinkText() {
        return suggestedLinkText;
    }

    public void setSuggestedLinkText(String suggestedLinkText) {
        this.suggestedLinkText = suggestedLinkText;
    }

}
