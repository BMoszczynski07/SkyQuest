package com.example.lotycrud.Models.Response;

public class ResponseDTO<T> {
    public int http;
    public T res;

    public ResponseDTO (int http, T res) {
        this.http = http;
        this.res = res;
    }
}
