package com.team41.boromi.callbacks;


/**
 * An interface defining callback methods for books
 */
public interface BookCallback {

    void onSuccess(String message);

    void onFailure(String message);
}
