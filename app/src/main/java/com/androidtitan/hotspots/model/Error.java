package com.androidtitan.hotspots.model;

/**
 * Created by amohnacs on 9/1/16.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Handles JSON error messages for persistence
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//        "code",
//        "message"
//})
public class Error {

    //@JsonProperty("code")
    private Integer code;
    //@JsonProperty("message")
    private String message;
    //@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The code
     */
    //@JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    /**
     *
     * @param code
     * The code
     */
    //@JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     *
     * @return
     * The message
     */
    //@JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    //@JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    //@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    //@JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}