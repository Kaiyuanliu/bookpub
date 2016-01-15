package com.kaiyuan.bookpub.utils;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

public class IsbnEditor extends PropertyEditorSupport{
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(new Isbn(text.trim()));
        } else {
            setValue(null);
        }
    }

    @Override
    public String getAsText() {
        Isbn isbn = (Isbn) getValue();
        return (isbn != null) ? isbn.getIsbn() : "";
    }
}
