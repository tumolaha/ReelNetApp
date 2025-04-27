package com.learning.reelnet.modules.vocabulary.domain.model;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Etymology extends BaseEntity<UUID> {

    private Vocabulary vocabulary;

    private String originLanguage; // Ngôn ngữ gốc (Latin, Greek, etc.)

    private String etymologyDescription;// Mô tả nguồn gốc từ
} 