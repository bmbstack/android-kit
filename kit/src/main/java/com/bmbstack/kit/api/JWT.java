package com.bmbstack.kit.api;

public interface JWT {

    /**
     * Token available
     *
     * @return boolean
     */
    boolean available();

    /**
     * Get token from local storage
     */
    String get();

    /**
     * Refresh token when http response status code is 401
     */
    String refresh();
}
