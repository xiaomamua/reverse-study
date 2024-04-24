package com.demo.taxi.http;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

  private Integer code;
  private T data;
  private String msg;


  public static <T> Result<T> success() {
    return success(null, null);
  }

  public static <T> Result<T> success(T data) {
    return success(data, null);
  }

  public static <T> Result<T> success(T data, String msg) {
    return new Result<>(ResultStatusCode.SUCCESS, data, msg);
  }

  public static <T> Result<T> failure() {
    return failure(null, null);
  }

  public static <T> Result<T> failure(T data) {
    return failure(data, null);
  }

  public static <T> Result<T> failure(T data, String msg) {
    return new Result<>(ResultStatusCode.FAILURE, data, msg);
  }
}
