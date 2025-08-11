package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.CleaningEntity;
import com.example.demo.form.CleaningForm;
import com.example.demo.repository.CleaningRepository;

/**
* 掃除情報 Service
*/
@Service
public class CleaningService {
	/**
	 * 掃除情報 Repository
	 */
	@Autowired
	private CleaningRepository cleaningRepository;

	/**
	 * 掃除情報 全検索
	 * @return  検索結果
	 */
	public List<CleaningEntity> searchAll() {
		return cleaningRepository.findAll();
	}

	/**
	 * 掃除情報 新規登録
	 * @param  cleaning 掃除情報
	 */
	public void create(CleaningForm cleaningRequest) {
		CleaningEntity cleaning = new CleaningEntity();
		cleaning.setCleaning(cleaningRequest.getCleaning());
		cleaningRepository.save(cleaning);
	}

	/**
	 * 掃除情報 主キー検索
	 * @return  検索結果
	 */
	public CleaningEntity findById(Integer id) {
		return cleaningRepository.getOne(id);
	}

	/**
	 * 掃除情報 更新
	 * @param  cleaning 科目情報
	 */
	public void update(CleaningForm cleaningUpdateRequest) {
		CleaningEntity cleaning = findById(cleaningUpdateRequest.getId());
		cleaning.setCleaning(cleaningUpdateRequest.getCleaning());
		cleaningRepository.save(cleaning);
	}

	/**
	 * 項目情報 物理削除
	 * @param  id ID
	 */
	public void delete(Integer id) {
		CleaningEntity cleaning = findById(id);
		cleaningRepository.delete(cleaning);
	}
}