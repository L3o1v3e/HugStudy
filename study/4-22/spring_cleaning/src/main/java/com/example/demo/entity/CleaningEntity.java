package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
* 掃除情報 Entity
*/
@Data
@Entity
@Table(name = "cleaning")
public class CleaningEntity {

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/**
	 * 項目
	 */
	@Column(name = "cleaning")
	private String cleaning;
}
