package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.CleaningEntity;
import com.example.demo.form.CleaningForm;
import com.example.demo.service.CleaningService;

/**
* 掃除情報 Controller
*/
@Controller
public class CleaningController {

	/**
	 * 掃除情報 Service
	 */
	//使用クラスのインスタンス化
	@Autowired
	CleaningService cleaningService;

	/**
	 * 掃除情報一覧画面を表示
	 * @param  model Model
	 * @return  掃除情報一覧画面のHTML
	 */
	@GetMapping("/cleaning/list")
	public String cleaningList(Model model) {
		//掃除テーブルのデータを全て取得するメソッドを呼び出す。
		List<CleaningEntity> cleaninglist = cleaningService.searchAll();
		//取得した掃除データの情報を画面側で利用できるようにmodelへ格納する。
		model.addAttribute("cleaninglist", cleaninglist);
		return "cleaning/list";
	}

	/**
	 * 掃除項目新規登録画面を表示
	 * @param  model Model
	 * @return  掃除情報一覧画面
	 */
	@GetMapping("/cleaning/add")
	public String cleaningRegister(Model model) {
		model.addAttribute("cleaningRequest", new CleaningForm());
		return "cleaning/add";
	}

	/**
	 * 掃除項目新規登録
	 * @param  userRequest リクエストデータ
	 * @param  model Model
	 * @return  掃除項目情報一覧画面
	 */
	@PostMapping("/cleaning/create")
	public String cleaningCreate(@Validated CleaningForm cleaningRequest, BindingResult result, Model model) {
		if (result.hasErrors()) {
			// 入力チェックエラーの場合
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("cleaningRequest", new CleaningForm());
			model.addAttribute("validationError", errorList);
			return "cleaning/add";
		}
		// 掃除項目情報の登録
		cleaningService.create(cleaningRequest);
		return "redirect:/cleaning/list";
	}

	/**
	 * 掃除項目情報詳細画面を表示
	 * @param  id 表示する項目ID
	 * @param  model Model
	 * @return  掃除項目情報詳細画面
	 */
	@GetMapping("/cleaning/{id}")
	public String userDetail(@PathVariable Integer id, Model model) {
		CleaningEntity cleaning = cleaningService.findById(id);
		model.addAttribute("cleaning", cleaning);
		return "cleaning/detail";
	}

	/**
	 * 項目編集画面を表示
	 * @param  id 表示する項目ID
	 * @param  model Model
	 * @return  項目編集画面
	 */
	@GetMapping("/cleaning/{id}/edit")
	public String userEdit(@PathVariable Integer id, Model model) {
		CleaningEntity cleaning = cleaningService.findById(id);
		CleaningForm cleaningUpdateRequest = new CleaningForm();
		cleaningUpdateRequest.setId(cleaning.getId());
		cleaningUpdateRequest.setCleaning(cleaning.getCleaning());
		model.addAttribute("cleaningUpdateRequest", cleaningUpdateRequest);
		return "cleaning/edit";
	}

	/**
	 * 項目更新
	 * @param  userRequest リクエストデータ
	 * @param  model Model
	 * @return  掃除項目情報詳細画面
	 */
	@PostMapping("/cleaning/update")
	public String cleaningpdate(@Validated @ModelAttribute CleaningForm cleaningUpdateRequest, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "cleaning/edit";
		}
		// 掃除項目情報の更新
		cleaningService.update(cleaningUpdateRequest);
		return String.format("redirect:/cleaning/%d", cleaningUpdateRequest.getId());
	}

	/**
	 * 項目情報削除
	 * @param  id 表示するID
	 * @param  model Model
	 * @return  項目情報詳細画面
	 */
	@GetMapping("/cleaning/{id}/delete")
	public String cleaningDelete(@PathVariable Integer id, Model model) {
		// 科目情報の削除
		cleaningService.delete(id);
		return "redirect:/cleaning/list";
	}
}