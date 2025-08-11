package com.example.demo.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

/**
* 項目情報 リクエストデータ
*/
@Data
public class CleaningForm {
 /**
  * ID
  */
 private Integer id;
  /**
  * 科目
  */
 @NotEmpty(message = "項目を入力してください")
 @Size(max = 50, message = "項目は50文字以内で入力してください")
 private String cleaning;

}